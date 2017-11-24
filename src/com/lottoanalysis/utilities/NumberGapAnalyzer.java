package com.lottoanalysis.utilities;

import java.util.*;

/**
 * Created by briztonfloyd on 9/14/17.
 */
public class NumberGapAnalyzer {

    private List<Integer> gapValues = new ArrayList<>();
    private Map<Integer, Map<String,Integer[]>> direction = new HashMap<>();
    private Map<Integer, Integer[]> gapHitCounter = new TreeMap<>();
    private int avgRouded = 0;

    public NumberGapAnalyzer(int[] numbers){

        analyze(numbers);
        gapHitCounterAnalysis();
    }

    private void gapHitCounterAnalysis() {

        for(int num : gapValues){
            if(!gapHitCounter.containsKey(num)){
                gapHitCounter.put(num, new Integer[]{1,0});
                incrementgamesOut(gapHitCounter, num);
            }
            else{
                Integer[] data = gapHitCounter.get(num);
                data[0]++;
                data[1] = 0;
                incrementgamesOut(gapHitCounter, num);
            }
        }
    }

    private void analyze(int[] numbers) {

        for(int i = 0; i < numbers.length; i++){

            if(i < numbers.length - 1){

                int diff = Math.abs( (numbers[i] - numbers[i + 1]) );
                gapValues.add(diff);
            }
        }

        int sum = 0;
        for(int num: gapValues){
            sum+= num;
        }

        avgRouded = Math.round(sum / gapValues.size()) + 4;
        direction.put(avgRouded, new HashMap<>());
        Map<String, Integer[]> directionalData = direction.get(avgRouded);
        directionalData.put("Up", new Integer[]{0,0});
        directionalData.put("Down", new Integer[]{0,0});
        directionalData.put("Equal", new Integer[]{0,0});

        for(int num : gapValues){

            if(num > avgRouded){

                Integer[] vals = directionalData.get("Up");
                vals[0]++;
                vals[1] = 0;
                NumberPatternAnalyzer.incrementGamesOutForMatrix(directionalData, "Up");
            }
            else if(num < avgRouded){

                Integer[] vals = directionalData.get("Down");
                vals[0]++;
                vals[1] = 0;
                NumberPatternAnalyzer.incrementGamesOutForMatrix(directionalData, "Down");
            }
            else if(num == avgRouded){

                Integer[] vals = directionalData.get("Equal");
                vals[0]++;
                vals[1] = 0;
                NumberPatternAnalyzer.incrementGamesOutForMatrix(directionalData, "Equal");
            }
            direction.put(avgRouded, directionalData);
        }
    }

    private void incrementgamesOut(Map<Integer, Integer[]> data, Integer integer) {

        for(Map.Entry<Integer,Integer[]> d : data.entrySet()){

            if(d.getKey() != integer){
                Integer[] dd = d.getValue();
                dd[1]++;
            }
        }
    }

}
