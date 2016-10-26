/**
 * Copyright 2016 Areeb Beigh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.areebbeigh.qrcodeutility.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.BaseInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.ContactInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.EmailComprehensiveFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.EmailSimpleFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.GeolocationInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.PhoneInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.SMSInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.TextInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.URLInputFragment;
import com.areebbeigh.qrcodeutility.fragments.inputfragments.WiFiInputFragment;
import com.areebbeigh.qrcodeutility.generator.QRCode;
import com.pavelsikun.vintagechroma.ChromaDialog;
import com.pavelsikun.vintagechroma.IndicatorMode;
import com.pavelsikun.vintagechroma.OnColorSelectedListener;
import com.pavelsikun.vintagechroma.colormode.ColorMode;

// TODO: Make the create fragment layout scrollable

public class CreateFragment extends BaseFragment {

    private static final String TEXT = "Text";
    private static final String URL = "URL";
    private static final String PHONE = "Phone";
    private static final String SMS = "SMS";
    private static final String EMAIL_SIMPLE = "E-Mail 1";
    private static final String EMAIL_COMPREHENSIVE = "E-Mail 2";
    private static final String CONTACT = "Contact";
    private static final String GEOLOCATION = "Geolocation";
    private static final String WIFI = "WiFi";

    public static int foregroundColor = QRCode.DEFAULT_FG;

    private Button chooserButton;
    private Button colorPickerButton;

    static String[] contentOptions = {
            TEXT,
            URL,
            PHONE,
            SMS,
            EMAIL_SIMPLE,
            EMAIL_COMPREHENSIVE,
            CONTACT,
            GEOLOCATION,
            WIFI,
    };

    // The type of content to put in the QR Code
    static String barcodeContent = TEXT;

    static CreateFragmentListener activityCommander;

    public interface CreateFragmentListener {
        /**
         * Sets (or replaces) the input section of the create fragment to the given layout
         */
        void setInputSection(FragmentManager fragmentManager, int inputSectionID, BaseInputFragment fragment);

        /**
         * Get a reference to the create button in the create fragment
         */
        Button getCreateButton();

        /**
         * Returns a FragmentManager instance from the parent activity
         */
        android.support.v4.app.FragmentManager getSupportFragmentMgr();
    }

    @Override
    public int getViewLayout() {
        return R.layout.fragment_create;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityCommander = (CreateFragmentListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        chooserButton = (Button) findViewById(R.id.chooserButton);
        chooserButton.setText(barcodeContent);

        chooserButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        (new CreateFragment.BarcodeContentChooserFragment()).show(getFragmentManager(), null);
                    }
                }
        );

        colorPickerButton = (Button) findViewById(R.id.color_picker_button);

        // The color chooser for the QR Code
        colorPickerButton.setBackgroundColor(foregroundColor);
        colorPickerButton.setText("");
        colorPickerButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ChromaDialog.Builder()
                                .initialColor(foregroundColor)
                                .colorMode(ColorMode.ARGB)
                                .indicatorMode(IndicatorMode.DECIMAL)
                                .onColorSelected(new OnColorSelectedListener() {
                                    @Override
                                    public void onColorSelected(@ColorInt int color) {
                                        colorPickerButton.setBackgroundColor(color);
                                        foregroundColor = color;
                                    }
                                })
                                .create()
                                .show(activityCommander.getSupportFragmentMgr(), "ChromaDialog");
                    }
                }
        );

        activityCommander.setInputSection(getFragmentManager(), R.layout.input_text, new TextInputFragment());
        return getView();
    }

    @SuppressLint("ValidFragment")
    public class BarcodeContentChooserFragment extends DialogFragment {

        public BarcodeContentChooserFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            ListAdapter listAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, contentOptions);

            return new AlertDialog.Builder(getActivity())
                    .setTitle("Choose the QR Code content type")
                    .setAdapter(listAdapter,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int position) {
                                    barcodeContent = contentOptions[position];
                                    chooserButton.setText(barcodeContent);
                                    BaseInputFragment fragment;
                                    switch (barcodeContent) {
                                        case TEXT:
                                            fragment = new TextInputFragment();
                                            break;
                                        case CONTACT:
                                            fragment = new ContactInputFragment();
                                            break;
                                        case EMAIL_SIMPLE:
                                            fragment = new EmailSimpleFragment();
                                            break;
                                        case EMAIL_COMPREHENSIVE:
                                            fragment = new EmailComprehensiveFragment();
                                            break;
                                        case GEOLOCATION:
                                            fragment = new GeolocationInputFragment();
                                            break;
                                        case PHONE:
                                            fragment = new PhoneInputFragment();
                                            break;
                                        case URL:
                                            fragment = new URLInputFragment();
                                            break;
                                        case SMS:
                                            fragment = new SMSInputFragment();
                                            break;
                                        case WIFI:
                                            fragment = new WiFiInputFragment();
                                            break;
                                        default:
                                            fragment = new TextInputFragment(); // Others get the lousy text
                                            break;
                                    }
                                    activityCommander.setInputSection(getFragmentManager(), R.id.inputSection, fragment);
                                }
                            })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Nothing here
                        }
                    }).show();
        }
    }
}