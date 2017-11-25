package com.lottoanalysis.lottoinfoandgames.data;

import com.lottoanalysis.MainController;
import com.lottoanalysis.common.LotteryGameDaoConstants;
import com.lottoanalysis.lottoinfoandgames.*;
import com.lottoanalysis.lottoinfoandgames.lottogames.FiveDigitLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickFourLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickThreeLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.SixDigitLotteryGame;
import com.lottoanalysis.utilities.OnlineFileUtility;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.*;

public class LotteryGameDaoImpl extends Task<Void> implements LotteryGameDao {

    private MainController controller;
    private Connection connection;

    public LotteryGameDaoImpl(MainController controller) {
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


        try (Connection connection = establishConnection();
             PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_GAMES)) {

            verifyActiveConnection( connection );

            rs = pstmt.executeQuery();

            while (rs.next()) {

                allGameIds.add(rs.getInt("game_id"));
            }


            String line;
            for (int i = 0; i < allGameFiles.size(); i++) {

                updateMessage("Saving " + allGameFiles.get(i) + " Information To Database");

                file = new File(allGameFiles.get(i) + "Ver2.txt");

                //select the first record in the sql database for the given game being played
                String query = getTopRecords().get(allGameIds.get(i));
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

        controller.lottoInfoAndGamesController.unbindData();
        controller.lottoInfoAndGamesController.hideProgressBarAndLabeVbox();

        for (String file : OnlineFileUtility.getUrlPaths().keySet()) {

            File file1 = new File(file + "Ver2.txt");
            file1.delete();
        }
    }

    @Override
    public void updateDbTableForGame(String drawNum, String date, String[] positionNumbers, int gameId) {

        String query = getLottoQueryString().get(gameId);

        try (Connection con = establishConnection();
             PreparedStatement pstmt = con.prepareStatement(query)) {

            verifyActiveConnection( connection );

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

        try (Connection connection = establishConnection();
                PreparedStatement pstmt = connection.prepareStatement(query)) {

            verifyActiveConnection( connection );

            rs = pstmt.executeQuery();

            while (rs.next()) {
                num = rs.getInt("draw_number");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return num;
    }

    @Override
    public List<String> selectAllGames() {
        ResultSet rs;
        List<String> games = new LinkedList<>();

        try (Connection connection = establishConnection();
             PreparedStatement pstmt = connection.prepareStatement(SELECT_ALL_GAMES)) {

            verifyActiveConnection( connection );

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
    public LotteryGame getLotteryGameInstance(int id, String databaseName, int numPositions) {

        ResultSet rs;
        List<Drawing> drawData = new LinkedList<>();
        Drawing drawing = null;
        LotteryGame game = null;

        try (Connection connection = establishConnection()) {

            verifyActiveConnection( connection );

            String queryString = getSqlString(databaseName);

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
             PreparedStatement statement = connection.prepareStatement(GAME_ID_QUERY)) {

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
     *
     * @return
     */
    @Override
    public Connection establishConnection() {
        try {

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection(LotteryGameDaoConstants.DATABASE_CONNECTION);
            return conn;

        } catch (Exception e) {
            System.out.print(e);
            return null;

        }
    }

    /**
     * Determine if there is an active database connection
     * @param connection
     */
    private void verifyActiveConnection(Connection connection) {

        if (connection == null) {
            System.exit(1);
        }
    }

    private Map<Integer,String> getTopRecords() {

        Map<Integer, String> topRecords = new HashMap<>();

        topRecords.put(1, SELECT_TOP_RECORD_FANTASY_FIVE);
        topRecords.put(2, SELECT_TOP_RECORD_POWERBALL);
        topRecords.put(3, SELECT_TOP_RECORD_MEGAMILLIONS);
        topRecords.put(4, SELECT_TOP_RECORD_SUPERLOTTOPLUS);
        topRecords.put(5, SELECT_TOP_RECORD_DAILY4);
        topRecords.put(6, SELECT_TOP_RECORD_DAILY3);

        return topRecords;
    }

    private  Map<Integer, String> getLottoQueryString(){

        return loadData();
    }

    private  Map<Integer, String> loadData() {

        Map<Integer, String> lotto_query = new LinkedHashMap<>();

        lotto_query.put(1,INSERT_HISTORICAL_FANTASY_FIVE);
        lotto_query.put(2,INSERT_HISTORICAL_POWERBALL);
        lotto_query.put(3,INSERT_HISTORICAL_MEGA_MILLIONS);
        lotto_query.put(4,INSERT_HISTORICAL_SUPER_LOTTO_PLUS);
        lotto_query.put(5,INSERT_HISTORICAL_PICK_4);
        lotto_query.put(6,INSERT_HISTORICAL_PICK_3);

        return lotto_query;
    }

    private String getSqlString(String databaseName) {

        String[] query = new String[LOAD_DATA_FOR_GAME_QUERY.split("[?]").length + 2];
        query[0] = LOAD_DATA_FOR_GAME_QUERY.split("[?]")[0];
        query[1] = databaseName;
        query[2] = LOAD_DATA_FOR_GAME_QUERY.split("[?]")[1];
        query[3] = "?";
        query[4] = LOAD_DATA_FOR_GAME_QUERY.split("[?]")[2];

        return query[0] + query[1] + query[2] + query[3] + query[4];
    }

    // Queries
    private String GAME_ID_QUERY = "SELECT g.game_id, gg.min_number, gg.max_number FROM game g " +
            "INNER JOIN game_min_max gg ON gg.game_id = g.game_id " +
            "WHERE game_name = ?";

    private String SELECT_ALL_GAMES = "SELECT * From game";


    private String LOAD_DATA_FOR_GAME_QUERY = "SELECT * FROM ? where game_id = ?" +
            " ORDER BY draw_number";

    private String INSERT_HISTORICAL_POWERBALL =
            "INSERT OR IGNORE INTO powerball_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five,bonus_number, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private String INSERT_HISTORICAL_SUPER_LOTTO_PLUS =
            "INSERT OR IGNORE INTO super_lotto_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five,bonus_number, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private  String INSERT_HISTORICAL_MEGA_MILLIONS =
            "INSERT OR IGNORE INTO mega_million_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five,bonus_number, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?,?)";

    private  String INSERT_HISTORICAL_FANTASY_FIVE =
            "INSERT OR IGNORE INTO fantasy_five_results (draw_number, draw_date, position_one, position_two, position_three, position_four," +
                    "position_five, game_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    private  String INSERT_HISTORICAL_PICK_4 =
            "INSERT OR IGNORE INTO pick4_results (draw_number, draw_date, position_one, position_two, position_three, position_four, game_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";

    private  String INSERT_HISTORICAL_PICK_3 =
            "INSERT OR IGNORE INTO pick3_results (draw_number, draw_date, position_one, position_two, position_three, game_id) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";

    private  String SELECT_TOP_RECORD_FANTASY_FIVE = "SELECT fs.draw_number FROM fantasy_five_results fs" +
            " ORDER BY fs.draw_number desc" +
            " LIMIT 1";

    private  String SELECT_TOP_RECORD_MEGAMILLIONS = "SELECT fs.draw_number FROM mega_million_results fs" +
            " ORDER BY fs.draw_number desc" +
            " LIMIT 1";
    private  String SELECT_TOP_RECORD_DAILY4 = "SELECT fs.draw_number FROM pick4_results fs" +
            " ORDER BY fs.draw_number desc" +
            " LIMIT 1";
    private  String SELECT_TOP_RECORD_DAILY3 = "SELECT fs.draw_number FROM pick3_results fs" +
            " ORDER BY fs.draw_number desc" +
            " LIMIT 1";
    private  String SELECT_TOP_RECORD_POWERBALL = "SELECT fs.draw_number FROM powerball_results fs" +
            " ORDER BY fs.draw_number desc" +
            " LIMIT 1";
    private  String SELECT_TOP_RECORD_SUPERLOTTOPLUS = "SELECT fs.draw_number FROM super_lotto_results fs" +
            " ORDER BY fs.draw_number desc" +
            " LIMIT 1";
}