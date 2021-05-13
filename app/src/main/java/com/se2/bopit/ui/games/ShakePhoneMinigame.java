package com.se2.bopit.ui.games;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.se2.bopit.R;
import com.se2.bopit.domain.SoundEffects;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.services.BackgroundServiceAccelerometer;


public class ShakePhoneMinigame extends Fragment implements MiniGame {
    private static final String MYPREF = "myCustomSharedPref";
    private static final String PREF_KEY_EFFECT = "effect";
    private GameListener listener;
    private BroadcastReceiver broadcastReceiver;
    private Intent intent;
    private SharedPreferences customSharedPreferences;

    public ShakePhoneMinigame() {
        super(R.layout.fragment_shake_phone_game);
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateData(intent);
            }
        };
        intent = new Intent(getActivity(), BackgroundServiceAccelerometer.class);
    }

    @Override
    public void onStart() {
        super.onStart();
        getActivity().startService(intent);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver((broadcastReceiver),
                new IntentFilter(BackgroundServiceAccelerometer.SHAKE_ACTION));
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().stopService(intent);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shake_phone_game, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        ImageView shakeImage = getView().findViewById(R.id.imageShake);
        shakeImage.setImageResource(R.drawable.ic_cocktailshaker);
    }

    private void updateData(Intent intent) {
        boolean isShaked = intent.getBooleanExtra(BackgroundServiceAccelerometer.SHAKED, false);
        if (isShaked) {
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
            if (checkPref()) {
                SoundEffects soundEffects = new SoundEffects(getContext(), 0);
            }
            listener.onGameResult(true);
        }
    }

    private boolean checkPref() {
        customSharedPreferences = getContext().getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        boolean effect = customSharedPreferences.getBoolean(PREF_KEY_EFFECT, false);
        return effect;
    }
}



