package com.lottoanalysis.lottoinfoandgames.data;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.lottoinfoandgames.LottoGame;

import java.sql.*;
import java.util.List;

public interface LotteryGameDao {

    boolean isDbConnected();

    void updateDbTableForGame(String drawNum, String date, String[] positionNumbers, int gameId);

    Object[] retrieveGameId(String gameName);

    List<String> selectAllGames();

    void loadUpDrawings(LottoGame game);

    int getCurrentWinningGameNumber(int id);

}
