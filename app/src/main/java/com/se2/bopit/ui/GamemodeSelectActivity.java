package com.se2.bopit.ui;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.se2.bopit.R;

public class GamemodeSelectActivity extends AppCompatActivity {

    private Button
            singleplayerButton,
            localMultiplayerButton,
            onlineMultiplayerButton;
    private ImageButton settingsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gamemode_select);

        initializeButtons();
        initializeListeners();
    }


    private void initializeButtons() {
        singleplayerButton = findViewById(R.id.singleplayerButton);
        localMultiplayerButton = findViewById(R.id.localMultiplayerButton);
        onlineMultiplayerButton = findViewById(R.id.onlineMultiplayerButton);
        settingsButton = findViewById(R.id.settingsButton);
    }

    private void initializeListeners() {
        singleplayerButton.setOnClickListener(v -> {
        });

        localMultiplayerButton.setOnClickListener(v -> {
        });

        onlineMultiplayerButton.setOnClickListener(v -> {
        });

        settingsButton.setOnClickListener(v -> {
        });
    }

}