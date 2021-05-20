package com.se2.bopit.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;
import com.se2.bopit.domain.services.BackgroundSoundService;
import com.se2.bopit.ui.providers.MiniGamesRegistry;


public class SplashActivity extends BaseActivity {
    private static final String TAG = SplashActivity.class.getSimpleName();

    private ImageView waveView;
    private GoogleSignInClient mGoogleSignInClient;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waveView = findViewById(R.id.waveView);

        startService(new Intent(this, BackgroundSoundService.class));

        startLoadingAnimation(waveView);

        // check available games here, because this view is loaded first
        Log.d(TAG, "checking available sensors ...");
        MiniGamesRegistry registry = MiniGamesRegistry.getInstance();
        registry.checkAvailability(getApplicationContext());
        Log.d(TAG, "done checking available sensors");

        if (!BuildConfig.DEBUG) {
            mGoogleSignInClient = GoogleSignIn.getClient(this,
                    new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_GAMES_SIGN_IN).requestId().requestProfile().build());

            ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    result -> {
                        GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(result.getData());
                        if (signInResult != null && signInResult.isSuccess()) {
                            startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
                            finish();
                        }
                    });

            activityResultLauncher.launch(mGoogleSignInClient.getSignInIntent());
        } else {
            startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
            finish();
        }
    }

    private void startLoadingAnimation(View view) {
        Animation a = new Animation() {
            boolean isNextIteration = false;

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (isNextIteration) {
                    interpolatedTime = 1 - interpolatedTime;
                    isNextIteration = (interpolatedTime > 0);
                } else {
                    isNextIteration = (interpolatedTime == 1);
                }
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) waveView.getLayoutParams();
                newLayoutParams.bottomMargin = (int) (100 * interpolatedTime);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                newLayoutParams.width = (int) (2000 * interpolatedTime + metrics.widthPixels);
                newLayoutParams.height = (int) (interpolatedTime + metrics.heightPixels * 0.5);
                newLayoutParams.horizontalBias = interpolatedTime;
                waveView.setLayoutParams(newLayoutParams);
            }
        };
        a.setDuration(8000); // in ms
        a.setRepeatCount(Animation.INFINITE);

        view.startAnimation(a);
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
}