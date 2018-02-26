package com.lottoanalysis.dbconnections;

import com.lottoanalysis.constants.LotteryGameDaoConstants;
import com.lottoanalysis.interfaces.Database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDatabaseImpl implements Database{

    private static Connection conn;

    private static Connection connect() throws SQLException{
        try {

            Class.forName(LotteryGameDaoConstants.DRIVER_STRING).newInstance();
            conn = DriverManager.getConnection(LotteryGameDaoConstants.MYSQL_DATABASE_CONNECTION,
                                               LotteryGameDaoConstants.USER_NAME,
                                               LotteryGameDaoConstants.PASSWORD);

        }catch (Exception e){

        }

        return conn;
    }


    /**
     * Returns a MYSql Connection
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @Override
    public Connection getConnection() throws SQLException, ClassNotFoundException {
        if(conn !=null && !conn.isClosed())
            return conn;
        connect();
        return conn;
    }

}

