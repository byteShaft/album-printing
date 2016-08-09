package com.byteshaft.albumsapp.utils.network;

import java.net.HttpURLConnection;

public interface HttpRequestStateListener {
    void onReadyStateChanged(HttpURLConnection connection, int requestType, int readyState);
}
