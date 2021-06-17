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
import com.se2.bopit.domain.Difficulty;
import com.se2.bopit.domain.TextToSpeech;
import com.se2.bopit.domain.annotations.RequireSensor;
import com.se2.bopit.domain.gamemodel.CoverLightSensorMiniGameModel;
import com.se2.bopit.ui.DifficultyActivity;
import com.se2.bopit.ui.SensorMiniGameFragment;

@RequireSensor(CoverLightSensorMiniGameModel.SENSOR_TYPE)
public class CoverLightSensorMiniGame extends SensorMiniGameFragment<CoverLightSensorMiniGameModel> {

    public CoverLightSensorMiniGame() {
        this(new CoverLightSensorMiniGameModel());
    }

    public CoverLightSensorMiniGame(CoverLightSensorMiniGameModel gameModel) {
        super(gameModel);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_component, container, false);
        ImageView imageView = view.findViewById(R.id.actionImage);
        imageView.setImageResource(R.drawable.solar_eclipse);
        TextView messageText = view.findViewById(R.id.actionText);
        messageText.setText(R.string.challenge_cover_light_sensor);
        new TextToSpeech().sayText("Dark!", this.getContext());

        return view;
    }

    @Override
    public long getTime(Difficulty difficulty, int score) {
        double maxExponent = 7;
        double multiplier = 0.075;

        int base = 2000;
        switch (DifficultyActivity.difficulty) {
            case EASY:
                base = 2000;
            case MEDIUM:
                base = 1500;
            case HARD:
                base = 1000;
        }

        return generateTime(maxExponent, multiplier, base, score);
    }
}
