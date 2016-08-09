package com.byteshaft.albumsapp.utils.network;

import android.content.Context;
import android.support.annotation.WorkerThread;

import java.io.File;

public class FormDataHttpRequest extends FormDataHttpRequestUtil {

    public FormDataHttpRequest(Context context) {
        super(context);
    }

    public interface FileUploadProgressUpdateListener {
        void onFileUploadProgressUpdate(File file, long uploaded, long total);
    }

    public void setOnReadyStateChangedListener(HttpRequestStateListener listener) {
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

    @WorkerThread
    public void addField(String name, String value) {
        addFormField(name, value);
    }

    @WorkerThread
    public void addFile(String fieldName, File file) {
        addFilePart(fieldName, file);
    }

    @WorkerThread
    public void finish() {
        super.finish();
    }

    public String getResponseText() {
        return mResponseText;
    }
}
