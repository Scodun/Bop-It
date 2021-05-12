package com.se2.bopit.ui.games;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ImageButtonMinigame extends Fragment implements MiniGame {

    private GameListener listener;

    private ImageButton one;
    private ImageButton two;
    private ImageButton three;

    public ImageButtonMinigame(){
        super(R.layout.fragment_image_button_game);
        listener = null;
    }

    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        LinearLayout layout = getView().findViewById(R.id.linearLayout);

        one = getView().findViewById(R.id.imageButton);
        two = getView().findViewById(R.id.imageButton2);
        three = getView().findViewById(R.id.imageButton3);

        List<ImageButton> imageButtonList = new ArrayList<>();
        imageButtonList.add(one);
        imageButtonList.add(two);
        imageButtonList.add(three);

        Collections.shuffle(imageButtonList);

        layout.removeAllViewsInLayout();

        for(ImageButton imageButton : imageButtonList){
            layout.addView(imageButton);
        }
        String begin = "Select the ";
        String end = "";

        List<Integer> randomAnimal = new ArrayList<>();
        randomAnimal.add(0);
        randomAnimal.add(1);
        randomAnimal.add(2);

        Collections.shuffle(randomAnimal);

        if(imageButtonList.get(randomAnimal.get(0)).getId() == R.id.imageButton){
            end = "Cat";
        }
        else if(imageButtonList.get(randomAnimal.get(0)).getId() == R.id.imageButton2){
            end = "Dog";
        }
        else if(imageButtonList.get(randomAnimal.get(0)).getId() == R.id.imageButton3){
            end = "Elephant";
        }
        TextView textView = getView().findViewById(R.id.textView);
        textView.setText(begin.concat(end));
    }

    public void initializeButtons(){
        getView().findViewById(R.id.imageButton).setOnClickListener(clickHandler);
        getView().findViewById(R.id.imageButton2).setOnClickListener(clickHandler);
        getView().findViewById(R.id.imageButton3).setOnClickListener(clickHandler);
    }

    private final View.OnClickListener clickHandler = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TextView textView = getView().findViewById(R.id.textView);
            String textInTextView = textView.getText().toString();

            switch (textInTextView){
                case "Select the Cat":
                    listener.onGameResult(v.getId() == R.id.imageButton);
                    one.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.pressed_button_green,null));
                    break;
                case "Select the Dog":
                    listener.onGameResult(v.getId() == R.id.imageButton2);
                    two.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.pressed_button_green,null));
                    break;
                case "Select the Elephant":
                    listener.onGameResult(v.getId() == R.id.imageButton3);
                    three.setBackground(ResourcesCompat.getDrawable(getResources(),R.drawable.pressed_button_green,null));
                    break;
                default:
                    break;
            }
        }
    };
}
