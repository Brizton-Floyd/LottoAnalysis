package com.lottoanalysis.lottoinfoandgames;

import javafx.collections.ObservableList;
import com.lottoanalysis.utilities.FileTweaker;

/**
 * Created by briztonfloyd on 8/31/17.
 */
public class SixDigitLotteryGame extends LotteryGame {

    public SixDigitLotteryGame() {

    }

    public SixDigitLotteryGame(String game) {

        setGameName(game);
    }
    public SixDigitLotteryGame(int lottoId,  ObservableList<Drawing> drawingData) {
        super(lottoId, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }
}
