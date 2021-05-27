package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.se2.bopit.R;

import info.hoang8f.widget.FButton;

public class GamemodeSelectActivity extends BaseActivity {

    private FButton
            singleplayerButton,
            localMultiplayerButton,
            onlineMultiplayerButton;
    private ImageButton settingsButton;
    private ConstraintLayout customRulesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode_select);

        initializeButtons();
        initializeListeners();
    }


    private void initializeButtons() {
        singleplayerButton = findViewById(R.id.singleplayerButton);
        localMultiplayerButton = findViewById(R.id.localMultiplayerButton);
        onlineMultiplayerButton = findViewById(R.id.onlineMultiplayerButton);
        settingsButton = findViewById(R.id.settingsButton);
        customRulesButton = findViewById(R.id.customRulesButton);
    }

    private void initializeListeners() {
        singleplayerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, GameActivity.class));
        });

        localMultiplayerButton.setOnClickListener(v -> {
        });

        onlineMultiplayerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, HostJoinActivity.class));
        });

        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        customRulesButton.setOnClickListener(v -> startActivity(new Intent(this, CustomizeGameRulesActivity.class)));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

}
