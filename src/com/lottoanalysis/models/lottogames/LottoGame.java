package com.lottoanalysis.models.lottogames;

import com.lottoanalysis.models.drawhistory.DrawModelBase;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Set;

public abstract class LottoGame extends DrawModelBase{

    private int lottoId;
    protected String gameName;
    private String currentEstimatedJackpot;
    private int minNumber;
    private int maxNumber;
    private int positionNumbersAllowed;
    private ObservableList<Drawing> drawingData;

    public String getCurrentEstimatedJackpot() {
        return currentEstimatedJackpot;
    }

    void setCurrentEstimatedJackpot(String currentEstimatedJackpot) {
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
        if(drawingData == null) {
            drawingData = FXCollections.observableArrayList();
        }
        return drawingData;
    }

    public void setDrawingData(ObservableList<Drawing> drawingData) {
        this.drawingData = drawingData;
    }

    public abstract void startThreadForJackpotRetrieval();

    public Set<String> extractDaysOfWeekFromResults(ObservableList<Drawing> drawingData) {

        return Drawing.extractDays( drawingData );
    }
}
