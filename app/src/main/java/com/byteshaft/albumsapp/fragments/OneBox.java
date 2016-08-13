package com.byteshaft.albumsapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.byteshaft.albumsapp.R;

public class OneBox extends Fragment {

    private View mBaseView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBaseView  = inflater.inflate(R.layout.fragment_one_box, container, false);
        return mBaseView;

    }

}
