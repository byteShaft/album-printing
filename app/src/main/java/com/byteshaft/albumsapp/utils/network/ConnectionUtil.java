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
    protected HttpURLConnection mConnection;
    protected String mResponseText = "";
    private ArrayList<HttpRequestStateListener> mListeners;

    public ConnectionUtil(Context context) {
        mListeners = new ArrayList<>();
        mListenerHelpers = new StateListenerHelpers(context);
    }

    protected void openConnection(String requestMethod, String url) {
        try {
            URL urlObject = new URL(url);
            mConnection = (HttpURLConnection) urlObject.openConnection();
            mConnection.setRequestMethod(requestMethod);
            mListenerHelpers.emitOnReadyStateChanged(
                    mListeners,
                    mConnection,
                    HttpRequest.STATE_OPENED
            );
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readResponse() {
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
                HttpRequest.STATE_DONE
        );
    }

    protected void sendRequest(final String contentType, final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnection.setRequestProperty("Content-Type", contentType);
                if (data != null) {
                    sendRequestData(data);
                }
                readResponse();
            }
        }).start();
    }

    protected void addReadyStateListener(HttpRequestStateListener listener) {
        mListeners.add(listener);
    }
}
