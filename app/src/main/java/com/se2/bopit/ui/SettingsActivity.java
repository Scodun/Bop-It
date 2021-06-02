package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputLayout;
import com.se2.bopit.R;
import com.se2.bopit.domain.services.BackgroundSoundService;

import java.util.Objects;


public class SettingsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_SOUND = "sound";
    private static final String PREF_KEY_EFFECT = "effect";
    private static final String PREF_KEY_NAME = "name";
    private static final String PREF_KEY_SCORE = "highscore";
    private static final String PREF_KEY_SCORE_MEDIUM = "highscore2";
    private static final String PREF_KEY_SCORE_HARD = "highscore3";


    private Toolbar toolbar;
    private TextInputLayout textInputName;
    private SwitchCompat switchSound;
    private TextView summarySound;
    private SwitchCompat switchEffect;
    private TextView summaryEffect;
    private Button buttonReset;
    private Button buttonSave;
    private TextView highScore;
    SharedPreferences customSharedPreferences;

    private TextView mediumHighscore;
    private TextView hardHighscore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.settings_activity);

        setContentView(R.layout.settings_try_layout);

        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        customSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        initializeView();
        setPrefValues();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.title_activity_settings));
        }
        // set onCheckedChangeListener for switches
        switchSound.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                summarySound.setText(getString(R.string.summary_on));
            } else {
                summarySound.setText(getString(R.string.summary_off));
            }
        });
        switchEffect.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                summaryEffect.setText(getString(R.string.summary_on));
            } else {
                summaryEffect.setText(getString(R.string.summary_off));
            }
        });

        //set onClickListener for buttons
        buttonReset.setOnClickListener(v -> resetSharedPreferences());

        buttonSave.setOnClickListener(v -> {
            saveSharedPreferences();
            finish();
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
        textInputName = findViewById(R.id.textFieldName);
        ImageView iconSound = findViewById(R.id.iconSound);
        iconSound.setImageResource(R.drawable.ic_sound);
        switchSound = findViewById(R.id.switchSound);
        summarySound = findViewById(R.id.summarySound);
        ImageView iconEffect = findViewById(R.id.iconEffect);
        iconEffect.setImageResource(R.drawable.ic_effect);
        switchEffect = findViewById(R.id.switchEffect);
        summaryEffect = findViewById(R.id.summaryEffect);
        highScore = findViewById(R.id.textViewHighscore);
        ImageView iconHighscore = findViewById(R.id.iconHighscore);
        iconHighscore.setImageResource(R.drawable.ic_highscore);
        buttonReset = findViewById(R.id.buttonReset);
        buttonSave = findViewById(R.id.buttonSave);
        mediumHighscore = findViewById(R.id.textViewMediumHighscore);
        hardHighscore = findViewById(R.id.textViewHardHighscore);
        ImageView iconMediumHighscoreImage = findViewById(R.id.iconMediumHighscore);
        iconMediumHighscoreImage.setImageResource(R.drawable.ic_highscore);
        ImageView iconHardHighscoreImage = findViewById(R.id.iconHardHighscore);
        iconHardHighscoreImage.setImageResource(R.drawable.ic_highscore);
    }

    private void setPrefValues() {
        Objects.requireNonNull(textInputName.getEditText()).setText(customSharedPreferences.getString(PREF_KEY_NAME, ""));
        switchSound.setChecked(customSharedPreferences.getBoolean(PREF_KEY_SOUND, true));
        switchEffect.setChecked(customSharedPreferences.getBoolean(PREF_KEY_EFFECT, true));
        highScore.setText(String.valueOf(customSharedPreferences.getInt(PREF_KEY_SCORE, 0)));
        mediumHighscore.setText(String.valueOf(customSharedPreferences.getInt(PREF_KEY_SCORE_MEDIUM, 0)));
        hardHighscore.setText(String.valueOf(customSharedPreferences.getInt(PREF_KEY_SCORE_HARD, 0)));

    }


    private void resetSharedPreferences() {
        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = customSharedPreferences.edit();
        editor.putString(PREF_KEY_NAME, "");
        editor.putBoolean(PREF_KEY_SOUND, true);
        editor.putBoolean(PREF_KEY_EFFECT, true);
        editor.putInt(PREF_KEY_SCORE, 0);
        editor.putInt(PREF_KEY_SCORE_MEDIUM, 0);
        editor.putInt(PREF_KEY_SCORE_HARD, 0);
        editor.apply();
        setPrefValues();
    }

    private void saveSharedPreferences() {
        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = customSharedPreferences.edit();
        editor.putString(PREF_KEY_NAME, Objects.requireNonNull(textInputName.getEditText()).getText().toString());
        editor.putBoolean(PREF_KEY_SOUND, switchSound.isChecked());
        editor.putBoolean(PREF_KEY_EFFECT, switchEffect.isChecked());
        editor.apply();
    }

    @Override
    public void onResume() {
        super.onResume();
        customSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        customSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(PREF_KEY_SOUND)) {
            stopService(new Intent(this, BackgroundSoundService.class));
            startService(new Intent(this, BackgroundSoundService.class));
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSharedPreferences();
    }
}