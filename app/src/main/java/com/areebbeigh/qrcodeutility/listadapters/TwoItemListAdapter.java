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

package com.areebbeigh.qrcodeutility.listadapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.areebbeigh.qrcodeutility.R;

import java.util.ArrayList;

/**
 * This list adapter is used by the about, licenses fragments and other lists which require two item lists
 */
public class TwoItemListAdapter extends ArrayAdapter<String[]> {
    public TwoItemListAdapter(Context context, ArrayList<String[]> objects) {
        super(context, R.layout.two_line_list_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.two_line_list_item, parent, false);

        String title = null;
        String summary = null;

        try {
            title = getItem(position)[0];
            summary = getItem(position)[1];
        }
        catch (NullPointerException e) {
            // Pass
        }

        TextView titleView = (TextView) view.findViewById(R.id.text1);
        TextView summaryView = (TextView) view.findViewById(R.id.text2);

        if (title != null)
            titleView.setText(title);
        if (summary != null)
            summaryView.setText(summary);

        return view;
    }
}
