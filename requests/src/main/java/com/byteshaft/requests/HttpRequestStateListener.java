package com.byteshaft.requests;

import java.net.HttpURLConnection;

public interface HttpRequestStateListener {
    void onReadyStateChanged(HttpURLConnection connection, int requestType, int readyState);
}
