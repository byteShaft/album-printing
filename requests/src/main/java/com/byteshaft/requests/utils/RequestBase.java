package com.byteshaft.requests.utils;

import android.content.Context;

import com.byteshaft.requests.HttpRequest;
import com.byteshaft.requests.HttpRequestStateListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RequestBase {

    protected HttpURLConnection mConnection;
    protected String mResponseText = "";
    private ArrayList<HttpRequestStateListener> mListeners;
    private ListenersUtil mListenerHelpers;
    private int mRequestType;

    protected RequestBase(Context context, int requestType) {
        mRequestType = requestType;
        mListeners = new ArrayList<>();
        mListenerHelpers = ListenersUtil.getInstance(context);
    }

    protected void openConnection(String requestMethod, String url) {
        try {
            URL urlObject = new URL(url);
            mConnection = (HttpURLConnection) urlObject.openConnection();
            mConnection.setRequestMethod(requestMethod);
            mListenerHelpers.emitOnReadyStateChanged(
                    mListeners,
                    mConnection,
                    mRequestType,
                    HttpRequest.STATE_OPENED
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void readResponse() {
        try {
            InputStream inputStream = mConnection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append('\n');
            }
            mResponseText = output.toString();
        } catch (IOException ignore) {
        }
        mListenerHelpers.emitOnReadyStateChanged(
                mListeners,
                mConnection,
                mRequestType,
                HttpRequest.STATE_DONE
        );
    }

    protected void addReadyStateListener(HttpRequestStateListener listener) {
        mListeners.add(listener);
    }
}
