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

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.util.ActivityHelper;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.android.BeepManager;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.IOException;
import java.util.List;

import static com.areebbeigh.qrcodeutility.MainActivity.BEEP;
import static com.areebbeigh.qrcodeutility.MainActivity.BROWSE_IMAGE_REQUEST_CODE;
import static com.areebbeigh.qrcodeutility.MainActivity.SHOW_ORIGINAL_PREVIEW;
import static com.areebbeigh.qrcodeutility.MainActivity.VIBRATE;
import static com.areebbeigh.qrcodeutility.MainActivity.barcodeView;
import static com.areebbeigh.qrcodeutility.MainActivity.imagePreview;
import static com.areebbeigh.qrcodeutility.MainActivity.releaseMemory;

public class ScanFragment extends BaseFragment {
    private ActivityHelper activityHelper;

    @Override
    public int getViewLayout() {
        return R.layout.fragment_scan;
    }

    private BarcodeCallback callback = new BarcodeCallback() {
        @Override
        public void barcodeResult(BarcodeResult result) {
            if (result.getText() != null) {
                BeepManager bm = new BeepManager(getActivity());
                bm.setVibrateEnabled(true);

                if (!BEEP) {
                    bm.setBeepEnabled(false);
                }

                if (!VIBRATE) {
                    bm.setVibrateEnabled(false);
                }

                bm.updatePrefs();
                bm.playBeepSoundAndVibrate();

                if (imagePreview == null && SHOW_ORIGINAL_PREVIEW)
                    imagePreview = result.getBitmap();

                activityHelper.launchDetailActivity(imagePreview, result.getResult(), true);
            }
        }

        @Override
        public void possibleResultPoints(List<ResultPoint> resultPoints) {
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        barcodeView = (DecoratedBarcodeView) findViewById(R.id.barcode_scanner);
        barcodeView.setStatusText(getString(R.string.scan_activity_help));
        activityHelper = new ActivityHelper(getActivity());
        return getView();
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.decodeSingle(callback);
        barcodeView.resume();
        Log.i("com.areebbeigh", "Resumed scan");
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause();
        Log.i("com.areebbeigh", "Paused scan");
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeView.onKeyDown(keyCode, event) || getActivity().onKeyDown(keyCode, event);
    }

    // Get the results:
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BROWSE_IMAGE_REQUEST_CODE && data != null && data.getData() != null) {
            Uri uri = data.getData();

            try {
                imagePreview = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
            } catch (IOException e) {
                rollback("");
                return;
            }

            Result result;

            try {
                result = activityHelper.scanBitmap(imagePreview);
            } catch (Exception e) {
                rollback("");
                releaseMemory();
                return;
            }

            int width = getResources().getDimensionPixelSize(R.dimen.qr_detail_icon_width);
            int height = getResources().getDimensionPixelSize(R.dimen.qr_detail_icon_height);
            imagePreview = Bitmap.createScaledBitmap(imagePreview, width, height, true);

            try {
                callback.barcodeResult(new BarcodeResult(result, null));
            }
            catch (Exception e) {
                Toast.makeText(getActivity(), R.string.scan_error_msg, Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }

    private void rollback(String message) {
        if (message.equals(""))
            message = getString(R.string.scan_error_msg);
        Toast.makeText(getActivity(), message, Toast.LENGTH_LONG).show();
        releaseMemory();
    }

    // Select an image from gallery
    public void browseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select the image with the QR Code"), BROWSE_IMAGE_REQUEST_CODE);
    }
}
