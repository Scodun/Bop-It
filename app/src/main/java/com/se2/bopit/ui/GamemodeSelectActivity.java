package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.ui.helpers.WaveAnimator;

import java.util.Objects;

import info.hoang8f.widget.FButton;

public class GamemodeSelectActivity extends BaseActivity {

    private ActivityResultLauncher<Intent> activityResultLauncher;

    private FButton singleplayerButton;
    private FButton localMultiplayerButton;
    private FButton onlineMultiplayerButton;

    private ImageButton settingsButton;
    private ImageButton achievementButton;
    private ImageButton leaderboardButton;

    private ConstraintLayout toRulesButton;
    private ConstraintLayout customRulesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode_select);

        new WaveAnimator(this, findViewById(R.id.waveView6)).animate(5000, true);

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
        leaderboardButton = findViewById(R.id.leaderboardsButton);
        toRulesButton = findViewById(R.id.rulesButton);
    }

    private void initializeListeners() {
        singleplayerButton.setOnClickListener(v ->
                startActivity(new Intent(this, DifficultyActivity.class)
                        .putExtra(GameActivity.GAME_MODE, GameMode.SINGLE_PLAYER))
        );

        localMultiplayerButton.setOnClickListener(v ->
                startActivity(new Intent(this, HostJoinActivity.class)
                        .putExtra(HostJoinActivity.NETWORK_MODE, "nearby")));

        onlineMultiplayerButton.setOnClickListener(v ->
                startActivity(new Intent(this, HostJoinActivity.class)
                        .putExtra(HostJoinActivity.NETWORK_MODE, "websocket")));

        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        customRulesButton.setOnClickListener(v -> startActivity(new Intent(this, CustomizeGameRulesActivity.class)));

        achievementButton.setOnClickListener(v -> startActivity(new Intent(this, AchievementsSelectActivity.class)));

        leaderboardButton.setOnClickListener(v -> {
            if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .getAllLeaderboardsIntent()
                        .addOnSuccessListener(intent -> activityResultLauncher.launch(intent));
            }
        });

        toRulesButton.setOnClickListener(v ->
                startActivity(new Intent(this, RulesActivity.class)));

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finishAffinity();
    }

    private void initActivityLauncher() {
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                });
    }

}
