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

package com.areebbeigh.qrcodeutility;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.areebbeigh.qrcodeutility.data.HistoryDBHandler;
import com.areebbeigh.qrcodeutility.fragments.CreateHistoryFragment;
import com.areebbeigh.qrcodeutility.fragments.ScanHistoryFragment;

// TODO: Add preview images to barcode list view

public class HistoryActivity extends BaseActivity {
    public static final String HISTORY_TYPE = "HISTORY_TYPE";

    public enum HistoryType {
        SCAN,
        CREATE
    }

    private HistoryType currentFragment;
    @Override
    public int getViewLayout() {
        return R.layout.activity_history;
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

        if (getIntent().getExtras().get(HISTORY_TYPE) == HistoryType.SCAN) {
            getFragmentManager().beginTransaction().replace(R.id.history_content, new ScanHistoryFragment()).commit();
            actionBar.setTitle(getString(R.string.title_activity_scan_history));
            currentFragment = HistoryType.SCAN;
        }
        else {
            getFragmentManager().beginTransaction().replace(R.id.history_content, new CreateHistoryFragment()).commit();
            actionBar.setTitle(getString(R.string.title_activity_create_history));
            currentFragment = HistoryType.CREATE;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_history_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.toolbar_delete_button:
                HistoryDBHandler dbHandler = new HistoryDBHandler(this, null, null, 0);
                if (currentFragment == HistoryType.SCAN)
                    dbHandler.deleteScanHistory();
                else
                    dbHandler.deleteCreateHistory();
                Toast.makeText(this, "Deleted history", Toast.LENGTH_LONG).show();
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
