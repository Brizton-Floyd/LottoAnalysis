package com.lottoanalysis.common;

import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;

import java.util.*;

public class MenuBarHelper {

    public static List<Integer> buildRangeSizeList( final int maxVal, final String gameName){

        int startingPoint = 1;
        int tailPointer = 0;
        if (gameName.equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                gameName.equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {

            startingPoint = 0;
            tailPointer++;
        }

        List<Integer> ranges = new ArrayList<>();
        int counter = 0;
        int indexer = 0;
        for(int i = startingPoint; i <= maxVal; i++){

            counter++;
            if(counter == 6){

                if( ranges.size() ==0 ){
                    ranges.add( counter );
                    counter = 0;
                }
                else{
                    int count = ranges.get( indexer++ );
                    ranges.add( count + counter );
                    counter = 0;
                }

            }
        }

        ranges.add( maxVal + tailPointer);
        Set<Integer> numbers = new LinkedHashSet<>( ranges );

        ranges = new ArrayList<>( numbers );
        return ranges;
    }
}
