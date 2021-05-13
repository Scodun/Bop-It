package com.se2.bopit.ui;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.se2.bopit.BuildConfig;
import com.se2.bopit.R;
import com.se2.bopit.domain.services.BackgroundSoundService;



public class SplashActivity extends AppCompatActivity {
    private ImageView waveView;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        waveView=findViewById(R.id.waveView);

        startService(new Intent(this, BackgroundSoundService.class));

        startLoadingAnimation(waveView);

        if(!BuildConfig.DEBUG) {
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
        }
        else{
            startActivity(new Intent(SplashActivity.this, GamemodeSelectActivity.class));
            finish();
        }
    }

    private void startLoadingAnimation(View view){
        Animation a = new Animation() {
            boolean isNextIteration=false;
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if(isNextIteration){
                    interpolatedTime=1-interpolatedTime;
                    isNextIteration=(interpolatedTime>0);
                }
                else {
                    isNextIteration = (interpolatedTime == 1);
                }
                ConstraintLayout.LayoutParams newLayoutParams = (ConstraintLayout.LayoutParams) waveView.getLayoutParams();
                newLayoutParams.bottomMargin =(int)(100*interpolatedTime);
                DisplayMetrics metrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(metrics);
                newLayoutParams.width=(int)(2000*interpolatedTime+ metrics.widthPixels);
                newLayoutParams.height=(int)(interpolatedTime+ metrics.heightPixels*0.5);
                newLayoutParams.horizontalBias= interpolatedTime;
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
        if(!BuildConfig.DEBUG)
            signInSilently();
    }

    private void signInSilently() {
        mGoogleSignInClient.silentSignIn();
    }
}