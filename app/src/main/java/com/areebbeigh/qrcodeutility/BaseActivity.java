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
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getViewLayout());
    }

    private long onRecentBackPressedTime;

    @Override
    public void onBackPressed() {
        Toast t = Toast.makeText(this, R.string.backpress_hint, Toast.LENGTH_SHORT);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (shouldIgnoreBackStack()) {
                if (System.currentTimeMillis() - onRecentBackPressedTime > 2000) {
                    onRecentBackPressedTime = System.currentTimeMillis();
                    t.show();
                    return;
                }
            }
            super.onBackPressed();
            t.cancel();
        }
    }

    public Toolbar setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        return toolbar;
    }

    /**
     * Returns the layout resource of the activity
     */
    public abstract int getViewLayout();

    /**
     * Returns true if this activity should ignore the back stack and exit if the back button is pressed twice
     */
    public abstract boolean shouldIgnoreBackStack();
}
