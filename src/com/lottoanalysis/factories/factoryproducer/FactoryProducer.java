package com.lottoanalysis.factories.factoryproducer;

import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.factories.DatabaseFactory;
import com.lottoanalysis.factories.LotteryGameFactory;
import com.lottoanalysis.factories.LotteryGameManagerFactory;

public class FactoryProducer {

    public static AbstractFactory getFactory(String factory){

        if(factory == null)
            return null;

        if(factory.equalsIgnoreCase("lotteryGameFactory")){

            return new LotteryGameFactory();
        }
        if(factory.equalsIgnoreCase("dataBaseFactory")){
            return new DatabaseFactory();
        }
        if(factory.equalsIgnoreCase("lotteryGameManagerFactory")){
            return new LotteryGameManagerFactory();
        }

        return null;
    }
}
