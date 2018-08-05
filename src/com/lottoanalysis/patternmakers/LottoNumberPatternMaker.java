package com.lottoanalysis.patternmakers;

import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class LottoNumberPatternMaker extends PatternMaker {

    /**
     * Method builds out the hit distribution pattern over the history of the lottery game
     * @param game
     */
    @Override
    public void constructGameOutHitPattern(LottoGame game) {

        Map<Integer,Object[]> lottoNumberData = super.getLottoNumberPatternMap();

        loadGameRangeIntoMap( lottoNumberData, game.getMinNumber(), game.getMaxNumber());

        ObservableList<Drawing> drawData = game.getDrawingData();

        for( Drawing drawing: drawData)
        {
            String[] numbers = drawing.getNums();
            List<Integer> recentWinningNumbers = Arrays.stream( numbers ).map(Integer::valueOf).collect(Collectors.toList());
            Map<Integer,StringBuilder> lottoNumberHitIndicies = findLottoNumberHitIndicies( recentWinningNumbers );

            lottoNumberData.forEach( (k,v) -> {

                if(recentWinningNumbers.contains( k )){

                    Object[] lottoNumberDataArray = lottoNumberData.get( k );
                    lottoNumberDataArray[0] = 0;

                    List<String> patternList = (List<String>) lottoNumberDataArray[1];
                    patternList.add(String.format("P-%s", lottoNumberHitIndicies.get(k)));

                }
                else
                {
                    Object[] lottoNumberDataArray = lottoNumberData.get( k );
                    lottoNumberDataArray[0] = (int) lottoNumberDataArray[0] + 1;

                    List<String> patternList = (List<String>) lottoNumberDataArray[1];
                    patternList.add(lottoNumberDataArray[0] + "");
                }
            });
        }



    }

    private Map<Integer,StringBuilder> findLottoNumberHitIndicies(List<Integer> recentWinningNumbers) {

        Map<Integer,StringBuilder> hitHolder = new LinkedHashMap<>();

        IntStream.range(0,recentWinningNumbers.size()).forEach( number -> {

            if( !hitHolder.containsKey( recentWinningNumbers.get( number)) ){
                StringBuilder builder = new StringBuilder();
                builder.append( (number + 1) );
                hitHolder.put(  recentWinningNumbers.get(number), builder);
            }
            else
            {
                StringBuilder builder = hitHolder.get( recentWinningNumbers.get(number) );
                builder.append( (number+1) );
            }
        });

        return hitHolder;
    }

    /**
     * Method simply loads the game number range into the map
     * @param lottoNumberData
     * @param minNumber
     * @param maxNumber
     */
    private void loadGameRangeIntoMap(Map<Integer, Object[]> lottoNumberData, int minNumber, int maxNumber) {

        for(int i = minNumber; i <= maxNumber; i++){

            lottoNumberData.put(i, new Object[]{0,new ArrayList<String>()});
        }
    }
}
