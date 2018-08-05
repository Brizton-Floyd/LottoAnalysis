package com.lottoanalysis.models.factories.factoryproducer;

import com.lottoanalysis.models.factories.enums.Factory;
import com.lottoanalysis.models.factories.abstractfactory.AbstractFactory;
import com.lottoanalysis.models.factories.DatabaseFactory;
import com.lottoanalysis.models.factories.LotteryGameFactory;
import com.lottoanalysis.models.factories.LotteryGameManagerFactory;

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
