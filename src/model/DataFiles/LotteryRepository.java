package model.DataFiles;

import controller.MainController;
import javafx.concurrent.Task;
import javafx.scene.control.MenuItem;
import model.Drawing;
import model.FiveDigitLotteryGame;
import model.LotteryGame;
import model.LotteryUrlPaths;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LotteryRepository extends Task<Void> {

    private MainController controller;
    private Connection connection;

    public LotteryRepository(MainController controller) {
        this.controller = controller;
        connection = SqlConnection.Connector();
        if (connection == null) {
            System.exit(1);
        } else {
            System.out.print("Connected");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public boolean isDbConnected() {
        try {

            return !connection.isClosed();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

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
        List<String> allGameFiles = new ArrayList<>(new LotteryUrlPaths().getPathFiles().keySet());
        List<Integer> allGameIds = new ArrayList<>();

        ResultSet rs;
        File file = null;

        try(Connection connection = SqlConnection.Connector();
                PreparedStatement pstmt = connection.prepareStatement(DaoConstants.SELECT_ALL_GAMES)){

            rs = pstmt.executeQuery();

            while (rs.next()){

                allGameIds.add(rs.getInt("game_id"));
            }

            String line;
            for(int i = 0; i < allGameFiles.size(); i++){

                updateMessage("Saving " + allGameFiles.get(i) + " Information To Database");

                file  = new File(allGameFiles.get(i) + ".txt");

                try(BufferedReader br = new BufferedReader(new FileReader(file))){

                    while((line = br.readLine()) != null){

                        String[] drawString = line.split("\\s+");

                        String drawNum = drawString[0];
                        String date = drawString[1] + " " + drawString[2] + " " + drawString[3] + " " + drawString[4];
                        String[] positionNumbers = new String[drawString.length - 5];

                        for( int k = 0; k < positionNumbers.length; k++ ){

                            positionNumbers[k] = (drawString[k + 5].length() == 1) ? "0"+drawString[k+5]:
                                                                                        drawString[k+5];
                        }

                        updateTableIfNeeded(drawNum,date,positionNumbers,allGameIds.get(i));
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

        Object data = new LotteryUrlPaths();

        for(String file : ((LotteryUrlPaths)data).getPathFiles().keySet() ){

            File file1 = new File(file + ".txt");
            file1.delete();
        }
    }

    private void updateTableIfNeeded(String drawNum, String date, String[] positionNumbers, int gameId) {

        String query = DaoConstants.getLottoQueryString().get(gameId);

        try(Connection con = SqlConnection.Connector();
                PreparedStatement pstmt = con.prepareStatement(query)){

            switch (positionNumbers.length){

                case 3:
                    pstmt.setString(1, drawNum);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setInt(6,gameId);
                    break;
                case 4:
                    pstmt.setString(1, drawNum);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6,positionNumbers[3]);
                    pstmt.setInt(7,gameId);
                    break;
                case 5:
                    pstmt.setString(1, drawNum);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6,positionNumbers[3]);
                    pstmt.setString(7,positionNumbers[4]);
                    pstmt.setInt(8,gameId);
                    break;
                case 6:
                    pstmt.setString(1, drawNum);
                    pstmt.setString(2, date);
                    pstmt.setString(3, positionNumbers[0]);
                    pstmt.setString(4, positionNumbers[1]);
                    pstmt.setString(5, positionNumbers[2]);
                    pstmt.setString(6,positionNumbers[3]);
                    pstmt.setString(7,positionNumbers[4]);
                    pstmt.setString(8,positionNumbers[5]);
                    pstmt.setInt(9,gameId);
                    break;
                default:
                    break;
            }

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    public void insert(MenuItem gameItem) {

        try(PreparedStatement pstmt = connection.prepareStatement(DaoConstants.INSERT_GAME_QUERY)){

            pstmt.setString(1, gameItem.getText());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void update(MenuItem item, String game) {
        try(PreparedStatement pstmt = connection.prepareStatement(DaoConstants.UPDATE_GAME_GAME)){

            pstmt.setString(1, "CA: " + item.getText());
            pstmt.setString(2, game);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> selectAllGames() {
        ResultSet rs;
        List<String> games = new LinkedList<>();

        try(PreparedStatement pstmt = connection.prepareStatement(DaoConstants.SELECT_ALL_GAMES)){

            rs = pstmt.executeQuery();

            while(rs.next()){
                games.add(rs.getString("game_name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return games;
    }

    public LotteryGame loadFiveDigitLottoData(int id) {

        ResultSet rs;

        List<Drawing> drawData = new LinkedList<>();

        Drawing drawing = null;

        try( Connection connection = SqlConnection.Connector();
                    PreparedStatement pstmt = connection.prepareStatement(DaoConstants.FIVE_DIGIT_LOTTERY_GAME_QUERY)){

            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();

            while(rs.next()){

                 drawing = new Drawing(rs.getInt("draw_number"), rs.getString("draw_date"),
                            rs.getString("position_one"), rs.getString("position_two"),
                            rs.getString("position_three"), rs.getString("position_four"),
                            rs.getString("position_five"));
                    drawData.add(drawing);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new FiveDigitLotteryGame(id,"Fantasy Five",drawData);
    }

    public int retrieveGameId(String gameName) {

        int id = 0 ;
        try(Connection connection = SqlConnection.Connector();
                    PreparedStatement statement = connection.prepareStatement(DaoConstants.GAME_ID_QUERY )){

            statement.setString(1, gameName);

            ResultSet rs = statement.executeQuery();

            while (rs.next()){
                id = rs.getInt("game_id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return id;
    }
}