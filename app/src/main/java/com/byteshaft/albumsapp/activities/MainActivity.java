package com.byteshaft.albumsapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.WorkerThread;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.byteshaft.albumsapp.R;
import com.byteshaft.albumsapp.fragments.CreateAlbum;
import com.byteshaft.albumsapp.fragments.Printing;
import com.byteshaft.albumsapp.fragments.UpdateProfile;
import com.byteshaft.albumsapp.utils.AppGlobals;
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
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    public static TextView sToolbarTitle;
    private int[] mTabIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // icons for tabs
        mTabIcons = new int[]{
                R.mipmap.ic_single_box,
                R.mipmap.ic_all_box,
                R.mipmap.profile
        };

        Log.i("TAG", "activation "+ Config.isUserActive() + " user reg " + Config.isUSerRegister());
        if (!Config.isUserActive() && Config.isUSerRegister()) {
            startActivity(new Intent(getApplicationContext(), ActivateAccount.class));
            finish();
            return;

        } else if (!Config.isLoggedIn()) {
            startActivity(new Intent(getApplicationContext(), SignIn.class));
            finish();
            return;
        }
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        sToolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        sToolbarTitle.setTypeface(AppGlobals.typeface);
        sToolbarTitle.setText("Single Box");
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        final TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(mTabIcons[0]);
        tabLayout.getTabAt(1).setIcon(mTabIcons[1]);
        tabLayout.getTabAt(2).setIcon(mTabIcons[2]);
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mViewPager.setCurrentItem(tab.getPosition(), true);
                        sToolbarTitle.setText("Single Box");
                        break;
                    case 1:
                        mViewPager.setCurrentItem(tab.getPosition(), true);
                        sToolbarTitle.setText("All Box");
                        break;
                    case 2:
                        mViewPager.setCurrentItem(tab.getPosition(), true);
                        sToolbarTitle.setText("My Profile");
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

//        TextView welcomeText = (TextView) findViewById(R.id.text_view_title_main_screen);
//        welcomeText.setText("Welcome " + Config.getFullName());
//        String externalPath = Environment.getExternalStorageDirectory().getAbsolutePath();
//        String filePath = externalPath + "/ok.png";
//        final File file = new File(filePath);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                addPhoto(file);
//            }
//        }).start();
//        createAlbum("Test Album");
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

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new CreateAlbum();
                case 1:
                    return new Printing();
                case 2:
                    return new UpdateProfile();
                default:
                    return new CreateAlbum();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
//                    return "SECTION 1";
                case 1:
//                    return "SECTION 2";
                case 2:
//                    return "SECTION 3";
            }
            return null;
        }
    }
}
