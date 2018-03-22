package com.lottoanalysis.factories;

import com.lottoanalysis.enums.Databases;
import com.lottoanalysis.enums.LotteryGame;
import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.managers.LotteryGameManagerImpl;

public class LotteryGameManagerFactory extends AbstractFactory {
    @Override
    public LottoGame getLotteryGame(LotteryGame game) {
        return null;
    }

    @Override
    public Database getDataBase(Databases dbName) {
        return null;
    }

    @Override
    public LotteryGameManager getLotteryGameManager() {

        return new LotteryGameManagerImpl();
    }
}
