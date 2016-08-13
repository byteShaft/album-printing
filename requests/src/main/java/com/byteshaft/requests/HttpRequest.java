package com.byteshaft.requests;

import android.content.Context;

import com.byteshaft.requests.utils.HttpRequestUtil;

import java.io.File;
import java.net.HttpURLConnection;

public class HttpRequest extends HttpRequestUtil {

    public static final short STATE_UNSET = 0;
    public static final short STATE_OPENED = 1;
    public static final short STATE_HEADERS_RECEIVED = 2;
    public static final short STATE_LOADING = 3;
    public static final short STATE_DONE = 4;
    public static final short REQUEST_TYPE_UNSET = -1;
    public static final short REQUEST_TYPE_JSON = 1;
    public static final short REQUEST_TYPE_FORM = 2;
    public static final String CONTENT_TYPE_JSON = "application/json";
    public static final String CONTENT_TYPE_FORM = String.format(
            "multipart/form-data; boundary=%s", FormData.BOUNDARY
    );

    public HttpRequest(Context context) {
        super(context);
    }

    public interface FileUploadProgressUpdateListener {
        void onFileUploadProgressUpdate(File file, long uploaded, long total);
    }

    public interface OnReadyStateChangeListener {
        void onReadyStateChange(HttpURLConnection connection, int readyState);
    }

    public void setOnReadyStateChangeListener(OnReadyStateChangeListener listener) {
        addReadyStateListener(listener);
    }

    public void setOnFileUploadProgressUpdateListener(FileUploadProgressUpdateListener listener) {
        addProgressUpdateListener(listener);
    }

    public void open(String requestMethod, String url) {
        openConnection(requestMethod, url);
    }

    public void setRequestHeader(String key, String value) {
        if (mConnection == null) {
            throw new RuntimeException("open() must be called first.");
        }
        mConnection.setRequestProperty(key, value);
    }

    public void send(String data) {
        sendRequest(CONTENT_TYPE_JSON, data);
    }

    public void send() {
        sendRequest(CONTENT_TYPE_JSON, (String) null);
    }

    public void send(FormData formData) {
        sendRequest(CONTENT_TYPE_FORM, formData);
    }

    public String getResponseText() {
        return mResponseText;
    }
}
