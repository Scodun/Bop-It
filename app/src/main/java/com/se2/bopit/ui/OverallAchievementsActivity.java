package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.se2.bopit.R;
import info.hoang8f.widget.FButton;

public class OverallAchievementsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String MYPREF = "myCustomSharedPref";
    private static final String KEY_SCORE_MINIGAMES_EASY = "score1";
    private static final String KEY_SCORE_MINIGAMES_MEDIUM = "score2";
    private static final String KEY_SCORE_MINIGAMES_HARD = "score3";

    SharedPreferences customSharedPreferences;

    ImageView
            checkEasy,
            checkMedium,
            checkHard;
    FButton backButton;
    Button reset;

    TextView easyAchievement;
    TextView mediumAchievement;
    TextView hardAchievement;

    int scoreOverall = 10;
    int checkEasyScore;
    int checkMediumScore;
    int checkHardScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.overall_achievements);

        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        customSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        initializeViews();
        setPreferences();
        checkIsTrue();

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, AchievementsSelectActivity.class));
        });
        reset.setOnClickListener(v -> resetPreferences());
    }
    private void initializeViews(){
        checkEasy = findViewById(R.id.check1);
        checkMedium = findViewById(R.id.check2);
        checkHard = findViewById(R.id.check3);
        backButton = findViewById(R.id.backtoHome);

        easyAchievement = findViewById(R.id.easyAchievement);
        mediumAchievement = findViewById(R.id.mediumAchievement);
        hardAchievement = findViewById(R.id.hardAchievement);

        reset = findViewById(R.id.resetButtonAchieve);

        checkEasy.setVisibility(View.INVISIBLE);
        checkMedium.setVisibility(View.INVISIBLE);
        checkHard.setVisibility(View.INVISIBLE);
    }
    private void setPreferences() {
        easyAchievement.setText(String.valueOf(customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_EASY, 0)));
        mediumAchievement.setText(String.valueOf(customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_MEDIUM, 0)));
        hardAchievement.setText(String.valueOf(customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_HARD, 0)));

        checkEasyScore = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_EASY,0);
        checkMediumScore = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_MEDIUM,0);
        checkHardScore = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_HARD,0);

    }

    private void checkIsTrue(){
        if(checkEasyScore >=scoreOverall){
            checkEasy.setVisibility(View.VISIBLE);
            easyAchievement.setVisibility(View.INVISIBLE);
        }
        if(checkMediumScore >=scoreOverall){
            checkMedium.setVisibility(View.VISIBLE);
            mediumAchievement.setVisibility(View.INVISIBLE);
        }
        if(checkHardScore >=scoreOverall){
            checkHard.setVisibility(View.VISIBLE);
            hardAchievement.setVisibility(View.INVISIBLE);
        }
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
    }
    private void resetPreferences() {
        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = customSharedPreferences.edit();
        editor.putInt(KEY_SCORE_MINIGAMES_EASY, 0);
        editor.putInt(KEY_SCORE_MINIGAMES_MEDIUM, 0);
        editor.putInt(KEY_SCORE_MINIGAMES_HARD, 0);

        checkEasy.setVisibility(View.INVISIBLE);
        checkMedium.setVisibility(View.INVISIBLE);
        checkHard.setVisibility(View.INVISIBLE);

        easyAchievement.setVisibility(View.VISIBLE);
        mediumAchievement.setVisibility(View.VISIBLE);
        hardAchievement.setVisibility(View.VISIBLE);

        editor.apply();
        setPreferences();
    }
}
