package com.lottoanalysis.models.factories.abstractfactory;

import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.LotteryGame;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;
import com.lottoanalysis.models.lottogames.LottoGame;

public interface  AbstractFactory {

    LottoGame getLotteryGame(LotteryGame game);
    Database getDataBase(Databases dbName);
    LotteryGameManager getLotteryGameManager();

}
