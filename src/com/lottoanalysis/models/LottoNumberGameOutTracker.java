package com.lottoanalysis.models;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LottoNumberGameOutTracker{

    private int[][] drawData; 
    private Map<Integer,Integer[]> lottoNumberMap = new TreeMap<>();

    public LottoNumberGameOutTracker( int[][] drawData ){

        this.drawData = drawData;
        findPostionAndGamesOut();
    }

    private void findPostionAndGamesOut() {

        Set<Integer> winningNumbers = new TreeSet<>();

        for(int i = 0; i < drawData[0].length; i++){

            int[] drawArray = new int[drawData.length];
            for(int j = 0; j < drawArray.length; j++){

                drawArray[j] = drawData[j][i]; //This will form an array containing a result of an actual draw 
            }

            IntStream.range(0, drawArray.length ).forEach(k -> {

                if( !lottoNumberMap.containsKey( drawArray[k] ) ){
                    lottoNumberMap.put(drawArray[k], new Integer[]{0, k+1});
                }
                else{

                    Integer[] data = lottoNumberMap.get( drawArray[k] );
                    data[0] = 0;
                    data[1] = k+1;
                }

            });

            winningNumbers.addAll(Arrays.stream(drawArray).boxed().collect(Collectors.toList()));

            lottoNumberMap.forEach((k,v) -> {

                if(!winningNumbers.contains(k)){
                    v[0]++;
                }
            });

            winningNumbers.clear();

        }
    }

    public void insertLastHitPositionAndActualGamesOut(Map<String, SingleDigitRangeTracker> data ){

        for (Map.Entry<String, SingleDigitRangeTracker> d : data.entrySet()) {

            SingleDigitRangeTracker tracker = d.getValue();

            Map<String,SingleDigitRangeTracker> trackerData = tracker.getTracker();

            trackerData.forEach((k,v) -> {

                List<Map<Integer, Integer[]>> dd = v.getLottoNumberHolder();

                dd.forEach( map -> {

                    for (Map.Entry<Integer, Integer[]> entry : map.entrySet()) {

                        if (lottoNumberMap.containsKey(entry.getKey())) {

                            Integer[] hitInfo = lottoNumberMap.get(entry.getKey());
                            Integer[] incomingInfo = entry.getValue();
                            incomingInfo[2] = hitInfo[0];
                            incomingInfo[3] = hitInfo[1];
                        } else {
                            Integer[] incomingInfo = entry.getValue();
                            incomingInfo[2] = -1;
                            incomingInfo[3] = -1;
                        }
                    }


                });

            });

        }

    }


}
