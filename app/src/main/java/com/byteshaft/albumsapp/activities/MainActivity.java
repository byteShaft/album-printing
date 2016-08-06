package com.byteshaft.albumsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private TextView mTextWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Config.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            return;
        }
        mTextWelcome = (TextView) findViewById(R.id.text_view_title_main_screen);
        JSONObject obj = Config.getUserProfile();
        try {
            mTextWelcome.setText("Welcome " + obj.get("full_name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
