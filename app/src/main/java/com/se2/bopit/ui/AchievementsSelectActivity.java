package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;

import com.se2.bopit.R;

import info.hoang8f.widget.FButton;

public class AchievementsSelectActivity extends BaseActivity{
    private FButton
            overallAchievements,
            minigameAchievements,
            backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements_select_screen);

        initializeButtons();
        initializeListeners();
    }

    private void initializeButtons() {
        overallAchievements = findViewById(R.id.overall);
        minigameAchievements = findViewById(R.id.minigameAchievements);
        backButton = findViewById(R.id.backtoHome);
    }

    private void initializeListeners() {
        overallAchievements.setOnClickListener(v -> {
            startActivity(new Intent(this, OverallAchievementsActivity.class));
        });

        minigameAchievements.setOnClickListener(v -> {
            startActivity(new Intent(this, MinigameAchievementsActivity.class));
        });

        backButton.setOnClickListener(v -> {
            startActivity(new Intent(this, GamemodeSelectActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

}
