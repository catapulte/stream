package net.lolcat.workflow.sink;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import net.lolcat.workflow.model.Temperature;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.bson.Document;

import java.util.Arrays;

public class TemperatureSink extends RichSinkFunction<Temperature> {

    private final MongoClient client;


    public TemperatureSink(String host, int port, String login, String db, String password) {

        ServerAddress serverAddress = new ServerAddress(host, port);
        MongoCredential credential = MongoCredential.createCredential(login, db, password.toCharArray());

        client = new MongoClient(serverAddress, Arrays.asList(credential));
    }

    @Override
    public void invoke(Temperature value) throws Exception {

        Document document = new Document();
        document.append("position", value.getPosition());
        document.append("temperature", value.getTemperature());
        document.append("humidity", value.getTemperature());

        client.getDatabase("cat").getCollection("temperature").insertOne(document);
    }
}
