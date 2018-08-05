package com.lottoanalysis.models.factories;

import com.lottoanalysis.models.factories.enums.Databases;
import com.lottoanalysis.models.factories.enums.LotteryGame;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.models.lottogames.*;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;

public class LotteryGameFactory implements AbstractFactory {

    @Override
    public LottoGame getLotteryGame(LotteryGame game) {

        switch (game){
            case ThreeDigit:
                return new PickThreeLotteryGameImpl();
            case FourDigit:
                return new PickFourLotteryGameImpl();
            case SixDigit:
                return new SixDigitLotteryGameImpl();
            case FiveDigit:
                return new FiveDigitLotteryGameImpl();
            default:
                return null;
        }

    }

    @Override
    public Database getDataBase(Databases dbName) {
        return null;
    }

    @Override
    public LotteryGameManager getLotteryGameManager() {
        return null;
    }

}
