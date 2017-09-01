package model;

import javafx.collections.ObservableList;

/**
 * Created by briztonfloyd on 8/31/17.
 */
public class SixDigitLotteryGame extends LotteryGame {

    public SixDigitLotteryGame() {

    }

    public SixDigitLotteryGame(String game) {

        this(0,game,null);
    }
    public SixDigitLotteryGame(int lottoId, String gameName, ObservableList<Drawing> drawingData) {
        super(lottoId, gameName, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }

    @Override
    public LotteryGame loadGameData() {
        lottoId = repository.retrieveGameId(gameName);
        return repository.loadLotteryData(lottoId,6);

    }
}
