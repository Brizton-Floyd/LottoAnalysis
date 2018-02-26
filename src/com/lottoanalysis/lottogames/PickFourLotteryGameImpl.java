package com.lottoanalysis.lottogames;

import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.lottogames.drawing.Drawing;
import javafx.collections.ObservableList;

/**
 * Created by briztonfloyd on 9/1/17.
 */
public class PickFourLotteryGameImpl implements LotteryGame {

    private int lottoId;
    private String gameName;
    private int minNumber;
    private int maxNumber;
    private int positionNumbersAllowed;
    private ObservableList<Drawing> drawingData;


    public PickFourLotteryGameImpl() {
        this.gameName = "CA: Pick 4";
        this.positionNumbersAllowed = 4;
    }

    @Override
    public LotteryGame getGame() {
        return new PickThreeLotteryGameImpl();
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
