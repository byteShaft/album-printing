package com.byteshaft.albumsapp.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.utils.AppGlobals;
import com.byteshaft.albumsapp.utils.Constants;
import com.byteshaft.requests.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;

public class UpdateProfile extends Fragment implements View.OnClickListener, HttpRequest.OnReadyStateChangeListener {

    private View mBaseView;
    private HttpRequest mRequest;

    /// buttons
    private Button mDetailsButton;
    private Button mAddressButton;
    private Button mPaymentButton;
    private Button mUpdateButton;

    // text view
    private TextView mNameText;
    private TextView mEmailText;
    private TextView mPhoneText;
    private TextView mPasswordText;
    private TextView mNewPasswordText;

    /// Edit text
    private EditText mNameField;
    private EditText mEmailField;
    private EditText mPhoneField;
    private EditText mPasswordField;
    private EditText mNewPasswordField;

    // Edittext fields Strings

    private String mName;
    private String mEmail;
    private String mPhoneNumber;
    private String mPassword;
    private String mNewPassword;

    private Typeface typeface = AppGlobals.typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView = inflater.inflate(R.layout.fragment_update_profile, container, false);
        /// init buttons refs
        mDetailsButton = (Button) mBaseView.findViewById(R.id.button_detail);
        mAddressButton = (Button) mBaseView.findViewById(R.id.button_address);
        mPaymentButton = (Button) mBaseView.findViewById(R.id.button_payment);
        mUpdateButton = (Button) mBaseView.findViewById(R.id.button_update);

        // buttons listeners
        mDetailsButton.setOnClickListener(this);
        mAddressButton.setOnClickListener(this);
        mPaymentButton.setOnClickListener(this);
        mUpdateButton.setOnClickListener(this);

        /// init Edittext refs
        mNameField = (EditText) mBaseView.findViewById(R.id.et_name);
        mEmailField = (EditText) mBaseView.findViewById(R.id.et_email);
        mPhoneField = (EditText) mBaseView.findViewById(R.id.et_phone);
        mPasswordField = (EditText) mBaseView.findViewById(R.id.et_password);
        mNewPasswordField = (EditText) mBaseView.findViewById(R.id.et_new_password);

        /// init textViews refs
        mNameText = (TextView) mBaseView.findViewById(R.id.tv_name);
        mEmailText = (TextView) mBaseView.findViewById(R.id.tv_emali);
        mPhoneText = (TextView) mBaseView.findViewById(R.id.tv_phone);
        mPasswordText = (TextView) mBaseView.findViewById(R.id.tv_password);
        mNewPasswordText = (TextView) mBaseView.findViewById(R.id.tv_new_password);

        // set typeface to views
        setTyoeface();

        return mBaseView;
    }

    private void setTyoeface() {
        mDetailsButton.setTypeface(typeface);
        mAddressButton.setTypeface(typeface);
        mPaymentButton.setTypeface(typeface);
        mUpdateButton.setTypeface(typeface);

        mNameField.setTypeface(typeface);
        mEmailField.setTypeface(typeface);
        mPhoneField.setTypeface(typeface);
        mPasswordField.setTypeface(typeface);
        mNewPasswordField.setTypeface(typeface);

        mNameText.setTypeface(typeface);
        mEmailText.setTypeface(typeface);
        mPhoneText.setTypeface(typeface);
        mPasswordText.setTypeface(typeface);
        mNewPasswordText.setTypeface(typeface);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_detail:
                System.out.println("clicked");
                break;
            case R.id.button_address:
                System.out.println("clicked");
                break;
            case R.id.button_payment:
                System.out.println("clicked");
                break;
            case R.id.button_update:
                System.out.println("clicked");
                validateUpdateData();
                break;
        }
    }

    private void validateUpdateData() {
        mName = mNameField.getText().toString();
        mEmail = mEmailField.getText().toString();
        mPhoneNumber = mPhoneField.getText().toString();
        mPassword = mPasswordField.getText().toString();
        mNewPassword = mNewPasswordField.getText().toString();

        EditText[] inputFields = {
                mNameField,
                mEmailField,
                mPhoneField,
                mPasswordField,
                mNewPasswordField
        };

        for (EditText editText: inputFields) {
            raiseFieldMandatory(editText);
        }

        if (mName.isEmpty()) {
            mNameField.setError("Required field.");
        }


        if (mPhoneNumber.isEmpty()) {
            mPhoneField.setError("Required field.");
        }

        if (!isEmailValid(mEmail)) {
            mEmailField.setError("Invalid Email.");
            return;
        }

        if (!mPassword.equals(mNewPassword)) {
            mNewPasswordField.setError("Passwords should be same.");
            return;
        }

        updateProfile(mEmail, mPassword, mName, mPhoneNumber);
    }

    private String getUpdateData(
            String email,
            String password,
            String fullName,
            String mobile
    ) {
        JSONObject object = new JSONObject();
        try {
            object.put("full_name", fullName);
            object.put("email", email);
            object.put("mobile_number", mobile);
            object.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object.toString();
    }

    private void updateProfile(String email, String password, String name, String phone) {
            Log.i("TAG", "sending request");
            mRequest = new HttpRequest(getContext());
            mRequest.setOnReadyStateChangeListener(this);
            mRequest.open("PUT", Constants.ENDPOINT_REGISTER);
            mRequest.send(getUpdateData(email, password, name, phone));
            Log.i("TAG", "sent request");
    }

    private void raiseFieldMandatory(EditText editText) {
        if (isEditTextEmpty(editText)) {
            editText.setError("Required field.");
        }
    }

    private boolean isEditTextEmpty(EditText editText) {
        String text = editText.getText().toString();
        return text.isEmpty();
    }

    private boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public void onReadyStateChange(HttpURLConnection connection, int requestType, int readyState) {
    }
}
