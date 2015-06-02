package com.demon.motiontracker.jsonParser;

/**
 * Created by Piotr on 2015-06-02.
 */
public class Coordinates {
    float lat;
    float lon;
    String ld;

    public Coordinates(float lat, float lon, String lt){
        this.lat = lat;
        this.lon = lon;
        this.ld = lt;
    }
}
