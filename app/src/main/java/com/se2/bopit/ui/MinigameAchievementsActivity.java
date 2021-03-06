package com.se2.bopit.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.se2.bopit.R;

import info.hoang8f.widget.FButton;

public class MinigameAchievementsActivity extends BaseActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static final String MYPREF = "myCustomSharedPref";

    SharedPreferences customSharedPreferences;

    ImageView checkImageButton;
    ImageView checkColorButton;
    ImageView checkCoverLightSensor;
    ImageView checkSlider;
    ImageView checkDrawing;
    ImageView checkRightButtons;
    ImageView checkShakePhone;
    ImageView checkPlacePhone;
    ImageView checkTextBased;
    ImageView checkVolumeButton;

    FButton backButton;

    TextView imageButtonAchievement;
    TextView colorButtonAchievement;
    TextView coverLightSensorAchievement;
    TextView rightButtonCombinationAchievement;
    TextView textBasedAchievement;
    TextView volumeButtonAchievement;
    TextView placePhoneAchievement;
    TextView shakePhoneAchievement;
    TextView drawingAchievement;
    TextView sliderAchievement;


    int scoreToAchieve = 10;
    int checkImageButtonScore;
    int checkCoverLightSensorScore;
    int checkColorButtonScore;
    int checkPlacePhoneScore;
    int checkShakePhoneScore;
    int checkVolumeButtonScore;
    int checkRightButtonsScore;
    int checkTextBasedScore;
    int checkSliderScore;
    int checkDrawingScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.minigame_achievements);

        customSharedPreferences = getSharedPreferences(MYPREF, Context.MODE_PRIVATE);
        customSharedPreferences.registerOnSharedPreferenceChangeListener(this);
        initializeViews();
        setPrefValues();
        checkAchievement();

        backButton.setOnClickListener(v -> startActivity(new Intent(this, AchievementsSelectActivity.class)));
    }

    private void initializeViews() {
        imageButtonAchievement = findViewById(R.id.ImagebuttonScore);
        colorButtonAchievement = findViewById(R.id.ColorButtonScore);
        placePhoneAchievement = findViewById(R.id.PlacePhoneScore);
        shakePhoneAchievement = findViewById(R.id.ShakePhoneScore);
        coverLightSensorAchievement = findViewById(R.id.CoverLightSensorScore);
        rightButtonCombinationAchievement = findViewById(R.id.RightButtonCombinationScore);
        sliderAchievement = findViewById(R.id.SliderMinigameScore);
        drawingAchievement = findViewById(R.id.DrawingMinigameScore);
        volumeButtonAchievement = findViewById(R.id.VolumeButtonScore);
        textBasedAchievement = findViewById(R.id.TextbasedScore);
        checkImageButton = findViewById(R.id.checkImageButtons);
        checkColorButton = findViewById(R.id.checkColorButton);
        checkCoverLightSensor = findViewById(R.id.checkCoverLightSensor);
        checkDrawing = findViewById(R.id.checkDrawingMinigame);
        checkPlacePhone = findViewById(R.id.checkPlacePhone);
        checkShakePhone = findViewById(R.id.checkShakePhoneMinigame);
        checkVolumeButton = findViewById(R.id.checkVolumeButton);
        checkTextBased = findViewById(R.id.checkTextBasedMinigame);
        checkRightButtons = findViewById(R.id.checkRightButtonCombination);
        checkSlider = findViewById(R.id.checkSliderMinigame);
        backButton = findViewById(R.id.backtoHome);

        setVisibility();
    }

    public void setVisibility() {
        checkImageButton.setVisibility(View.INVISIBLE);
        checkSlider.setVisibility(View.INVISIBLE);
        checkRightButtons.setVisibility(View.INVISIBLE);
        checkTextBased.setVisibility(View.INVISIBLE);
        checkVolumeButton.setVisibility(View.INVISIBLE);
        checkCoverLightSensor.setVisibility(View.INVISIBLE);
        checkShakePhone.setVisibility(View.INVISIBLE);
        checkPlacePhone.setVisibility(View.INVISIBLE);
        checkDrawing.setVisibility(View.INVISIBLE);
        checkColorButton.setVisibility(View.INVISIBLE);
    }

    private void setPrefValues() {
        imageButtonAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_IMAGEBUTTONMINIGAME), 0)));
        colorButtonAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_COLORBUTTONMINIGAME), 0)));
        shakePhoneAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SHAKEPHONEMINIGAME), 0)));
        placePhoneAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_PLACEPHONEMINIGAME), 0)));
        rightButtonCombinationAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_RIGHTBUTTONCOMBINATION), 0)));
        sliderAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SLIDERMINIGAME), 0)));
        drawingAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_DRAWINGMINIGAME), 0)));
        textBasedAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_TEXTBASEDMINIGAME), 0)));
        volumeButtonAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_VOLUMEBUTTON), 0)));
        coverLightSensorAchievement.setText(String.valueOf(customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_COVERLIGHTSENSORMINIGAME), 0)));

        checkImageButtonScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_IMAGEBUTTONMINIGAME), 0);
        checkColorButtonScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_COLORBUTTONMINIGAME), 0);
        checkCoverLightSensorScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_COVERLIGHTSENSORMINIGAME), 0);
        checkTextBasedScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_TEXTBASEDMINIGAME), 0);
        checkShakePhoneScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SHAKEPHONEMINIGAME), 0);
        checkPlacePhoneScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_PLACEPHONEMINIGAME), 0);
        checkSliderScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_SLIDERMINIGAME), 0);
        checkDrawingScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_DRAWINGMINIGAME), 0);
        checkVolumeButtonScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_VOLUMEBUTTON), 0);
        checkRightButtonsScore = customSharedPreferences.getInt(this.getString(R.string.KEY_SCORE_RIGHTBUTTONCOMBINATION), 0);
    }

    private void checkAchievement() {
        if (checkImageButtonScore >= scoreToAchieve) {
            checkImageButton.setVisibility(View.VISIBLE);
            imageButtonAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkColorButtonScore >= scoreToAchieve) {
            checkColorButton.setVisibility(View.VISIBLE);
            colorButtonAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkCoverLightSensorScore >= scoreToAchieve) {
            checkCoverLightSensor.setVisibility(View.VISIBLE);
            coverLightSensorAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkTextBasedScore >= scoreToAchieve) {
            checkTextBased.setVisibility(View.VISIBLE);
            textBasedAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkShakePhoneScore >= scoreToAchieve) {
            checkShakePhone.setVisibility(View.VISIBLE);
            shakePhoneAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkPlacePhoneScore >= scoreToAchieve) {
            checkPlacePhone.setVisibility(View.VISIBLE);
            placePhoneAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkSliderScore >= scoreToAchieve) {
            checkSlider.setVisibility(View.VISIBLE);
            sliderAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkDrawingScore >= scoreToAchieve) {
            checkDrawing.setVisibility(View.VISIBLE);
            drawingAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkVolumeButtonScore >= scoreToAchieve) {
            checkVolumeButton.setVisibility(View.VISIBLE);
            volumeButtonAchievement.setVisibility(View.INVISIBLE);
        }
        if (checkRightButtonsScore >= scoreToAchieve) {
            checkRightButtons.setVisibility(View.VISIBLE);
            rightButtonCombinationAchievement.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        customSharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        customSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        customSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //ignore
    }
}
