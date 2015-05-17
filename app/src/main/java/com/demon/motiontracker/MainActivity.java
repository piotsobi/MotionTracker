package com.demon.motiontracker;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.GpsStatus;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.demon.motiontracker.modules.GPSHandler;
import com.demon.motiontracker.modules.GPSProvider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.SystemService;
import com.googlecode.androidannotations.annotations.ViewById;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements GpsStatus.Listener {

    @ViewById
    Button buttonOpenGPS;
    @ViewById
    Button btnGetLocation;
    @SystemService
    Vibrator mVib;
    boolean mGpsStarted = false;
    // GPSTracker class
    GPSProvider gps;
    HttpClient mClient;
    String URL;
    GPSHandler gpsHandler;
    public static File mFile;
    boolean isStarted = false;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mFile = new File(Environment.getExternalStorageDirectory() + File.separator + "gpsTest.txt");
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            Log.e("nie utworzono pliku", "error");
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Click(R.id.btnSendID)
    void btnSendIDClicked(){
        String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mClient = new DefaultHttpClient();
        URL = "http://student.agh.edu.pl/~mmankows/cgi/track.cgi?d_id=" + android_id + "&lon=" + "MTEuMTIzMjI" + "%3D&lat=" + "MTEuMTIzMjI" + "%3D";
        //Toast.makeText(context, URL, Toast.LENGTH_SHORT).show();


        Thread thread = new Thread(new Runnable(){
            @Override
            public void run() {
                try {
                    HttpGet httpPost = new HttpGet(URL);
                    HttpResponse response = mClient.execute(httpPost);
                    //Toast.makeText(MainActivity.this, "ID wyslane", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    //Toast.makeText(context, "COS NIE TAK", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        mVib.vibrate(800);
        Toast.makeText(MainActivity.this, "ID urządzenia przesłane", Toast.LENGTH_SHORT).show();

        Intent mIntent = new Intent(this, GPSTest_.class);
        startActivity(mIntent);
    }

    @Click(R.id.btnGetLocation)
    void btnGetLocationClicked(){
        //gps = new GPSProvider(MainActivity.this);
        //gps.dts(69.696969);
        if(mGpsStarted) {
            if (!isStarted) {
                gpsHandler = new GPSHandler(MainActivity.this);
                gpsHandler.synchroGPSLocation();
                isStarted = true;
                btnGetLocation.setBackgroundColor(Color.parseColor("#cdc9c9"));
                btnGetLocation.setText("STOP");
                Toast.makeText(MainActivity.this, "Tracking rozpoczęty", Toast.LENGTH_SHORT).show();
            } else {
                gpsHandler.stopTimer();
                btnGetLocation.setBackgroundColor(Color.parseColor("#00AFD1"));
                btnGetLocation.setText("START");
                Toast.makeText(MainActivity.this, "Tracking zakonczony", Toast.LENGTH_SHORT).show();

            }
        } else{

        }
       /* String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        mClient = new DefaultHttpClient();
        URL = "http://student.agh.edu.pl/~mmankows/cgi-bin/track.cgi?d_id=" + android_id + "&lon=" + "MTEuMTIzMjI" + "%3D&lat=" + "MTEuMTIzMjI" + "%3D";
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





        // Check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        } else {
            // Can't get location.
            // GPS or network is not enabled.
            // Ask user to enable GPS/network in settings.
            gps.showSettingsAlert();
        }
        */
        buttonOpenGPS.setBackgroundColor(Color.parseColor("#00AFD1"));


    }

    @Click(R.id.buttonOpenGPS)
    void buttonOpenGPSClicked(){

        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(mFile), "text/plain");
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGpsStatusChanged(int i) {
        // GPS_EVENT_STARTED_STOPED
        switch(i){
            case GpsStatus.GPS_EVENT_STARTED:
                mGpsStarted = true;
                break;
            case GpsStatus.GPS_EVENT_STOPPED:
                mGpsStarted = false;
        }
    }
}
