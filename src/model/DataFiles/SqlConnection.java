package model.DataFiles;

import java.sql.*;

public class SqlConnection {

    public static Connection Connector() {

        try{

            Class.forName("org.sqlite.JDBC");
            Connection conn = DriverManager.getConnection("jdbc:sqlite:LotteryDb.sqlite");
            return conn;

        }catch (Exception e){
            System.out.print(e);
            return null;

        }

    }
}