package model;

import javafx.collections.ObservableList;
import utils.FileTweaker;

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

    @Override
    public LotteryGame loadGameData() {
        Object[] data = repository.retrieveGameId(gameName);
        lottoId = (int) data[0];
        String gameName = FileTweaker.trimStateAbrrFromGameName(getGameName());

        LotteryGame game = repository.loadLotteryData(lottoId, "pick4_results", 4);
        game.setPositionNumbersAllowed(4);
        game.setMinNumber((int) data[1]);
        game.setMaxNumber((int) data[2]);
        game.setGameName(gameName);
        return game;
    }
}
