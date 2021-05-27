package com.se2.bopit.ui.games;

import android.annotation.SuppressLint;
//import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.se2.bopit.R;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;

import java.util.Random;

public class SpeechRecognitionMiniGame extends Fragment implements MiniGame {
    private static final String TAG = "SpeechRecognitionMiniGame";
    private GameListener listener;
    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private String[] possibleAnswers = {"apple","beer", "forest", "sunday", "dog", "feeling"};
    private String correctAnswer = "beer";//getRandomAnswer();


    public SpeechRecognitionMiniGame(){
        super(R.layout.fragment_action_component);
    }
    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_component, container, false);
        ImageView imageView = view.findViewById(R.id.actionImage);
        imageView.setImageResource(R.drawable.speaking);

        TextView messageText = view.findViewById(R.id.actionText);
        messageText.setText("Say \"" + correctAnswer+"\"!");
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {
                //Log.e(TAG,error);
            }

            @Override
            public void onResults(Bundle results) {

                for(String res : results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)){
                    Log.println(Log.DEBUG,"Heard",res);
                    if(res.toLowerCase().equals(correctAnswer.toLowerCase()) ){
                        listener.onGameResult(true);
                        speechRecognizer.stopListening();
                    }
                    else {
                        speechRecognizer.startListening(intentRecognizer);
                    }
                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });
        speechRecognizer.startListening(intentRecognizer);


        return view;
    }
    private String getRandomAnswer(){
        int rnd = new Random().nextInt(possibleAnswers.length);
        return possibleAnswers[rnd];
    }


}
