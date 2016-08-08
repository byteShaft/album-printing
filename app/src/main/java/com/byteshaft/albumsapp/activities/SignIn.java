package com.byteshaft.albumsapp.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.Config;
import com.byteshaft.albumsapp.utils.Constants;
import com.byteshaft.albumsapp.utils.network.HttpRequest;
import com.byteshaft.albumsapp.utils.network.HttpRequestStateListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.HttpURLConnection;

import static com.byteshaft.albumsapp.utils.ui.Helpers.showToast;

public class SignIn extends AppCompatActivity implements View.OnClickListener,
        HttpRequestStateListener {

    private EditText mEmailEntry;
    private EditText mPasswordEntry;
    private Button mSignInButton;
    private Button mSignUpButton;

    private HttpRequest mHttp;

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
                startActivity(new Intent(getApplicationContext(), SignUp.class));
                finish();
                break;
        }
    }

    private void login(String email, String password) {
        String loginData = getLoginString(email, password);
        mHttp = new HttpRequest(getApplicationContext());
        mHttp.setOnReadyStateChangedListener(this);
        mHttp.open("POST", Constants.ENDPOINT_LOGIN);
        mHttp.send(loginData);
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

    @Override
    public void onReadyStateChanged(HttpURLConnection connection, int readyState) {
        switch (readyState) {
            case HttpRequest.STATE_DONE:
                try {
                    switch (connection.getResponseCode()) {
                        case HttpURLConnection.HTTP_OK:
                            Config.saveUserProfile(mHttp.getResponseText());
                            Config.setIsLoggedIn(true);
                            finish();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            break;
                        case HttpURLConnection.HTTP_UNAUTHORIZED:
                            showToast("Wrong email or password");
                            break;
                        case HttpURLConnection.HTTP_FORBIDDEN:
                            showAccountNotActiveDialog();
                            break;
                        case HttpURLConnection.HTTP_NOT_FOUND:
                            showToast("Account does not exist.");
                            break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showAccountNotActiveDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setTitle("Account not active.");
        dialogBuilder.setPositiveButton("Activate now...", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                startActivity(new Intent(getApplicationContext(), ActivateAccount.class));
            }
        });
        dialogBuilder.setNegativeButton("Not now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.show();
    }
}
