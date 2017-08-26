package model.DataFiles;

import controller.MainController;
import javafx.concurrent.Task;
import utils.FileTweaker;

import java.sql.Connection;
import java.sql.SQLException;

public class LotteryRepository {

    private MainController controller;
    private Connection connection;

    public LotteryRepository(MainController controller) {
        this.controller = controller;
        connection = SqlConnection.Connector();
        if(connection == null){
            System.exit(1);
        }else{
            System.out.print("Connected");
        }
    }

    public boolean isDbConnected() {
        try {

            return !connection.isClosed();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}