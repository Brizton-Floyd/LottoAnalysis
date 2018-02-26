package com.lottoanalysis.utilities;

import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.lottoinfoandgames.LottoGame;

import java.util.*;


/**
 * Class will analyze and determine how many games out a lotto number is and list which position the number
 * was last drawn in
 */
public class GamesOutPositionTracker {

    private Map<Integer, Object[]>  hitAndLocationTracker = new TreeMap<>();
    private Map<Integer,Integer> gamesOutTracker = new TreeMap<>();
    private Map<Integer,Object[]>  analysisForCurrentRow;
    private List<Integer> vals;

    public GamesOutPositionTracker(Set<Integer> allKeyvals, LottoGame lotteryGame) {
        vals = new ArrayList<>(allKeyvals);
        for(int i = lotteryGame.getMinNumber(); i <= lotteryGame.getMaxNumber(); i++)
            hitAndLocationTracker.put(i, new Object[]{0,0,0});

        determineHowManyPositionsToAnalyze(lotteryGame);
        assignGamesOutHitCountToThirdIndex();
    }

    public Map<Integer, Object[]> getAnalysisForCurrentRow() {
        return analysisForCurrentRow;
    }

    private void assignGamesOutHitCountToThirdIndex() {

        if(analysisForCurrentRow == null)
            analysisForCurrentRow = new TreeMap<>();

        for(int num : vals){
            analysisForCurrentRow.put(num, new Object[]{0,0,0,""});
        }

        for(Map.Entry<Integer, Object[]> d : hitAndLocationTracker.entrySet()){

            int key = d.getKey();
            if(analysisForCurrentRow.containsKey(key)){
                analysisForCurrentRow.put(key, d.getValue());
            }
        }

        for(Map.Entry<Integer, Integer> d : gamesOutTracker.entrySet()) {

            for(Map.Entry<Integer, Object[]> dd : hitAndLocationTracker.entrySet()) {

                if(d.getKey() == dd.getValue()[0]){

                    int hitCount = d.getValue();
                    Object[] allData = dd.getValue();
                    allData[2] = hitCount;
                }
            }
        }

    }

    private void determineHowManyPositionsToAnalyze(LottoGame lotteryGame) {

        int positions = lotteryGame.getPositionNumbersAllowed();
        int[][] positionsAndNumbers;

        if(lotteryGame.getPositionNumbersAllowed() > 5)
            positionsAndNumbers = new int[lotteryGame.getDrawingData().size()][positions - 1];
        else
            positionsAndNumbers = new int[lotteryGame.getDrawingData().size()][positions];

        for(int i = 0; i < lotteryGame.getDrawingData().size(); i++){

            switch (positions){
                case 3:
                    positionsAndNumbers[i][0] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosOne());
                    positionsAndNumbers[i][1] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosTwo());
                    positionsAndNumbers[i][2] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosThree());
                    break;
                case 4:
                    positionsAndNumbers[i][0] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosOne());
                    positionsAndNumbers[i][1] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosTwo());
                    positionsAndNumbers[i][2] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosThree());
                    positionsAndNumbers[i][3] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosFour());
                    break;
                case 5:
                    positionsAndNumbers[i][0] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosOne());
                    positionsAndNumbers[i][1] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosTwo());
                    positionsAndNumbers[i][2] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosThree());
                    positionsAndNumbers[i][3] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosFour());
                    positionsAndNumbers[i][4] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosFive());
                    break;
                case 6:
                    positionsAndNumbers[i][0] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosOne());
                    positionsAndNumbers[i][1] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosTwo());
                    positionsAndNumbers[i][2] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosThree());
                    positionsAndNumbers[i][3] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosFour());
                    positionsAndNumbers[i][4] = Integer.parseInt(lotteryGame.getDrawingData().get(i).getPosFive());
                    break;
            }
        }

        analyzeDraw(positionsAndNumbers);
    }

    private void analyzeDraw(int[][] positionsAndNumbers) {

        int[] currentWinning = null;

        for(int i = 0; i < positionsAndNumbers.length; i++){

            currentWinning = new int[positionsAndNumbers[i].length];

            for(int k = 0; k < positionsAndNumbers[i].length; k++){

                currentWinning[k] = positionsAndNumbers[i][k];
            }
            incrementGamesOut(hitAndLocationTracker, currentWinning);
        }
    }

    private void incrementGamesOut(Map<Integer, Object[]> hitAndLocationTracker, int[] vals ) {

        List<Integer> valTwo = new ArrayList<>();
        for(int num : vals)
            valTwo.add(num);

        int count = 1;
        for(Map.Entry<Integer, Object[]> dd : hitAndLocationTracker.entrySet()){

            int value = dd.getKey();

            if(!valTwo.contains(value)){

                Object[] data = dd.getValue();
                data[0] = (int)data[0] + 1;
                hitAndLocationTracker.put(value, data);

            }else{

                Object[] data = dd.getValue();

                if(!gamesOutTracker.containsKey(data[0])){
                    gamesOutTracker.put((int)data[0],1);
                }else{
                    int num = gamesOutTracker.get(data[0]);
                    gamesOutTracker.put((int)data[0], ++num);
                }

                data[0] = 0;
                data[1] = "Position " + count++;
            }
        }
    }
}
