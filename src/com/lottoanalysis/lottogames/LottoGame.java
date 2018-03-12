package com.lottoanalysis.lottogames;

import com.lottoanalysis.interfaces.JackpotRetriever;
import com.lottoanalysis.lottogames.drawing.Drawing;
import com.lottoanalysis.retrievers.FiveAndSixDigitJackpotRetrieverImpl;
import javafx.collections.ObservableList;

import java.util.List;

public abstract class LottoGame {

    protected int lottoId;
    protected String gameName;
    protected String currentEstimatedJackpot;
    protected int minNumber;
    protected int maxNumber;
    protected int positionNumbersAllowed;
    protected ObservableList<Drawing> drawingData;

    public String getCurrentEstimatedJackpot() {
        return currentEstimatedJackpot;
    }

    public void setCurrentEstimatedJackpot(String currentEstimatedJackpot) {
        this.currentEstimatedJackpot = currentEstimatedJackpot;
    }

    public int getLottoId() {
        return lottoId;
    }

    public void setLottoId(int lottoId) {
        this.lottoId = lottoId;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
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

    public int getPositionNumbersAllowed() {
        return positionNumbersAllowed;
    }

    public void setPositionNumbersAllowed(int positionNumbersAllowed) {
        this.positionNumbersAllowed = positionNumbersAllowed;
    }

    public ObservableList<Drawing> getDrawingData() {
        return drawingData;
    }

    public void setDrawingData(ObservableList<Drawing> drawingData) {
        this.drawingData = drawingData;
    }

    public void startThreadForJackpotRetrieval(){

    }

}
