package model;

import javafx.collections.ObservableList;
import utils.FileTweaker;

import java.util.Arrays;

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

    @Override
    public LotteryGame loadGameData() {
        Object[] data = repository.retrieveGameId(gameName);
        lottoId = (int) data[0];

        String twekedGameName = FileTweaker.trimStateAbrrFromGameName(gameName);

        LotteryGame game;
        final String databaseName;

        switch (lottoId){
            case 2:
                databaseName = "powerball_results";
                game = repository.loadLotteryData(lottoId,databaseName,6);
                game.setPositionNumbersAllowed(6);
                game.setMinNumber((int) data[1]);
                game.setMaxNumber((int) data[2]);
                game.setGameName(twekedGameName);
                return game;
            case 3:
                databaseName = "mega_million_results";
                game = repository.loadLotteryData(lottoId,databaseName,6);
                game.setPositionNumbersAllowed(6);
                game.setMinNumber((int) data[1]);
                game.setMaxNumber((int) data[2]);
                game.setGameName(twekedGameName);
                return game;
            case 4:
                databaseName = "super_lotto_results";
                game = repository.loadLotteryData(lottoId,databaseName,6);
                game.setPositionNumbersAllowed(6);
                game.setMinNumber((int) data[1]);
                game.setMaxNumber((int) data[2]);
                game.setGameName(twekedGameName);
                return game;

        }
        return null;

    }
}
