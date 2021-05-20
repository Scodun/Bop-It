package com.se2.bopit.domain;

import android.content.Context;

import java.util.Locale;

public class TextToSpeech {
    private android.speech.tts.TextToSpeech t1;

    public void sayText(String text, Context context) {
        t1 = new android.speech.tts.TextToSpeech(context, status -> {
            if (status != android.speech.tts.TextToSpeech.ERROR) {
                t1.setLanguage(Locale.UK);
                t1.setPitch((float) 0.9);
                t1.setSpeechRate((float) 1.5);
                t1.speak(text, android.speech.tts.TextToSpeech.QUEUE_FLUSH, null);
            }
        });
    }
}
