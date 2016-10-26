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

package com.areebbeigh.qrcodeutility.fragments.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.listadapters.TwoItemListAdapter;

import java.util.ArrayList;

public class LicenseDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int[][] android = {
                // {title, license, url}
                {R.string.android_title, R.string.android_license, R.string.android_uri},
                {R.string.zxing_title, R.string.zxing_license, R.string.zxing_uri},
                {R.string.zxing_embedded_title, R.string.zxing_embedded_license, R.string.zxing_embedded_uri},
                {R.string.vintage_chroma_title, R.string.vintage_chroma_license, R.string.vintage_chroma_uri}
        };

        ArrayList<String[]> temp = new ArrayList<>();

        for (int[] array : android) {
            String[] s = new String[3];
            int index = 0;
            for (int i : array) {
                s[index] = getString(i);
                index++;
            }
            temp.add(s);
        }

        final ArrayList<String[]> dialogOptions = new ArrayList<>(temp);

        return new AlertDialog.Builder(getActivity())
                .setTitle("Licenses")
                .setAdapter(new TwoItemListAdapter(getActivity(), dialogOptions),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                String[] items = dialogOptions.get(position);
                                Uri uri = Uri.parse(items[2]);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                startActivity(intent);
                            }
                        }).show();
    }
}
