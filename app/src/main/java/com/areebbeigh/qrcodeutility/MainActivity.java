/**
 * Copyright 2016 Areeb Beigh
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.areebbeigh.qrcodeutility;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.areebbeigh.qrcodeutility.fragments.CreateFragment;
import com.areebbeigh.qrcodeutility.fragments.ScanFragment;
import com.areebbeigh.qrcodeutility.fragments.dialogs.HistoryOptionsDialog;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.BaseInputFragment;
import com.google.zxing.Result;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        CreateFragment.CreateFragmentListener {

    private static String TAG = MainActivity.class.getSimpleName();

    // Preferences
    public static boolean BEEP;
    public static boolean VIBRATE;
    public static boolean AUTO_COPY;
    static boolean KEEP_AWAKE;
    public static boolean SHOW_ORIGINAL_PREVIEW;

    // Constant keys for the intent data sent to the DetailActivity
    public static final String ERROR_CORRECTION_LEVEL = "ERROR_CORRECTION_LEVEL";
    public static final String TEXT = "TEXT";
    public static final String CONTAINS_PREVIEW = "CONTAINS_PREVIEW";
    public static String AUTO_COPY_KEY; // Assigned in onCreate()

    public static Result resultObject;
    public static Bitmap imagePreview;
    public static Boolean isInScan;

    public static final int BROWSE_IMAGE_REQUEST_CODE = 1;

    private Fragment fragmentObj;
    private boolean isTorchOn = false;
    private NavigationView navigationView;

    public static DecoratedBarcodeView barcodeView;

    public enum Fragments {
        SCAN_FRAGMENT,
        CREATE_FRAGMENT
    }

    public static Fragments currentFragment;

    @Override
    public int getViewLayout() {
        return R.layout.activity_main;
    }

    @Override
    public boolean shouldIgnoreBackStack() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpDrawer();
        updatePreferences();
        AUTO_COPY_KEY = getString(R.string.pref_auto_copy_key);
        switchFragment(Fragments.SCAN_FRAGMENT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePreferences();
        updateNavigationView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (currentFragment) {
            case SCAN_FRAGMENT:
                getMenuInflater().inflate(R.menu.activity_scan_menu, menu);
                break;
            case CREATE_FRAGMENT:
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.browse_button:
                // Only the scan fragment has a browse option
                ScanFragment f = (ScanFragment) fragmentObj;
                f.browseImage();
                break;
            case R.id.flash_button:
                if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
                    Toast.makeText(this, "Your device does not have a camera flash", Toast.LENGTH_LONG).show();
                    return true;
                }
                toggleFlash();
                break;
        }

        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_scan:
                switchFragment(Fragments.SCAN_FRAGMENT);
                break;
            case R.id.nav_create:
                switchFragment(Fragments.CREATE_FRAGMENT);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.nav_history:
                (new HistoryOptionsDialog()).show(getFragmentManager(), null);
                isInScan = false;
                break;
            case R.id.nav_about:
                Intent aboutIntent = new Intent(this, AboutActivity.class);
                startActivity(aboutIntent);
                break;
            default:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void setInputSection(FragmentManager fragmentManager, int inputSectionID, BaseInputFragment fragment) {
        fragmentManager.beginTransaction().replace(R.id.inputSection, fragment, null).commit();
    }

    @Override
    public Button getCreateButton() {
        return (Button) findViewById(R.id.createButton);
    }

    @Override
    public android.support.v4.app.FragmentManager getSupportFragmentMgr() {
        return getSupportFragmentManager();
    }

    // Release memory
    public static void releaseMemory() {
        if (imagePreview != null) {
            Log.i(TAG, "Released memory");
            imagePreview.recycle();
            imagePreview = null;
        }
    }

    // Updates the preferences
    public void updatePreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        BEEP = sp.getBoolean(getString(R.string.pref_beep_key), Boolean.parseBoolean(getString(R.string.pref_beep_default)));
        VIBRATE = sp.getBoolean(getString(R.string.pref_vibrate_key), Boolean.parseBoolean(getString(R.string.pref_vibrate_default)));
        AUTO_COPY = sp.getBoolean(getString(R.string.pref_auto_copy_key), Boolean.parseBoolean(getString(R.string.pref_auto_copy_default)));
        KEEP_AWAKE = sp.getBoolean(getString(R.string.pref_keep_awake_key), Boolean.parseBoolean(getString(R.string.pref_keep_awake_default)));
        SHOW_ORIGINAL_PREVIEW = sp.getBoolean(getString(R.string.pref_original_img_key), Boolean.parseBoolean(getString(R.string.pref_original_img_default)));
    }

    public void updateNavigationView() {
        if (currentFragment == Fragments.CREATE_FRAGMENT)
            navigationView.setCheckedItem(R.id.nav_create);
        else
            navigationView.setCheckedItem(R.id.nav_scan);
    }

    private void toggleFlash() {
        if (isTorchOn) {
            barcodeView.setTorchOff();
            isTorchOn = false;
        } else {
            barcodeView.setTorchOn();
            isTorchOn = true;
        }
    }

    private void switchFragment(Fragments whichFragment) {
        ViewGroup rootView = (ViewGroup) findViewById(R.id.drawer_layout);

        if (whichFragment == Fragments.SCAN_FRAGMENT) {
            // Manage keep awake preference
            rootView.setKeepScreenOn(KEEP_AWAKE);
            fragmentObj = new ScanFragment();
            currentFragment = Fragments.SCAN_FRAGMENT;
            this.setTitle(getString(R.string.title_activity_Scan));
            isInScan = true;
            navigationView.setCheckedItem(R.id.nav_scan);
        } else {
            rootView.setKeepScreenOn(false);
            fragmentObj = new CreateFragment();
            currentFragment = Fragments.CREATE_FRAGMENT;
            this.setTitle(getString(R.string.title_activity_Create));
            isInScan = false;
            navigationView.setCheckedItem(R.id.nav_create);
        }
        getFragmentManager().beginTransaction().replace(R.id.content_view, fragmentObj).commit();

        this.invalidateOptionsMenu();
    }

    public void setUpDrawer() {
        Toolbar toolbar = setUpToolbar();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }
}

