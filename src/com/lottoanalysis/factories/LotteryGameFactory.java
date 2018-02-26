package com.lottoanalysis.factories;

import com.lottoanalysis.lottogames.*;
import com.lottoanalysis.interfaces.Database;
import com.lottoanalysis.interfaces.LotteryGame;
import com.lottoanalysis.interfaces.LotteryGameManager;

public class LotteryGameFactory extends AbstractFactory{

    @Override
    public LotteryGame getLotteryGame(String game) {

        if(game.equalsIgnoreCase("three")){

            return new PickThreeLotteryGameImpl();
        }
        if(game.equalsIgnoreCase("five")){

            return new FiveDigitLotteryGameImpl();
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
