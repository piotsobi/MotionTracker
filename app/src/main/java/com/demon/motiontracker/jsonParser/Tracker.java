package com.demon.motiontracker.jsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Piotr on 2015-06-02.
 */
public class Tracker {
    String token;
    String device_id;
   // Coordinates coords[];
    List<Coordinates> coords = new ArrayList<Coordinates>();

    public Tracker(String token, String device_id, Coordinates mCoordinates){
        this.token = token;
        this.device_id = device_id;
        this.coords.add(mCoordinates);

    }

    public void addCoord(Coordinates mCoordinates){
        this.coords.add(mCoordinates);
    }
}
