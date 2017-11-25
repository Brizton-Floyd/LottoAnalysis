package com.lottoanalysis.lottoinfoandgames;

import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDao;
import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDaoImpl;
import com.lottoanalysis.utilities.FileTweaker;

import java.util.List;

public class LotteryGameManagerImpl implements LotteryGameManager {

    private LotteryGameDao lotteryGameDao;

    /**
     * Selects all games from the database to populate a dropdown list
     * @return
     */
    @Override
    public List<String> getAllGames() {

        return getDaoInstance().selectAllGames();
    }


    /**
     * Returns a game instance
     * @param gagmeID
     * @param dataBaseName
     * @param numberOfPositions
     * @return
     */
    @Override
    public LotteryGame loadLotteryData(int gagmeID, String dataBaseName, int numberOfPositions) {

        return getDaoInstance().getLotteryGameInstance( gagmeID, dataBaseName, numberOfPositions);
    }

    /**
     * Returns a game instance
     * @param game
     * @param dataBaseName
     * @param numberOfPositions
     * @return
     */
    @Override
    public LotteryGame loadLotteryData(String game, String dataBaseName, int numberOfPositions ) {

        Object[] data = getDaoInstance().retrieveGameId( game );

        LotteryGame newGame = loadLotteryData((int)data[0], dataBaseName, numberOfPositions);

        newGame.setPositionNumbersAllowed( numberOfPositions );
        newGame.setMinNumber((int) data[1]);
        newGame.setMaxNumber((int) data[2]);
        newGame.setGameName( FileTweaker.trimStateAbrrFromGameName( game ) );

        return newGame;
    }

    private LotteryGameDao getDaoInstance() {

        return lotteryGameDao = new LotteryGameDaoImpl(null);
    }
}
