package com.byteshaft.requests.utils;

import android.content.Context;
import android.os.Handler;

import com.byteshaft.requests.HttpRequest;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

public class ListenersUtil {

    private Handler mMainHandler;
    private static ListenersUtil sListenersUtil;

    public static ListenersUtil getInstance(Context context) {
        if (sListenersUtil == null) {
            sListenersUtil = new ListenersUtil(context);
        }
        return sListenersUtil;
    }

    private ListenersUtil(Context context) {
        mMainHandler = new Handler(context.getMainLooper());
    }

    protected void emitOnReadyStateChange(
            ArrayList<HttpRequest.OnReadyStateChangeListener> listeners,
            final HttpURLConnection connection,
            final int readyState
    ) {
        for (final HttpRequest.OnReadyStateChangeListener listener : listeners) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onReadyStateChange(connection, readyState);
                }
            });
        }
    }

    protected void emitOnFileUploadProgress(
            ArrayList<HttpRequest.FileUploadProgressListener> listeners,
            final File file,
            final long uploaded,
            final long total
    ) {
        for (final HttpRequest.FileUploadProgressListener listener : listeners) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onFileUploadProgress(file, uploaded, total);
                }
            });
        }
    }
}
