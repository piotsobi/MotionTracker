package com.demon.motiontracker.modules;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.demon.motiontracker.MainActivity;
import com.demon.motiontracker.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Created by Piotr on 2015-04-20.
 */
public class GPSProvider extends AsyncTask<String, Integer, Integer> implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 6 * 1; // 1 minute 6 sec

    private Location mLocation = null;
    protected LocationManager mLocationManager;
    String mLong, mLat;
    String URL;
    double longitude;
    double latitude = 66;
    public Context context;
    HttpClient mClient;
    int i = 0;
    double longtmp = 0;
    double latitmp = 0;
    boolean isGPSEnabled, isNetworkEnabled;
    boolean canGetLocation;
    private String android_id;

    public GPSProvider(Context context){
        //mLocation = new Location()
         android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        this.context = context;
    }
    public Location getLocation(){
        try {
            mLocationManager = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);

            // Getting GPS status
            isGPSEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // Getting network status
            isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGPSEnabled) {
                // No network provider is enabled
                this.canGetLocation=false;
                longitude = 77.777;
                latitude = 88.982;
            } else {


                // If GPS enabled, get latitude/longitude using GPS Services

                    if (mLocation == null) {
                        this.canGetLocation = true;
                        mLocationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        Log.d("GPS Enabled", "GPS Enabled");
                        if (mLocationManager != null) {
                            mLocation = mLocationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if (mLocation != null) {

                                latitude = mLocation.getLatitude();
                                longitude = mLocation.getLongitude();
                                Log.d("LAT SET", "LONG SET");
                            }
                        }
                    }

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    return mLocation;
    }
    public void doStuff() throws FileNotFoundException {

        //longitude = mLocation.getLongitude();
        //latitude = mLocation.getLatitude();
        /*getLocation();
        if ((longtmp == longtmp) && (latitmp == latitude)){

        }
        else {
            encodeBase64(longitude, latitude);
        }
        Log.d("DO STUFF", " ");
        */
        getLocation();
        if(this.canGetLocation && (mLocation != null)){

            double tmplong = mLocation.getLongitude();
            double tmplat = mLocation.getLatitude();
            if(MainActivity.mFile.exists()) {
                String gspString = "GPS: LONG "+tmplong+"; LAT "+tmplat + System.getProperty("line.separator");
                FileOutputStream fo = new FileOutputStream(MainActivity.mFile);
                try {
                    fo.write(gspString.getBytes());
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }


    void encodeBase64(double longitude, double latitude){
        mClient = new DefaultHttpClient();
        longtmp = longitude;
        latitmp = latitude;
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
/*
    public void dts(double longitude, double latitude){
        String dt;
        dt = String.valueOf(longitude);
        dt = "" + longitude;
        encodeBase64(dt,);
    }
*/
    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    /**
     * Function to get latitude
     * */
    public double getLatitude(){
        if(mLocation != null){
            latitude = mLocation.getLatitude();
        }

        // return latitude
        return latitude;
    }


    /**
     * Function to get longitude
     * */
    public double getLongitude(){
        if(mLocation != null){
            longitude = mLocation.getLongitude();
        }

        // return longitude
        return longitude;
    }

    @Override
    protected Integer doInBackground(String... strings) {
        //Toast.makeText(context, strings[0], Toast.LENGTH_SHORT).show();
        try {
            doStuff();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}
