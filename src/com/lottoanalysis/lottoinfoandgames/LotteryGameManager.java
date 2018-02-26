package com.lottoanalysis.lottoinfoandgames;

import java.util.List;

public interface LotteryGameManager {

    List<String> getAllGames();

    void populateDrawings(LottoGame game);

    LottoGame loadLotteryData( LottoGame game );

}
