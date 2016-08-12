package com.byteshaft.albumsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.AppGlobals;
import com.byteshaft.albumsapp.utils.Config;
import com.byteshaft.albumsapp.utils.Constants;
import com.byteshaft.requests.HttpRequest;
import com.byteshaft.requests.HttpRequestStateListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

public class ActivateAccount extends AppCompatActivity implements View.OnClickListener,
        HttpRequestStateListener {

    private EditText mEntryEmail;
    private EditText mEntryActivationKey;
    private Button mActivateButton;
    private TextView mHeadingText;
    private HttpRequest mRequest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_activation);
        mEntryEmail = (EditText) findViewById(R.id.entry_activation_email);
        mEntryActivationKey = (EditText) findViewById(R.id.entry_activation_key);
        mActivateButton = (Button) findViewById(R.id.button_activate_execute);
        mHeadingText = (TextView) findViewById(R.id.heading);
        assert mActivateButton != null;
        mActivateButton.setOnClickListener(this);
        setCustomFont();
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
                        case HttpURLConnection.HTTP_OK:
                            Config.saveUserProfile(mRequest.getResponseText());
                            Config.userActivationState(true);
                            Config.setIsLoggedIn(true);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_activate_execute:
                if (validateInputFields() == 0) {
                    activate(getFieldText(mEntryEmail), getFieldText(mEntryActivationKey));
                }
        }
    }

    private String getFieldText(EditText editText) {
        return editText.getText().toString();
    }

    private int validateInputFields() {
        int errorsCount = 0;
        if (getFieldText(mEntryEmail).isEmpty()) {
            mEntryEmail.setError("Field mandatory.");
            errorsCount += 1;
        }
        if (getFieldText(mEntryActivationKey).isEmpty()) {
            mEntryActivationKey.setError("Field mandatory.");
            errorsCount += 1;
        }
        return errorsCount;
    }

    private void activate(String email, String activationKey) {
        mRequest = new HttpRequest(getApplicationContext());
        mRequest.setOnReadyStateChangedListener(this);
        mRequest.open("POST", Constants.ENDPOINT_ACTIVATE);
        mRequest.send(getActivationRequestData(email, activationKey));
    }

    private String getActivationRequestData(String email, String activationKey) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("activation_key", activationKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    private void setCustomFont() {
        mEntryEmail.setTypeface(AppGlobals.typeface);
        mEntryActivationKey.setTypeface(AppGlobals.typeface);
        mActivateButton.setTypeface(AppGlobals.typeface);
        mHeadingText.setTypeface(AppGlobals.typeface);
    }
}
