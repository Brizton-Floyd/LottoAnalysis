package com.lottoanalysis.utilities.analyzerutilites;

import java.util.*;

import com.lottoanalysis.constants.*;
import com.lottoanalysis.lottogames.LottoGame;

@SuppressWarnings("unchecked")
public class UpperLowerRangeAnalyzer {

    private static Map<Integer[],Object[]> uppperLowerRangeData = new LinkedHashMap<>();

    public static void analyze(int[] drawPositionNumbers, LottoGame game)
    {

        // Clear function so everytime a new position in loaded we only analyze 
        // numbers pertaining to that position 
        clear();
        plugUpperAndLowerHalfRangesIntoMap(game);

        beginAnalyzingProcess(drawPositionNumbers);
        determineHitsAtGamesOutAndLastTimeOfOccurrence();
        print();
    }

    private static void print()
    {
        int count = 0;

        for(Map.Entry<Integer[],Object[]> data : uppperLowerRangeData.entrySet())
        {
            Object[] d = data.getValue();
            String val = (count == 1) ? "Upper" : "Lower";
            
            System.out.println( "\n" + val + " Half Numbers: " + Arrays.toString(data.getKey()));
            System.out.println(String.format("\n%26s %4d %15s %2d %22s %2d %20s %2d","Hits:",d[0] , "Games Out:", d[1], "Hits At Games Out:", d[2], "Last Appearance:",d[3]));
            
            Map<Integer[], Object[]> lastDigitData = (Map<Integer[],Object[]>)d[5];
            count = 0;
            
            for(Map.Entry<Integer[],Object[]> ld : lastDigitData.entrySet())
            {
                Object[] pp = ld.getValue();
                val = (count == 1) ? "Upper" : "Lower";
                System.out.println(String.format("\n%45s %10s","LAST DIGIT PERFORMANCE WITHIN GROUP "  + val.toUpperCase() ,Arrays.toString(ld.getKey())));
                System.out.println(String.format("\n%26s %4d %15s %2d %22s %2d %20s %2d","Hits:",pp[0] , "Games Out:", pp[1], "Hits At Games Out:", pp[2], "Last Appearance:",pp[3]));
                
                Map<Integer,Integer[]> lottoNumberData = (Map<Integer,Integer[]>)pp[5];
                for(Map.Entry<Integer,Integer[]> ln : lottoNumberData.entrySet())
                {
                    Integer[] lnValues = ln.getValue();
                    System.out.println(String.format("\n%26s %2d %15s %4d %22s %2d","Lotto Num #:", ln.getKey() , "Hits:", lnValues[0], "Games Out:",lnValues[1]));                   
                }

                Map<Integer,Object[]> remainderData = (Map<Integer,Object[]>)pp[6];
                for(Map.Entry<Integer,Object[]> ln : remainderData.entrySet())
                {
                    Object[] lnValues = ln.getValue();
                    Integer[] arr = ((List<Integer>)lnValues[2]).toArray(new Integer[((List<Integer>)lnValues[2]).size()]);
                    System.out.println(String.format("\n%26s %2d %15s %4d %22s %2d %40s","Remainder:", ln.getKey() , "Hits:", lnValues[0], "Games Out:",lnValues[1], "Numbers At Remainder " + Arrays.toString(arr)));
                }

                count++;
                
            }
             
            count = 1;
        }
        
    }
    
    private static void determineHitsAtGamesOutAndLastTimeOfOccurrence()
    {
        uppperLowerRangeData.forEach((k,v) -> {

            int count = 0;
            int currentGamesOut = (int)v[1];
            List<Integer> gamesOutHolder = (List<Integer>) v[4];

            for(int i = 0; i < gamesOutHolder.size(); i++)
            {
                if(gamesOutHolder.get(i) == currentGamesOut)
                {
                    count++;
                }
            }

            v[2] = count;
            v[3] = Math.abs( gamesOutHolder.size() - gamesOutHolder.lastIndexOf(currentGamesOut)) ;

            Map<Integer[], Object[]> lastDigitData = (Map<Integer[],Object[]>) v[5];
            lastDigitData.forEach((kk,vv) -> {

                int countTwo = 0;
                int currentGamesOutTwo = (int)vv[1];
                List<Integer> gamesOutHolderTwo = (List<Integer>) vv[4];

                for(int i = 0; i < gamesOutHolderTwo.size(); i++)
                {
                    if(gamesOutHolderTwo.get(i) == currentGamesOutTwo)
                    {
                        countTwo++;
                    }
                }

                vv[2] = countTwo;
                vv[3] = Math.abs( gamesOutHolderTwo.size() - gamesOutHolderTwo.lastIndexOf(currentGamesOutTwo)) ;
    
            });

        });
    }

