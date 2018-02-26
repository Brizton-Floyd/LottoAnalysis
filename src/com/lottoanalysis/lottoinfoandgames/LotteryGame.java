package com.lottoanalysis.lottoinfoandgames;


import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDao;
import javafx.collections.ObservableList;
import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDaoImpl;

public abstract class LotteryGame {

    protected int lottoId;
    protected String gameName;
    protected int minNumber;
    protected int maxNumber;
    protected int positionNumbersAllowed;

    protected ObservableList<Drawing> drawingData;

    public LotteryGame() {

    }
    public LotteryGame(int lottoId, ObservableList<Drawing> drawingData){

        this.lottoId = lottoId;
        this.drawingData = drawingData;
    }

    public int getPositionNumbersAllowed() {
        return positionNumbersAllowed;
    }

    public void setPositionNumbersAllowed(int positionNumbersAllowed) {
        this.positionNumbersAllowed = positionNumbersAllowed;
    }

    public int getMinNumber() {
        return minNumber;
    }

    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public ObservableList<Drawing> getDrawingData() {
        return drawingData;
    }

    public abstract void saveLotteryDrawingInformation();
}
