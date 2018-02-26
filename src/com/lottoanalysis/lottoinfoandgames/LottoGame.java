package com.lottoanalysis.lottoinfoandgames;

import javafx.collections.ObservableList;

import java.util.List;

public interface LottoGame {

    LottoGame getGame();
    int getPositionNumbersAllowed();
    int getGameId();
    void setPositionNumbersAllowed(int positionNumbersAllowed);
    int getMinNumber();
    void setMinNumber(int minNumber);
    void setGameId(int id);
    int getMaxNumber();
    void setMaxNumber(int maxNumber);
    String getGameName();
    void setGameName(String gameName);
    ObservableList<Drawing> getDrawingData();
    void setDrawingData(ObservableList<Drawing> data);

}
