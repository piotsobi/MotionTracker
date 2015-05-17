package com.demon.motiontracker.modules;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

/**
 * Created by Piotr on 2015-05-16.
 */
public class GPSService extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 6 * 1; // 1 minute 6 sec

    String URL, android_id;
    HttpClient mClient;
    LocationManager locationManager;

    @Override
    public void onCreate() {
        super.onCreate();

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        double latitude = (double) (location.getLatitude());
        double longitude = (double) (location.getLongitude());

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    void encodeBase64(double longitude, double latitude){
        mClient = new DefaultHttpClient();
        //longtmp = longitude;
        //latitmp = latitude;
        String baseStringLong, baseStringLat;
        String longi, lati;
        longi = "" + longitude;
        lati = "" + latitude;

        baseStringLong = Base64.encodeToString(longi.getBytes(), Base64.NO_PADDING + Base64.URL_SAFE + Base64.NO_WRAP);
        baseStringLat = Base64.encodeToString(lati.getBytes(), Base64.URL_SAFE + Base64.NO_PADDING + Base64.NO_WRAP);
        //Toast.makeText(context, "STRING " + string + " base " + baseString, Toast.LENGTH_SHORT).show();
        URL = "http://student.agh.edu.pl/~mmankows/cgi/track.cgi?d_id=" + android_id + "&lon=" + baseStringLong + "%3D&lat=" + baseStringLat + "%3D";
        //Toast.makeText(context, URL, Toast.LENGTH_SHORT).show();


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpGet httpPost = new HttpGet(URL);
                    HttpResponse response = mClient.execute(httpPost);

                } catch (IOException e) {
                    //Toast.makeText(context, "COS NIE TAK", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        thread.start();

        //return result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}