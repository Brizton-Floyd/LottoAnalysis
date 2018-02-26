package com.lottoanalysis.lottoinfoandgames.data;

import com.lottoanalysis.lottoanalysisnav.LottoAnalysisHomeController;
import com.lottoanalysis.lottoinfoandgames.*;
import com.lottoanalysis.lottoinfoandgames.lottogames.FiveDigitLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickFourLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickThreeLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.SixDigitLotteryGame;
import com.lottoanalysis.utilities.OnlineFileUtility;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class LotteryGameDaoImpl extends Task<Void> implements LotteryGameDao {

    private LottoInfoAndGamesController controller;
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public LotteryGameDaoImpl(LottoInfoAndGamesController controller) {
        this.controller = controller;
    }

    @Override
    public boolean isDbConnected() {
        try {

            return !connection.isClosed();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected Void call() throws Exception {

        // Get all files currently in directory
        List<String> allGameFiles = new ArrayList<>(OnlineFileUtility.getUrlPaths().keySet());
        List<Integer> allGameIds = new ArrayList<>();

        ResultSet rs;
        File file = null;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(ID_QUERY)) {

            rs = pstmt.executeQuery();

            while (rs.next()) {

                allGameIds.add(rs.getInt("ID"));
            }


            String line;
            for (int i = 0; i < allGameFiles.size(); i++) {

                updateMessage("Saving " + allGameFiles.get(i) + " Information To Database");

                file = new File(allGameFiles.get(i) + "Ver2.txt");

                //select the first record in the sql database for the given game being played
                //String query = getTopRecords().get(allGameIds.get(i));
                int currentDrawNumber = getCurrentWinningGameNumber( allGameIds.get(i) );

                try (BufferedReader br = new BufferedReader(new FileReader(file))) {

                    while ((line = br.readLine()) != null) {

                        String[] drawString = line.split("\\s+");

                        String drawNum = drawString[0];
                        String date = drawString[1] + " " + drawString[2] + " " + drawString[3] + " " + drawString[4];
                        String[] positionNumbers = new String[drawString.length - 5];

                        for (int k = 0; k < positionNumbers.length; k++) {

                            positionNumbers[k] = (drawString[k + 5].length() == 1) ? "0" + drawString[k + 5] :
                                    drawString[k + 5];
                        }

                        if (Integer.parseInt(drawNum) > currentDrawNumber) {
                            updateDbTableForGame(drawNum, date, positionNumbers, allGameIds.get(i));
                        }
                    }
                }
            }

        }

        updateMessage("All Files Saved To Database");

        return null;
    }

    protected void succeeded() {

        controller.unbindData();
        controller.hideProgressBarAndLabeVbox();

        for (String file : OnlineFileUtility.getUrlPaths().keySet()) {

            File file1 = new File(file + "Ver2.txt");
            file1.delete();
        }

        LottoAnalysisHomeController lottoAnalysisHomeController = new LottoAnalysisHomeController();
        lottoAnalysisHomeController.loadLotteryDashBoardScreen();
    }

    @Override
    public void updateDbTableForGame(String drawNum, String date, String[] positionNumbers, int gameId) {

        String[] queryData = getInsertQuery( positionNumbers.length );

        String query = "INSERT INTO DRAWING " + queryData[0] + queryData[1];

        try (Connection con = DBConnection.getConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            // verifyActiveConnection( connection );

            switch (positionNumbers.length) {

                case 3:
                    pstmt.setInt(1, gameId);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setInt(6, Integer.parseInt(drawNum));
                    break;
                case 4:
                    pstmt.setInt(1, gameId );
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6, positionNumbers[3]);
                    pstmt.setInt(7, Integer.parseInt(drawNum));
                    break;
                case 5:
                    pstmt.setInt(1, gameId);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6, positionNumbers[3]);
                    pstmt.setString(7, positionNumbers[4]);
                    pstmt.setInt(8, Integer.parseInt(drawNum));
                    break;
                case 6:
                    pstmt.setInt(1, gameId);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6, positionNumbers[3]);
                    pstmt.setString(7, positionNumbers[4]);
                    pstmt.setString(8, positionNumbers[5]);
                    pstmt.setInt(9, Integer.parseInt(drawNum));
                    break;
                default:
                    break;
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getCurrentWinningGameNumber(int id) {

        ResultSet rs;
        int num = 0;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(GET_RECENT_DRAW_NUMBER_QUERY)) {

            pstmt.setInt(1, id);

            // verifyActiveConnection( connection );

            rs = pstmt.executeQuery();

            while (rs.next()) {
                num = rs.getInt("DRAW_NUMBER");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return num;
    }

    @Override
    public List<String> selectAllGames() {
        ResultSet rs;
        List<String> games = new LinkedList<>();

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_GAMES)) {

            rs = pstmt.executeQuery();

            while (rs.next()) {
                games.add(rs.getString("game_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return games;
    }

    @Override
    public void loadUpDrawings(LottoGame game) {

        ResultSet rs;
        ObservableList<Drawing> drawData = FXCollections.observableArrayList();
        Drawing drawing = null;

        try (Connection connection = DBConnection.getConnection()) {

            PreparedStatement pstmt = connection.prepareStatement(SELECT_APPROPRIATE_GAME_QUERY);
            pstmt.setInt(1, game.getGameId());

            rs = pstmt.executeQuery();

            while (rs.next()) {

                switch (game.getPositionNumbersAllowed()) {

                    case 3:
                        drawing = new Drawing(rs.getInt("DRAW_NUMBER"), rs.getString("DRAW_DATE"),
                                rs.getString("DRAW_POS_ONE"), rs.getString("DRAW_POS_TWO"),
                                rs.getString("DRAW_POS_THREE"));
                        break;
                    case 4:
                        drawing = new Drawing(rs.getInt("DRAW_NUMBER"), rs.getString("DRAW_DATE"),
                                rs.getString("DRAW_POS_ONE"), rs.getString("DRAW_POS_TWO"),
                                rs.getString("DRAW_POS_THREE"), rs.getString("DRAW_POS_FOUR"));
                        break;
                    case 5:
                        drawing = new Drawing(rs.getInt("DRAW_NUMBER"), rs.getString("DRAW_DATE"),
                                rs.getString("DRAW_POS_ONE"), rs.getString("DRAW_POS_TWO"),
                                rs.getString("DRAW_POS_THREE"), rs.getString("DRAW_POS_FOUR"),
                                rs.getString("DRAW_POS_FIVE"));
                        break;
                    case 6:
                        drawing = new Drawing(rs.getInt("DRAW_NUMBER"), rs.getString("DRAW_DATE"),
                                rs.getString("DRAW_POS_ONE"), rs.getString("DRAW_POS_TWO"),
                                rs.getString("DRAW_POS_THREE"), rs.getString("DRAW_POS_FOUR"),
                                rs.getString("DRAW_POS_FIVE"), rs.getString("BONUS_NUMBER"));
                        break;

                }

                drawData.add(drawing);
            }

            // finally add drawings to game
            game.setDrawingData(drawData);

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public Object[] retrieveGameId(String gameName) {

        Object[] data = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(GAME_ID_QUERY)) {

            statement.setString(1, gameName);

            ResultSet rs = statement.executeQuery();
            data = new Object[4];

            while (rs.next()) {

                data[0] = rs.getInt("ID");
                data[1] = rs.getInt("GAME_MIN_NUMBER");
                data[2] = rs.getInt("GAME_MAX_NUMBER");
                data[3] = rs.getInt("NUMBER_OF_POSITIONS");

            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }

    private String[] getInsertQuery(int number){

        Map<Integer,String[]> insertQueryData = new TreeMap<>();
        insertQueryData.put(3, new String[]{"(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, " +
                "DRAW_NUMBER)",
                " VALUES(?,?,?,?,?,?)"});
        insertQueryData.put(4, new String[]{"(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, " +
                "DRAW_POS_FOUR, DRAW_NUMBER)",
                " VALUES(?,?,?,?,?,?,?)"});
        insertQueryData.put(5, new String[]{"(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, " +
                "DRAW_POS_FOUR, DRAW_POS_FIVE, DRAW_NUMBER)",
                " VALUES(?,?,?,?,?,?,?,?)"});
        insertQueryData.put(6, new String[]{"(LOTTERY_GAME_ID, DRAW_DATE, DRAW_POS_ONE, DRAW_POS_TWO, DRAW_POS_THREE, " +
                "DRAW_POS_FOUR, DRAW_POS_FIVE, BONUS_NUMBER, DRAW_NUMBER)",
                " VALUES(?,?,?,?,?,?,?,?,?)"});

        return insertQueryData.get( number );
    }

    // Queries
    private String GAME_ID_QUERY = "SELECT G.ID, G.GAME_MIN_NUMBER, G.GAME_MAX_NUMBER, G.NUMBER_OF_POSITIONS FROM LOTTERY_GAME G " +
            "WHERE G.GAME_NAME = ?";

    private String SELECT_ALL_GAMES = "SELECT * From LOTTERY_GAME";

    private String SELECT_APPROPRIATE_GAME_QUERY = "SELECT * FROM DRAWING WHERE LOTTERY_GAME_ID = ? ORDER BY DRAW_NUMBER limit 8000;";

    private String GET_RECENT_DRAW_NUMBER_QUERY = "SELECT DRAW_NUMBER FROM DRAWING " +
            "WHERE LOTTERY_GAME_ID = ? " +
            "ORDER BY DRAW_NUMBER DESC " +
            "LIMIT 1";

    private String ID_QUERY = "SELECT ID FROM LOTTERY_GAME";
}