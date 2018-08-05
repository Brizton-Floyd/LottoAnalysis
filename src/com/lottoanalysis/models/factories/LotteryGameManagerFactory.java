package com.lottoanalysis.models.factories;

import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.LotteryGame;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.managers.LotteryGameManagerImpl;

public class LotteryGameManagerFactory implements AbstractFactory {
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
