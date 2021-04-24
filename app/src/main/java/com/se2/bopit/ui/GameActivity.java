package com.se2.bopit.ui;

import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.GameEngine;
import com.se2.bopit.domain.interfaces.GameEngineListener;
import com.se2.bopit.domain.interfaces.MiniGame;

public class GameActivity extends AppCompatActivity {

    //views
    private ProgressBar timeBar;
    private TextView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //get Views
        timeBar = (ProgressBar) findViewById(R.id.timeBar);
        scoreView = (TextView) findViewById(R.id.scoreView);

        //start game Engine and register listeners
        GameEngine engine = new GameEngine();
        engine.setGameEngineListener(gameEngineListener);
        engine.startNewGame();
    }

    private final GameEngineListener gameEngineListener = new GameEngineListener() {
        @Override
        public void onGameEnd(int score) {
            //TODO: Add to resource
            scoreView.setText("Final Score: "+ score);
        }

        @Override
        public void onScoreUpdate(int score) {
            //TODO: Add to resource
            scoreView.setText("Current Score: "+ score);
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

}