    private static void beginAnalyzingProcess(int[] drawPositionNumbers)
    {

        for(int i = 0; i < drawPositionNumbers.length; i++)
        {
            final int winningNumber = drawPositionNumbers[i];

            uppperLowerRangeData.forEach( (key,value) -> {

                List<Integer> numbers = Arrays.asList(key);
                if(numbers.contains(winningNumber))
                {
                    Object[] rangeData = value;
                    rangeData[0] = (int) rangeData[0] + 1;
                    ((List<Integer>)rangeData[4]).add((int)rangeData[1]); // add current lottogames out for range to holder before reseting to zero
                    rangeData[1] = 0;
                    incrementGamesOut(uppperLowerRangeData, key);

                    final int lastDigitOfWinningNumber = (Integer.toString(winningNumber).length() > 1) ? Character.getNumericValue(Integer.toString(winningNumber).charAt(1)) :
                                                                                                    Character.getNumericValue(Integer.toString(winningNumber).charAt(0));

                    Map<Integer[], Object[]> lastDigitData = (Map<Integer[],Object[]>) rangeData[5];


                    lastDigitData.forEach( (keyTwo, valueTwo) -> {

                        List<Integer> lastDigitNums = Arrays.asList(keyTwo);
                        if(lastDigitNums.contains(lastDigitOfWinningNumber))
                        {
                            Object[] lastDigitDataTwo = valueTwo;
                            lastDigitDataTwo[0] = (int) lastDigitDataTwo[0] + 1;
                            ((List<Integer>)lastDigitDataTwo[4]).add((int)lastDigitDataTwo[1]); // add current lottogames out for last digit range to holder before reseting to zero
                            lastDigitDataTwo[1] = 0;
                            incrementGamesOut(lastDigitData, keyTwo);

                            // Add winning number to map 
                            Map<Integer,Integer[]> winningLottoNumberMap = (Map<Integer,Integer[]>) lastDigitDataTwo[5];
                            if(!winningLottoNumberMap.containsKey(winningNumber))
                            {
                                // Third index will hold the index number of last position the lotto number hit in
                                winningLottoNumberMap.put(winningNumber, new Integer[]{1,0});
                                incrementGamesOut(winningLottoNumberMap, winningNumber);

                                int remainder = winningNumber % 3;
                                Map<Integer,Object[]> remainderData = (Map<Integer,Object[]>) lastDigitDataTwo[6];
                                if(!remainderData.containsKey(remainder))
                                {
                                    remainderData.put(remainder, new Object[]{1,0, new ArrayList<Integer>()});
                                    List<Integer> nums = (List<Integer>) remainderData.get(remainder)[2];
                                    nums.add(winningNumber);

                                    incrementGamesOut(remainderData, remainder);
                                }
                                else
                                {
                                    Object[] obj = remainderData.get(remainder);
                                    obj[0] = (int)obj[0] + 1;
                                    obj[1] = 0;
                                    List<Integer> nums = (List<Integer>) obj[2];
                                    if(!nums.contains(winningNumber))
                                         nums.add(winningNumber);
                                    incrementGamesOut(remainderData, remainder);

                                }

                            }
                            else
                            {
                                Integer[] winningNumberData = winningLottoNumberMap.get(winningNumber);
                                winningNumberData[0]++;
                                winningNumberData[1] = 0; 
                                incrementGamesOut(winningLottoNumberMap, winningNumber);

                                int remainder = winningNumber % 3;
                                Map<Integer,Object[]> remainderData = (Map<Integer,Object[]>) lastDigitDataTwo[6];
                                if(!remainderData.containsKey(remainder))
                                {
                                    remainderData.put(remainder, new Object[]{1,0, new ArrayList<Integer>()});
                                    List<Integer> nums = (List<Integer>) remainderData.get(remainder)[2];
                                    nums.add(winningNumber);

                                    incrementGamesOut(remainderData, remainder);
                                }
                                else
                                {
                                    Object[] obj = remainderData.get(remainder);
                                    obj[0] = (int)obj[0] + 1;
                                    obj[1] = 0;

                                    List<Integer> nums = (List<Integer>) obj[2];
                                    if(!nums.contains(winningNumber))
                                        nums.add(winningNumber);

                                    incrementGamesOut(remainderData, remainder);

                                }
                            }
                        }
                    });
                }

            });
        }
    }

