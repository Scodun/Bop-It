package com.se2.bopit.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.se2.bopit.R;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView headerPersSet;
    private TextInputLayout textInputName;
    private TextView headerSoundSet;
    private ImageView iconSound;
    private SwitchCompat switchSound;
    private TextView summarySound;
    private ImageView iconEffect;
    private SwitchCompat switchEffect;
    private TextView summaryEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        initializeView();
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    summarySound.setText(getString(R.string.summary_on));
                }else{
                    summarySound.setText(getString(R.string.summary_off));
                }
            }
        });
        switchEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    summaryEffect.setText(getString(R.string.summary_on));
                }else{
                    summaryEffect.setText(getString(R.string.summary_off));
                }
            }
        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_activity_settings));
        }
    }

    private void initializeView() {
        toolbar = findViewById(R.id.toolbar_settings);
        headerPersSet = findViewById(R.id.textViewHeaderPersonalSettings);
        textInputName = findViewById(R.id.textFieldName);
        headerSoundSet = findViewById(R.id.textViewHeaderSoundSettings);
        iconSound = findViewById(R.id.iconSound);
        iconSound.setImageResource(R.drawable.ic_sound);
        switchSound = findViewById(R.id.switchSound);
        summarySound = findViewById(R.id.summarySound);
        iconEffect = findViewById(R.id.iconEffect);
        iconEffect.setImageResource(R.drawable.ic_effect);
        switchEffect = findViewById(R.id.switchEffect);
        summaryEffect = findViewById(R.id.summaryEffect);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}