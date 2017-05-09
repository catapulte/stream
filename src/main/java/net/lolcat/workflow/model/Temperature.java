package net.lolcat.workflow.model;

import org.mongodb.morphia.annotations.Id;


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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }
}
