package com.se2.bopit.domain.interfaces;

public interface NetworkGameListener {

    void onYourTurnStart(String minigame);

    void onOtherTurnEnd(String user, boolean successful);

}
