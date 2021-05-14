package com.se2.bopit.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;

import java.util.Objects;

public class WinLossActivity extends AppCompatActivity {
    private Button bu_return;
    private Button bu_share;
    private Button bu_leaderboard;
    private TextView tv_score;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int score;

    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_SCORE = "highscore";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loss);

        initializeButtons();
        initializeListeners();
        initializeFields();
        initActivityLauncher();
        showScore();
        updateHighscore();
        setPrefHighscore();
    }

    private void setPrefHighscore() {
        SharedPreferences customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        int lastHighscore = customSharedPreferences.getInt(PREF_KEY_SCORE, 0);
        if (score > lastHighscore) {
            SharedPreferences.Editor editor = customSharedPreferences.edit();
            editor.putInt(PREF_KEY_SCORE, score);
            editor.apply();
        }
    }

    private void updateHighscore() {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .submitScore(getString(R.string.leaderboard_highscore), score);
        }
    }

    private void initializeFields() {
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
    }

    private void initializeListeners() {
        bu_share.setOnClickListener(onShare);
        bu_return.setOnClickListener(onReturnToGameSelectMode);
        bu_leaderboard.setOnClickListener(onLeaderboardSelect);
    }

    private void initializeButtons() {
        bu_return = findViewById(R.id.bu_return);
        bu_share = findViewById(R.id.bu_share);
        tv_score = findViewById(R.id.tv_score);
        bu_leaderboard = findViewById(R.id.leaderboardButton);
    }

    private void showScore() {
        tv_score = findViewById(R.id.tv_score);
        tv_score.setText("Score: " + score);
    }

    private final View.OnClickListener onShare = v -> {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share your Bop-It Score");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey everyone, my Bop-It score is " + score + "! Can you beat it?");
        startActivity(Intent.createChooser(shareIntent, "Share score"));
    };

    private final View.OnClickListener onReturnToGameSelectMode = v -> {
        Intent gmSelectActivityIntent = new Intent(getBaseContext(), GamemodeSelectActivity.class);
        startActivity(gmSelectActivityIntent);
    };

    private final View.OnClickListener onLeaderboardSelect = v -> {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getLeaderboardIntent(getString(R.string.leaderboard_highscore))
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
        startActivity(new Intent(this, GamemodeSelectActivity.class));
        finish();
    }

}