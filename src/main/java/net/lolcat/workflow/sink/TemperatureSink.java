package net.lolcat.workflow.sink;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import net.lolcat.workflow.model.Temperature;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.bson.Document;

import java.util.Arrays;

public class TemperatureSink extends RichSinkFunction<Temperature> {

    private final MongoConfig mongoConfig;

    private transient MongoClient mongoClient;


    public TemperatureSink(MongoConfig mongoConfig) {
        this.mongoConfig = mongoConfig;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        ServerAddress serverAddress = new ServerAddress(mongoConfig.getHost(), mongoConfig.getPort());
        MongoCredential credential = MongoCredential.createCredential(mongoConfig.getLogin(), mongoConfig.getDb(), mongoConfig.getPassword().toCharArray());

        mongoClient = new MongoClient(serverAddress, Arrays.asList(credential));

    }

    @Override
    public void invoke(Temperature value) throws Exception {

        Document document = new Document();

        BasicDBList positions = new BasicDBList();
        positions.add(value.getPosition()[0]);
        positions.add(value.getPosition()[1]);
        document.append("position", positions);

        document.append("temperature", value.getTemperature());
        document.append("humidity", value.getTemperature());

        mongoClient.getDatabase("cat").getCollection("temperature").insertOne(document);
    }
}
