package com.se2.bopit.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;

import com.se2.bopit.R;
import com.se2.bopit.domain.GameRuleItemModel;
import com.se2.bopit.domain.GameRules;
import com.se2.bopit.ui.providers.MiniGamesRegistry;

import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Map;

public class CustomizeGameRulesActivity extends AppCompatActivity {

    private static final String TAG = CustomizeGameRulesActivity.class.getSimpleName();

    Toolbar toolbar;

    GameRules model;

    Map<SwitchCompat, GameRuleItemModel> uiModelsMap = new IdentityHashMap<>();

    SwitchCompat switchAvoidRepeatingTypes;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_game_rules);

        model = MiniGamesRegistry.getInstance().gameRules;

        toolbar = findViewById(R.id.toolbar_game_rules);

        switchAvoidRepeatingTypes = findViewById(R.id.switch_avoid_repeating_game);
        switchAvoidRepeatingTypes.setOnClickListener(
                v -> model.avoidRepeatingGameTypes = v.isSelected());

        LinearLayout rulesLayout = findViewById(R.id.rules_layout);

        SwitchCompat switchTemplate = switchAvoidRepeatingTypes;
        createSwitchListFromGameModel(switchTemplate);
        applyModel();

        uiModelsMap.entrySet().stream()
                .sorted(Comparator.comparing(e -> e.getValue().name))
                .map(Map.Entry::getKey)
                .forEach(rulesLayout::addView);


        toolbar.setNavigationOnClickListener(this::navigationOnClick);

        toolbar.setOnMenuItemClickListener(this::menuItemOnClick);
    }

    @SuppressLint("NewApi")
    void createSwitchListFromGameModel(SwitchCompat template) {
        uiModelsMap.clear();
        for (GameRuleItemModel item : model.getItems()) {

            SwitchCompat sw = new SwitchCompat(template.getContext());

            sw.setText(item.name);
            sw.setChecked(item.enabled);
            sw.setTypeface(template.getTypeface());
            sw.setTextSize(template.getTextSizeUnit(), template.getTextSize());
            sw.setFontFeatureSettings(template.getFontFeatureSettings());
            sw.setFontVariationSettings(template.getFontVariationSettings());

            sw.setOnClickListener(v -> item.enabled = sw.isChecked());

            uiModelsMap.put(sw, item);
        }
    }

    boolean menuItemOnClick(MenuItem item) {
        Log.d(TAG, "options item selected: " + item.getItemId());
        if(item.getItemId() == R.id.action_revert) {
            revertToDefault();
            return true;
        }
        return false;
    }

    void navigationOnClick(View view) {
        onBackPressed();
    }

    void revertToDefault() {
        // reset model
        model.resetToDefault();
        // update ui from model
        applyModel();
    }

    @SuppressLint("NewApi")
    void applyModel() {
        switchAvoidRepeatingTypes.setSelected(model.avoidRepeatingGameTypes);
        uiModelsMap.forEach((k,v) -> k.setChecked(v.enabled));
    }
}