package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.se2.bopit.R;

public class WinLossActivity extends AppCompatActivity {

    public int[] Points;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loss);
    }
}