package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;

import java.util.Objects;

import info.hoang8f.widget.FButton;

public class GamemodeSelectActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private FButton
            singleplayerButton,
            localMultiplayerButton,
            onlineMultiplayerButton,
            googlePlayAchievements;
    private ImageButton
            settingsButton,
            achievementButton;
    private ConstraintLayout customRulesButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode_select);

        initializeButtons();
        initializeListeners();
        initActivityLauncher();
    }


    private void initializeButtons() {
        singleplayerButton = findViewById(R.id.singleplayerButton);
        localMultiplayerButton = findViewById(R.id.localMultiplayerButton);
        onlineMultiplayerButton = findViewById(R.id.onlineMultiplayerButton);
        settingsButton = findViewById(R.id.settingsButton);
        customRulesButton = findViewById(R.id.customRulesButton);
        achievementButton = findViewById(R.id.achievments);

        googlePlayAchievements = findViewById(R.id.googlePlayAchievements);
    }

    private void initializeListeners() {
        singleplayerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, DifficultyActivity.class));
        });

        localMultiplayerButton.setOnClickListener(v -> {
        });

        onlineMultiplayerButton.setOnClickListener(v -> {
            startActivity(new Intent(this, HostJoinActivity.class));
        });

        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        customRulesButton.setOnClickListener(v -> startActivity(new Intent(this, CustomizeGameRulesActivity.class)));

        achievementButton.setOnClickListener(v -> startActivity(new Intent(this, AchievementsSelectActivity.class)));

        googlePlayAchievements.setOnClickListener(onAchievements);

    }
    private final View.OnClickListener onAchievements = v -> {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getAchievementsIntent()
                    .addOnSuccessListener(intent -> activityResultLauncher.launch(intent));
        }
    };

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

}
