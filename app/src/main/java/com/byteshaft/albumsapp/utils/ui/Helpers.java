package com.byteshaft.albumsapp.utils.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.byteshaft.albumsapp.utils.AppGlobals;

public class Helpers {

    private static ProgressDialog progressDialog;

    public static void showToast(String text) {
        Toast.makeText(AppGlobals.getContext(), text, Toast.LENGTH_SHORT).show();
    }


    public static void showProgressDialog(Activity activity, String message) {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }

    public static void dismissProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
