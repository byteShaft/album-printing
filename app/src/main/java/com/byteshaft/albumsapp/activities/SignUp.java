package com.byteshaft.albumsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.byteshaft.albumsapp.utils.network.HttpRequestStateListener;

public class SignUp extends AppCompatActivity implements HttpRequestStateListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onConnectionOpened() {

    }

    @Override
    public void onDataSent() {

    }

    @Override
    public void onResponse(int responseCode, String responseText) {

    }
}
