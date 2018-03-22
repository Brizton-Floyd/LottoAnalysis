package com.lottoanalysis.factories.factoryproducer;

import com.lottoanalysis.enums.Factory;
import com.lottoanalysis.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.factories.DatabaseFactory;
import com.lottoanalysis.factories.LotteryGameFactory;
import com.lottoanalysis.factories.LotteryGameManagerFactory;

public class FactoryProducer {

    public static AbstractFactory getFactory(Factory factory){

        if(factory == null)
            return null;

        switch (factory){

            case ManagerFactory:
                return new LotteryGameManagerFactory();
            case DataBaseFactory:
                return new DatabaseFactory();
            case LotteryGameFactory:
                return new LotteryGameFactory();
            default:
                return null;
        }
    }
}
