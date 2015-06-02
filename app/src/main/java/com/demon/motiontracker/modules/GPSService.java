package com.demon.motiontracker.modules;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;

import com.demon.motiontracker.MainActivity;
import com.demon.motiontracker.jsonParser.Coordinates;
import com.demon.motiontracker.jsonParser.Tracker;
import com.demon.motiontracker.jsonParser.jsonGson;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Piotr on 2015-05-16.
 */
public class GPSService extends Service implements LocationListener {

    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 10 * 1; // 1 minute 6 sec
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String URL, android_id;
    HttpClient mClient;
    LocationManager locationManager;
    String token;// = "1db532dc44091f4061f8d0a10f560f62";
    Coordinates mCoord = null;
    Tracker mTracker;
    boolean check = true;
    SharedPreferences mPref;
    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("STATUS", "started");
        mPref = this.getApplicationContext().getSharedPreferences("myPref",Context.MODE_PRIVATE);
        android_id = Settings.Secure.getString(GPSService.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, 0, this);
    }

    public boolean checkInternetStatus() {
        ConnectivityManager connec = (ConnectivityManager) GPSService.this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = connec.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobile = connec.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        // Check if wifi or mobile network is available or not. If any of them is
        // available or connected then it will return true, otherwise false;
        return wifi.isConnected() || mobile.isConnected();
    }

    @Override
    public void onLocationChanged(Location location) {
        float latitude = (float) (location.getLatitude());
        float longitude = (float) (location.getLongitude());
        long time = location.getTime();

        Date mDate = new Date(time);
        Log.i("TIME", ""+ mSimpleDateFormat.format(mDate));
       // Log.i("PREFERENCJA", mPref.getString("token",null));

        mCoord = new Coordinates(latitude,longitude,mSimpleDateFormat.format(mDate));
        if(check) {
            mTracker = new Tracker(mPref.getString("token", "0"), android_id, mCoord);
        }

        if(checkInternetStatus()) {

            jsonGson.jsonTracker(mTracker); //wysylanie
            Log.i("STATUS", "ONLINE");
            mTracker = new Tracker(mPref.getString("token","0"), android_id, mCoord);

        }
        else{
            check = false;
            mTracker.addCoord(mCoord);
            Log.i("STATUS", "OFFLINE");
        }

        Log.i("Geo_Location", "Latitude: " + latitude + ", Longitude: " + longitude);
    }

    public void loggi(){
        Log.d("Logger","started");
    }
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d("STATUS", "CHANGED");
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

    public void encodeBase64(double longitude, double latitude){
        mClient = new DefaultHttpClient();
        //longtmp = longitude;
        //latitmp = latitude;
        String baseStringLong, baseStringLat;
        String longi, lati;
        longi = "" + longitude;
        lati = "" + latitude;
        Log.d("STATUS", "encoding");
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
                    Log.d("STATUS", "SEND");

                } catch (IOException e) {
                    //Toast.makeText(context, "COS NIE TAK", Toast.LENGTH_SHORT).show();
                    Log.d("STATUS", "ERROR WHILE SENDING");
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
        if(locationManager!=null) {
            locationManager.removeUpdates(this);
        }
        Log.i("STATUS","destroyed");
    }

    public void stopTracking(){
        if(locationManager!=null) {
            locationManager.removeUpdates(this);
        }
        onDestroy();
        Log.i("STATUS", "stopped");
    }
}
