package com.lottoanalysis.lottoinfoandgames.lottogames;

import com.lottoanalysis.lottoinfoandgames.Drawing;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import javafx.collections.ObservableList;
import com.lottoanalysis.utilities.FileTweaker;

/**
 * Created by briztonfloyd on 9/1/17.
 */
public class PickThreeLotteryGame extends LotteryGame {

    public PickThreeLotteryGame() {

    }

    public PickThreeLotteryGame(String game) {

        setGameName(game);
    }
    public PickThreeLotteryGame(int lottoId,  ObservableList<Drawing> drawingData) {
        super(lottoId, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }

}
