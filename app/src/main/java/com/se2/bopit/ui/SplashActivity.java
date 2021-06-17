package com.se2.bopit.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;
import com.se2.bopit.domain.services.BackgroundSoundService;
import com.se2.bopit.ui.helpers.WaveAnimator;
import com.se2.bopit.ui.providers.MiniGamesRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private ImageView waveView;
    private GoogleSignInClient mGoogleSignInClient;
    private List<String> listPermissionsNeeded;
    private boolean loginDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waveView = findViewById(R.id.waveView);

        startService(new Intent(this, BackgroundSoundService.class));

        new WaveAnimator(this, waveView).animate(8000, true);

        getPermissions();
        checkSensors();

        startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!BuildConfig.DEBUG)
            signInSilently();
    }

    private void signInSilently() {
        mGoogleSignInClient.silentSignIn();
    }

    private void getPermissions() {
        int internet = ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET);
        int loc = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        int loc2 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int mic = ContextCompat.checkSelfPermission(this,
                Manifest.permission.RECORD_AUDIO);
        listPermissionsNeeded = new ArrayList<>();

        if (internet != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.INTERNET);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if (loc2 != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (mic != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECORD_AUDIO);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]), 1);
        } else if ((loginDone || BuildConfig.DEBUG)) {
            startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
            finish();
        }
    }

    private void checkSensors() {
        // check available games here, because this view is loaded first
        Log.d(TAG, "checking available sensors ...");
        MiniGamesRegistry registry = MiniGamesRegistry.getInstance();
        registry.checkAvailability(getApplicationContext());
        Log.d(TAG, "done checking available sensors");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.removeAll(Arrays.asList(permissions));
        }

        if (listPermissionsNeeded.isEmpty() && (loginDone || BuildConfig.DEBUG)) {
            startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
            finish();
        }
    }
}