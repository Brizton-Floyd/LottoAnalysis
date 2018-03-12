package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import com.lottoanalysis.utilities.numberpatternutilities.PatternFinder;
import java.util.*;

@SuppressWarnings("unchecked")
public class BetSlipAnalyzer {

        private BetSlipDefinitions betSlipDefinitions;
        private Map<String, BetSlipDistributionAnalyzer> hitAndGameOutTracker = new HashMap<>();

        public void analyzeDrawData(int[][] drawNumbers, LottoGame lottoGame){

            List<StringBuilder> data = formDrawStrings( drawNumbers );
            Integer[][] betSlipDefinitions = getBetSlipDefinitions( lottoGame );
            findRowAndColumnHits(betSlipDefinitions, data);
        }

        
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

                    hitAndGameOutTracker.put(format, new BetSlipDistributionAnalyzer(1, 0, new HashMap<String,Integer[]>()));
                    BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = hitAndGameOutTracker.get( format ); 
                    betSlipDistributionAnalyzer.incrementGamesOutForAllOtherFormatOccurences(hitAndGameOutTracker, format);

                    Map<String, Integer[]> winninNumberDistibutionMap = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder(); 
                    if(!winninNumberDistibutionMap.containsKey( data.get(i).toString() )){

                        winninNumberDistibutionMap.put(data.get(i).toString(), new Integer[]{0,0});
                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinningAndIncrementWinningDistribution(hitAndGameOutTracker, data.get(i).toString());
                    }
                }
                else{

                    BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = hitAndGameOutTracker.get(format);
                    int hits = betSlipDistributionAnalyzer.getFormatHits().intValue(); 
                    betSlipDistributionAnalyzer.setFormatHits( ++hits );
                    betSlipDistributionAnalyzer.setFormatGamesOut( 0 );
                    betSlipDistributionAnalyzer.incrementGamesOutForAllOtherFormatOccurences(hitAndGameOutTracker, format);

                    Map<String, Integer[]> winninNumberDistibutionMap = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder(); 
                    if(!winninNumberDistibutionMap.containsKey( data.get(i).toString())){

                        winninNumberDistibutionMap.put(data.get(i).toString(), new Integer[]{0,0});
                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinningAndIncrementWinningDistribution(hitAndGameOutTracker, data.get(i).toString());
                    }
                    else{

                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinningAndIncrementWinningDistribution(hitAndGameOutTracker, data.get(i).toString());
                    }
                }
            }

            List<Map.Entry<String,BetSlipDistributionAnalyzer>> sortedEntries = new ArrayList<>(hitAndGameOutTracker.entrySet());
            sortedEntries.sort( (v1,v2) -> v1.getValue().getFormatHits().compareTo( v2.getValue().getFormatHits() ));
            Collections.reverse(sortedEntries);

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
