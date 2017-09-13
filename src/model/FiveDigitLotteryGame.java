package model;


import javafx.collections.ObservableList;
import utils.FileTweaker;

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

    @Override
    public LotteryGame loadGameData() {
        Object[] data = repository.retrieveGameId(getGameName());
        lottoId = (int)data[0];
        String gameName = FileTweaker.trimStateAbrrFromGameName(getGameName());

        LotteryGame game = repository.loadLotteryData(lottoId, "fantasy_five_results", 5);
        game.setPositionNumbersAllowed(5);
        game.setMinNumber((int) data[1]);
        game.setMaxNumber((int) data[2]);
        game.setGameName(gameName);
        return game;
    }


}
