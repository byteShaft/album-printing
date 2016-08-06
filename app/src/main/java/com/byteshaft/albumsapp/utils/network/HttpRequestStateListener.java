package com.byteshaft.albumsapp.utils.network;

public interface HttpRequestStateListener {
    void onConnectionOpened();
    void onDataSent();
    void onResponse(int responseCode, String responseText);
}
