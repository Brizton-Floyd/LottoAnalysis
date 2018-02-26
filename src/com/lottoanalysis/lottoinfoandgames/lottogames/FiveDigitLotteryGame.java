package com.lottoanalysis.lottoinfoandgames.lottogames;


import com.lottoanalysis.lottoinfoandgames.Drawing;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.lottoinfoandgames.LottoGame;
import javafx.collections.ObservableList;
import com.lottoanalysis.utilities.FileTweaker;

public class FiveDigitLotteryGame implements LottoGame {

    private int lottoId;
    private String gameName;
    private int minNumber;
    private int maxNumber;
    private int positionNumbersAllowed;
    private ObservableList<Drawing> drawingData;

    public FiveDigitLotteryGame() {
        this.gameName = "";
    }

    @Override
    public LottoGame getGame() {
        return new PickThreeLotteryGame();
    }

    @Override
    public int getPositionNumbersAllowed() {
        return positionNumbersAllowed;
    }

    @Override
    public int getGameId() {
        return lottoId;
    }

    @Override
    public void setPositionNumbersAllowed(int positionNumbersAllowed) {
        this.positionNumbersAllowed = positionNumbersAllowed;
    }

    @Override
    public int getMinNumber() {
        return minNumber;
    }

    @Override
    public void setMinNumber(int minNumber) {
        this.minNumber = minNumber;
    }

    @Override
    public void setGameId(int id) {
        this.lottoId = id;
    }

    @Override
    public int getMaxNumber() {
        return maxNumber;
    }

    @Override
    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    @Override
    public String getGameName() {
        return gameName;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public ObservableList<Drawing> getDrawingData() {
        return drawingData;
    }

    @Override
    public void setDrawingData(ObservableList<Drawing> data) {
        this.drawingData = data;
    }

}
