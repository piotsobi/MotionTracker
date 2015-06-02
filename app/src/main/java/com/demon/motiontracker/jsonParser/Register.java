package com.demon.motiontracker.jsonParser;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Piotr on 2015-06-02.
 */
public class Register {
    String login;
    String pass;
    String did;
    String cs;

    public Register(String user, String pass, String did){
        this.login = user;
        this.pass = pass;
        this.did = did;
        this.cs = makeMd5(user,pass,did);
    }
    public String makeMd5(String login, String password, String deviceId){
        String result = null;
        byte[] csByte = null;
        String test = login+"-"+password+"-"+deviceId;

        //MessageDigest mMessageDigest = null;
        try {
            MessageDigest mMessageDigest = MessageDigest.getInstance("MD5");
            mMessageDigest.update(test.getBytes());
            csByte = mMessageDigest.digest();
            result = Base64.encodeToString(csByte,Base64.NO_PADDING + Base64.NO_WRAP);
        } catch (NoSuchAlgorithmException e) {
            Log.e("MESSAGEDIGEST", "no such algorithm");
            e.printStackTrace();
        }



        //Log.i("TAG", cs);
        Log.i("RESULT", result);
        return result;
    }



}
