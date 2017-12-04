package com.lottoanalysis.lottoinfoandgames.data;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;

import java.sql.*;
import java.util.List;

public interface LotteryGameDao {

    boolean isDbConnected();

    void updateDbTableForGame(String drawNum, String date, String[] positionNumbers, int gameId);

    Object[] retrieveGameId(String gameName);

    List<String> selectAllGames();

    LotteryGame getLotteryGameInstance(int id, String databaseName, int numPositions);

    int getCurrentWinningGameNumber(int id);

}
