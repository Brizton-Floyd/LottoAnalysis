package com.lottoanalysis.ui.homeview;

import java.beans.EventHandler;

public enum EventSource {

    BET_SLIP_ANALYSIS("Bet Slip Analysis"),
    GAME_PANEL("Load Game Panel"),
    LOTTO_DASHBOARD("Lotto Dashboard");

    private String text;

    EventSource(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
