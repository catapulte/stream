package net.lolcat.workflow.model;

import java.io.Serializable;

/**
 * Created by erwann on 08/01/17.
 */
public class CatData implements Serializable {

    private String id;

    private double[] position;

    private double angle;

    private long timestamp;

    private double vbat;

    private int satelliteNb;

    private double altitude;

    private double temperature;

    private double humidity;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double[] getPosition() {
        return position;
    }

    public void setPosition(double[] position) {
        this.position = position;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getVbat() {
        return vbat;
    }

    public void setVbat(double vbat) {
        this.vbat = vbat;
    }

    public int getSatelliteNb() {
        return satelliteNb;
    }

    public void setSatelliteNb(int satelliteNb) {
        this.satelliteNb = satelliteNb;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
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
