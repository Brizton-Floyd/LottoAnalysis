package com.lottoanalysis.factories;

import com.lottoanalysis.dbconnections.MySqlDatabaseImpl;
import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;

public class DatabaseFactory extends AbstractFactory {

    @Override
    public LottoGame getLotteryGame(String game) {
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
