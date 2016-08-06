package com.byteshaft.albumsapp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.Constants;
import com.byteshaft.albumsapp.utils.network.HttpRequestStateListener;
import com.byteshaft.albumsapp.utils.network.Requests;

import org.json.JSONException;
import org.json.JSONObject;

public class SignIn extends AppCompatActivity implements HttpRequestStateListener,
        View.OnClickListener {

    private EditText mEmailEntry;
    private EditText mPasswordEntry;
    private Button mSignInButton;
    private Button mSignUpButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        mEmailEntry = (EditText) findViewById(R.id.entry_email);
        mPasswordEntry = (EditText) findViewById(R.id.entry_password);
        mSignInButton = (Button) findViewById(R.id.button_sign_in);
        mSignUpButton = (Button) findViewById(R.id.button_sign_up);
        mSignInButton.setOnClickListener(this);
        mSignUpButton.setOnClickListener(this);
    }

    @Override
    public void onConnectionOpened() {

    }

    @Override
    public void onDataSent() {

    }

    @Override
    public void onResponse(int responseCode, String responseText) {
        showToast(responseText);
    }

    @Override
    public void onClick(View view) {
        String email = mEmailEntry.getText().toString();
        String password = mPasswordEntry.getText().toString();
        switch (view.getId()) {
            case R.id.button_sign_in:
                if (!isEmailValid(email)) {
                    showToast("You need to enter a valid email to login.");
                    return;
                } else if (!isPasswordValid(password)) {
                    showToast("Please enter a password to login.");
                    return;
                }
                login(email, password);
                break;
            case R.id.button_sign_up:
                break;
        }
    }

    private void login(String email, String password) {
        String loginData = getLoginString(email, password);
        Requests requests = new Requests(getApplicationContext(), Constants.ENDPOINT_LOGIN);
        requests.setHttpRequestStateChangedListener(this);
        requests.post(Constants.CONTENT_TYPE_JSON, loginData);
    }

    private String getLoginString(String email, String password) {
        JSONObject object = new JSONObject();
        try {
            object.put("email", email);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    private boolean isEmailValid(String email) {
        return !email.isEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        return !password.isEmpty();
    }

    private void showToast(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
