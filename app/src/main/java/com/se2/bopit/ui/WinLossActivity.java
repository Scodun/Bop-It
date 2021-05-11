package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;
import com.se2.bopit.R;

import java.util.Objects;

public class WinLossActivity extends AppCompatActivity {
    private Button bu_return;
    private Button bu_share;
    private TextView tv_score;
    private Intent intent;
    private int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loss);
        
        initializeButtons();
        initializeListeners();
        initializeFields();
        showScore();
        updateHighscore();
    }

    private void updateHighscore(){
        if(GoogleSignIn.getLastSignedInAccount(this)!=null) {
            Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                    .submitScore(getString(R.string.leaderboard_highscore), score);
        }
    }

    private void initializeFields() {
        intent = getIntent();
        score = intent.getIntExtra("score",0);
    }

    private void initializeListeners() {
        bu_share.setOnClickListener(onShare);
        bu_return.setOnClickListener(onReturnToGameSelectMode);
    }

    private void initializeButtons() {
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_share = (Button) findViewById(R.id.bu_share);
        tv_score =(TextView) findViewById(R.id.tv_score);
    }

    private void showScore() {
        tv_score =(TextView) findViewById(R.id.tv_score);
        tv_score.setText("Score: " + score);
    }

    private View.OnClickListener onShare = v ->{
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Share your Bop-It Score");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey everyone, my Bop-It score is "+ score + "! Can you beat it?" );
        startActivity(Intent.createChooser(shareIntent,"Share score"));
    };

    private View.OnClickListener onReturnToGameSelectMode = v -> {
        Intent gmSelectActivityIntent = new Intent(getBaseContext(),GamemodeSelectActivity.class);
        startActivity(gmSelectActivityIntent);
    };

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(this, GamemodeSelectActivity.class));
        finish();
    }

    public void showLeaderboard(View view) {
        Games.getLeaderboardsClient(this, Objects.requireNonNull(GoogleSignIn.getLastSignedInAccount(this)))
                .getLeaderboardIntent(getString(R.string.leaderboard_highscore))
                .addOnSuccessListener(intent -> startActivityForResult(intent, 9004));
    }

}