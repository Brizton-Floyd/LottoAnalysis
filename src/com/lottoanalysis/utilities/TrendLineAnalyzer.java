package com.lottoanalysis.utilities;

import java.util.*;
import java.math.*;

@SuppressWarnings("unchecked")
public class TrendLineAnalyzer {

    private static Map<String,Object[]> directionHolderMap = new LinkedHashMap<>();
    private static Map<String,Map<String,Integer[]>> directionHolderRetracementMap = new LinkedHashMap<>();
    private static List<String> directionHolderList = new ArrayList<>();
    private static String currentDirection;
    
    public static void analyzeData( int[] lottoNumbers )
    {
        clear();
        loadUpDirectionHolderMap();
        startTrendLineAnalysis(lottoNumbers);
        storeDirectionHitsAtGamesOut();
        populateDirectionRetracmentList();
        print();
    }

    private static void populateDirectionRetracmentList() {

        String currentWinningDirection = directionHolderList.get(directionHolderList.size()-1);
        for(int i = 0; i < directionHolderList.size() -1; i++){

            if(directionHolderList.get(i).equals(currentWinningDirection)){

                String nextDirection = directionHolderList.get(i+1);
                Map<String,Integer[]> data = directionHolderRetracementMap.get(currentWinningDirection);
                if(!data.containsKey(nextDirection)){
                    data.put(nextDirection,new Integer[]{1,0});
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(data,nextDirection);
                }
                else{
                    Integer[] dataTwo = data.get(nextDirection);
                    dataTwo[0]++;
                    dataTwo[1] =0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(data,nextDirection);
                }
            }
        }
    }

    private static void storeDirectionHitsAtGamesOut() {

        System.out.println(directionHolderList.get(directionHolderList.size() -1));
        directionHolderMap.forEach( (k,v) -> {

            ((Map<String,Object[]>)v[3]).forEach((kk,vv) -> {

                int currentGamesOut = (int)vv[1];
                List<Integer> gameOutContainer = (List<Integer>)vv[3];

                if(gameOutContainer.contains(currentGamesOut)){

                    int lastIndex = gameOutContainer.lastIndexOf(currentGamesOut);
                    int gamesOut = Math.abs(lastIndex - gameOutContainer.size()) - 1;
                    vv[4] = gamesOut;
                }else
                {
                    vv[4] = -1;
                }

                long occurences = gameOutContainer.stream().filter( count -> count == currentGamesOut).count();
                int value = Math.toIntExact(occurences);
                vv[5] = value;
            });
        });
    }

    private static void print(){
        
        directionHolderMap.forEach( (k,v) -> {
            System.out.println("************************************************************************************************");
            System.out.println(String.format("\n%1s %6s %30s %3s %14s %6s %14s %6s \n","Direction:",k,"Current Direction Count:",v[0],"Total Hits",v[2],"Games Out",v[1]));

            System.out.println("************************************************************************************************");

            ((Map<String,Object[]>)v[3]).forEach((kk,vv) -> {
                
                System.out.println(String.format("<------ %10s %16s %6s %15s %6s %15s %3s %20s %3s ------>",kk,"Hits:",vv[0],"Games Out:",vv[1],
                        "Last Seen:",vv[4],"Hits At G Out:",vv[5]));
                
                System.out.println(String.format("\n%1s %2s\n","Lotto Num Due For Direction",kk));
                
                ((Map<Integer,Integer[]>)vv[2]).forEach((kkk,vvv) -> {
                    
                   System.out.println(String.format("%1s %3s %16s %6s %15s %6s","Lotto Num Due:",kkk,"Hits:",vvv[0],"Games Out",vvv[1]));
                });
                System.out.println("");
            });

        });   
    }
    
    private static void clear(){
        directionHolderRetracementMap.clear();
        directionHolderList.clear();
        directionHolderMap.clear();
    }

    private static void loadUpDirectionHolderMap(){

        String[] directions = {"Up","Down","Equal"};

        for(String string : directions){

            directionHolderMap.put(string, new Object[]{0,0,0,new LinkedHashMap<String,Object[]>()});
            
        }

    }

    private static void startTrendLineAnalysis(int[] numbers)
    {
        for(int i = 0; i < numbers.length-1; i++)
        {
            int numberOne = numbers[i];
            int numberTwo = numbers[i +1];

            Object[] data;

            if(numberOne < numberTwo){

                data = directionHolderMap.get("Up");
                currentDirection = "Up";
            }
            else if(numberOne > numberTwo){
                data = directionHolderMap.get("Down");
                currentDirection = "Down";
            }
            else{
                data = directionHolderMap.get("Equal");
                currentDirection = "Equal";
            }

            directionHolderList.add(currentDirection);
            data[0] = (int) data[0] + 1;
            data[1] = 0;
            data[2] = (int) data[2] + 1;
            Map<String,Object[]> trendlineDirectionData = (Map<String,Object[]>) data[3];

            if(!trendlineDirectionData.containsKey(currentDirection + " +" +data[0])){

                trendlineDirectionData.put(currentDirection + " +" +data[0], new Object[]{0,0,new TreeMap<Integer,Integer[]>(),new ArrayList<Integer>(),0,0});
            }

            Object[] dataTwo = trendlineDirectionData.get(currentDirection + " +" + data[0]);

            dataTwo[0] = (int) dataTwo[0] + 1;
            ((List<Integer>)dataTwo[3]).add((int)dataTwo[1]);
            dataTwo[1] = 0;

            incrementGamesOut(directionHolderMap,currentDirection);

            // increment games out for other trendline directions
            incrementGamesOut(trendlineDirectionData,currentDirection + " +" +data[0]);

            Map<Integer,Integer[]> lineLengthHolderForDirection = (Map<Integer,Integer[]>)trendlineDirectionData.get(currentDirection + " +" + data[0])[2];

            int dif = Math.abs( numberOne - numberTwo );

            if(!lineLengthHolderForDirection.containsKey(numberTwo))
            {
                lineLengthHolderForDirection.put(numberTwo, new Integer[]{1,0});
                incrementGamesOut(lineLengthHolderForDirection, numberTwo);
            }
            else{
                Integer[] lineLenData = lineLengthHolderForDirection.get(numberTwo);
                lineLenData[0]++;
                lineLenData[1] = 0;
                incrementGamesOut(lineLengthHolderForDirection, numberTwo);
            }

            // reset all other direction back to zero
            directionHolderMap.forEach((k,v) -> {

                if(!k.equals(currentDirection))
                {
                    v[0] = 0;
                }
            });

        }

        directionHolderRetracementMap.put(directionHolderList.get(directionHolderList.size()-1),new LinkedHashMap<>());

    }

    private static void incrementGamesOut(Map<String,Object[]> data, String direction)
    {
        data.forEach((k,v) -> {

            if(!k.equals(direction)){
               // v[0] = 0;
                v[1] = (int) v[1] + 1;
            }
        });
    }
    private static void incrementGamesOut(Map<Integer,Integer[]> data , int number)
    {
        data.forEach( (k,v) -> {

            if(k != number){
                v[1]++;
            }
        });
    }
}
