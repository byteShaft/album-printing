package com.byteshaft.albumsapp.utils.network;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ConnectionUtil {

    private StateListenerHelpers mListenerHelpers;
    private HttpURLConnection mConnection;
    private ArrayList<HttpRequestStateListener> mListeners;

    public ConnectionUtil(Context context, String url) {
        mListeners = new ArrayList<>();
        mListenerHelpers = new StateListenerHelpers(context);
        mConnection = openConnection(url);
    }

    protected HttpURLConnection getConnectionInstance() {
        return mConnection;
    }

    private HttpURLConnection openConnection(String url) {
        HttpURLConnection connection = null;
        try {
            URL urlObject = new URL(url);
            connection = (HttpURLConnection) urlObject.openConnection();
            mListenerHelpers.emitOnConnectionOpened(mListeners);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void setConnectionAttributes(String contentType, String method) {
        try {
            mConnection.setRequestProperty("Content-Type", contentType);
            mConnection.setRequestMethod(method);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequestData(String body) {
        try {
            byte[] outputInBytes = body.getBytes("UTF-8");
            OutputStream os = mConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();
            mListenerHelpers.emitOnDataSent(mListeners);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() {
        try {
            InputStream is = mConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuilder response = new StringBuilder();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            String responseText = response.toString();
            mListenerHelpers.emitOnResponse(
                    mListeners,
                    responseText,
                    mConnection.getResponseCode()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void sendRequest(
            final String method,
            final String contentType,
            final String data
    ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                setConnectionAttributes(contentType, method);
                if (data != null) {
                    sendRequestData(data);
                }
                readResponse();
            }
        }).start();
    }

    protected void addStateListener(HttpRequestStateListener listener) {
        mListeners.add(listener);
    }
}
