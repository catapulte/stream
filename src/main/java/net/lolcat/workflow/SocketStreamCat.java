package net.lolcat.workflow;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import net.lolcat.workflow.mapper.CatMapper;
import net.lolcat.workflow.model.CatData;
import net.lolcat.workflow.model.CatMoves;
import net.lolcat.workflow.model.Temperature;
import net.lolcat.workflow.sink.CatDataSink;
import net.lolcat.workflow.sink.MongoConfig;
import net.lolcat.workflow.sink.TemperatureSink;
import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSink;
import org.apache.flink.streaming.connectors.rabbitmq.RMQSource;
import org.apache.flink.streaming.connectors.rabbitmq.common.RMQConnectionConfig;
import org.apache.flink.streaming.util.serialization.SimpleStringSchema;
import org.apache.flink.util.Collector;

import java.io.IOException;
import java.util.Arrays;


public class SocketStreamCat {

    public static void main(String[] args) throws Exception {

        ParameterTool parameter = ParameterTool.fromPropertiesFile("stream.properties");

        String rabbitHost = parameter.get("rabbitmq.host", "127.0.0.1");
        int rabbitPort = parameter.getInt("rabbitmq.port", 5672);
        String rabbitLogin = parameter.get("rabbitmq.user", "guest");
        String rabbitPassword = parameter.get("rabbitmq.pass", "guest");

        String mongoHost = parameter.get("mongo.host", "127.0.0.1");
        int mongoPort = parameter.getInt("mongo.port",27017);
        String mongoLogin =  parameter.get("mongo.user", "guest");
        String mongoPassword =  parameter.get("mongo.pass", "guest");

        String catDB = parameter.get("mongo.db", "cat");


        ServerAddress serverAddress = new ServerAddress(mongoHost, mongoPort);
        MongoCredential credential = MongoCredential.createCredential(mongoLogin, catDB, mongoPassword.toCharArray());

        MongoClient client = new MongoClient(serverAddress, Arrays.asList(credential));

        MongoConfig mongoConfig = new MongoConfig(mongoHost, mongoPort, catDB, mongoLogin, mongoPassword);


        ObjectMapper objectMapper = new ObjectMapper();


        // get the execution environment
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();


        RMQConnectionConfig connectionConfig = new RMQConnectionConfig.Builder()
                .setHost(rabbitHost).setPort(rabbitPort).setUserName(rabbitLogin)
                .setPassword(rabbitPassword).setVirtualHost("/").build();


        RMQSource<String> amqpSource = new RMQSource<>(connectionConfig, "cat.data", new SimpleStringSchema());
        RMQSink<String> catmovesSink = new RMQSink(connectionConfig, "cat.moves", new SimpleStringSchema()) {

            protected void setupQueue() throws IOException {
                channel.queueDeclare(queueName, true, false, false, null);
            }

        };


        CatDataSink catData = new CatDataSink(mongoConfig);
        TemperatureSink temperatureSink = new TemperatureSink(mongoConfig);

        DataStreamSource<String> stringDataSource = env.addSource(amqpSource);

        SingleOutputStreamOperator<CatData> catReducer = stringDataSource
                .map(new CatMapper())
                .flatMap(
                        new FlatMapFunction<CatData, CatData>() {
                            @Override
                            public void flatMap(CatData o, Collector<CatData> collector) throws Exception {
                                collector.collect(o);
                            }
                        }
                )
                .keyBy("id", "timestamp")
                .timeWindow(Time.seconds(5))
                .reduce(
                        new ReduceFunction<CatData>() {
                            @Override
                            public CatData reduce(CatData o, CatData t1) throws Exception {
                                return o;
                            }
                        });


        // store catData in Mongo
        catReducer.name("Cat Mongo Writer").addSink(catData);

        // send position to proximity-aggregator
        catReducer.map(new MapFunction<CatData, String>() {

            @Override
            public String map(CatData catData) throws Exception {
                CatMoves catMoves = new CatMoves(catData.getId(), catData.getPosition()[0], catData.getPosition()[1]);
                return objectMapper.writeValueAsString(catMoves);
            }
        }).addSink(catmovesSink);


        // store temperatur history
        catReducer.map(new MapFunction<CatData, Temperature>() {

            @Override
            public Temperature map(CatData catData) throws Exception {
                return new Temperature(catData.getPosition(), catData.getTemperature(), catData.getHumidity());
            }
        }).addSink(temperatureSink);

        catReducer.name("Cat Printer").print();

        env.execute("Cat position duplicate processor");
    }
}
