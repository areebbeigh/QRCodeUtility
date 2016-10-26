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

package com.areebbeigh.qrcodeutility.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;

import com.areebbeigh.qrcodeutility.DetailActivity;
import com.areebbeigh.qrcodeutility.data.HistoryDBHandler;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.ResultMetadataType;
import com.google.zxing.common.HybridBinarizer;

import static com.areebbeigh.qrcodeutility.MainActivity.AUTO_COPY;
import static com.areebbeigh.qrcodeutility.MainActivity.AUTO_COPY_KEY;
import static com.areebbeigh.qrcodeutility.MainActivity.CONTAINS_PREVIEW;
import static com.areebbeigh.qrcodeutility.MainActivity.ERROR_CORRECTION_LEVEL;
import static com.areebbeigh.qrcodeutility.MainActivity.Fragments;
import static com.areebbeigh.qrcodeutility.MainActivity.SHOW_ORIGINAL_PREVIEW;
import static com.areebbeigh.qrcodeutility.MainActivity.TEXT;
import static com.areebbeigh.qrcodeutility.MainActivity.currentFragment;
import static com.areebbeigh.qrcodeutility.MainActivity.imagePreview;
import static com.areebbeigh.qrcodeutility.MainActivity.releaseMemory;
import static com.areebbeigh.qrcodeutility.MainActivity.resultObject;
import static com.areebbeigh.qrcodeutility.fragments.CreateFragment.foregroundColor;

/**
 * A helper class that provides some communication methods between fragments and activities
 */
public class ActivityHelper extends ActivityCompat {
    private Context context;

    public ActivityHelper(Context context) {
        this.context = context;
    }

    public Result scanBitmap(Bitmap bitmap) throws FormatException, ChecksumException, NotFoundException {
        Reader reader = new MultiFormatReader();
        Result result;

        int[] intArray = new int[bitmap.getWidth() * bitmap.getHeight()];
        //copy pixel data from the Bitmap into the 'intArray' array
        bitmap.getPixels(intArray, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bitmap.getWidth(), bitmap.getHeight(), intArray);
        BinaryBitmap bMap = new BinaryBitmap(new HybridBinarizer(source));

        result = reader.decode(bMap);

        return result;
    }

    /**
     * Launches the Detail Activity with the given information
     */
    public void launchDetailActivity(Bitmap bitmap, Result result, Boolean addToHistory) {
        String text = result.getText();
        String ecl;

        try {
            ecl = result.getResultMetadata().get(ResultMetadataType.ERROR_CORRECTION_LEVEL).toString();
        } catch (NullPointerException e) {
            ecl = "Unknown";
        }

        resultObject = result;

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(ERROR_CORRECTION_LEVEL, ecl);
        intent.putExtra(TEXT, text);
        intent.putExtra(AUTO_COPY_KEY, AUTO_COPY);

        imagePreview = bitmap;

        if (!SHOW_ORIGINAL_PREVIEW) {
            releaseMemory();
        }

        if (imagePreview != null)
            intent.putExtra(CONTAINS_PREVIEW, true);
        else
            intent.putExtra(CONTAINS_PREVIEW, false);

        context.startActivity(intent);

        if (addToHistory) {
            HistoryDBHandler dbHandler = new HistoryDBHandler(context, null, null, 0);

            if (currentFragment == Fragments.SCAN_FRAGMENT) {
                for (String[] array : dbHandler.scanDatabaseToList()) {
                    if (array[0].equals(text))
                        return;
                }
                dbHandler.addScanItem(text, ecl);
            } else if (currentFragment == Fragments.CREATE_FRAGMENT) {
                for (String[] array : dbHandler.createDatabaseToList()) {
                    if (array[0].equals(text))
                        return;
                }
                dbHandler.addCreateItem(text, ecl, foregroundColor);
            }
        }
    }

}
