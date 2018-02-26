package com.lottoanalysis.lottoinfoandgames;

public class FactoryProducer {

    public static AbstractFactory getFactory(String factory){

        if(factory == null)
            return null;

        if(factory.equalsIgnoreCase("lotteryGameFactory")){

            return new LotteryGameFactory();
        }

        return null;
    }
}
