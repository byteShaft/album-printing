package com.byteshaft.albumsapp.utils.network;

import android.content.Context;

import com.byteshaft.albumsapp.utils.Constants;

public class HttpRequest extends ConnectionUtil {

    public static final short STATE_UNSET = 0;
    public static final short STATE_OPENED = 1;
    public static final short STATE_HEADERS_RECEIVED = 2;
    public static final short STATE_LOADING = 3;
    public static final short STATE_DONE = 4;

    public static final short RESPONSE_OK = 200;
    public static final short RESPONSE_UNAUTHORIZED = 401;
    public static final short RESPONSE_BAD = 400;
    public static final short RESPONSE_CREATED = 201;
    public static final short RESPONSE_FORBIDDEN = 403;
    public static final short RESPONSE_NOTFOUND = 404;

    public HttpRequest(Context context) {
        super(context);
    }

    public void setOnReadyStateChangedListener(HttpRequestStateListener listener) {
        addReadyStateListener(listener);
    }

    public void open(String requestMethod, String url) {
        openConnection(requestMethod, url);
    }

    public void setRequestHeader(String key, String value) {
        if (mConnection == null) {
            throw new RuntimeException("open() must be called first.");
        }
        mConnection.setRequestProperty(key, value);
    }

    public void send(String data) {
        sendRequest(Constants.CONTENT_TYPE_JSON, data);
    }

    public String getResponseText() {
        return mResponseText;
    }
}
