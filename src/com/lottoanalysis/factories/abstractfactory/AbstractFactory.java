package com.lottoanalysis.factories.abstractfactory;

import com.lottoanalysis.enums.Databases;
import com.lottoanalysis.enums.LotteryGame;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;

public abstract class AbstractFactory {

    public abstract LottoGame getLotteryGame(LotteryGame game);
    public abstract Database getDataBase(Databases dbName);
    public abstract LotteryGameManager getLotteryGameManager();

}
