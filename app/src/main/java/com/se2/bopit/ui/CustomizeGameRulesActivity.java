package com.se2.bopit.ui;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.RequiresApi;
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

@RequiresApi(api = Build.VERSION_CODES.R)
public class CustomizeGameRulesActivity extends AppCompatActivity {

    private static final String TAG = CustomizeGameRulesActivity.class.getSimpleName();

    Toolbar toolbar;

    GameRules model;

    Map<SwitchCompat, GameRuleItemModel> uiModelsMap = new IdentityHashMap<>();

    SwitchCompat switchAvoidRepeatingTypes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customize_game_rules);

        MiniGamesRegistry registry = MiniGamesRegistry.getInstance();
        model = registry.gameRules;

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

    SwitchCompat createSwitchControl(SwitchCompat template) {
        return new SwitchCompat(template.getContext());
    }

    void createSwitchListFromGameModel(SwitchCompat template) {
        uiModelsMap.clear();
        for (GameRuleItemModel item : model.getItems()) {

            SwitchCompat sw = createSwitchControl(template);

            sw.setText(item.name);
            if(item.available) {
                sw.setChecked(item.enabled);
            } else {
                sw.setChecked(false);
                sw.setEnabled(false);
            }

            sw.setTypeface(template.getTypeface());
            sw.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
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

    void applyModel() {
        switchAvoidRepeatingTypes.setSelected(model.avoidRepeatingGameTypes);
        uiModelsMap.forEach((k,v) -> k.setChecked(v.enabled));
    }
}