package model;


import javafx.collections.ObservableList;

import java.util.List;

public class FiveDigitLotteryGame extends LotteryGame{


    public FiveDigitLotteryGame() {

    }

    public FiveDigitLotteryGame(String game) {

        this(0,game,null);
    }
    public FiveDigitLotteryGame(int lottoId, String gameName, ObservableList<Drawing> drawingData) {
        super(lottoId, gameName, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }

    @Override
    public LotteryGame loadGameData() {
        lottoId = repository.retrieveGameId(gameName);
        return repository.loadFiveDigitLottoData(lottoId);
    }


}
