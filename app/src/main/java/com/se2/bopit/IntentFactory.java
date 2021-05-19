package com.se2.bopit;

import android.content.Context;
import android.content.Intent;

public class IntentFactory {
    public static final String SHAKE_ACTION = "com.se2.bopit.ui.games.SHAKE";
    public static final String SHAKED = "isShaking";
    private Context context;

    public IntentFactory(Context context){
        this.context = context;
    }

    public Intent accelIntent(boolean isShaked) {
        Intent intent = new Intent(SHAKE_ACTION);
        intent.putExtra(SHAKED, isShaked);
        return intent;
    }
}
