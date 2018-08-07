package com.lottoanalysis.models.gameselection;

import com.lottoanalysis.models.factories.enums.LotteryGame;

import java.util.Map;

public class GameSelectionModelImpl implements GameSelectionModel {

    private Map<String, Integer> gameNameAndIds;
    private String defaultGameName = "CA: Fantasy Five";
    private LotteryGame lotteryGame;

    @Override
    public void setGameNameAndIds(Map<String, Integer> gameNameAndIds) {

        this.gameNameAndIds = gameNameAndIds;
    }

    @Override
    public LotteryGame getLotteryGame() {
        return lotteryGame;
    }

    @Override
    public void setFactory(LotteryGame lotteryGame) {
        this.lotteryGame = lotteryGame;
    }

    @Override
    public String getDefaultName() {
        return defaultGameName;
    }

    @Override
    public void setGameName(String name) {
        this.defaultGameName = name;
    }

    @Override
    public Map<String, Integer> getGameNameAndIds() {
        return this.gameNameAndIds;
    }

    @Override
    public String getTitle() {
        String selectionTitle = "Game Selection and Draw Updater";
        return selectionTitle;
    }
}
