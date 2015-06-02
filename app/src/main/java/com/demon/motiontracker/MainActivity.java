package com.demon.motiontracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.provider.Settings;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

//import com.demon.motiontracker.modules.GPSProvider;
import com.demon.motiontracker.jsonParser.Register;
import com.demon.motiontracker.jsonParser.jsonGson;
import com.demon.motiontracker.modules.GPSService;
import com.demon.motiontracker.modules.jsonSender;
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
public class MainActivity extends Activity {

    public static String token = null;
    @ViewById
    Button btnGetLocation;
    boolean mGpsStarted = false;
    // GPSTracker class
    //GPSProvider gps;
    boolean checkCancel = true;
    GPSService mGps;;
    HttpClient mClient;
    String URL;
    //GPSHandler gpsHandler;
    @SystemService
    LocationManager mLocationManager;
    public static File mFile;
    boolean isStarted = false;

    SharedPreferences mPref = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        mPref = this.getApplicationContext().getSharedPreferences("myPref", Context.MODE_PRIVATE);
        mGps = new GPSService();
        mFile = new File(Environment.getExternalStorageDirectory() + File.separator + "gpsTest.txt");
        try {
            mFile.createNewFile();
        } catch (IOException e) {
            Log.e("nie utworzono pliku", "error");
            e.printStackTrace();
        }
        //Log.i("TOKEN", mPref.getString("token", null));

        if(mPref.contains("firstrun")){
            Log.i("COS KURWA NIE GRA", "ANI TROCHE");
        }
        if(mPref.getBoolean("firstrun",true)){
            alertDialog();
        }else{
            mPref.edit().putString("kurwa","chuj").commit();
            Toast.makeText(MainActivity.this,"COJESTKURWA",Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPref.edit().putBoolean("firstrun",false).commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mPref.edit().putBoolean("firstrun",false).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!checkCancel) {
            Log.i("On resume", "START");
            mPref.edit().putBoolean("firstrun", false).commit();
        }
    }

    @Click(R.id.btnSendID)
    void btnSendIDClicked(){
        Intent mIntent = new Intent(this, MapView.class);
        startActivity(mIntent);

    }

    @Click(R.id.btnGetLocation)
    void btnGetLocationClicked(){
        //gps = new GPSProvider(MainActivity.this);
        //gps.dts(69.696969);
        init();
        if(mGpsStarted) {
            if (!isStarted) {
                //Log.i("TOKENKUTASIARZ", token);

                //mPref.edit().putString("token",token).commit();

                Log.i("KURWACHUJ", mPref.getString("kurwa","cipka"));
                if(mPref.getBoolean("firstrun",true)) {
                    Log.i("COMMIT", "TOKEN");
                    mPref.edit().putString("token", token).commit();
                    Log.i("DODANE", "KURWESTWO");
                    Log.i("TOKENIARACWANIARA", mPref.getString("token", "0"));
                    mPref.edit().putBoolean("firstrun",false);
                }

                //token = mPref.getString("token","0");
                if(mPref.contains("token")) {
                    Log.i("PREF", mPref.getString("token", "0"));
                }
                Intent mIntent = new Intent(this, GPSService.class);
                Log.i("TOKEN", mPref.getString("token", "0"));
               // startService(mIntent);
                //isStarted = true;
                btnGetLocation.setBackgroundColor(Color.parseColor("#cdc9c9"));
                btnGetLocation.setText("STOP");
                Toast.makeText(MainActivity.this, "Tracking rozpoczęty", Toast.LENGTH_SHORT).show();
            } else {
                //mGps.stopTracking();
                stopService(new Intent(this, GPSService.class));
                btnGetLocation.setBackgroundColor(Color.parseColor("#00AFD1"));
                btnGetLocation.setText("START");
                Toast.makeText(MainActivity.this, "Tracking zakonczony", Toast.LENGTH_SHORT).show();

            }
        } else{
            Toast.makeText(MainActivity.this, "Włacz GPS", Toast.LENGTH_SHORT).show();
        }




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


    void init(){

        mGpsStarted = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    void alertDialog(){
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this).setTitle("Zarejestuj się");
        final FrameLayout mFrameView = new FrameLayout(MainActivity.this.getBaseContext());
        mBuilder.setView(mFrameView);
        final AlertDialog mAlert = mBuilder.create();
        LayoutInflater mInflater = mAlert.getLayoutInflater();
        View mView = mInflater.inflate(R.layout.register_alertdialog,mFrameView);
        final EditText textLogin = (EditText) mFrameView.findViewById(R.id.editLogin);
        final EditText textPass = (EditText) mFrameView.findViewById(R.id.editPass);
        Button buttonClose = (Button) mFrameView.findViewById(R.id.buttonClose);
        Button buttonRegister = (Button) mFrameView.findViewById(R.id.buttonRegister);
        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkCancel = false;
                MainActivity.this.finish();

            }
        });
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String android_id = Settings.Secure.getString(MainActivity.this.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                String login = textLogin.getText().toString();
                String pass = textPass.getText().toString();
                Register obj = new Register(login,pass,android_id);
                jsonGson.gsonRegister(obj);

                mAlert.dismiss();
            }
        });

        mAlert.show();
    }


}
