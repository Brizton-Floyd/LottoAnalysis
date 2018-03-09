package com.lottoanalysis.factories;

import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.managers.LotteryGameManagerImpl;

public class LotteryGameManagerFactory extends AbstractFactory {
    @Override
    public LottoGame getLotteryGame(String game) {
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
