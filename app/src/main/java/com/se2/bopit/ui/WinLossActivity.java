package com.se2.bopit.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.se2.bopit.R;

import org.w3c.dom.Text;

public class WinLossActivity extends AppCompatActivity {
    Button bu_return;
    Button bu_share;
    TextView tv_score;
    int score;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_loss);
        //region Control bindings
        bu_return = (Button) findViewById(R.id.bu_return);
        bu_share = (Button) findViewById(R.id.bu_share);
        tv_score =(TextView) findViewById(R.id.tv_score);
        //endregion
        //region Event listeners
        bu_share.setOnClickListener(onShare);
        //endregion
        Intent intent = getIntent();
        showScore(intent.getIntExtra("score",0));
    }

    private void showScore(int score) {
        TextView tv_score =(TextView) findViewById(R.id.tv_score);
        tv_score.setText("Score: " + score);
    }

    private View.OnClickListener onShare = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT, score );
                startActivity(Intent.createChooser(intent,"Share score"));

            }

    };

    private void onShare(Button bu_share, int score){


    }

}