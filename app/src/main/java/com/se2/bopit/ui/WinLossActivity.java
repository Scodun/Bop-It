package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;
import com.se2.bopit.domain.MinigameAchievementCounters;

import java.util.Objects;

import info.hoang8f.widget.FButton;

public class WinLossActivity extends BaseActivity {
    private Button bu_return;
    private Button bu_share;
    private Button bu_leaderboardEasy;
    private FButton bu_leaderboardMedium;
    private FButton bu_leaderboardHard;
    private TextView tv_score;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private int score;

    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_SCORE = "highscore";
    private static final String PREF_KEY_SCORE_MEDIUM = "highscore2";
    private static final String PREF_KEY_SCORE_HARD = "highscore3";

    private static final String KEY_SCORE_MINIGAMES_EASY = "score1";
    private static final String KEY_SCORE_MINIGAMES_MEDIUM = "score2";
    private static final String KEY_SCORE_MINIGAMES_HARD = "score3";

    private static final String KEY_SCORE_IMAGEBUTTONMINIGAME = "minigameScore";
    private static final String KEY_SCORE_COLORBUTTONMINIGAME = "minigameScore1";
    private static final String KEY_SCORE_PLACEPHONEMINIGAME = "minigameScore2";
    private static final String KEY_SCORE_SHAKEPHONEMINIGAME = "minigameScore3";
    private static final String KEY_SCORE_COVERLIGHTSENSORMINIGAME = "minigameScore4";
    private static final String KEY_SCORE_RIGHTBUTTONCOMBINATION = "minigameScore5";
    private static final String KEY_SCORE_SLIDERMINIGAME = "minigameScore6";
    private static final String KEY_SCORE_DRAWINGMINIGAME = "minigameScore7";
    private static final String KEY_SCORE_VOLUMEBUTTON = "minigameScore8";
    private static final String KEY_SCORE_TEXTBASEDMINIGAME = "minigameScore9";

    int counter10 = 10;
    int counter25 = 25;
    int counter50 = 50;
    int counter100 = 100;
    int counter1000 = 1000;
    int counter10000 = 10000;

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
        int lastHighscoreEasy = customSharedPreferences.getInt(PREF_KEY_SCORE, 0);
        int lastHighscoreMedium = customSharedPreferences.getInt(PREF_KEY_SCORE_MEDIUM,0);
        int lastHighscoreHard = customSharedPreferences.getInt(PREF_KEY_SCORE_HARD,0);

