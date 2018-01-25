package com.lottoanalysis.utilities;

import java.util.*;

public class PatternFinder {

    private static Map<String,Map<String,Integer[]>> patternCompanionHits = new HashMap<>();;
    private static List<StringBuilder> patternHolder = new LinkedList<>();

    public static void analyze(int[][] alldrawNumbers){

        plugStringBuilderIntoList( alldrawNumbers[0].length);
        analyzePattern(alldrawNumbers);
        findAllIndexesAndPlugIntoPatternCompanionHits( patternHolder );

    }

    private static void findAllIndexesAndPlugIntoPatternCompanionHits(List<StringBuilder> patternHolder) {

        String currentWinningPattern = patternHolder.get( patternHolder.size() - 1).toString();
        List<Integer> indexes = new ArrayList<>();

        for(int i = 0; i < patternHolder.size(); i++){

            if(patternHolder.get(i).toString().equals(currentWinningPattern)){
                indexes.add(i);
            }
        }

        patternCompanionHits.put(currentWinningPattern, new LinkedHashMap<>());

        for(int i = 0; i < indexes.size()-1; i++){

            String nextWinningPattern = patternHolder.get( indexes.get(i) + 1).toString();
            Map<String,Integer[]> data = patternCompanionHits.get( currentWinningPattern);

            if(!data.containsKey(nextWinningPattern)){

                data.put(nextWinningPattern, new Integer[]{1,0});
                NumberPatternAnalyzer.incrementGamesOutForMatrix(data,nextWinningPattern);
            }else{

                Integer[] valData = data.get(nextWinningPattern);
                valData[0]++;
                valData[1] = 0;
                NumberPatternAnalyzer.incrementGamesOutForMatrix(data,nextWinningPattern);
            }
        }

    }

    private static void plugStringBuilderIntoList(int length) {

        for(int i = 0; i < length; i++){
            patternHolder.add(new StringBuilder());
        }
    }

    private static void analyzePattern(int[][] alldrawNumbers) {

        for(int i = 0; i < alldrawNumbers.length; i++){

            for( int k = 0; k < alldrawNumbers[i].length; k++){

                StringBuilder builder = patternHolder.get(k);
                builder.append( getOddOrEven( alldrawNumbers[i][k] ) );

            }
        }
    }

    private static String getOddOrEven(int i) {

        if( i % 2 == 0)
            return "E";
        else
            return "O";

    }
}
