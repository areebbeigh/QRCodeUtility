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

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.generator.QRCode;
import com.areebbeigh.qrcodeutility.generator.schemes.VCard;

public class ContactInputFragment extends BaseInputFragment {

    @Override
    public int getViewLayout() {
        return R.layout.input_contact;
    }

    @Override
    public Button.OnClickListener getCreateButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String name = ((EditText) findViewById(R.id.contact_name)).getText().toString();
                String org = ((EditText) findViewById(R.id.contact_org)).getText().toString();
                String title = ((EditText) findViewById(R.id.contact_title)).getText().toString();
                String tel = ((EditText) findViewById(R.id.contact_tel)).getText().toString();
                String website = ((EditText) findViewById(R.id.contact_website)).getText().toString();
                String email = ((EditText) findViewById(R.id.contact_email)).getText().toString();
                String address = ((EditText) findViewById(R.id.contact_address)).getText().toString();

                VCard vCard = new VCard(name)
                        .setCompany(org)
                        .setTitle(title)
                        .setPhoneNumber(tel)
                        .setWebsite(website)
                        .setEmail(email)
                        .setAddress(address);

                QRCode qrCode = new QRCode(vCard);
                showQRCode(qrCode);
            }
        };
    }
}