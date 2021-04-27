package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.se2.bopit.R;

public class WinLossActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loss);
        Intent intent = getIntent();
        showScore(intent.getIntExtra("score",100));
    }

    private void showScore(Integer score) {
        TextView tv_score =(TextView) findViewById(R.id.tv_score);
        tv_score.setText("Score: " + score);
    }
}