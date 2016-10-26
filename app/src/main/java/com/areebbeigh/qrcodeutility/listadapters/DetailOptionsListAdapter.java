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
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.areebbeigh.qrcodeutility.R;

import java.util.HashMap;

public class DetailOptionsListAdapter extends ArrayAdapter<String> {
    private HashMap<String, Integer> map = new HashMap<>();

    public DetailOptionsListAdapter(Context context, String[] objects) {
        super(context, R.layout.list_item, objects);

        // Options that don't have an icon should use -1 as drawable ID
        map.put(context.getString(R.string.button_add_calendar), R.drawable.ic_calendar);
        map.put(context.getString(R.string.button_add_contact), R.drawable.ic_add_contact);
        map.put(context.getString(R.string.button_web_search), R.drawable.ic_search);
        map.put(context.getString(R.string.button_dial), R.drawable.ic_dial);
        map.put(context.getString(R.string.button_email), R.drawable.ic_email);
        map.put(context.getString(R.string.button_open_browser), R.drawable.ic_browse);
        map.put(context.getString(R.string.button_share_by_email), R.drawable.ic_email);
        map.put(context.getString(R.string.button_email), R.drawable.ic_email);
        map.put(context.getString(R.string.button_share_by_sms), R.drawable.ic_sms);
        map.put(context.getString(R.string.button_sms), R.drawable.ic_sms);
        map.put(context.getString(R.string.button_mms), R.drawable.ic_mms);
        map.put(context.getString(R.string.button_wifi), R.drawable.ic_wifi);
        map.put(context.getString(R.string.button_show_map), R.drawable.ic_location);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list_item, parent, false);

        String title = getItem(position);
        TextView textView = (TextView) customView.findViewById(R.id.list_item_title);
        ImageView iconView = (ImageView) customView.findViewById(R.id.list_item_vector_icon);
        textView.setText(title);
        int drawableID = map.get(title);

        if (drawableID != -1) {
            // TODO: IDK why the F-word does this throw a resource not found exception here on API 16 and maybe lower
            try {
                iconView.setImageDrawable(ContextCompat.getDrawable(getContext(), drawableID));
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
                // nothing, you just wont get the awesome icons on the phone :(
            }
        }

        return customView;
    }
}
