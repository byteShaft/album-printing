package com.byteshaft.albumsapp.utils.network;

import android.content.Context;
import android.os.Handler;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class StateListenerHelpers {

    private Handler mMainHandler;

    public StateListenerHelpers(Context context) {
        mMainHandler = new Handler(context.getMainLooper());

    }

    protected void emitOnReadyStateChanged(
            ArrayList<HttpRequestStateListener> listeners,
            final HttpURLConnection connection,
            final int readyState
    ) {
        for (final HttpRequestStateListener listener : listeners) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onReadyStateChanged(connection, readyState);
                }
            });
        }
    }
}
