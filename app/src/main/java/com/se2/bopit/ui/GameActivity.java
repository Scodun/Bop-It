package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.platform.AndroidPlatformFeaturesProvider;
import com.se2.bopit.ui.providers.MiniGamesRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    //views
    private ProgressBar timeBar;
    private TextView scoreView;
    private Random rand;
    private ArrayList<Integer> colors;
    private GameEngine engine;
    private boolean gameEnd=false;

    // providers
    MiniGamesRegistry miniGamesProvider = new MiniGamesRegistry();
    AndroidPlatformFeaturesProvider platformFeaturesProvider = new AndroidPlatformFeaturesProvider();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get Views
        timeBar = findViewById(R.id.timeBar);
        scoreView = findViewById(R.id.scoreView);

        //start game Engine and register listeners

        engine = new GameEngine(miniGamesProvider, platformFeaturesProvider, gameEngineListener);

        engine.startNewGame();

        rand = new Random();
        colors = new ArrayList<>(
                Arrays.asList(
                        ContextCompat.getColor(this, R.color.primary),
                        ContextCompat.getColor(this, R.color.secondary),
                        ContextCompat.getColor(this, R.color.primary_variant),
                        ContextCompat.getColor(this, R.color.secondary_variant_2)
                )
        );
    }

    private final GameEngineListener gameEngineListener = new GameEngineListener() {
        @Override
        public void onGameEnd(int score) {
            if(!gameEnd) {
                gameEnd=true;
                Intent intent = new Intent(getBaseContext(), WinLossActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
        }

        @Override
        public void onScoreUpdate(int score) {
            scoreView.setTextColor(colors.get(rand.nextInt(colors.size())));
            scoreView.setText(String.valueOf(score));
        }

        @Override
        public void onGameStart(MiniGame game, long time) {
            getSupportFragmentManager().beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fragment_container_view, (Fragment) game , null)
                    .commit();
            timeBar.setMax((int) time);
        }

        @Override
        public void onTimeTick(long time) {
            timeBar.setProgress((int) time);
        }
    };

    @Override
    public void onBackPressed()
    {
        engine.stopCurrentGame();
        super.onBackPressed();
    }

    @Override
    public void onStop() {
        engine.stopCurrentGame();
        super.onStop();
    }
}