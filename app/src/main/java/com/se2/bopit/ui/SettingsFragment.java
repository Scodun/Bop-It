package com.se2.bopit.ui;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.se2.bopit.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);
    }
}

