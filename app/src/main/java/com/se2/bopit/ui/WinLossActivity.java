package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;
import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.MinigameAchievementCounters;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.ui.helpers.WaveAnimator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

import info.hoang8f.widget.FButton;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;

import static com.se2.bopit.ui.GameActivity.GAME_MODE;

public class WinLossActivity extends BaseActivity {
    private Button buReturn;
    private Button buShare;
    private Button buPlayAgain;
    private Button buLeaderboardEasy;
    private FButton buLeaderboardMedium;
    private FButton buLeaderboardHard;
    private LinearLayout scoreLayout;
    private ActivityResultLauncher<Intent> intentActivityResultLauncher;
    private int score;
    private ArrayList<User> playerScores;
    private String userId;

    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_SCORE = "highscore";
    private static final String PREF_KEY_SCORE_MEDIUM = "highscore2";
    private static final String PREF_KEY_SCORE_HARD = "highscore3";

    private static final String KEY_SCORE_MINIGAMES_EASY = "score1";
    private static final String KEY_SCORE_MINIGAMES_MEDIUM = "score2";
    private static final String KEY_SCORE_MINIGAMES_HARD = "score3";

    int counter10 = 10;
    int counter25 = 25;
    int counter50 = 50;
    int counter100 = 100;
    int counter1000 = 1000;
    int counter10000 = 10000;
    private GameMode gameMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loss);

        new WaveAnimator(this, findViewById(R.id.waveViewWinLoss)).animate(5000, true);
        KonfettiView konfettiView = findViewById(R.id.viewKonfetti);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        konfettiView.build()
                .addColors(
                        ContextCompat.getColor(this, R.color.red),
                        ContextCompat.getColor(this, R.color.blue),
                        ContextCompat.getColor(this, R.color.primary),
                        ContextCompat.getColor(this, R.color.primary_variant)
                        )
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 3f)
                .setFadeOutEnabled(true)
                .setTimeToLive(5000L)
                .addShapes(Shape.Square.INSTANCE, Shape.Circle.INSTANCE)
                .addSizes(new Size(12, 5f))
                .setPosition(-50f, displayMetrics.widthPixels + 50f, -50f, -50f)
                .streamFor(100, 2000L);

        initializeFields();
        initializeButtons();
        initializeListeners();
        initActivityLauncher();
        showScore();
        setPrefHighscore();
    }

    private void setPrefHighscore() {
        SharedPreferences customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        int lastHighscoreEasy = customSharedPreferences.getInt(PREF_KEY_SCORE, 0);
        int lastHighscoreMedium = customSharedPreferences.getInt(PREF_KEY_SCORE_MEDIUM, 0);
        int lastHighscoreHard = customSharedPreferences.getInt(PREF_KEY_SCORE_HARD, 0);

        int scoreEasy = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_EASY, 0);
        int scoreMedium = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_MEDIUM, 0);
        int scoreHard = customSharedPreferences.getInt(KEY_SCORE_MINIGAMES_HARD, 0);

        int scoreImageButtonMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_IMAGEBUTTONMINIGAME), 0);
        int scoreColorButtonMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_COLORBUTTONMINIGAME), 0);
        int scoreCoverlightSensorMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_COVERLIGHTSENSORMINIGAME), 0);
        int scoreSliderMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SLIDERMINIGAME), 0);
        int scorePlacePhoneMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_PLACEPHONEMINIGAME), 0);
        int scoreShakePhoneMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SHAKEPHONEMINIGAME), 0);
        int scoreRightButtonCombinationMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_RIGHTBUTTONCOMBINATION), 0);
        int scoreVolumeButtonMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_VOLUMEBUTTON), 0);
        int scoreDrawingMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_DRAWINGMINIGAME), 0);
        int scoreTextBasedMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_TEXTBASEDMINIGAME), 0);
        int scoreSpecialTextMinigame = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SPECIALTEXTMINIGAME), 0);


        SharedPreferences.Editor editor = customSharedPreferences.edit();

        scoreColorButtonMinigame += MinigameAchievementCounters.getCounterColorButtonMinigame();
        scoreCoverlightSensorMinigame += MinigameAchievementCounters.getCounterCoverLightSensorMinigame();
        scoreImageButtonMinigame += MinigameAchievementCounters.getImageButtonMinigameCounter();
        scoreDrawingMinigame += MinigameAchievementCounters.getCounterDrawingMinigame();
        scoreSliderMinigame += MinigameAchievementCounters.getCounterSliderMinigame();
        scoreShakePhoneMinigame += MinigameAchievementCounters.getCounterShakePhoneMinigame();
        scorePlacePhoneMinigame += MinigameAchievementCounters.getCounterPlacePhoneMinigame();
        scoreVolumeButtonMinigame += MinigameAchievementCounters.getCounterVolumeButtonMinigame();
        scoreTextBasedMinigame += MinigameAchievementCounters.getCounterTextBasedMinigame();
        scoreRightButtonCombinationMinigame += MinigameAchievementCounters.getCounterRightButtonsMinigame();
        scoreSpecialTextMinigame += MinigameAchievementCounters.getCounterSpecialTextButtonMinigame();

        editor.putInt(this.getString(R.string.KEY_SCORE_IMAGEBUTTONMINIGAME), scoreImageButtonMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_COLORBUTTONMINIGAME), scoreColorButtonMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_COVERLIGHTSENSORMINIGAME), scoreCoverlightSensorMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_RIGHTBUTTONCOMBINATION), scoreRightButtonCombinationMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_PLACEPHONEMINIGAME), scorePlacePhoneMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_SLIDERMINIGAME), scoreSliderMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_SHAKEPHONEMINIGAME), scoreShakePhoneMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_DRAWINGMINIGAME), scoreDrawingMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_TEXTBASEDMINIGAME), scoreTextBasedMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_VOLUMEBUTTON), scoreVolumeButtonMinigame);
        editor.putInt(this.getString(R.string.KEY_SCORE_SPECIALTEXTMINIGAME), scoreSpecialTextMinigame);

        MinigameAchievementCounters.resetCounter();

        switch (DifficultyActivity.difficulty) {
            case EASY:
                easyScoreHandler(scoreEasy, editor);
                break;
            case MEDIUM:
                mediumScoreHandler(scoreMedium, editor);
                break;
            case HARD:
                hardScoreHandler(scoreHard, editor);
                break;
            default:
                Log.d("WinLoss", "unknown achieventType");
                break;
        }
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            if (score >= counter10) {
                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .unlock(getString(R.string.gamesInRow10));
            }
            if (score >= counter25) {
                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .unlock(getString(R.string.gamesInRow25));
            }

            if (score >= counter50) {
                Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                        .unlock(getString(R.string.gamesInRow50));
            }
        }

        if (score > lastHighscoreEasy && DifficultyActivity.difficulty == Difficulty.EASY) {
            editor.putInt(PREF_KEY_SCORE, score);
        } else if (score > lastHighscoreMedium && DifficultyActivity.difficulty == Difficulty.MEDIUM) {
            editor.putInt(PREF_KEY_SCORE_MEDIUM, score);
        } else if (score > lastHighscoreHard && DifficultyActivity.difficulty == Difficulty.HARD) {
            editor.putInt(PREF_KEY_SCORE_HARD, score);
        }
        editor.apply();

    }

    private void easyScoreHandler(int scoreEasy, SharedPreferences.Editor editor) {
        scoreEasy += score;
        editor.putInt(KEY_SCORE_MINIGAMES_EASY, scoreEasy);
        updateHighscore(getString(R.string.leaderboard_highscore_easy), score);
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .increment(getString(R.string.easy100), score);
        }
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .increment(getString(R.string.easy1000), score);
        }
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .increment(getString(R.string.easy10000), score);
        }
    }

    private void mediumScoreHandler(int scoreMedium, SharedPreferences.Editor editor) {
        scoreMedium += score;
        editor.putInt(KEY_SCORE_MINIGAMES_MEDIUM, scoreMedium);
        updateHighscore(getString(R.string.leaderboard_highscore_medium), score);
        if (scoreMedium >= counter100 && !BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.medium100));
        }
        if (scoreMedium >= counter1000 && !BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.medium1000));
        }
        if (scoreMedium >= counter10000 && !BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.medium10000));
        }
    }


    private void hardScoreHandler(int scoreHard, SharedPreferences.Editor editor) {
        scoreHard += score;
        editor.putInt(KEY_SCORE_MINIGAMES_HARD, scoreHard);
        updateHighscore(getString(R.string.leaderboard_highscore_hard), score);
        if (scoreHard >= counter100 && !BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.hard100));
        }
        if (scoreHard >= counter1000 && !BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {

            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.hard1000));
        }
        if (scoreHard >= counter10000 && !BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getAchievementsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .unlock(getString(R.string.hard10000));
        }
    }

    private void updateHighscore(String leaderboard, int score) {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .submitScore(leaderboard, score);
        }
    }

    private void initializeFields() {
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        userId = intent.getStringExtra("userId");
        gameMode = (GameMode) intent.getSerializableExtra(GAME_MODE);
        if(gameMode != GameMode.SINGLE_PLAYER)
            playerScores = intent.getParcelableArrayListExtra("playerScores");
    }

    private void initializeListeners() {
        buShare.setOnClickListener(onShare);
        buReturn.setOnClickListener(onReturnToGameSelectMode);
        buLeaderboardEasy.setOnClickListener(onEasyLeaderboardSelect);
        buLeaderboardMedium.setOnClickListener(onMediumLeaderboardSelect);
        buLeaderboardHard.setOnClickListener(onHardLeaderboardSelect);
        if(gameMode == GameMode.SINGLE_PLAYER)
            buPlayAgain.setOnClickListener(onPlayAgainListener);
    }

    private void initializeButtons() {
        buReturn = findViewById(R.id.bu_return);
        buShare = findViewById(R.id.bu_share);
        scoreLayout = findViewById(R.id.scoreLayout);
        buPlayAgain = findViewById(R.id.bu_play_again);
        buLeaderboardEasy = findViewById(R.id.leaderboardEasyButton);

        buLeaderboardMedium = findViewById(R.id.leaderboardMediumButton);
        buLeaderboardHard = findViewById(R.id.leaderboardHardButton);


        if(gameMode != GameMode.SINGLE_PLAYER)
            buPlayAgain.setVisibility(View.INVISIBLE);
    }

    private TextView newScoreField(){
        TextView tv = new TextView(this);
        tv.setBackgroundResource(R.drawable.rounded_corners);
        tv.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_variant)));
        tv.setTextSize(50.0f);
        tv.setPadding(10,10,10,10);
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
        return tv;
    }

    private void showScore() {
        if(gameMode == GameMode.SINGLE_PLAYER) {
            TextView tv = newScoreField();
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            tv.setText("Score\n" + score);
            scoreLayout.addView(tv);
        }
        else
        {
            int i=1;
            Collections.sort(playerScores);
            for (User user:playerScores) {
                TextView tv = newScoreField();
                tv.setTextSize(25.0f);
                tv.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                tv.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                tv.setText(i++ +". " + user.getName()+"\nScore: "+user.getScore());
                scoreLayout.addView(tv);
                Space spaceView = new Space(this);
                spaceView.setMinimumHeight(10);
                scoreLayout.addView(spaceView);
            }
        }
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

    private final View.OnClickListener onEasyLeaderboardSelect = v -> {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getLeaderboardIntent(getString(R.string.leaderboard_highscore_easy))
                    .addOnSuccessListener(intent -> intentActivityResultLauncher.launch(intent));
        }
    };

    private final View.OnClickListener onMediumLeaderboardSelect = v -> {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getLeaderboardIntent(getString(R.string.leaderboard_highscore_medium))
                    .addOnSuccessListener(intent -> intentActivityResultLauncher.launch(intent));
        }
    };
    private final View.OnClickListener onHardLeaderboardSelect = v -> {
        if (!BuildConfig.DEBUG && GoogleSignIn.getLastSignedInAccount(this) != null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .getLeaderboardIntent(getString(R.string.leaderboard_highscore_hard))
                    .addOnSuccessListener(intent -> intentActivityResultLauncher.launch(intent));
        }
    };

    private final View.OnClickListener onPlayAgainListener = v -> startActivity(new Intent(this, GameActivity.class));

    private void initActivityLauncher() {
        intentActivityResultLauncher = registerForActivityResult(
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