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

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.areebbeigh.qrcodeutility.R;
import com.areebbeigh.qrcodeutility.listadapters.HistoryListAdapter;
import com.areebbeigh.qrcodeutility.util.ActivityHelper;
import com.areebbeigh.qrcodeutility.util.HistoryObject;
import com.google.zxing.ChecksumException;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.WriterException;

import java.util.ArrayList;

public abstract class BaseHistoryFragment extends Fragment {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        ListView listView = (ListView) findViewById(R.id.history_list);
        listView.setAdapter(new HistoryListAdapter(getActivity(), getItems()));

        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        HistoryObject item = (HistoryObject) parent.getItemAtPosition(position);

                        ActivityHelper helper = new ActivityHelper(getActivity());
                        Bitmap bmap = null;
                        try {
                            bmap = item.getBitmap();
                        } catch (WriterException e) {
                            e.printStackTrace();
                            Log.i("com.areebbeigh", e.getLocalizedMessage());
                        }

                        try {
                            helper.launchDetailActivity(
                                    bmap,
                                    helper.scanBitmap(bmap),
                                    false
                            );
                        } catch (FormatException | ChecksumException | NotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        return view;
    }

    /**
     * Returns a list of items to populate the history list with
     */
    public abstract ArrayList<HistoryObject> getItems();

    /**
     * Same as findViewById() in activities except its scope is only in the fragment's view
     */
    public View findViewById(int id) {
        return view.findViewById(id);
    }

    public View getView() {
        return view;
    }
}
