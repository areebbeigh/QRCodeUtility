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

package com.areebbeigh.qrcodeutility.fragments.inputfragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.generator.QRCode;
import com.areebbeigh.qrcodeutility.generator.schemes.WiFi;

public class WiFiInputFragment extends BaseInputFragment {
    private String security;

    @Override
    public int getViewLayout() {
        return R.layout.input_wifi;
    }

    @Override
    public Button.OnClickListener getCreateButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid = ((EditText) findViewById(R.id.input_wifi_ssid)).getText().toString();
                String passwd = "";

                if (security != "")
                    passwd = ((EditText) findViewById(R.id.input_wifi_password)).getText().toString();

                WiFi wifi = new WiFi()
                        .setSsid(ssid)
                        .setPassword(passwd)
                        .setType(security);

                QRCode qrCode = new QRCode(wifi);
                showQRCode(qrCode);
            }
        };
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = super.onCreateView(inflater, container, savedInstanceState);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.wifi_radio_group);
        radioGroup.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        View passwdInput = findViewById(R.id.input_wifi_password);
                        switch (checkedId) {
                            case R.id.open: // Nice guy :) or maybe not..
                                passwdInput.setEnabled(false);
                                security = "";
                                break;
                            case R.id.wpa:
                                security = "WPA";
                            case R.id.wep:
                                security = "WEP";
                            default:
                                passwdInput.setEnabled(true);
                                break;
                        }
                    }
                }
        );
        return v;
    }
}
