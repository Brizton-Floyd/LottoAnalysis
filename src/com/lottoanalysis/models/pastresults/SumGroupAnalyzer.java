package com.lottoanalysis.models.pastresults;

import com.lottoanalysis.lottogames.LottoGame;

import java.util.*;
import java.util.stream.Collectors;

public class SumGroupAnalyzer {

    private int groupHits;
    private int groupGamesOut;
    private List<Integer> lottoNumberInSumRangeHolder = new ArrayList<>();
    private Map<Integer[],SumGroupAnalyzer> groupAnalyzerMap = new LinkedHashMap<>();
    private List<Integer> gameOutHitHolder = new ArrayList<>();


    // Getters
    public  List<Integer> getGameOutHitHolder() {

        Collection<SumGroupAnalyzer> sumGroupAnalyzers = groupAnalyzerMap.values();
        Set<Integer> sumGroupGamesOut = new HashSet<>();
        for(SumGroupAnalyzer sumGroupAnalyzer : sumGroupAnalyzers)
        {
            sumGroupGamesOut.add(sumGroupAnalyzer.getGroupGamesOut());
        }

        gameOutHitHolder.removeIf( value -> !sumGroupGamesOut.contains(value));
        return gameOutHitHolder;
    }

    public int getGroupHits() {
        return groupHits;
    }

    public void setGroupHits(int groupHits) {
        this.groupHits = groupHits;
    }

    public int getGroupGamesOut() {
        return groupGamesOut;
    }

    public void setGroupGamesOut(int groupGamesOut) {
        this.groupGamesOut = groupGamesOut;
    }

    public List<Integer> getLottoNumberInSumRangeHolder() {
        return lottoNumberInSumRangeHolder;
    }

    public Map<Integer[], SumGroupAnalyzer> getGroupAnalyzerMap() {
        return groupAnalyzerMap;
    }

    //Methods
    public void analyze(Set<Integer> currentValidNumbers, int[] historicalDrawData, boolean isNumberDivideCheckNeeded, LottoGame lottoGame) {

        populateMap();
        Map<Integer,List<Integer>> numberMultipleMap = null;

        if(isNumberDivideCheckNeeded){
            numberMultipleMap = new LinkedHashMap<>();
            loadUpNumberMultipleMap(numberMultipleMap, lottoGame);
            currentValidNumbers = new HashSet<>(numberMultipleMap.keySet());
        }

        int number;
        for(Integer drawNum : historicalDrawData)
        {
            if(isNumberDivideCheckNeeded)
                number = getAppropriateMultiple(numberMultipleMap, drawNum);
            else
                number = drawNum;

            if( currentValidNumbers.contains(number) )
            {
                final String numAsString = number +"";
                int sum;
                if(numAsString.length() > 1)
                {

                    String[] data = numAsString.split("");
                    sum = Integer.parseInt(data[0].trim()) + Integer.parseInt(data[1].trim());
                }
                else
                {
                    sum = Integer.parseInt(numAsString);
                }

                groupAnalyzerMap.forEach((k,v) -> {

                    if(sum >= k[0] && sum <= k[1] )
                    {
                        gameOutHitHolder.add(v.getGroupGamesOut());
                        v.setGroupGamesOut(0);
                        int hits = v.getGroupHits();
                        v.setGroupHits(++hits);

                        List<Integer> list = v.getLottoNumberInSumRangeHolder();
                        list.add(drawNum);
                    }
                    else
                    {

                        int gamesOut = v.getGroupGamesOut();
                        v.setGroupGamesOut(++gamesOut);
                    }
                });
            }
        }

        List<Map.Entry<Integer[],SumGroupAnalyzer>> entries = groupAnalyzerMap.entrySet().stream()
                                                                            .sorted( Map.Entry.comparingByValue(
                                                                                    (v1,v2) ->  v2.getGroupHits() - v1.getGroupHits()))
                                                                            .collect(Collectors.toList());

        groupAnalyzerMap.clear();

        entries.forEach( val -> {
            groupAnalyzerMap.put(val.getKey(), val.getValue());
        });

    }

    private int getAppropriateMultiple(Map<Integer, List<Integer>> numberMultipleMap, Integer drawNum) {

        for(Map.Entry<Integer,List<Integer>> entry : numberMultipleMap.entrySet()){
            if(drawNum % entry.getKey() == 0){
                return entry.getKey();
            }
        }

        return -1;
    }

    private void loadUpNumberMultipleMap(Map<Integer, List<Integer>> numberMultipleMap, LottoGame lottoGame) {
        numberMultipleMap.put(7, new ArrayList<>());
        numberMultipleMap.put(5, new ArrayList<>());
        numberMultipleMap.put(3, new ArrayList<>());
        numberMultipleMap.put(2, new ArrayList<>());
        numberMultipleMap.put(1, new ArrayList<>());

        for(int i = lottoGame.getMinNumber(); i <= lottoGame.getMaxNumber(); i++){
            for(Map.Entry<Integer, List<Integer>> entry : numberMultipleMap.entrySet() ){
                if(i % entry.getKey() == 0){
                    entry.getValue().add(i);
                    break;
                }
            }
        }
    }

    private void populateMap() {

        groupAnalyzerMap.clear();
        groupAnalyzerMap.put(new Integer[]{0,2}, new SumGroupAnalyzer());
        groupAnalyzerMap.put(new Integer[]{3,5}, new SumGroupAnalyzer());
        groupAnalyzerMap.put(new Integer[]{6,8}, new SumGroupAnalyzer());
        groupAnalyzerMap.put(new Integer[]{9,11}, new SumGroupAnalyzer());
        groupAnalyzerMap.put(new Integer[]{12,14}, new SumGroupAnalyzer());
        groupAnalyzerMap.put(new Integer[]{15,17}, new SumGroupAnalyzer());
    }
}
