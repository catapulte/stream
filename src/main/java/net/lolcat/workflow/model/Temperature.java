package net.lolcat.workflow.model;

import lombok.Data;
import org.mongodb.morphia.annotations.Id;


@Data
public class Temperature {

    public Temperature() {
        // Default constructor
    }

    public Temperature(double[] position, double temperature, double humidity) {
        this.position = position;
        this.temperature = temperature;
        this.humidity = humidity;
    }

    @Id
    private long id;

    private double[] position;

    private double temperature;

    private double humidity;
}
