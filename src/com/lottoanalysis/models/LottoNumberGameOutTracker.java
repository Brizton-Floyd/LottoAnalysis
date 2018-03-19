package com.lottoanalysis.models;

import java.util.;

public class LottoNumberGameOutTracker{

    private int[][] drawData; 
    private Map<Integer,Integer[]> lottoNumberMap = new TreeMap<>();

    public LottoNumberGameOutTracker( int[][] drawData ){

        this.drawData = drawData;
    }

    private void findPostionAndGamesOut() {

        Set<Integer> winningNumbers = new TreeSet<>();

        for(int i = 0; i < drawData[0].length; i++){

            int[] drawArray = new int[drawData.length];
            for(int j = 0; j < drawArray.length; j++){

                drawArray[j] = drawData[j][i]; //This will form an array containing a result of an actual draw 
            }

            IntStream.range(0, drawArray.length ).forEach( i -> {

                if( !lottoNumberMap.containsKey( drawArray[i] ) ){
                    lottoNumberMap.put(drawArray[i], new Integer[]{0, i+1});
                }
                else{

                    Integer[] data = lottoNumberMap.get( drawArray[i] );
                    data[0] = 0;
                    data[1] = i+1;
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

    public void insertLastHitPositionAndActualGamesOut(List<Map<Integer,Integer[]>> data){

    }


}
