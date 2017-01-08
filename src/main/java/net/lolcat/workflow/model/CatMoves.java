package net.lolcat.workflow.model;


import lombok.Data;

@Data
public class CatMoves {

    private String id;

    private double latitude;

    private double longitude;


    public CatMoves() {
    }

    public CatMoves(String id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

}
