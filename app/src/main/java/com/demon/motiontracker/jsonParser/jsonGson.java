package com.demon.motiontracker.jsonParser;





import android.util.Log;

import com.demon.motiontracker.modules.jsonSender;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Piotr on 2015-06-02.
 */
public class jsonGson {
    public static void gsonRegister(Register obj) {

        //Register obj = new Register("pieciak","to","chuj");
        Gson gson = new Gson();

        // convert java object to JSON format,
        // and returned as JSON formatted string
        String json = gson.toJson(obj);
        /*
        try {
            //write converted json data to a file named "file.json"
            FileWriter writer = new FileWriter("c:\\file.json");
            writer.write(json);
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        */
        System.out.println("KURWAAAAAAAA " + json);
        jsonSender.sendRegisterJson(json);
    }

    public static void jsonTracker(Tracker mTracker){
        Gson gson = new Gson();
        String json = gson.toJson(mTracker);
        System.out.println("KURWAAAAAAAA " + json);
        jsonSender.sendTrackerJson(json);
    }

    //public void saveToken(String )
    public static String readJson(String json) throws JSONException {
        JSONObject mJSONObject = new JSONObject(json);
        String token = mJSONObject.getString("token");
        Log.i("READ JSON", token);

        return token;
    }
}
