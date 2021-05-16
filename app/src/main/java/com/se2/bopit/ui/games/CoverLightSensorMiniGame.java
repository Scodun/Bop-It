package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.se2.bopit.R;
import com.se2.bopit.domain.CoverLightSensorMiniGameModel;
import com.se2.bopit.domain.SensorMiniGameModel;
import com.se2.bopit.ui.MiniGameFragment;

public class CoverLightSensorMiniGame extends MiniGameFragment<SensorMiniGameModel> {

    public CoverLightSensorMiniGame() {
        super(new CoverLightSensorMiniGameModel());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_component, container, false);
        ImageView imageView = view.findViewById(R.id.actionImage);
        imageView.setImageResource(R.drawable.solar_eclipse);
        TextView messageText = view.findViewById(R.id.actionText);
        messageText.setText(R.string.challenge_cover_light_sensor);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        gameModel.resumeSensor(getContext());
    }

    @Override
    public void onPause() {
        super.onPause();
        gameModel.pauseSensor();
    }
}
