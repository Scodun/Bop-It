package com.se2.bopit.ui.helpers;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.se2.bopit.R;

public class CustomToast {
    public static void showToast(String message, Context context) {

        Toast toast = new Toast(context);

        View view = LayoutInflater.from(context)
                .inflate(R.layout.toast_countdown_layout, null);

        TextView tvMessage = view.findViewById(R.id.tvMessage);
        tvMessage.setText(message);

        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);

        toast.setView(view);
        toast.show();
    }
}
