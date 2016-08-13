package com.byteshaft.requests.utils;

import android.content.Context;

import com.byteshaft.requests.HttpRequest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class RequestBase {

    protected HttpURLConnection mConnection;
    protected OutputStream mOutputStream;
    protected String mResponseText = "";
    private ArrayList<HttpRequest.OnReadyStateChangeListener> mStateChangedListeners;
    private ArrayList<HttpRequest.FileUploadProgressUpdateListener> mProgressListeners;
    private ListenersUtil mListenersUtil;

    protected RequestBase(Context context) {
        mStateChangedListeners = new ArrayList<>();
        mProgressListeners = new ArrayList<>();
        mListenersUtil = ListenersUtil.getInstance(context);
    }

    protected void openConnection(String requestMethod, String url) {
        try {
            URL urlObject = new URL(url);
            mConnection = (HttpURLConnection) urlObject.openConnection();
            mConnection.setRequestMethod(requestMethod);
            mListenersUtil.emitOnReadyStateChanged(
                    mStateChangedListeners,
                    mConnection,
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
        mListenersUtil.emitOnReadyStateChanged(
                mStateChangedListeners,
                mConnection,
                HttpRequest.STATE_DONE
        );
    }

    protected void sendRequestData(String body, boolean closeOnDone) {
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

    protected void writeContent(String uploadFilePath) {
        File uploadFile = new File(uploadFilePath);
        long total = uploadFile.length();
        long uploaded = 0;
        try {
            mOutputStream.flush();
            FileInputStream inputStream = new FileInputStream(uploadFile);
            final byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                mOutputStream.write(buffer, 0, bytesRead);
                mOutputStream.flush();
                uploaded += bytesRead;
                mListenersUtil.emitOnFileUploadProgressChanged(
                        mProgressListeners,
                        uploadFile,
                        uploaded,
                        total
                );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addReadyStateListener(HttpRequest.OnReadyStateChangeListener listener) {
        mStateChangedListeners.add(listener);
    }

    protected void addProgressUpdateListener(
            HttpRequest.FileUploadProgressUpdateListener listener
    ) {
        mProgressListeners.add(listener);
    }
}
