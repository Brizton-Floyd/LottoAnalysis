package com.lottoanalysis.managers;

import com.lottoanalysis.data.LotteryGameDao;
import com.lottoanalysis.data.LotteryGameDaoImpl;
import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.utilities.fileutilities.FileTweaker;

import java.util.List;

public class LotteryGameManagerImpl implements LotteryGameManager {

    public LotteryGameManagerImpl(){}

    /**
     * Selects all lottogames from the database to populate a dropdown list
     * @return
     */
    @Override
    public List<String> getAllGames() {

        return getDaoInstance().selectAllGames();
    }

    @Override
    public void populateDrawings(LotteryGame game) {

        getDaoInstance().loadUpDrawings(game);

    }

    /**
     * Returns a game instance
     * @param game
     * @return
     */
    @Override
    public LotteryGame loadLotteryData(LotteryGame game) {

        Object[] data = getDaoInstance().retrieveGameId( game.getGameName() );

        game.setGameId((int)data[0]);
        game.setPositionNumbersAllowed((int)data[3]);
        game.setMinNumber((int) data[1]);
        game.setMaxNumber((int) data[2]);
        game.setGameName( FileTweaker.trimStateAbrrFromGameName( game.getGameName() ) );

        populateDrawings(game);

        return game;
    }

    @Override
    public LotteryGameDao getDaoInstance() {

        return new LotteryGameDaoImpl(null);
    }
}
