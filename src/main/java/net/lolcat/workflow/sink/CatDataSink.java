package net.lolcat.workflow.sink;

import com.mongodb.BasicDBList;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.model.UpdateOptions;
import net.lolcat.workflow.model.CatData;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;
import org.bson.BsonDocument;
import org.bson.BsonString;
import org.bson.Document;

import java.util.Arrays;

public class CatDataSink extends RichSinkFunction<CatData> {

    private final MongoConfig mongoConfig;

    private transient MongoClient mongoClient;


    public CatDataSink(MongoConfig mongoConfig) {
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


        mongoClient.getDatabase("cat").getCollection("catdata").replaceOne(update, document, new UpdateOptions().upsert(true));

    }
}
