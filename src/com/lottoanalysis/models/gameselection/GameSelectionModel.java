package com.lottoanalysis.models.gameselection;

import com.lottoanalysis.models.factories.enums.LotteryGame;

import java.util.Map;

public interface GameSelectionModel {

    void setGameNameAndIds(Map<String,Integer> gameNameAndIds);

    void setGameName(String name);

    void setFactory(LotteryGame lotteryGame);

    LotteryGame getLotteryGame();

    String getDefaultName();

    Map<String,Integer> getGameNameAndIds();

    String getTitle();
}
