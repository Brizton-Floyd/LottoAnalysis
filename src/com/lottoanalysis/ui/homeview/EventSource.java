package com.lottoanalysis.ui.homeview;

import java.beans.EventHandler;

public enum EventSource {

    BET_SLIP_ANALYSIS("Bet Slip Analysis"),
    GAME_PANEL("Load Game Panel"),
    LOTTO_DASHBOARD("Lotto Dashboard"),
    GAME_OUT_ANALYSIS("Game Out Chart"),
    Lottery_Number_Gaps("Lottery Number Gaps"),
    COMPANION_NUMBER("Companion Numbers"),
    POSITION_HIT_SEQUENCE("Position Sequence Chart");

    private String text;

    EventSource(String text){
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
