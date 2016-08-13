package com.byteshaft.requests.utils;

import android.content.Context;

import com.byteshaft.requests.FormData;

import java.util.ArrayList;


public class HttpRequestUtil extends RequestBase {

    protected HttpRequestUtil(Context context) {
        super(context);
    }

    protected void sendRequest(final String contentType, final String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnection.setRequestProperty("Content-Type", contentType);
                if (data != null) {
                    sendRequestData(data, true);
                }
                readResponse();
            }
        }).start();
    }

    protected void sendRequest(final String contentType, final FormData data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mConnection.setRequestProperty("Content-Type", contentType);
                ArrayList<FormData.MultiPartData> requestItems = data.getData();
                for (FormData.MultiPartData item : requestItems) {
                    sendRequestData(item.getPreContentData(), false);
                    if (item.getContentType() == FormData.TYPE_CONTENT_TEXT) {
                        sendRequestData(item.getContent(), false);
                    } else {
                        writeContent(item.getContent());
                    }
                    sendRequestData(item.getPostContentData(), false);
                }
                sendRequestData(FormData.FINISH_LINE, true);
                readResponse();
            }
        }).start();
    }
}
