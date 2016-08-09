package com.byteshaft.albumsapp.utils.network;

import android.content.Context;

import java.io.IOException;
import java.io.OutputStream;


public class HttpRequestUtil extends RequestBase {

    protected HttpRequestUtil(Context context) {
        super(context, HttpRequest.REQUEST_TYPE_SIMPLE);
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
}
