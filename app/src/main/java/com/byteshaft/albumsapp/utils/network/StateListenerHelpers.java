package com.byteshaft.albumsapp.utils.network;

import android.content.Context;
import android.os.Handler;

import java.util.ArrayList;

public class StateListenerHelpers {

    private Handler mMainHandler;

    public StateListenerHelpers(Context context) {
        mMainHandler = new Handler(context.getMainLooper());
    }

    protected void emitOnConnectionOpened(ArrayList<HttpRequestStateListener> listeners) {
        for (final HttpRequestStateListener listener : listeners) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onConnectionOpened();
                }
            });
        }
    }

    protected void emitOnDataSent(ArrayList<HttpRequestStateListener> listeners) {
        for (final HttpRequestStateListener listener : listeners) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onDataSent();
                }
            });
        }
    }

    protected void emitOnResponse(
            final ArrayList<HttpRequestStateListener> listeners,
            final String responseText,
            final int responseCode
    ) {
        for (final HttpRequestStateListener listener : listeners) {
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onResponse(responseCode, responseText);
                }
            });
        }
    }
}
