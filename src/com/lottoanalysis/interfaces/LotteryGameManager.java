package com.lottoanalysis.interfaces;

import com.lottoanalysis.data.LotteryGameDao;
import com.lottoanalysis.interfaces.LotteryGame;

import java.util.List;

public interface LotteryGameManager {

    List<String> getAllGames();

    void populateDrawings(LotteryGame game);

    LotteryGame loadLotteryData(LotteryGame game );

    LotteryGameDao getDaoInstance();

}
