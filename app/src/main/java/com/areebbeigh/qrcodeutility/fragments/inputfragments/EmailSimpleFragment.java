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
import com.areebbeigh.qrcodeutility.generator.schemes.Email;

public class EmailSimpleFragment extends BaseInputFragment {
    @Override
    public int getViewLayout() {
        return R.layout.input_email_simple;
    }

    @Override
    public Button.OnClickListener getCreateButtonListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mailto = ((EditText) findViewById(R.id.mailto)).getText().toString();

                Email email = new Email(Email.MailType.SIMPLE).setTo(mailto);

                QRCode qrCode = new QRCode(email);
                showQRCode(qrCode);
            }
        };
    }
}
