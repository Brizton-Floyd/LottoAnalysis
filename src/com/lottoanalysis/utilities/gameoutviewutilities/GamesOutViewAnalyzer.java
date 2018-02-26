package com.lottoanalysis.utilities.gameoutviewutilities;

import com.lottoanalysis.interfaces.LotteryGame;

import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by briztonfloyd on 9/17/17.
 */
public class GamesOutViewAnalyzer {

    private int[][] drawData;
    private LotteryGame lotteryGame;
    private int[][] gameRanges;

    public GamesOutViewAnalyzer(int[][] drawData, LotteryGame lotteryGame){

        this.drawData = drawData;
        this.lotteryGame = lotteryGame;

    }

    public Map<String, Map<String, Integer[]>> analyzeWinningNumberDistrubution() {

        gameRanges = new int[2][];

        int firstArraySize = (lotteryGame.getMaxNumber() % 2 == 0) ? (lotteryGame.getMaxNumber() / 2) : (int)Math.ceil(lotteryGame.getMaxNumber() / 2.0);
        gameRanges[0] = new int[firstArraySize];
        int arraySize = (firstArraySize == 5) ? (lotteryGame.getMaxNumber() - firstArraySize) + 1 : (lotteryGame.getMaxNumber() - firstArraySize);
        gameRanges[1] = new int[arraySize];
        int nums = (lotteryGame.getGameName().equals("DailyPick4") || lotteryGame.getGameName().equals("DailyPick3")) ? 0 : 1;

        for(int i = nums; i <= lotteryGame.getMaxNumber(); ){

            for(int k = 0; k < gameRanges.length; k++){

                for(int m = 0; m < gameRanges[k].length; m++){

                    gameRanges[k][m] = i++;
                }
            }
        }

        Integer[] firsRange = new Integer[]{gameRanges[0][0], gameRanges[0][gameRanges[0].length - 1]};
        Integer[] secondRange = new Integer[]{gameRanges[1][0], gameRanges[1][gameRanges[1].length - 1]};

        Map<String, Map<String, Integer[]>> winningNumTracker = new TreeMap<>();
        winningNumTracker.put(Arrays.toString(firsRange), new TreeMap<>());
        winningNumTracker.put(Arrays.toString(secondRange), new TreeMap<>());

        for(Map.Entry<String, Map<String, Integer[]>> data : winningNumTracker.entrySet()){

            Map<String, Integer[]> ranges = data.getValue();
            int count = 0;

            for(int i = 0; i <= drawData.length; i++){
                ranges.put(String.format("%1s of %2s",count++, drawData.length), new Integer[]{0,0});
            }
        }

        int firstRangesCount = 0;
        int secondRangesCount = 0;
        int count = 0;

        for(int i = 0; i < drawData[0].length; i++){

            while(count < drawData.length){

                int num = drawData[count++][i];

                if(num >= firsRange[0] && num <= firsRange[firsRange.length -1]){
                    firstRangesCount++;
                }else{
                    secondRangesCount++;
                }
            }

            // do correct operations with the winning Num Tracker Map once the correct firstRangesCount and secondRangesCount
            // has been determined
            count = 0;
            String resOne = String.format("%1s of %2s", firstRangesCount, drawData.length);
            String resTwo= String.format("%1s of %2s",  secondRangesCount, drawData.length);
            for(Map.Entry<String, Map<String, Integer[]>> dd : winningNumTracker.entrySet()){

                Map<String, Integer[]> ds = dd.getValue();

                if(count == 0){
                    if(ds.containsKey(resOne)){
                        Integer[] res = ds.get(resOne);
                        res[0]++;
                        res[1] = 0;
                        incrementGamesOut(ds,resOne);
                        count++;
                    }
                }else{
                    if(ds.containsKey(resTwo)){
                        Integer[] resT = ds.get(resTwo);
                        resT[0]++;
                        resT[1] = 0;
                        incrementGamesOut(ds, resTwo);
                    }
                }
            }
            firstRangesCount = 0;
            secondRangesCount = 0;
            count =0;
        }

        return winningNumTracker;
    }

    private void incrementGamesOut(Map<String, Integer[]> ds, String resOne) {

        for(Map.Entry<String, Integer[]> data : ds.entrySet()){

            if(!data.getKey().equals(resOne)){
                Integer[] res = data.getValue();
                res[1]++;
            }
        }
    }
}
