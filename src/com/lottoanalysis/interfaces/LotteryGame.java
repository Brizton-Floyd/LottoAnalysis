package com.lottoanalysis.interfaces;

import com.lottoanalysis.lottogames.drawing.Drawing;
import javafx.collections.ObservableList;

public interface LotteryGame {

    LotteryGame getGame();
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
