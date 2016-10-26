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

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.areebbeigh.qrcodeutility.generator.QRCode;
import com.areebbeigh.qrcodeutility.listadapters.DetailOptionsListAdapter;
import com.areebbeigh.qrcodeutility.zxingadditional.result.ResultHandler;
import com.areebbeigh.qrcodeutility.zxingadditional.result.ResultHandlerFactory;
import com.google.zxing.WriterException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.areebbeigh.qrcodeutility.MainActivity.AUTO_COPY_KEY;
import static com.areebbeigh.qrcodeutility.MainActivity.CONTAINS_PREVIEW;
import static com.areebbeigh.qrcodeutility.MainActivity.ERROR_CORRECTION_LEVEL;
import static com.areebbeigh.qrcodeutility.MainActivity.TEXT;
import static com.areebbeigh.qrcodeutility.MainActivity.imagePreview;
import static com.areebbeigh.qrcodeutility.MainActivity.isInScan;
import static com.areebbeigh.qrcodeutility.MainActivity.releaseMemory;
import static com.areebbeigh.qrcodeutility.MainActivity.resultObject;

public class DetailActivity extends BaseActivity {
    private final String TAG = BaseActivity.class.getSimpleName();

    @Override
    public int getViewLayout() {
        return R.layout.activity_detail;
    }

    @Override
    public boolean shouldIgnoreBackStack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setUpToolbar();

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.title_activity_Detail));

        ImageView imagePreviewView = (ImageView) findViewById(R.id.imagePreview);
        TextView resultText = (TextView) findViewById(R.id.resultText);
        TextView contentTypeText = (TextView) findViewById(R.id.contentTypeTextView);
        TextView additionalInfo = (TextView) findViewById(R.id.additionalInfoTextView);
        final ScrollView scrollView = (ScrollView) findViewById(R.id.scroll_view);

        // Enables scrolling of the TextView
        resultText.setMovementMethod(new ScrollingMovementMethod());

        resultText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        Bundle extras = getIntent().getExtras();
        String text = extras.getString(TEXT);

        if (extras.getBoolean(CONTAINS_PREVIEW)) {
            imagePreviewView.setImageBitmap(imagePreview);
        } else {
            // Generate a new QR Code instead
            QRCode prevQR = new QRCode(text);
            try {
                imagePreview = prevQR.getSimpleBitmap(null);
                imagePreviewView.setImageBitmap(imagePreview);
            } catch (WriterException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.image_preview_error), Toast.LENGTH_LONG).show();
            }
        }

        final ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(this, resultObject);

        resultText.setText(text);
        contentTypeText.setText(resultHandler.getDisplayTitle());
        additionalInfo.append(" " + extras.getString(ERROR_CORRECTION_LEVEL));

        if (isInScan && extras.getBoolean(AUTO_COPY_KEY)) {
            // Copy the text automatically only if the user scanned a barcode himself, this is needed
            // all other components that need to show a barcode's details use this activity
            copyResult();
        }

        // Create the buttons
        ListView optionsMenu = (ListView) findViewById(R.id.optionsMenu);

        int menuItemsCount = resultHandler.getButtonCount();
        final String[] menuItems = new String[menuItemsCount];

        for (int i = 0; i < menuItemsCount; i++) {
            menuItems[i] = getString(resultHandler.getButtonText(i));
        }

        ListAdapter menuAdapter = new DetailOptionsListAdapter(this, menuItems);
        optionsMenu.setAdapter(menuAdapter);
        optionsMenu.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        resultHandler.handleButtonPress(i);
                    }
                }
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseMemory();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.toolbar_copy_button:
                copyResult();
                break;
            case R.id.toolbar_save_button:
                storeImage(imagePreview);
                break;
        }
        return true;
    }

    private void storeImage(Bitmap image) {
        File file = getOutputMediaFile();
        if (file == null) {
            Log.d(TAG, "Couldn't get media file, check writable permissions");
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 100, fos);
            Toast.makeText(this, "Saved as " + file.toString(), Toast.LENGTH_LONG).show();
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    /**
     * Create a File for saving an image or video
     */
    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/QR Code Utility/Saves");
        Log.i("com.areebbeigh", mediaStorageDir.toString());

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "IMG_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void copyResult() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("QR Code Data", resultObject.getText());
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, getString(R.string.copy_success_msg), Toast.LENGTH_LONG).show();
    }
}
