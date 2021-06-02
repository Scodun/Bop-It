package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;

import com.se2.bopit.R;

import info.hoang8f.widget.FButton;

public class DifficultyActivity extends BaseActivity{
    FButton easy;
    FButton medium;
    FButton hard;

    public static String difficulty = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        initializeButtons();
        initializeOnClickListeners();
    }

    private void initializeButtons() {
        easy = findViewById(R.id.Easy);
        medium = findViewById(R.id.Medium);
        hard = findViewById(R.id.Hard);
    }

    private void initializeOnClickListeners() {
        easy.setOnClickListener(v -> {
            setDifficulty("easy");
            startActivity(new Intent(this, GameActivity.class));
        });

        medium.setOnClickListener(v -> {
            setDifficulty("medium");
            startActivity(new Intent(this, GameActivity.class));
        });

        hard.setOnClickListener(v -> {
            setDifficulty("hard");
            startActivity(new Intent(this, GameActivity.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    public void setDifficulty(String difficulty) {
        DifficultyActivity.difficulty = difficulty;
    }
}