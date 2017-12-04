package com.lottoanalysis.lottoinfoandgames;

import java.util.List;

public interface LotteryGameManager {

    List<String> getAllGames();

    LotteryGame loadLotteryData(int gagmeID, String dataBaseName, int numberOfPositions);

    LotteryGame loadLotteryData( String game, String dataBaseName, int numberOfPositions );

}
