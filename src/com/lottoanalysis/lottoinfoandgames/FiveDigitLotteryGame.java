package com.lottoanalysis.lottoinfoandgames;


import javafx.collections.ObservableList;
import com.lottoanalysis.utilities.FileTweaker;

public class FiveDigitLotteryGame extends LotteryGame {


    public FiveDigitLotteryGame() {

    }

    public FiveDigitLotteryGame(String game) {

        setGameName(game);
    }

    public FiveDigitLotteryGame(int lottoId, ObservableList<Drawing> drawingData) {
        super(lottoId, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }
}
