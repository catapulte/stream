package net.lolcat.workflow.sink;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.model.UpdateOptions;
import net.lolcat.workflow.model.CatData;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.Arrays;

public class CatDataSink extends RichSinkFunction<CatData> {

    private final MongoClient client;


    public CatDataSink(String host, int port, String login, String db, String password) {

        ServerAddress serverAddress = new ServerAddress(host, port);
        MongoCredential credential = MongoCredential.createCredential(login, db, password.toCharArray());

        client = new MongoClient(serverAddress, Arrays.asList(credential));
    }

    @Override
    public void invoke(CatData value) throws Exception {

        Document document = new Document();
        document.append("_id", value.getId());
        document.append("altitude", value.getAltitude());
        document.append("timestamp", value.getTimestamp());
        document.append("vbat", value.getVbat());
        document.append("angle", value.getAngle());
        document.append("humidity", value.getHumidity());

        BasicDBList positions = new BasicDBList();
        positions.add(value.getPosition()[0]);
        positions.add(value.getPosition()[1]);
        document.append("position", positions);
        document.append("satelliteNb", value.getSatelliteNb());
        document.append("temperature", value.getTemperature());


        BsonDocument update = new BsonDocument();
        update.append("_id", new BsonString(value.getId()));


        client.getDatabase("cat").getCollection("catdata").replaceOne(update, document, new UpdateOptions().upsert(true));

    }
}
