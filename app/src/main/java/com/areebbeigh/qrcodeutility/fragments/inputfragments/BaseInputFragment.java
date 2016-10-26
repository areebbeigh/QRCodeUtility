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

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.fragments.CreateFragment;
import com.areebbeigh.qrcodeutility.generator.QRCode;
import com.areebbeigh.qrcodeutility.util.ActivityHelper;
import com.google.zxing.ChecksumException;
import com.google.zxing.EncodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;

import static com.areebbeigh.qrcodeutility.fragments.CreateFragment.foregroundColor;

public abstract class BaseInputFragment extends Fragment {
    private View view;
    private final String TAG = BaseInputFragment.class.getSimpleName();

    CreateFragment.CreateFragmentListener createActivityCommander;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createActivityCommander = (CreateFragment.CreateFragmentListener) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(getViewLayout(), container, false);
        createActivityCommander.getCreateButton().setOnClickListener(getCreateButtonListener());
        //Button createButton = (Button) view.findViewById(R.id.createButton);
        //createButton.setOnClickListener(getCreateButtonListener());
        // TODO: Use the create button in the toolbar instead of a simple button
        //Log.i("com.areebbeigh", findViewById(R.id.create_button).toString());
        return view;
    }

    public void showQRCode(QRCode qrCode) {
        Bitmap bitmap;

        // Get the error correction level
        ErrorCorrectionLevel ecl = ErrorCorrectionLevel.values()[
                Integer.parseInt(
                        PreferenceManager
                                .getDefaultSharedPreferences(getActivity())
                                .getString(getString(R.string.pref_ecl_key),
                                        getString(R.string.pref_ecl_default)))];

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, ecl);

        try {
            bitmap = qrCode.getSimpleBitmap(foregroundColor, hints); // The color of the QR Code foreground
        } catch (WriterException e) {
            Toast.makeText(getActivity(), "Couldn't generate QR Code", Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return;
        }

        ActivityHelper activityHelper = new ActivityHelper(getActivity());

        try {
            activityHelper.launchDetailActivity(bitmap, activityHelper.scanBitmap(bitmap), true);
        } catch (NotFoundException e) {
            Snackbar.make(view, getActivity().getString(R.string.invalid_color_barcode), Snackbar.LENGTH_LONG).show();
            e.printStackTrace();
            bitmap.recycle();
            bitmap = null;
        } catch (ChecksumException | FormatException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Something went wrong, try restarting the app", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Returns the layout resource of this fragment
     */

    public abstract int getViewLayout();

    /**
     * Returns the listener for the create button
     */
    public abstract Button.OnClickListener getCreateButtonListener();

    public View findViewById(int id) {
        return view.findViewById(id);
    }

}
