package com.lottoanalysis.utilities.analyzerutilites;

import com.lottoanalysis.models.comparators.HitComparator;
import com.lottoanalysis.models.lottogames.LottoGame;

import java.util.*;
import java.util.stream.IntStream;

public class SplitDigitAnalyzer {

    // game name, draw position, draw number key
    private static Map<String,Map<Integer,Map<Integer,List<Integer>>>> drawDataMap = new HashMap<>();
    private static Map<Integer,Map<Integer,Integer[]>> keyHitTrackerForPositions = new TreeMap<>();
    private static Map<Integer,Map<Integer,Integer[]>> winningElementCompaionHits = new TreeMap<>();
    private int drawIndex;
    private LottoGame lottoGame;
    private int[] drawData;
    private String winningNumberWithElement;

    public void analyze(int[] drawData, int drawIndex, LottoGame game){

        this.drawIndex = drawIndex;
        this.lottoGame = game;
        this.drawData = drawData;

        winningElementCompaionHits.clear();
        populateDrawDataMap( drawData, drawIndex, game);
        processElementSelection(1);
        getData();
    }

    public Set<Integer> getElementOneValues(){
        return keyHitTrackerForPositions.get(drawIndex).keySet();
    }

    public Object[] getData(){

        Map<Integer,Integer[]> valueData = winningElementCompaionHits.get(winningElementCompaionHits.keySet().iterator().next());
        List<Map.Entry<Integer,Integer[]>> companionHitEntries = new ArrayList<>(valueData.entrySet());
        companionHitEntries.sort( new HitComparator());

        List<Map.Entry<Integer,Integer[]>> keyHitEntries = new ArrayList<>(keyHitTrackerForPositions.get(drawIndex).entrySet());
        keyHitEntries.sort(new HitComparator());

        return new Object[]{winningNumberWithElement, companionHitEntries, keyHitEntries};
    }

    public void processElementSelection( int element ){

        Map<Integer,List<Integer>> elementData = drawDataMap.get(lottoGame.getGameName()).get(drawIndex);
        List<Integer> drawNumbers = elementData.get( element );

        String recentWinningNumberWithElement="";

        for(int i = drawData.length - 1; i >= 0; i--){

            String numberAsString = drawData[i] + "";
            numberAsString = (numberAsString.length() > 1) ? numberAsString : "0" + numberAsString;

            if(Character.toString( numberAsString.charAt(0)).equals( Integer.toString(element))){
                recentWinningNumberWithElement = numberAsString;
                winningNumberWithElement = recentWinningNumberWithElement;
                break;
            }
        }

        final int winningElement = Character.getNumericValue(recentWinningNumberWithElement.charAt(1));
        int[] indexes = IntStream.range(0, drawNumbers.size()-1).filter( i -> drawNumbers.get(i) == winningElement).toArray();
        System.out.println();
        populateCompanionHits(indexes, drawNumbers);
    }

    private void populateCompanionHits(int[] indexes, List<Integer> drawNumbers) {

        winningElementCompaionHits.put(drawNumbers.get(indexes[0]), new TreeMap<>());
        Map<Integer,Integer[]> companionMap = winningElementCompaionHits.get( drawNumbers.get(indexes[0]));
        Map<Integer,Integer[]> gameOutTracker = new TreeMap<>();
        List<Integer> gameOutList = new ArrayList<>();

        for(int i : indexes){

            int nexWinningNumber = drawNumbers.get( i + 1);
            if(!companionMap.containsKey(nexWinningNumber)){
                companionMap.put(nexWinningNumber, new Integer[]{1,0,0,0});
                NumberAnalyzer.incrementGamesOut(companionMap, nexWinningNumber);
                gameOutList.add(0);
            }
            else {
                Integer[] data = companionMap.get( nexWinningNumber );
                data[0]++;
                gameOutList.add(data[1]);
                data[1] = 0;
                NumberAnalyzer.incrementGamesOut(companionMap, nexWinningNumber);
            }
        }

        gameOutList.forEach( num -> {

            if(!gameOutTracker.containsKey(num)){
                gameOutTracker.put(num, new Integer[]{1,0});
                NumberAnalyzer.incrementGamesOut(gameOutTracker, num);
            }
            else{
                Integer[] data = gameOutTracker.get(num);
                data[0]++;
                data[1] = 0;
                NumberAnalyzer.incrementGamesOut(gameOutTracker,num);
            }
        });

        // plug hit games out hits, and games ago
        companionMap.forEach( (k,v) -> {

            if(gameOutTracker.containsKey( v[1] )){

                Integer[] outHitsAndGamesOut = gameOutTracker.get( v[1] );
                v[2] = outHitsAndGamesOut[0];
                v[3] = outHitsAndGamesOut[1];
            }
            else{
                v[2] = -1;
                v[3] = -1;
            }
        });
    }

    private static void populateDrawDataMap(int[] drawData, int drawIndex, LottoGame game) {

        if( !drawDataMap.containsKey( game.getGameName() )) {

            drawDataMap.clear(); // clear just in case there were any other game information
            keyHitTrackerForPositions.clear();

            drawDataMap.put( game.getGameName(), new TreeMap<>());
        }

        Map<Integer,Map<Integer,List<Integer>>> positionData = drawDataMap.get( game.getGameName() );
        if(!positionData.containsKey( drawIndex ) && !keyHitTrackerForPositions.containsKey( drawIndex )){

            DigitParser parser = new DigitParser();
            Map<Integer,List<Integer>> values = parser.parseDataIntoKeyValuePairs( drawData, drawIndex);

            positionData.put( drawIndex, values);

        }

    }

    private static class DigitParser {

         Map<Integer,List<Integer>> parseDataIntoKeyValuePairs(int[] data, int drawIndex){

            Map<Integer,List<Integer>> results = new TreeMap<>();
            Map<Integer,Integer[]> keyHitTracker = new TreeMap<>();

            for (int aData : data) {

                String numberAsString = aData + "";
                numberAsString = (numberAsString.length() > 1) ? numberAsString : "0" + numberAsString;

                String[] keyValuePars = numberAsString.split("");

                int key = Integer.parseInt(keyValuePars[0]);
                int value = Integer.parseInt(keyValuePars[1]);

                if (!results.containsKey(key)) {
                    List<Integer> numbers = new ArrayList<>();
                    numbers.add(value);
                    results.put(key, numbers);
                } else {

                    List<Integer> numbers = results.get(key);
                    numbers.add(value);
                }

                if(!keyHitTracker.containsKey(key)){
                    keyHitTracker.put( key, new Integer[]{1,0});
                    NumberAnalyzer.incrementGamesOut(keyHitTracker, key);
                }
                else{
                    Integer[] keyData = keyHitTracker.get( key );
                    keyData[0]++;
                    keyData[1] = 0;
                    NumberAnalyzer.incrementGamesOut(keyHitTracker, key);
                }
            }

            keyHitTrackerForPositions.put(drawIndex, keyHitTracker);

            return results;
        }
    }
}
