package com.lottoanalysis.models.factories;

import com.lottoanalysis.models.dbconnections.MySqlDatabaseImpl;
import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.LotteryGame;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.models.lottogames.LottoGame;

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
