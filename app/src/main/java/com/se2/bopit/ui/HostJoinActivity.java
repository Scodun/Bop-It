package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.se2.bopit.R;

public class HostJoinActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_join);
    }

    public void onJoinClick(View view){
        Intent intent = new Intent(getBaseContext(), LobbyJoinActivity.class);
        intent.putExtra("isHost", false);
        startActivity(intent);
    }

    public void onHostClick(View view){
        Intent intent = new Intent(getBaseContext(), LobbyHostActivity.class);
        intent.putExtra("isHost", true);
        startActivity(intent);
    }
}