package com.lottoanalysis.lottoinfoandgames.data;

import com.lottoanalysis.MainController;
import com.lottoanalysis.common.LotteryGameDaoConstants;
import com.lottoanalysis.lottoinfoandgames.*;
import com.lottoanalysis.utilities.OnlineFileUtility;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.scene.control.MenuItem;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LotteryGameDaoImpl extends Task<Void> implements LotteryGameDao {

    private MainController controller;
    private Connection connection;

    public LotteryGameDaoImpl(MainController controller) {
        this.controller = controller;
        connection = establishConnection();
        if (connection == null) {
            System.exit(1);
        }
    }

    @Override
    public Connection getConnection() {
        return connection;
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
    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Void call() throws Exception {

        // Get all files currently in directory
        List<String> allGameFiles = new ArrayList<>(OnlineFileUtility.getUrlPaths().keySet());
        List<Integer> allGameIds = new ArrayList<>();

        ResultSet rs;
        File file = null;
        

        try (Connection connection = establishConnection();
             PreparedStatement pstmt = connection.prepareStatement(DaoConstants.SELECT_ALL_GAMES)) {

            rs = pstmt.executeQuery();

            while (rs.next()) {

                allGameIds.add(rs.getInt("game_id"));
            }


            String line;
            for (int i = 0; i < allGameFiles.size(); i++) {

                updateMessage("Saving " + allGameFiles.get(i) + " Information To Database");

                file = new File(allGameFiles.get(i) + "Ver2.txt");

                //select the first record in the sql database for the given game being played
                String query = DaoConstants.getTopRecords().get(allGameIds.get(i));
                int currentDrawNumber = getCurrentWinningGameNumber(query);

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

                        if( Integer.parseInt(drawNum) > currentDrawNumber) {
                            updateTableIfNeeded(drawNum, date, positionNumbers, allGameIds.get(i));
                        }
                    }
                }
            }

        }

        updateMessage("All Files Saved To Database");

        return null;
    }

    protected void succeeded() {

        controller.lottoInfoAndGamesController.unbindData();
        controller.lottoInfoAndGamesController.hideProgressBarAndLabeVbox();

        for (String file : OnlineFileUtility.getUrlPaths().keySet()) {

            File file1 = new File(file + "Ver2.txt");
            file1.delete();
        }
    }

    @Override
    public void updateTableIfNeeded(String drawNum, String date, String[] positionNumbers, int gameId) {

        String query = DaoConstants.getLottoQueryString().get(gameId);

        try (Connection con = establishConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            switch (positionNumbers.length) {

                case 3:
                    pstmt.setInt(1, Integer.parseInt(drawNum));
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setInt(6, gameId);
                    break;
                case 4:
                    pstmt.setInt(1, Integer.parseInt(drawNum));
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6, positionNumbers[3]);
                    pstmt.setInt(7, gameId);
                    break;
                case 5:
                    pstmt.setInt(1, Integer.parseInt(drawNum));
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6, positionNumbers[3]);
                    pstmt.setString(7, positionNumbers[4]);
                    pstmt.setInt(8, gameId);
                    break;
                case 6:
                    pstmt.setInt(1, Integer.parseInt(drawNum));
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6, positionNumbers[3]);
                    pstmt.setString(7, positionNumbers[4]);
                    pstmt.setString(8, positionNumbers[5]);
                    pstmt.setInt(9, gameId);
                    break;
                default:
                    break;
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getCurrentWinningGameNumber(String query) {

        ResultSet rs;
        int num = 0;
        //String q = "SELECT draw_number FROM fantasy_five_results";
        if(isDbConnected()){

            try (PreparedStatement pstmt = connection.prepareStatement(query)) {

                rs = pstmt.executeQuery();

                while (rs.next()) {
                    num = rs.getInt("draw_number");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }

        }

        return num;
    }

    @Override
    public List<String> selectAllGames() {
        ResultSet rs;
        List<String> games = new LinkedList<>();

        try (Connection connection = establishConnection();
             PreparedStatement pstmt = connection.prepareStatement(DaoConstants.SELECT_ALL_GAMES)) {

            rs = pstmt.executeQuery();

            while (rs.next()) {
                games.add(rs.getString("game_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    @Override
    public LotteryGame loadLotteryData(int id, String databaseName, int numPositions) {

        ResultSet rs;
        List<Drawing> drawData = new LinkedList<>();
        Drawing drawing = null;
        LotteryGame game = null;

        try (Connection connection = establishConnection()) {
            String[] query = new String[DaoConstants.LOAD_DATA_FOR_GAME_QUERY.split("[?]").length + 2];
            query[0] = DaoConstants.LOAD_DATA_FOR_GAME_QUERY.split("[?]")[0];
            query[1] = databaseName;
            query[2] = DaoConstants.LOAD_DATA_FOR_GAME_QUERY.split("[?]")[1];
            query[3] = "?";
            query[4] = DaoConstants.LOAD_DATA_FOR_GAME_QUERY.split("[?]")[2];

            String queryString = query[0] + query[1] + query[2] + query[3] + query[4];

            PreparedStatement pstmt = connection.prepareStatement(queryString);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                switch (numPositions) {

                    case 3:
                        drawing = new Drawing(rs.getInt("draw_number"), rs.getString("draw_date"),
                                rs.getString("position_one"), rs.getString("position_two"),
                                rs.getString("position_three"));
                        break;
                    case 4:
                        drawing = new Drawing(rs.getInt("draw_number"), rs.getString("draw_date"),
                                rs.getString("position_one"), rs.getString("position_two"),
                                rs.getString("position_three"), rs.getString("position_four"));
                        break;
                    case 5:
                        drawing = new Drawing(rs.getInt("draw_number"), rs.getString("draw_date"),
                                rs.getString("position_one"), rs.getString("position_two"),
                                rs.getString("position_three"), rs.getString("position_four"),
                                rs.getString("position_five"));
                        break;
                    case 6:
                        drawing = new Drawing(rs.getInt("draw_number"), rs.getString("draw_date"),
                                rs.getString("position_one"), rs.getString("position_two"),
                                rs.getString("position_three"), rs.getString("position_four"),
                                rs.getString("position_five"), rs.getString("bonus_number"));
                        break;

                }

                drawData.add(drawing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (numPositions == 5)
            return new FiveDigitLotteryGame(id, FXCollections.observableArrayList(drawData));
        else if (numPositions == 6)
            return new SixDigitLotteryGame(id, FXCollections.observableArrayList(drawData));
        else if (numPositions == 4)
            return new PickFourLotteryGame(id, FXCollections.observableArrayList(drawData));
        else if (numPositions == 3)
            return new PickThreeLotteryGame(id, FXCollections.observableArrayList(drawData));

        return null;
    }

    @Override
    public Object[] retrieveGameId(String gameName) {

        Object[] data = null;
        try (Connection connection = establishConnection();
             PreparedStatement statement = connection.prepareStatement(DaoConstants.GAME_ID_QUERY)) {

            statement.setString(1, gameName);

            ResultSet rs = statement.executeQuery();
            data = new Object[3];

            while (rs.next()) {

                data[0] = rs.getInt("game_id");
                data[1] = rs.getInt("min_number");
                data[2] = rs.getInt("max_number");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    /**
     * Establish a database connection
     * @return
     */
    @Override
    public Connection establishConnection() {
        try{

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(LotteryGameDaoConstants.DATABASE_CONNECTION);
            return conn;

        }catch (Exception e){
            System.out.print(e);
            return null;

        }
    }
}