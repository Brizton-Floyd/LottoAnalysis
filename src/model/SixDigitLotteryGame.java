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
        final String databaseName;

        switch (lottoId){
            case 2:
                databaseName = "powerball_results";
                return repository.loadLotteryData(lottoId,databaseName,6);
            case 3:
                databaseName = "mega_million_results";
                return repository.loadLotteryData(lottoId, databaseName, 6);
            case 4:
                databaseName = "super_lotto_results";
                return repository.loadLotteryData(lottoId, databaseName, 6);

        }
        return null;

    }
}
