package com.se2.bopit.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.GameMode;
import com.se2.bopit.domain.SoundEffects;
import com.se2.bopit.domain.engine.GameEngine;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.ui.helpers.CustomToast;
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
    ImageView mesh;
    GameEngine engine;
    boolean gameEnd = false;
    Button cheatButton;

    GameMode gameMode;

    // shared preferences
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_EFFECT = "effect";

    private boolean isSamePlayer = false;

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
        mesh = findViewById(R.id.fenceImage);

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

        scoreView.setText(String.valueOf(0));

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
                intent.putExtra(GAME_MODE,gameMode);
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
            if(gameMode != GameMode.SINGLE_PLAYER) {
                cheatButton.setText(engine.isMyTurn() ? R.string.cheatButton : R.string.reportButton);
                CustomToast.showToast(engine.isMyTurn() ? "YOU" : "OTHER", getApplicationContext(),false);
                if(engine.isMyTurn()){
                    mesh.setAlpha(0f);
                    isSamePlayer = false;
                    ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) mesh.getLayoutParams();
                    newLayoutParams.height = 0;
                    mesh.setLayoutParams(newLayoutParams);
                }
                else if(!isSamePlayer){
                    animateMesh();
                    mesh.setAlpha(0.8f);
                    isSamePlayer = true;
                }
            }
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

    private void animateMesh(){
        Activity context = this;
        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) mesh.getLayoutParams();
                DisplayMetrics metrics = new DisplayMetrics();
                context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                newLayoutParams.height = (int) ((interpolatedTime) * metrics.heightPixels);
                mesh.setLayoutParams(newLayoutParams);
            }
        };
        a.setDuration((long) (1000)); // in ms

        mesh.startAnimation(a);
    }

}