package net.lolcat.workflow.model;

import lombok.Data;
import org.bson.Document;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import java.io.Serializable;

/**
 * Created by erwann on 08/01/17.
 */
@Data
@Entity("cat")
public class CatData implements Serializable {

    @Id
    private String id;

    private double[] position;

    private double angle;

    private long timestamp;

    private double vbat;

    private int satelliteNb;

    private double altitude;

    private double temperature;

    private double humidity;


    public void toDocuments() {

        Document document = new Document();
        document.append("_id", id)
                .append("position", position)
                .append("angle", angle)
                .append("timestamp",timestamp)
                .append("vbat",vbat)
                .append("satelliteNb",satelliteNb)
                .append("altitude",altitude)
                .append("temperature",temperature)
                .append("humidity",humidity)
        ;


    }

}
