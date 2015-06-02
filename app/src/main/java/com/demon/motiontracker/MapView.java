package com.demon.motiontracker;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.googlecode.androidannotations.annotations.EActivity;

/**
 * Created by Piotr on 2015-06-02.
 */
@EActivity(R.layout.map_view)
public class MapView extends Activity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("http://student.agh.edu.pl/~mmankows");
    }
}
