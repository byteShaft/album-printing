package com.byteshaft.albumsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.Config;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Config.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            return;
        }
        System.out.print("Not HERE!");
    }
}
