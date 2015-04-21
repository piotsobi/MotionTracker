package com.demon.motiontracker.modules;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.demon.motiontracker.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Piotr on 2015-04-21.
 */
public class GPSHandler{

    //private Handler mHandler;
    //private Runnable mRunnable;
    public String deviceID;
    GPSProvider gps;
    int i =0;
    Context context;
    public GPSHandler(Context context){
        //gps = new GPSProvider(context);
        this.context = context;
    }
    public void synchroGPSLocation(){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                try {
                    new GPSProvider(context).execute("LOL");
                    Log.d("MSG", " " + i);
                    i++;

                } catch (Exception e) {
                    // TODO: handle exception
                    Log.e("exception ",e.toString());
                }

            }
        }, 0, 60000);
    }



}
