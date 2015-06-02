package com.demon.motiontracker.modules;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.demon.motiontracker.MainActivity;
import com.demon.motiontracker.jsonParser.jsonGson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Piotr on 2015-06-02.
 */
public class jsonSender{

    String jsonString;

    HttpPost httpPost;



    static String token;
    String line;

    public static void sendTrackerJson(final String jsonString){
        String URL = "http://student.agh.edu.pl/~mmankows/mtracker/cgi/track.cgi";
        final HttpPost httpPost = new HttpPost(URL);
        final HttpClient mClient = new DefaultHttpClient();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder mStringBuilder = new StringBuilder();
                try {
                    StatusLine mStatusLine;
                    httpPost.setEntity(new StringEntity(jsonString, "UTF8"));
                    httpPost.setHeader("Content-type", "application/json");

                    HttpResponse response = mClient.execute(httpPost);
                    mStatusLine = response.getStatusLine();
                    int statusCode = mStatusLine.getStatusCode();

                    Log.i("STATUS", "CODE " + statusCode);
                } catch (IOException e) {
                    //Toast.makeText(context, "COS NIE TAK", Toast.LENGTH_SHORT).show();
                    Log.i("STATUS", "ERROR WHILE SENDING");
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    public static void sendRegisterJson(final String jsonString) {
        String URL = "http://student.agh.edu.pl/~mmankows/mtracker/cgi/register.cgi";
        final HttpPost httpPost = new HttpPost(URL);
        final HttpClient mClient = new DefaultHttpClient();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                StringBuilder mStringBuilder = new StringBuilder();
                try {
                    StatusLine mStatusLine;
                    httpPost.setEntity(new StringEntity(jsonString, "UTF8"));
                    httpPost.setHeader("Content-type", "application/json");

                    HttpResponse response = mClient.execute(httpPost);
                    mStatusLine = response.getStatusLine();
                    int statusCode = mStatusLine.getStatusCode();

                    Log.i("STATUS", "CODE " + statusCode);
                    HttpEntity mHttpEntity = response.getEntity();
                    InputStream mInputStream = mHttpEntity.getContent();
                    BufferedReader mBufferReader = new BufferedReader(new InputStreamReader(mInputStream));
                    String line;
                    while((line = mBufferReader.readLine()) != null){
                        mStringBuilder.append(line);
                    }
                    Log.i("RESPONSE", mStringBuilder.toString());
                    try {
                       token =  jsonGson.readJson(mStringBuilder.toString());
                        Log.i("TOKENIECJAKZLOTO", token);
                        MainActivity.token = token;
                        Log.i("TOKENSKURWIEL",MainActivity.token);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    //Toast.makeText(context, "COS NIE TAK", Toast.LENGTH_SHORT).show();
                    Log.i("STATUS", "ERROR WHILE SENDING");
                    e.printStackTrace();
                }
            }
        });

        thread.start();


    }


}

