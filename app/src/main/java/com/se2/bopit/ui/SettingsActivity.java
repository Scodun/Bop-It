package com.se2.bopit.ui;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.se2.bopit.R;

import java.util.Timer;

public class SettingsActivity extends AppCompatActivity {
    final static String MYPREF = "myCustomSharedPref";
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
    private Button buttonReset;
    private Button buttonSave;
    SharedPreferences customSharedPreferences;
    private Timer timer;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        handler = new Handler();
        customSharedPreferences = getSharedPreferences(MYPREF, Activity.MODE_PRIVATE);
        initializeView();
        setPrefValues();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_activity_settings));
        }
        // set onCheckedChangeListener for switches
        switchSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    summarySound.setText(getString(R.string.summary_on));
                } else {
                    summarySound.setText(getString(R.string.summary_off));
                }
            }
        });
        switchEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    summaryEffect.setText(getString(R.string.summary_on));
                } else {
                    summaryEffect.setText(getString(R.string.summary_off));
                }
            }
        });

        //set onClickListener for buttons
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetSharedPreferences();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSharedPreferences();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 300);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
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
        buttonReset = findViewById(R.id.buttonReset);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void setPrefValues() {
        textInputName.getEditText().setText(customSharedPreferences.getString("name", ""));
        switchSound.setChecked(customSharedPreferences.getBoolean("sound", true));
        switchEffect.setChecked(customSharedPreferences.getBoolean("effect", true));
    }


    private void resetSharedPreferences() {
        customSharedPreferences = getSharedPreferences(MYPREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = customSharedPreferences.edit();
        editor.putString("name", "");
        editor.putBoolean("sound", true);
        editor.putBoolean("effect", true);
        editor.commit();
        setPrefValues();
    }


    private void saveSharedPreferences() {
        customSharedPreferences = getSharedPreferences(MYPREF, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = customSharedPreferences.edit();
        editor.putString("name", textInputName.getEditText().getText().toString());
        editor.putBoolean("sound", switchSound.isChecked());
        editor.putBoolean("effect", switchEffect.isChecked());
        editor.commit();
    }


}