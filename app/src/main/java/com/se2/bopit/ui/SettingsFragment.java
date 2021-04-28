package com.se2.bopit.ui;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.se2.bopit.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
