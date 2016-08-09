package com.byteshaft.albumsapp.utils.network;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FormDataHttpRequestUtil extends RequestBase {

    protected String mResponseText = "";
    private String mBoundary;
    private OutputStream mOutputStream;
    private PrintWriter mWriter;
    private ArrayList<FormDataHttpRequest.FileUploadProgressUpdateListener> mProgressListeners;
    private ListenersUtil mListenersUtil;

    private static final String CHARSET = "UTF-8";
    private static final String CRLF = "\r\n";

    protected FormDataHttpRequestUtil(Context context) {
        super(context, HttpRequest.REQUEST_TYPE_FORM_DATA);
        mListenersUtil = ListenersUtil.getInstance(context);
        mProgressListeners = new ArrayList<>();
        mBoundary = "---------------------------" + System.currentTimeMillis() % 1000;
    }

    private void ensureStreamOpened() {
        if (mWriter != null) {
            return;
        }
        try {
            String contentTypeMultiPart = "multipart/form-data; boundary=" + mBoundary;
            mConnection.setRequestProperty("Content-Type", contentTypeMultiPart);
            mOutputStream = mConnection.getOutputStream();
            mWriter = new PrintWriter(new OutputStreamWriter(mOutputStream, CHARSET), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void addFormField(String name, String value) {
        ensureStreamOpened();
        mWriter.append("--");
        mWriter.append(mBoundary);
        mWriter.append(CRLF);
        mWriter.append("Content-Disposition: form-data; name=\"").append(name).append("\"");
        mWriter.append(CRLF);
        mWriter.append("Content-Type: text/plain; charset=").append(CHARSET);
        mWriter.append(CRLF).append(CRLF).append(value).append(CRLF);
    }

    protected void addFilePart(String fieldName, File uploadFile) {
        ensureStreamOpened();
        String fileName = uploadFile.getName();
        mWriter.append("--");
        mWriter.append(mBoundary);
        mWriter.append(CRLF);
        mWriter.append("Content-Disposition: form-data; name=\"");
        mWriter.append(fieldName).append("\"; filename=\"").append(fileName);
        mWriter.append("\"").append(CRLF).append("Content-Type: ");
        mWriter.append("Content-Transfer-Encoding: binary").append(CRLF);
        mWriter.append(CRLF);
        mWriter.flush();
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
            mWriter.append(CRLF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void finish() {
        mWriter.append(CRLF).append("--").append(mBoundary).append("--").append(CRLF);
        mWriter.close();
        readResponse();
    }

    protected void addProgressUpdateListener(
            FormDataHttpRequest.FileUploadProgressUpdateListener listener
    ) {
        mProgressListeners.add(listener);
    }
}
