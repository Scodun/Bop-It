package com.se2.bopit.ui;


import android.os.Bundle;
import android.widget.ImageView;

import com.se2.bopit.R;

public class RulesActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        initializeView();
    }

    private void initializeView() {
        ImageView ivBullet1 = findViewById(R.id.iconBullet1);
        ivBullet1.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet2 = findViewById(R.id.iconBullet2);
        ivBullet2.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet3 = findViewById(R.id.iconBullet3);
        ivBullet3.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet4 = findViewById(R.id.iconBullet4);
        ivBullet4.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet5 = findViewById(R.id.iconBullet5);
        ivBullet5.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet6 = findViewById(R.id.iconBullet6);
        ivBullet6.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet7 = findViewById(R.id.iconBullet7);
        ivBullet7.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet8 = findViewById(R.id.iconBullet8);
        ivBullet8.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet9 = findViewById(R.id.iconBullet9);
        ivBullet9.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet10 = findViewById(R.id.iconBullet10);
        ivBullet10.setImageResource(R.drawable.ic_bullet);
        ImageView ivBullet11 = findViewById(R.id.iconBullet11);
        ivBullet11.setImageResource(R.drawable.ic_bullet);
    }

}
