package com.byteshaft.albumsapp.utils.ui;

import android.widget.Toast;

import com.byteshaft.albumsapp.utils.AppGlobals;

public class Helpers {

    public static void showToast(String text) {
        Toast.makeText(AppGlobals.getContext(), text, Toast.LENGTH_SHORT).show();
    }

}
