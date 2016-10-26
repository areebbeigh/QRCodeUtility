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

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.areebbeigh.qrcodeutility.fragments.dialogs.LicenseDialog;

public class AboutActivity extends AppCompatPreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setupToolbar();
        getSupportActionBar().setTitle(getString(R.string.title_actvity_About));
        getFragmentManager().beginTransaction().replace(R.id.about_content, new AboutFragment()).commit();
    }

    public static class AboutFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.about_fragment);
            setHasOptionsMenu(true);

            // Get application version
            PackageManager manager = getActivity().getPackageManager();
            PackageInfo info = null;
            try {
                info = manager.getPackageInfo(
                        getActivity().getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            String version = "1.0.0";

            if (info != null)
                version = info.versionName;

            // Listener to blank preferences
            Preference.OnPreferenceClickListener noClickListenser = new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    return true;
                }
            };

            // Set version info
            Preference versionPref = findPreference(getString(R.string.pref_version_info_key));
            versionPref.setOnPreferenceClickListener(noClickListenser);
            versionPref.setSummary(version);

            findPreference(getString(R.string.pref_noclick_key)).setOnPreferenceClickListener(noClickListenser);

            // Website redirects
            findPreference(getString(R.string.pref_dev_redirect_key)).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            openUri(getString(R.string.app_dev_uri));
                            return true;
                        }
                    }
            );

            findPreference(getString(R.string.pref_testing_redirect_key)).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            openUri(getString(R.string.app_testing_uri));
                            return true;
                        }
                    }
            );

            findPreference(getString(R.string.pref_icon_redirect_key)).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            openUri(getString(R.string.icon_components_uri));
                            return true;
                        }
                    }
            );


            // Rate app :)
            findPreference(getString(R.string.pref_rate_app_key)).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            openUri(getString(R.string.rate_app_uri));
                            return true;
                        }
                    }
            );

            findPreference(getString(R.string.pref_licenses_key)).setOnPreferenceClickListener(
                    new Preference.OnPreferenceClickListener() {
                        @Override
                        public boolean onPreferenceClick(Preference preference) {
                            (new LicenseDialog()).show(getFragmentManager(), null);
                            return true;
                        }
                    }
            );
        }

        private void openUri(String uri) {
            Uri _uri = Uri.parse(uri);
            Intent intent = new Intent(Intent.ACTION_VIEW, _uri);
            startActivity(intent);
        }
    }
}