    private static void incrementGamesOut(Map<Integer, Object[]> data, int remainder) {

        data.forEach((k,v) -> {

            if(k != remainder)
            {
                v[1] = (int)v[1] + 1;
            }
        });

    }

    private static void incrementGamesOut(Map<Integer,Integer[]> data, Integer number)
    {

        data.forEach((k,v) -> {

            if(k != number)
            {
                v[1]++;
            }
        });

    }

    private static void incrementGamesOut(Map<Integer[], Object[]> data, Integer[] key)
    {

        data.forEach((k,v) -> {

            if(!Arrays.equals(k, key))
            {
                v[1] = (int) v[1] + 1;
            }
        });

    }

    private static void plugUpperAndLowerHalfRangesIntoMap(LottoGame game)
    {
        int maxNumber = game.getMaxNumber();
        
        int half = (game.getMaxNumber() == 9) ? (maxNumber+1)/2 : maxNumber / 2;
        List<List<Integer>> upperLowerNumbers = new LinkedList<>();
        upperLowerNumbers.add(new ArrayList<>());
        upperLowerNumbers.add(new ArrayList<>());

        for(int i = game.getMinNumber(); i <= maxNumber; i++)
        {
            if(i < half)
            {
                List<Integer> lower = upperLowerNumbers.get(0);
                lower.add(i);
            }
            else
            {
                List<Integer> upper = upperLowerNumbers.get(1);
                upper.add(i);
            }
        }

        // plug data into map
        Integer[] lower = upperLowerNumbers.get(0).toArray(new Integer[upperLowerNumbers.get(0).size()]);
        Integer[] upper = upperLowerNumbers.get(1).toArray(new Integer[upperLowerNumbers.get(1).size()]);

        Map<Integer[], Object[]>[] lastDigitData = retriveLastDigitData(game);

        uppperLowerRangeData.put(lower,new Object[]{0,0,0,0,new ArrayList<Integer>(), lastDigitData[0]});
        uppperLowerRangeData.put(upper,new Object[]{0,0,0,0,new ArrayList<Integer>(), lastDigitData[1]});
    }

    private static Map<Integer[],Object[]>[] retriveLastDigitData(LottoGame game )
    {
        Map<Integer[],Object[]>[] mainData = new Map[2];
        
        if(!game.getGameName().contains(LotteryGameConstants.PICK3_GAME_NAME) && !game.getGameName().contains(LotteryGameConstants.PICK4_GAME_NAME))
        {
            for(int i = 0; i < mainData.length; i++)
            {
                Map<Integer[],Object[]> data = new LinkedHashMap<>();
                data.put(new Integer[]{0,1,2,3,4}, new Object[]{0,0,0,0, new ArrayList<Integer>(),new TreeMap<Integer,Integer[]>(), new TreeMap<Integer,Object[]>()});
                data.put(new Integer[]{5,6,7,8,9}, new Object[]{0,0,0,0, new ArrayList<Integer>(),new TreeMap<Integer,Integer[]>(), new TreeMap<Integer,Object[]>()});

                mainData[i] = data;
            }
        }
        else
        {
            for(int i = 0; i < mainData.length; i++)
            {
                Map<Integer[],Object[]> data = new LinkedHashMap<>();
                if(i == 0)
                    data.put(new Integer[]{0,1,2,3,4}, new Object[]{0,0,0,0, new ArrayList<Integer>(),new TreeMap<Integer,Integer[]>(),new TreeMap<Integer,Object[]>()});
                else
                    data.put(new Integer[]{5,6,7,8,9}, new Object[]{0,0,0,0, new ArrayList<Integer>(),new TreeMap<Integer,Integer[]>(), new TreeMap<Integer,Object[]>()});

                mainData[i] = data;
            }
        }

        return mainData;
    }

    private static void clear()
    {
        uppperLowerRangeData.clear();
    }
}
