package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;

import java.util.Objects;

import info.hoang8f.widget.FButton;

public class AchievementsSelectActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private FButton overallAchievements;
    private FButton minigameAchievements;
    private FButton googlePlayAchievements;
    private FButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.achievements_select_screen);

        initializeButtons();
        initializeListeners();
        initActivityLauncher();
    }

    private void initializeButtons() {
        overallAchievements = findViewById(R.id.overall);
        minigameAchievements = findViewById(R.id.minigameAchievements);
        backButton = findViewById(R.id.backtoHome);
        googlePlayAchievements = findViewById(R.id.googlePlayAchievements);
    }

    private void initializeListeners() {
        overallAchievements.setOnClickListener(v -> startActivity(new Intent(this, OverallAchievementsActivity.class)));

        minigameAchievements.setOnClickListener(v -> startActivity(new Intent(this, MinigameAchievementsActivity.class)));

        backButton.setOnClickListener(v -> startActivity(new Intent(this, GamemodeSelectActivity.class)));

        googlePlayAchievements.setOnClickListener(onAchievements);
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                });
    }

    private final View.OnClickListener onAchievements = v -> {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getAchievementsIntent()
                    .addOnSuccessListener(intent -> activityResultLauncher.launch(intent));
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

}
