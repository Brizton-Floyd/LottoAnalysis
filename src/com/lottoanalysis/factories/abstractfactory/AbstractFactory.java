package com.lottoanalysis.factories.abstractfactory;

import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;

public abstract class AbstractFactory {

    public abstract LottoGame getLotteryGame(String game);
    public abstract Database getDataBase(String dbName);
    public abstract LotteryGameManager getLotteryGameManager();

}
