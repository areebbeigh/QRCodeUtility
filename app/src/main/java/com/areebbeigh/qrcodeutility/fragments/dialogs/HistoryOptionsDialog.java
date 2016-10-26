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

package com.areebbeigh.qrcodeutility.fragments.dialogs;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.widget.ArrayAdapter;

import com.areebbeigh.qrcodeutility.HistoryActivity;
import com.areebbeigh.qrcodeutility.R;

import static com.areebbeigh.qrcodeutility.HistoryActivity.HistoryType;
import static com.areebbeigh.qrcodeutility.HistoryActivity.HISTORY_TYPE;

public class HistoryOptionsDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String SCAN_HISTORY = "Scan history";
        final String CREATE_HISTORY = "Create history";

        final String[] dialogOptions = {SCAN_HISTORY, CREATE_HISTORY};

        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.fragment_history_options_title)
                .setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, dialogOptions),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int position) {
                                String item = dialogOptions[position];

                                Intent historyIntent = new Intent(getActivity(), HistoryActivity.class);

                                if (item.equals(SCAN_HISTORY))
                                    historyIntent.putExtra(HISTORY_TYPE, HistoryType.SCAN);
                                else
                                    historyIntent.putExtra(HISTORY_TYPE, HistoryType.CREATE);

                                startActivity(historyIntent);
                            }
                        }).show();
    }
}
