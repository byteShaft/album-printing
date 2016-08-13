package com.byteshaft.requests.utils;

import android.content.Context;

import com.byteshaft.requests.FormData;
import com.byteshaft.requests.HttpRequest;

import java.io.IOException;
import java.util.ArrayList;


public class HttpRequestUtil extends RequestBase {

    protected HttpRequestUtil(Context context) {
        super(context, HttpRequest.REQUEST_TYPE_SIMPLE);
    }

    private void sendRequestData(String body, boolean closeOnDone) {
        try {
            byte[] outputInBytes = body.getBytes("UTF-8");
            if (mOutputStream == null) {
                mOutputStream = mConnection.getOutputStream();
            }
            mOutputStream.write(outputInBytes);
            mOutputStream.flush();
            if (closeOnDone) {
                mOutputStream.close();
            }
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
                    sendRequestData(data, true);
                }
                readResponse();
            }
        }).start();
    }

    protected void sendRequest(final FormData formData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String contentTypeMultiPart = "multipart/form-data; boundary=" + FormData.BOUNDARY;
                mConnection.setRequestProperty("Content-Type", contentTypeMultiPart);
                ArrayList<FormData.MultiPartData> data = formData.getData();
                for (FormData.MultiPartData item : data) {
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
