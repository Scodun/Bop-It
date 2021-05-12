package com.se2.bopit.ui.games;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.ImageButtonMinigameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageButtonMinigame extends Fragment implements MiniGame {

    ImageButtonMinigameModel imageButtonMinigameModel;

    List<ImageButton> imageButtonList;

    String end;

    String text;
    TextView textView;

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ImageButtonMinigame(){
        super(R.layout.fragment_image_button_game);
        imageButtonMinigameModel = ImageButtonMinigameModel.createRandomModel();
    }

    @Override
    public void setGameListener(GameListener listener) {
        imageButtonMinigameModel.setGameListener(listener);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initializeButtons();

        LinearLayout layout = getView().findViewById(R.id.linearLayout);

        imageButtonList = initializeImageButtonList();

        Collections.shuffle(imageButtonList);

        layout.removeAllViewsInLayout();

        for(ImageButton imageButton : imageButtonList){
            layout.addView(imageButton);
        }

        text = imageButtonMinigameModel.challenge;
        textView = getView().findViewById(R.id.textView);
        textView.setText(text);
    }

    public void initializeButtons(){
        getView().findViewById(R.id.imageButton).setOnClickListener(clickHandler);
        getView().findViewById(R.id.imageButton2).setOnClickListener(clickHandler);
        getView().findViewById(R.id.imageButton3).setOnClickListener(clickHandler);
    }

    public List<ImageButton> initializeImageButtonList(){
        List<ImageButton> imageButtonList = new ArrayList<>();
        imageButtonList.add(getView().findViewById(R.id.imageButton));
        imageButtonList.add(getView().findViewById(R.id.imageButton2));
        imageButtonList.add(getView().findViewById(R.id.imageButton3));
        return imageButtonList;
    }

    private final View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = getView().findViewById(R.id.textView);
            String textInTextView = textView.getText().toString();
        }
    };
    void setBackground(int buttonToSetBackground){
        getView().findViewById(buttonToSetBackground)
                .setBackground(ResourcesCompat.getDrawable(getResources(),
                        R.drawable.pressed_button_green,null));
    }
    public int findRightButton(){
        switch(imageButtonMinigameModel.correctResponse.image){
            case CAT:
                return R.id.imageButton;
            case DOG:
                return R.id.imageButton2;
            case ELEPHANT:
                return R.id.imageButton3;
            default:
                Log.e("ImageButtonMinigame","Unknown Image");
                return 0;
        }
    }
}