        int scoreEasy = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_EASY, 0);
        int scoreMedium = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_MEDIUM,0);
        int scoreHard = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_HARD,0);

        int scoreImageButtonMinigame = customSharedPreferences.getInt(KEY_SCORE_IMAGEBUTTONMINIGAME,0);
        int scoreColorButtonMinigame = customSharedPreferences.getInt(KEY_SCORE_COLORBUTTONMINIGAME,0);
        int scoreCoverlightSensorMinigame = customSharedPreferences.getInt(KEY_SCORE_COVERLIGHTSENSORMINIGAME,0);
        int scoreSliderMinigame = customSharedPreferences.getInt(KEY_SCORE_SLIDERMINIGAME,0);
        int scorePlacePhoneMinigame = customSharedPreferences.getInt(KEY_SCORE_PLACEPHONEMINIGAME,0);
        int scoreShakePhoneMinigame = customSharedPreferences.getInt(KEY_SCORE_SHAKEPHONEMINIGAME,0);
        int scoreRightButtonCombinationMinigame = customSharedPreferences.getInt(KEY_SCORE_RIGHTBUTTONCOMBINATION,0);
        int scoreVolumeButtonMinigame = customSharedPreferences.getInt(KEY_SCORE_VOLUMEBUTTON,0);
        int scoreDrawingMinigame = customSharedPreferences.getInt(KEY_SCORE_DRAWINGMINIGAME,0);
        int scoreTextBasedMinigame = customSharedPreferences.getInt(KEY_SCORE_TEXTBASEDMINIGAME,0);

        SharedPreferences.Editor editor = customSharedPreferences.edit();

        scoreColorButtonMinigame+= MinigameAchievementCounters.getCounterColorButtonMinigame();
        scoreCoverlightSensorMinigame+= MinigameAchievementCounters.getCounterCoverLightSensorMinigame();
        scoreImageButtonMinigame+= MinigameAchievementCounters.getImageButtonMinigameCounter();
        scoreDrawingMinigame+= MinigameAchievementCounters.getCounterDrawingMinigame();
        scoreSliderMinigame+= MinigameAchievementCounters.getCounterSliderMinigame();
        scoreShakePhoneMinigame+= MinigameAchievementCounters.getCounterShakePhoneMinigame();
        scorePlacePhoneMinigame+= MinigameAchievementCounters.getCounterPlacePhoneMinigame();
        scoreVolumeButtonMinigame+= MinigameAchievementCounters.getCounterVolumeButtonMinigame();
        scoreTextBasedMinigame+= MinigameAchievementCounters.getCounterTextBasedMinigame();
        scoreRightButtonCombinationMinigame+= MinigameAchievementCounters.getCounterRightButtonsMinigame();

        editor.putInt(KEY_SCORE_IMAGEBUTTONMINIGAME,scoreImageButtonMinigame);
        editor.putInt(KEY_SCORE_COLORBUTTONMINIGAME,scoreColorButtonMinigame);
        editor.putInt(KEY_SCORE_COVERLIGHTSENSORMINIGAME,scoreCoverlightSensorMinigame);
        editor.putInt(KEY_SCORE_RIGHTBUTTONCOMBINATION,scoreRightButtonCombinationMinigame);
        editor.putInt(KEY_SCORE_PLACEPHONEMINIGAME,scorePlacePhoneMinigame);
        editor.putInt(KEY_SCORE_SLIDERMINIGAME,scoreSliderMinigame);
        editor.putInt(KEY_SCORE_SHAKEPHONEMINIGAME,scoreShakePhoneMinigame);
        editor.putInt(KEY_SCORE_DRAWINGMINIGAME,scoreDrawingMinigame);
        editor.putInt(KEY_SCORE_TEXTBASEDMINIGAME,scoreTextBasedMinigame);
        editor.putInt(KEY_SCORE_VOLUMEBUTTON,scoreVolumeButtonMinigame);

        MinigameAchievementCounters.resetCounter();

        switch (DifficultyActivity.difficulty) {
            case "easy":
                scoreEasy+=score;
                editor.putInt(KEY_SCORE_MINIGAMES_EASY, scoreEasy);
                if(scoreEasy>=counter100) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.easy100));
                    }
                }
                if(scoreEasy>=counter1000) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.easy1000));
                    }
                }
                if(scoreEasy>=counter10000) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.easy10000));
                    }
                }
                break;
            case "medium":
                scoreMedium+=score;
                editor.putInt(KEY_SCORE_MINIGAMES_MEDIUM, scoreMedium);
                if(scoreMedium>=counter100) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.medium100));
                    }
                }
                if(scoreMedium>=counter1000) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.medium1000));
                    }
                }
                if(scoreMedium>=counter10000) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.medium10000));
                    }
                }
                break;
            case "hard":
                scoreHard+=score;
                editor.putInt(KEY_SCORE_MINIGAMES_HARD, scoreHard);
                if(scoreHard>=counter100) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.hard100));
                    }
                }
                if(scoreHard>=counter1000) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.hard1000));
                    }
                }
                if(scoreHard>=counter10000) {
                    if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                        Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                                .unlock(getString(R.string.hard10000));
                    }
                }
                break;
        }
        if(score>=counter10){
            if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .unlock(getString(R.string.gamesInRow10));
            }
        }
        if(score>=counter25) {
            if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .unlock(getString(R.string.gamesInRow25));
            }
        }

        if(score>=counter50) {
            if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .unlock(getString(R.string.gamesInRow50));
            }
        }

        if (score > lastHighscoreEasy && DifficultyActivity.difficulty.equals("easy")) {
            editor.putInt(PREF_KEY_SCORE, score);
        }
        else if(score > lastHighscoreMedium && DifficultyActivity.difficulty.equals("medium")){
            editor.putInt(PREF_KEY_SCORE_MEDIUM, score);
            }
        else if(score > lastHighscoreHard && DifficultyActivity.difficulty.equals("hard")){
            editor.putInt(PREF_KEY_SCORE_HARD, score);
        }
        editor.apply();
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
        bu_leaderboardEasy.setOnClickListener(onLeaderboardSelect);

        bu_leaderboardMedium.setOnClickListener(onLeaderboardSelect);
        bu_leaderboardHard.setOnClickListener(onLeaderboardSelect);
    }

    private void initializeButtons() {
        bu_return = findViewById(R.id.bu_return);
        bu_share = findViewById(R.id.bu_share);
        tv_score = findViewById(R.id.tv_score);
        bu_leaderboardEasy = findViewById(R.id.leaderboardEasyButton);

        bu_leaderboardMedium = findViewById(R.id.leaderboardMediumButton);
        bu_leaderboardHard = findViewById(R.id.leaderboardHardButton);
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