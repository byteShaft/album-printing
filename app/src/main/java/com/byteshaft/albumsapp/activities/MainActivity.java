package com.byteshaft.albumsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.WorkerThread;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.Config;
import com.byteshaft.albumsapp.utils.Constants;
import com.byteshaft.requests.FormDataHttpRequest;
import com.byteshaft.requests.HttpRequest;
import com.byteshaft.requests.HttpRequestStateListener;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;

import static com.byteshaft.albumsapp.utils.ui.Helpers.showToast;

public class MainActivity extends AppCompatActivity implements HttpRequestStateListener,
        FormDataHttpRequest.FileUploadProgressUpdateListener {

    private FormDataHttpRequest mMultiPartHttp;
    private HttpRequest mHttp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!Config.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
            return;
        }
        TextView welcomeText = (TextView) findViewById(R.id.text_view_title_main_screen);
        welcomeText.setText("Welcome " + Config.getFullName());
        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
        String filePath = externalPath + "/ok.png";
        final File file = new File(filePath);
        new Thread(new Runnable() {
            @Override
            public void run() {
                addPhoto(file);
            }
        }).start();
        createAlbum("Test Album");
    }

    @Override
    public void onReadyStateChanged(
            HttpURLConnection connection,
            int requestType,
            int readyState
    ) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                try {
                    switch (connection.getResponseCode()) {
                        case HttpURLConnection.HTTP_CREATED:
                            if (requestType == HttpRequest.REQUEST_TYPE_FORM_DATA) {
                                showToast("Created a photo");
                            } else {
                                showToast("Created album");
                            }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private void createAlbum(String name) {
        mHttp = new HttpRequest(getApplicationContext());
        mHttp.setOnReadyStateChangedListener(this);
        mHttp.open("POST", Constants.ENDPOINT_ALBUMS);
        mHttp.setRequestHeader("Authorization", "Token " + Config.getToken());
        mHttp.send("{\"name\": " + "\"" + name + "\"" + "}");
    }

    @WorkerThread
    private void addPhoto(final File file) {
        final int album = 4;
        mMultiPartHttp = new FormDataHttpRequest(getApplicationContext());
        mMultiPartHttp.setOnReadyStateChangedListener(this);
        mMultiPartHttp.setOnFileUploadProgressUpdateListener(this);
        mMultiPartHttp.open("POST", Constants.getPhotosEndpointForAlbum(album));
        mMultiPartHttp.setRequestHeader("Authorization", "Token " + Config.getToken());
        mMultiPartHttp.addFile("photo", file);
        mMultiPartHttp.finish();
    }

    @Override
    public void onFileUploadProgressUpdate(File file, long uploaded, long total) {
        System.out.println(file);
        System.out.println(uploaded);
        System.out.println(total);
    }
}
