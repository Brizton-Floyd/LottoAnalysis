package com.lottoanalysis.lottoinfoandgames;

import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDao;
import com.lottoanalysis.lottoinfoandgames.data.LotteryGameDaoImpl;
import com.lottoanalysis.utilities.FileTweaker;

import java.util.List;

public class LotteryGameManagerImpl implements LotteryGameManager {

    private LotteryGameDao lotteryGameDao;
    private static LotteryGameManager  manager = new LotteryGameManagerImpl();

    public static LotteryGameManager getInstance(){
        return manager;
    }
    private LotteryGameManagerImpl(){}


    /**
     * Selects all games from the database to populate a dropdown list
     * @return
     */
    @Override
    public List<String> getAllGames() {

        return getDaoInstance().selectAllGames();
    }

    @Override
    public void populateDrawings(LottoGame game) {

        getDaoInstance().loadUpDrawings(game);

    }

    /**
     * Returns a game instance
     * @param game
     * @return
     */
    @Override
    public LottoGame loadLotteryData(LottoGame game) {

        Object[] data = getDaoInstance().retrieveGameId( game.getGameName() );

        game.setGameId((int)data[0]);
        game.setPositionNumbersAllowed((int)data[3]);
        game.setMinNumber((int) data[1]);
        game.setMaxNumber((int) data[2]);
        game.setGameName( FileTweaker.trimStateAbrrFromGameName( game.getGameName() ) );

        populateDrawings(game);

        return game;
    }

    private LotteryGameDao getDaoInstance() {

        return lotteryGameDao = new LotteryGameDaoImpl(null);
    }
}
