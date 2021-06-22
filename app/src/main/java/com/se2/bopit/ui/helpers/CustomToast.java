package com.se2.bopit.ui.helpers;

import android.content.Context;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.se2.bopit.R;

public class CustomToast {

    private CustomToast(){}

    public static void showToast(String message, Context context, boolean isCountdown) {

        Toast toast = new Toast(context);
        View view = null;

        if(isCountdown) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.toast_countdown_layout, null);
            toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        }
        else {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.toast_text_layout, null);
            toast.setGravity(Gravity.BOTTOM, 0, 200);
        }

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setView(view);
        toast.show();

        if(!isCountdown) {
            Handler handler = new Handler();
            handler.postDelayed(toast::cancel, 500);
        }
    }
}
