package com.se2.bopit.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.SoundEffects;
import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.models.User;
import com.se2.bopit.ui.helpers.WaveAnimator;
import com.se2.bopit.ui.providers.GameEngineProvider;


public class GameActivity extends BaseActivity {
    static final String TAG = GameActivity.class.getSimpleName();

    public static final String GAME_MODE = "gameMode";

    //views
    ProgressBar timeBar;
    TextView scoreView;
    ImageView life1;
    ImageView life2;
    ImageView life3;
    GameEngine engine;
    boolean gameEnd = false;
    Button cheatButton;

    GameMode gameMode;

    // shared preferences
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_EFFECT = "effect";

    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get Views
        timeBar = findViewById(R.id.timeBar);
        scoreView = findViewById(R.id.scoreView);
        cheatButton = findViewById(R.id.cheatButton);
        life1 = findViewById(R.id.life1);
        life2 = findViewById(R.id.life2);
        life3 = findViewById(R.id.life3);

        //start game Engine and register listeners
        Intent intent = getIntent();
        if (intent.hasExtra(GAME_MODE)) {
            gameMode = (GameMode) intent.getSerializableExtra(GAME_MODE);
        } else {
            Log.w(TAG, "Fallback to default game mode");
            gameMode = GameMode.SINGLE_PLAYER;

        }

        //set visibility of cheat and detect button to gone in singleplayer mode
        if (gameMode == GameMode.SINGLE_PLAYER) {
            cheatButton.setVisibility(View.GONE);
        }

        new WaveAnimator(this, findViewById(R.id.waveView8)).animate(10000, true);

        engine = GameEngineProvider.getInstance().create(gameMode, gameEngineListener);


        cheatButton.setOnTouchListener((v, event) -> {
            if (engine.isMyTurn()) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    engine.pauseCountDown();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    engine.resumeCountDown();
                }
            } else {
                engine.reportCheat();
            }
            return false;
        });


        engine.startNewGame();
    }

    private final GameEngineListener gameEngineListener = new GameEngineListener() {
        @Override
        public void onGameEnd(int score) {
            Log.d(TAG, "onGameEnd");
            if (!gameEnd) {
                gameEnd = true;
                if (checkPref()) {
                    new SoundEffects(getBaseContext(), 1);
                }
                Intent intent = new Intent(getBaseContext(), WinLossActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
        }

        private boolean checkPref() {
            SharedPreferences customSharedPreferences = getBaseContext().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
            return customSharedPreferences.getBoolean(PREF_KEY_EFFECT, false);
        }

        @Override
        public void onScoreUpdate(int score) {
            scoreView.setText(String.valueOf(score));
            if (checkPref()) {
                new SoundEffects(getBaseContext(), 0);
            }
        }

        @Override
        public void onLifeUpdate(int life) {
            ImageView lifeToFade = null;
            if(life == 2){
                lifeToFade = life1;
            }
            else if(life == 1){
                lifeToFade = life2;
            }
            else if(life == 0){
                lifeToFade = life3;
            }
            if(lifeToFade != null) {
                ColorMatrix matrix = new ColorMatrix();
                matrix.setSaturation(0);
                ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
                lifeToFade.setColorFilter(cf);
                lifeToFade.setImageAlpha(128);
            }
        }

        @Override
        public void onGameStart(MiniGame game, long time) {
            Log.d(TAG, "onGameStart");
            cheatButton.setText(engine.isMyTurn() ? R.string.cheatButton : R.string.reportButton);
            scoreView.setText(engine.isMyTurn() ? "YOU" : "other");
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, (Fragment) game, null)
                    .commit();
            timeBar.setMax((int) time);
        }

        @Override
        public void onTimeTick(long time) {
            timeBar.setProgress((int) time);
        }


    };

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed");
        engine.stopCurrentGame();
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        Log.d(TAG, "onStop");
        engine.stopCurrentGame();
        super.onStop();
    }

}