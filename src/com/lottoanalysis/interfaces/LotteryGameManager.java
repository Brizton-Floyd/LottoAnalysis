package com.lottoanalysis.interfaces;

import com.lottoanalysis.data.LotteryGameDao;
import com.lottoanalysis.lottogames.LottoGame;

import java.util.List;

public interface LotteryGameManager {

    List<String> getAllGames();

    void populateDrawings(LottoGame game);

    LottoGame loadLotteryData(LottoGame game );

    LotteryGameDao getDaoInstance();

}
