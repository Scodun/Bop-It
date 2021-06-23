package com.se2.bopit.ui;


import android.os.Bundle;
import android.widget.ImageView;

import com.se2.bopit.R;

public class RulesActivity extends BaseActivity {
    private ImageView ivBullet1;
    private ImageView ivBullet2;
    private ImageView ivBullet3;
    private ImageView ivBullet4;
    private ImageView ivBullet5;
    private ImageView ivBullet6;
    private ImageView ivBullet7;
    private ImageView ivBullet8;
    private ImageView ivBullet9;
    private ImageView ivBullet10;
    private ImageView ivBullet11;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        initializeView();
    }

    private void initializeView() {
        ivBullet1 = findViewById(R.id.iconBullet1);
        ivBullet1.setImageResource(R.drawable.ic_bullet);
        ivBullet2 = findViewById(R.id.iconBullet2);
        ivBullet2.setImageResource(R.drawable.ic_bullet);
        ivBullet3 = findViewById(R.id.iconBullet3);
        ivBullet3.setImageResource(R.drawable.ic_bullet);
        ivBullet4 = findViewById(R.id.iconBullet4);
        ivBullet4.setImageResource(R.drawable.ic_bullet);
        ivBullet5 = findViewById(R.id.iconBullet5);
        ivBullet5.setImageResource(R.drawable.ic_bullet);
        ivBullet6 = findViewById(R.id.iconBullet6);
        ivBullet6.setImageResource(R.drawable.ic_bullet);
        ivBullet7 = findViewById(R.id.iconBullet7);
        ivBullet7.setImageResource(R.drawable.ic_bullet);
        ivBullet8 = findViewById(R.id.iconBullet8);
        ivBullet8.setImageResource(R.drawable.ic_bullet);
        ivBullet9 = findViewById(R.id.iconBullet9);
        ivBullet9.setImageResource(R.drawable.ic_bullet);
        ivBullet10 = findViewById(R.id.iconBullet10);
        ivBullet10.setImageResource(R.drawable.ic_bullet);
        ivBullet11 = findViewById(R.id.iconBullet11);
        ivBullet11.setImageResource(R.drawable.ic_bullet);
    }

}
