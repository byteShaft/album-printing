package com.byteshaft.albumsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.Config;
import com.byteshaft.albumsapp.utils.Constants;
import com.byteshaft.albumsapp.utils.network.HttpRequest;
import com.byteshaft.albumsapp.utils.network.HttpRequestStateListener;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity implements HttpRequestStateListener {

    private HttpRequest mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Config.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
            return;
        }
        TextView welcomeText = (TextView) findViewById(R.id.text_view_title_main_screen);
        welcomeText.setText("Welcome " + Config.getFullName());
        createAlbum("Test Album");
    }

    @Override
    public void onReadyStateChanged(HttpURLConnection connection, int readyState) {
        if (readyState == 4) {
            System.out.println(mHttp.getResponseText());
        }
    }

    private void createAlbum(String name) {
        String token = "798a9fc28b5ff6118df33d97456a84d2d8e7622b";
        mHttp = new HttpRequest(getApplicationContext());
        mHttp.setOnReadyStateChangedListener(this);
        mHttp.open("POST", Constants.ENDPOINT_ALBUMS);
        mHttp.setRequestHeader("Authorization", "Token " + token);
        mHttp.send("{\"name\": " + "\"" + name + "\"" + "}");
    }
}
