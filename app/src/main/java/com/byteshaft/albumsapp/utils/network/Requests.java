package com.byteshaft.albumsapp.utils.network;

import android.content.Context;

import java.net.HttpURLConnection;

public class Requests {

    private ConnectionUtil mConnector;

    public Requests(Context context, String url) {
        mConnector = new ConnectionUtil(context, url);
    }

    public void setHttpRequestStateChangedListener(HttpRequestStateListener listener) {
        mConnector.addStateListener(listener);
    }
    
    public HttpURLConnection getConnection() {
        return mConnector.getConnectionInstance();
    }

    public void post(String contentType, String data) {
        mConnector.sendRequest("POST", contentType, data);
    }

    public void get(String contentType) {
        mConnector.sendRequest("POST", contentType, null);
    }

    public void put(String contentType, String data) {
        mConnector.sendRequest("PUT", contentType, data);
    }

    public void patch(String contentType, String data) {
        mConnector.sendRequest("PATCH", contentType, data);
    }

    public void delete(String contentType) {
        mConnector.sendRequest("DELETE", contentType, null);
    }
}
