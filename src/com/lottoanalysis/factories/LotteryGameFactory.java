package com.lottoanalysis.factories;

import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.lottogames.*;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGameManager;

public class LotteryGameFactory extends AbstractFactory {

    @Override
    public LottoGame getLotteryGame(String game) {

        if(game.equalsIgnoreCase("three")){

            return new PickThreeLotteryGameImpl();
        }
        if(game.equalsIgnoreCase("five")){

            try {
                return new FiveDigitLotteryGameImpl();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if(game.equalsIgnoreCase("four")){

            return new PickFourLotteryGameImpl();
        }
        if(game.equalsIgnoreCase("six")){

            return new SixDigitLotteryGameImpl();
        }

        return null;
    }

    @Override
    public Database getDataBase(String dbName) {
        return null;
    }

    @Override
    public LotteryGameManager getLotteryGameManager() {
        return null;
    }

}
