package com.se2.bopit.ui.games;

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
import com.se2.bopit.domain.gamemodel.GameModel;
import com.se2.bopit.domain.interfaces.GameListener;
import com.se2.bopit.domain.interfaces.MiniGame;
import com.se2.bopit.domain.responsemodel.ResponseModel;

import java.util.Random;

public class SpeechRecognitionMiniGame extends Fragment implements MiniGame {
    private GameListener listener;
    private SpeechRecognizer speechRecognizer;
    private Intent intentRecognizer;
    private final String[] possibleAnswers = {"apple", "forest", "sunday", "guitar", "piano"};
    private final String correctAnswer = getRandomAnswer();


    public SpeechRecognitionMiniGame() {
        super(R.layout.fragment_action_component);
    }

    /**
     * @param listener - Game listener
     *                 Sets Game Listener
     */
    @Override
    public void setGameListener(GameListener listener) {
        this.listener = listener;
    }

    @Override
    public GameModel<? extends ResponseModel> getModel() {
        return null;
    }

    /**
     * @param inflater
     * @param container
     * @param savedInstanceState Binds view to game
     *                           injects image into view
     *                           sets up speech recognizer
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_action_component, container, false);
        ImageView imageView = view.findViewById(R.id.actionImage);
        imageView.setImageResource(R.drawable.speaking);

        TextView messageText = view.findViewById(R.id.actionText);
        messageText.setText("Say \"" + correctAnswer + "\"!");
        intentRecognizer = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intentRecognizer.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(view.getContext());
        recognizeSpeech();
        return view;
    }

    public void recognizeSpeech() {
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            /**
             * @param params
             * Called when the endpointer is ready for the user to start speaking.
             */
            @Override
            public void onReadyForSpeech(Bundle params) {
                //not needed
            }

            /**
             * The user has started to speak.
             */
            @Override
            public void onBeginningOfSpeech() {
                //not needed
            }

            /**
             * @param rmsdB
             * The sound level in the audio stream has changed.
             */
            @Override
            public void onRmsChanged(float rmsdB) {
                //not needed
            }

            /**
             * More sound has been received.
             */
            @Override
            public void onBufferReceived(byte[] buffer) {
                //not needed
            }

            /**
             * Called after the user stops speaking.
             */
            @Override
            public void onEndOfSpeech() {
                //not needed
            }

            /**
             * @param error
             * A network or recognition error occurred.
             */
            @Override
            public void onError(int error) {
                //not needed
            }

            /**
             * @param results - current sensor value
             * receives results of speech
             * Checks wether the said word matches the correct answer
             * if it matches, the Game listener is called and the game ends
             * if it doesnt match, the speech recognizer starts listening again for other words
             */
            @Override
            public void onResults(Bundle results) {

                for (String res : results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)) {
                    Log.println(Log.DEBUG, "Heard", res);
                    if (listener != null) {
                        if (res.equalsIgnoreCase(correctAnswer)) {
                            listener.onGameResult(true);
                            speechRecognizer.stopListening();
                        } else {
                            speechRecognizer.startListening(intentRecognizer);
                        }
                    }
                }

            }

            /**
             * @param partialResults - contains partial results
             * Called when partial recognition results are available.
             */
            @Override
            public void onPartialResults(Bundle partialResults) {
                //not needed
            }

            /**
             * @param eventType - eventType
             * @param params - params
             * Reserved for adding future events.
             */
            @Override
            public void onEvent(int eventType, Bundle params) {
                //not needed
            }
        });
        speechRecognizer.startListening(intentRecognizer);
    }

    /**
     * returns a random string from the
     * answer set.
     */
    private String getRandomAnswer() {
        int rnd = new Random().nextInt(possibleAnswers.length);
        return possibleAnswers[rnd];
    }


}
