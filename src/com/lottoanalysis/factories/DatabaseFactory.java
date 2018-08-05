package com.lottoanalysis.factories;

import com.lottoanalysis.dbconnections.MySqlDatabaseImpl;
import com.lottoanalysis.enums.Databases;
import com.lottoanalysis.enums.LotteryGame;
import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;

public class DatabaseFactory implements AbstractFactory {

    @Override
    public LottoGame getLotteryGame(LotteryGame game) {
        return null;
    }

    @Override
    public Database getDataBase(Databases dbName) {

        switch (dbName){
            case MySql:
                return new MySqlDatabaseImpl();
            default:
                return null;

        }

    }

    @Override
    public LotteryGameManager getLotteryGameManager() {
        return null;
    }
}
