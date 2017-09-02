package model;

import javafx.collections.ObservableList;

/**
 * Created by briztonfloyd on 9/1/17.
 */
public class PickThreeLotteryGame extends LotteryGame {

    public PickThreeLotteryGame() {

    }

    public PickThreeLotteryGame(String game) {

        this(0,game,null);
    }
    public PickThreeLotteryGame(int lottoId, String gameName, ObservableList<Drawing> drawingData) {
        super(lottoId, gameName, drawingData);
    }

    @Override
    public void saveLotteryDrawingInformation() {

    }

    @Override
    public LotteryGame loadGameData() {
        lottoId = repository.retrieveGameId(gameName);
        return repository.loadLotteryData(lottoId,"pick3_results",3);
    }
}
