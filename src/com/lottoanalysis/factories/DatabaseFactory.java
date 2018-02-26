package com.lottoanalysis.factories;

import com.lottoanalysis.dbconnections.MySqlDatabaseImpl;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.interfaces.LotteryGameManager;

public class DatabaseFactory extends AbstractFactory {

    @Override
    public LotteryGame getLotteryGame(String game) {
        return null;
    }

    @Override
    public Database getDataBase(String dbName) {

        if(dbName.equalsIgnoreCase("MySql")){

            return new MySqlDatabaseImpl();
        }

        return null;
    }

    @Override
    public LotteryGameManager getLotteryGameManager() {
        return null;
    }
}
