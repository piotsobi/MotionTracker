package com.demon.motiontracker;

import android.provider.Settings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.demon.motiontracker.modules.GPSHandler;
import com.demon.motiontracker.modules.GPSProvider;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Click;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {



    // GPSTracker class
    GPSProvider gps;
    HttpClient mClient;
    String URL;
    GPSHandler gpsHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

    }


    @Click(R.id.btnGetLocation)
    void btnGetLocationClicked(){
        //gps = new GPSProvider(MainActivity.this);
        //gps.dts(69.696969);
        gpsHandler = new GPSHandler(MainActivity.this);
        gpsHandler.synchroGPSLocation();
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
}
