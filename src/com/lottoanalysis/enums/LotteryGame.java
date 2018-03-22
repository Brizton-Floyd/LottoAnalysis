package com.lottoanalysis.enums;

public enum LotteryGame {

    FiveDigit,ThreeDigit,FourDigit,SixDigit,FantasyFive("Fantasy");

    private String gameName;

    LotteryGame(){

    }
    LotteryGame(String game){
        this.gameName = game;
    }

    @Override
    public String toString(){
        return this.gameName;
    }
}
