package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.github.javafaker.Faker;
import com.se2.bopit.R;
import org.apache.commons.lang3.StringUtils;

public class HostJoinActivity extends BaseActivity {
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_NAME = "name";
    private EditText userInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_join);

        userInput = findViewById(R.id.editTextUsername);

        SharedPreferences customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        String username = customSharedPreferences.getString(PREF_KEY_NAME, "");
        if (username.equals("")) {
            Faker fk = new Faker();
            username = StringUtils.capitalize(fk.color().name())+"_"+ StringUtils.capitalize(fk.animal().name());
        }
        userInput.setText(username);

    }

    public void onJoinClick(View view) {
        Intent intent = new Intent(getBaseContext(), LobbyJoinActivity.class);
        intent.putExtra("isHost", false);
        intent.putExtra("username", userInput.getText().toString());
        startActivity(intent);
    }

    public void onHostClick(View view) {
        Intent intent = new Intent(getBaseContext(), LobbyHostActivity.class);
        intent.putExtra("isHost", true);
        intent.putExtra("username", userInput.getText().toString());
        startActivity(intent);
    }
}