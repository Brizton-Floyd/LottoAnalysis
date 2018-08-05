package com.lottoanalysis.factories.abstractfactory;

import com.lottoanalysis.enums.Databases;
import com.lottoanalysis.enums.LotteryGame;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.lottogames.LottoGame;

public interface  AbstractFactory {

    LottoGame getLotteryGame(LotteryGame game);
    Database getDataBase(Databases dbName);
    LotteryGameManager getLotteryGameManager();

}
