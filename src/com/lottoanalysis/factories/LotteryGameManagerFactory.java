package com.lottoanalysis.factories;

import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.managers.LotteryGameManagerImpl;

public class LotteryGameManagerFactory extends AbstractFactory {
    @Override
    public LotteryGame getLotteryGame(String game) {
        return null;
    }

    @Override
    public Database getDataBase(String dbName) {
        return null;
    }

    @Override
    public LotteryGameManager getLotteryGameManager() {

        return new LotteryGameManagerImpl();
    }
}
