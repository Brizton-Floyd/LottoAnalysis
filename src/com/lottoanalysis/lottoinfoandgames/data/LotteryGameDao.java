package com.lottoanalysis.lottoinfoandgames.data;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;

import java.sql.*;
import java.util.List;

public interface LotteryGameDao {

    Connection establishConnection();

    Connection getConnection();

    boolean isDbConnected();

    void closeConnection();

    void updateTableIfNeeded(String drawNum, String date, String[] positionNumbers, int gameId);

    Object[] retrieveGameId(String gameName);

    List<String> selectAllGames();

    LotteryGame loadLotteryData(int id, String databaseName, int numPositions);

    int getCurrentWinningGameNumber(String query);

}
