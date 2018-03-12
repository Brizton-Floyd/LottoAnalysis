package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import com.lottoanalysis.utilities.numberpatternutilities.PatternFinder;

import java.util.*;

public class BetSlipAnalyzer {

    private BetSlipDefinitions betSlipDefinitions;
    private Map<String,Object[]> hitAndGameOutTracker = new HashMap<>();
    private Map<String,Integer> pp = new HashMap<>();

    public void analyzeDrawData(int[][] drawNumbers, LottoGame lottoGame){

        List<StringBuilder> data = formDrawStrings( drawNumbers );
        Integer[][] betSlipDefinitions = getBetSlipDefinitions( lottoGame );
        findRowAndColumnHits(betSlipDefinitions, data);
    }

    @SuppressWarnings("unchecked")
    private void findRowAndColumnHits(Integer[][] betSlipDefinitions, List<StringBuilder> data) {

        List<Integer[]> betSlipColumns = new ArrayList<>();
        betSlipColumns.addAll(Arrays.asList(betSlipDefinitions));

        for(int i = 0; i < data.size(); i++){

            String[] drawString = data.get(i).toString().split("-");

            StringBuilder columnString = new StringBuilder();
            StringBuilder rowString = new StringBuilder();

            // now analyze each draw number to see how many rows an columns were needed
            for(int k = 0; k < drawString.length; k++){

                int number = Integer.parseInt( drawString[k].trim() );
                for(int j = 0; j < betSlipColumns.size(); j++) {

                    if(Arrays.asList(betSlipColumns.get(j)).contains(number)){
                        columnString.append(j).append(",");
                        rowString.append( Arrays.asList(betSlipColumns.get(j)).indexOf(number) ).append(",");
                        break;
                    }
                }
            }

            columnString.setCharAt(columnString.lastIndexOf(","),' ');
            rowString.setCharAt(rowString.lastIndexOf(","),' ');

            Set<String> columnSet = new HashSet<>( Arrays.asList( columnString.toString().trim().split(",") ));
            Set<String> rowSet = new HashSet<>( Arrays.asList( rowString.toString().trim().split(",") ));

            String format = columnSet.size() + "Col" + " / " + rowSet.size() +"Row";

            if(!hitAndGameOutTracker.containsKey(format)){
                hitAndGameOutTracker.put(format, new Object[]{1,0, new HashMap<String,Integer[]>()});
                incrementGamesOut(hitAndGameOutTracker,format);

                // String occurrence data
                Map<String, Integer[]> drawNumberHitMap = (Map<String, Integer[]>) hitAndGameOutTracker.get(format)[2];
                if(!drawNumberHitMap.containsKey(data.get(i).toString())){

                    drawNumberHitMap.put(data.get(i).toString(), new Integer[]{1,0});
                    incrementGames(hitAndGameOutTracker, format);
                }
                else{
                    Integer[] dNHM = drawNumberHitMap.get(data.get(i).toString());
                    dNHM[0]++;
                    dNHM[1] = 0;
                    incrementGames(hitAndGameOutTracker, format);
                }
            }
            else{
                Object[] dat = hitAndGameOutTracker.get(format);
                dat[0] = (int)dat[0] + 1;
                dat[1] = 0;
                incrementGamesOut(hitAndGameOutTracker,format);

                // String occurrence data
                Map<String, Integer[]> drawNumberHitMap = (Map<String, Integer[]>) hitAndGameOutTracker.get(format)[2];
                if(!drawNumberHitMap.containsKey(data.get(i).toString())){

                    drawNumberHitMap.put(data.get(i).toString(), new Integer[]{1,0});
                    incrementGames(hitAndGameOutTracker, format);
                }
                else{
                    Integer[] dNHM = drawNumberHitMap.get(data.get(i).toString());
                    dNHM[0]++;
                    dNHM[1] = 0;
                    incrementGames(hitAndGameOutTracker, format);
                }
            }
        }

        List<Map.Entry<String,Object[]>> sortedEntries = new ArrayList<>(hitAndGameOutTracker.entrySet());
        sortedEntries.sort( (valOne,valTwo) -> {

            int valOneInner = (int)valOne.getValue()[0];
            int valTwoInner = (int)valTwo.getValue()[0];

            return Integer.toString(valOneInner).compareTo(Integer.toString(valTwoInner));

        });

    }

    private <K,V extends Comparable<? super V>> List<Map.Entry<K, V>> entriesSortedByValues(Map<K,V> map) {

        List<Map.Entry<K,V>> sortedEntries = new ArrayList<>(map.entrySet());

        Collections.sort(sortedEntries,
                (e1, e2) -> e2.getValue().compareTo(e1.getValue())
        );

        return sortedEntries;
    }

    @SuppressWarnings("unchecked")
    private void incrementGames(Map<String, Object[]> drawNumberHitMap, String s) {

        drawNumberHitMap.forEach((k,v) -> {

            if(!k.equals(s)){

                Map<String,Integer[]> ss = (Map<String,Integer[]>)v[2];
                ss.forEach( (kk,vv) -> {

                    vv[1]++;
                });
            }
                //v[1]++;
        });
    }

    private void incrementGamesOut(Map<String, Object[]> hitAndGameOutTracker, String format) {

        hitAndGameOutTracker.forEach( (k,v) -> {

            if(!k.equals(format)){
                v[1] = (int)v[1] + 1;
            }
        });
    }

    private Integer[][] getBetSlipDefinitions(LottoGame lottoGame) {

        if( betSlipDefinitions == null)
            betSlipDefinitions = new BetSlipDefinitions();

        return betSlipDefinitions.getDefinitionFile( lottoGame.getGameName() );
    }

    private List<StringBuilder> formDrawStrings(int[][] drawNumbers) {

        List<StringBuilder> data = new ArrayList<>();

        // add string builders to list for access later
        for(int i = 0; i < drawNumbers[0].length; i++)
            data.add(new StringBuilder());

        for(int i = 0; i < drawNumbers.length; i++){

            for(int k = 0; k < drawNumbers[i].length; k++){

                StringBuilder builder = data.get(k);
                String appendVal = (i < drawNumbers.length - 1) ? " - " : "";
                builder.append(drawNumbers[i][k] + appendVal);
            }
        }

        return data;
    }
}
