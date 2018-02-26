package com.lottoanalysis.factories;

import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.interfaces.LotteryGameManager;

public abstract class AbstractFactory {

    public abstract LotteryGame getLotteryGame(String game);
    public abstract Database getDataBase(String dbName);
    public abstract LotteryGameManager getLotteryGameManager();

}
