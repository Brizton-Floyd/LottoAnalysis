package com.lottoanalysis.lottoinfoandgames;

import javafx.collections.ObservableList;
import com.lottoanalysis.utilities.FileTweaker;

/**
 * Created by briztonfloyd on 9/1/17.
 */
public class PickFourLotteryGame extends LotteryGame {


    public PickFourLotteryGame() {

    }

    public PickFourLotteryGame(String game) {

        setGameName(game);
    }
    public PickFourLotteryGame(int lottoId, ObservableList<Drawing> drawingData) {
        super(lottoId, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }

}
