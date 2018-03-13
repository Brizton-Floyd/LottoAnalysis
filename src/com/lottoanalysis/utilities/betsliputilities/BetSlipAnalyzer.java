package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;

@SuppressWarnings("unchecked")
public class BetSlipAnalyzer {

        private BetSlipDefinitions betSlipDefinitions;
        private ColumnAndIndexHitAnalyzer[] columnAndIndexHitAnalyzers;
        private List<Map.Entry<String,BetSlipDistributionAnalyzer>> sortedEntries;
        private LottoGame lottoGame;

        public void analyzeDrawData(int[][] drawNumbers, LottoGame lottoGame){

            this.lottoGame = lottoGame;

            columnAndIndexHitAnalyzers = new ColumnAndIndexHitAnalyzer[drawNumbers.length];
            for(int i = 0; i < columnAndIndexHitAnalyzers.length; i++)
                columnAndIndexHitAnalyzers[i] = new ColumnAndIndexHitAnalyzer();

            List<StringBuilder> data = formDrawStrings( drawNumbers );
            Integer[][] betSlipDefinitions = getBetSlipDefinitions( lottoGame );
            findRowAndColumnHits(betSlipDefinitions, data);

        }

        private void findRowAndColumnHits(Integer[][] betSlipDefinitions, List<StringBuilder> data) {

            Map<String, BetSlipDistributionAnalyzer> hitAndGameOutTracker = new HashMap<>();

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
                Integer[][] betslipMatrix = getBetSlipDefinitions( lottoGame );

                for(int k = 0; k < columnAndIndexHitAnalyzers.length; k++){

                    ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer = columnAndIndexHitAnalyzers[k];
                    String[] columnIndices = columnString.toString().trim().split(",");
                    String[] rowIndices = rowString.toString().trim().split(",");

                    Map<Integer, Object[]> colAndIndexData = columnAndIndexHitAnalyzer.getColumnIndexHolder();
                    if(!colAndIndexData.containsKey( Integer.parseInt( columnIndices[k] ))){

                        colAndIndexData.put(Integer.parseInt( columnIndices[k] ), new Object[]{1,0, new TreeMap<Integer,Integer[]>(),
                                new ArrayList<Integer>()});

                        Map<Integer, Integer[]> rowInfo = (Map<Integer, Integer[]>) colAndIndexData.get( Integer.parseInt(columnIndices[k]))[2];
                        columnAndIndexHitAnalyzer.incrementGamesOutForNonWinningColumns( colAndIndexData, columnIndices[k]);

                        Integer[] winnincolumn = betslipMatrix[ Integer.parseInt(columnIndices[k]) ];
                        int winningDigit = winnincolumn[Integer.parseInt(rowIndices[k])];

                        if(!rowInfo.containsKey(winningDigit)){

                            List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get( Integer.parseInt(columnIndices[k]))[3];
                            winningNumberHolder.add(winningDigit);
                            rowInfo.put(winningDigit, new Integer[]{1,0});
                            NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                        }
                        else{

                            List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get( Integer.parseInt(columnIndices[k]))[3];
                            winningNumberHolder.add(winningDigit);

                            Integer[] winningDigitData = rowInfo.get(winningDigit);
                            winningDigitData[0]++;
                            winningDigitData[1] = 0;
                            NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                        }
                    }
                    else{

                        Object[] colAndIndexDataTwo = colAndIndexData.get( Integer.parseInt(columnIndices[k]) );
                        colAndIndexDataTwo[0] = (int)colAndIndexDataTwo[0] + 1;
                        colAndIndexDataTwo[1] = 0;
                        columnAndIndexHitAnalyzer.incrementGamesOutForNonWinningColumns(colAndIndexData, columnIndices[k]);
                        Map<Integer, Integer[]> rowInfo = (Map<Integer, Integer[]>) colAndIndexDataTwo[2];

                        Integer[] winnincolumn = betslipMatrix[ Integer.parseInt(columnIndices[k]) ];
                        int winningDigit = winnincolumn[Integer.parseInt(rowIndices[k])];

                        if(!rowInfo.containsKey(winningDigit)){

                            List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get( Integer.parseInt(columnIndices[k]))[3];
                            winningNumberHolder.add(winningDigit);

                            rowInfo.put(winningDigit, new Integer[]{1,0});
                            NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                        }
                        else{

                            List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get( Integer.parseInt(columnIndices[k]))[3];
                            winningNumberHolder.add(winningDigit);

                            Integer[] winningDigitData = rowInfo.get(winningDigit);
                            winningDigitData[0]++;
                            winningDigitData[1] = 0;
                            NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                        }
                    }
                }

                Set<String> columnSet = new HashSet<>( Arrays.asList( columnString.toString().trim().split(",") ));
                Set<String> rowSet = new HashSet<>( Arrays.asList( rowString.toString().trim().split(",") ));

                String format = columnSet.size() + "Col" + " / " + rowSet.size() +"Row";

                if(!hitAndGameOutTracker.containsKey(format)){

                    hitAndGameOutTracker.put(format, new BetSlipDistributionAnalyzer(1, 0, new HashMap<>()));
                    BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = hitAndGameOutTracker.get( format ); 
                    betSlipDistributionAnalyzer.incrementGamesOutForAllOtherFormatOccurences(hitAndGameOutTracker, format);

                    Map<String, Integer[]> winninNumberDistibutionMap = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder(); 
                    if(!winninNumberDistibutionMap.containsKey( data.get(i).toString() )){

                        winninNumberDistibutionMap.put(data.get(i).toString(), new Integer[]{1,0});
                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());
                    }
                    else{
                        Integer[] innerData = winninNumberDistibutionMap.get( data.get(i).toString() );
                        innerData[0]++;
                        innerData[1] = 0;
                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());

                    }
                }
                else{

                    BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = hitAndGameOutTracker.get(format);
                    int hits = betSlipDistributionAnalyzer.getFormatHits();
                    betSlipDistributionAnalyzer.setFormatHits( ++hits );
                    betSlipDistributionAnalyzer.setFormatGamesOut( 0 );
                    betSlipDistributionAnalyzer.incrementGamesOutForAllOtherFormatOccurences(hitAndGameOutTracker, format);

                    Map<String, Integer[]> winninNumberDistibutionMap = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder(); 
                    if(!winninNumberDistibutionMap.containsKey( data.get(i).toString())){

                        winninNumberDistibutionMap.put(data.get(i).toString(), new Integer[]{1,0});
                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());
                    }
                    else{

                        Integer[] innerData = winninNumberDistibutionMap.get( data.get(i).toString() );
                        innerData[0]++;
                        innerData[1] = 0;
                        betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());
                    }
                }
            }

            sortedEntries = new ArrayList<>(hitAndGameOutTracker.entrySet());
            sortedEntries.sort(Comparator.comparing(v -> v.getValue().getFormatHits()));
            Collections.reverse(sortedEntries);

            sortedEntries.forEach( k -> {
                System.out.println(String.format("Pattern Helper: %s \tHits: %s \tGames Out: %s",k.getKey(), k.getValue().getFormatHits(), k.getValue().getFormatGamesOut()));

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
