package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class BetSlipAnalyzer {

    private BetSlipDefinitions betSlipDefinitions;
    private ColumnAndIndexHitAnalyzer[] columnAndIndexHitAnalyzers;
    private List<Map.Entry<String, BetSlipDistributionAnalyzer>> sortedEntries;
    private LottoGame lottoGame;
    private List<List<Integer>> posData = new ArrayList<>();

    private static Set<Integer[]> sumRanges = new LinkedHashSet<>();
    static {
        sumRanges.add( new Integer[]{0,5});
        sumRanges.add( new Integer[]{6,10});
        sumRanges.add( new Integer[]{11,15});
        sumRanges.add( new Integer[]{16,20});
        sumRanges.add( new Integer[]{21,25});
        sumRanges.add( new Integer[]{26,30});
        sumRanges.add( new Integer[]{31,35});
        sumRanges.add( new Integer[]{36,40});
    }

    public Object[] getBetSlipData() {
        return new Object[]{columnAndIndexHitAnalyzers, sortedEntries};
    }

    public void analyzeDrawData(int[][] drawNumbers, LottoGame lottoGame) {

        this.lottoGame = lottoGame;

        columnAndIndexHitAnalyzers = new ColumnAndIndexHitAnalyzer[drawNumbers.length];
        for (int i = 0; i < columnAndIndexHitAnalyzers.length; i++){
            columnAndIndexHitAnalyzers[i] = new ColumnAndIndexHitAnalyzer();
            posData.add( new ArrayList<>() );
        }


        for(int i = 0; i < drawNumbers[0].length; i++) {

            if( posData.get(0).size() >= 20)
            {
                List<StringBuilder> data = formDrawStrings(drawNumbers, i);
                Integer[][] betSlipDefinitions = makeCustomBetSlip( convertPosDataToArrayOfArrays(posData) );
                findRowAndColumnHits(betSlipDefinitions, data);
                
                // remove first index from each list so it stays at a size of 20
                posData.forEach( list -> {

                    list.remove(0);
                });
            }
            else
            {
                for(int j = 0; j < posData.size(); j++ ){

                    List<Integer> listData = posData.get( j );
                    listData.add( drawNumbers[j][i] );
                }
            }
        }
        findWinningNumberCompaionHits();
        sortColumnAndIndexHits();

    }

    private int[][] convertPosDataToArrayOfArrays( List<List<Integer>> data ){

        int[][] dataToReturn = new int[data.size()][data.get(0).size()];

        for(int i = 0; i < data.size(); i++){

            int[] dat = new int[data.get(0).size()];
            for(int j = 0; j < data.get(i).size(); j++){

                dat[j] = data.get(i).get(j);
            }
            dataToReturn[i] = dat;
        }

        return dataToReturn;
    }

    private Integer[][] makeCustomBetSlip( int[][] numbers ){

        List<List<Integer>> columnData = new ArrayList<>();

        Set<Integer> modedColumnData = new TreeSet<>();
        for( int i = 0; i < numbers.length; i++){

            for( int j = 0; j < numbers[i].length; j++){

                modedColumnData.add( numbers[i][j] );

            }

            List<Integer> data = new ArrayList<>();
            data.addAll( modedColumnData );
            columnData.add( data );
            modedColumnData.clear();
        }
        
        for(int i = 1; i < columnData.size(); i++){

            List<Integer> listData = columnData.get( i );
            Iterator<Integer> lIterator = listData.iterator();

            while( lIterator.hasNext() ){

                if( columnData.get( i - 1 ).contains( lIterator.next()) ){

                    lIterator.remove();
                }
            }
        }
        
        Integer[][] customBetSlip = new Integer[columnData.size()][];
        int[] index = {0};
        columnData.forEach( list -> {

            Integer[] col = new Integer[list.size()];
            for(int i = 0; i < list.size(); i++){

                col[i] = list.get(i);
            }
            Arrays.sort(col);
            customBetSlip[index++] = col;
        });


        return customBetSlip;
    }

    private void sortColumnAndIndexHits() {

        Comparator<Map.Entry<Integer,Object[]>> hitComparator = (o1, o2) -> {

            Integer o1Hits = (Integer)o1.getValue()[0];
            Integer o2Hits = (Integer)o2.getValue()[0];

            int result = o1Hits.compareTo( o2Hits );
            if(result > 0){return -1;}
            else if( result < 0){return 1;}
            else {return 0;}

        };

        for (int k = 0; k < columnAndIndexHitAnalyzers.length; k++) {

            ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer = columnAndIndexHitAnalyzers[k];
            Map<Integer, Object[]> data = columnAndIndexHitAnalyzer.getColumnIndexHolder();
            List<Map.Entry<Integer, Object[]>> entryData = columnAndIndexHitAnalyzer.getEntries();

            entryData.addAll(data.entrySet());
            entryData.sort(hitComparator);
        }
    }

    private void findWinningNumberCompaionHits() {

        for (int i = 0; i < columnAndIndexHitAnalyzers.length; i++) {

            ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer = columnAndIndexHitAnalyzers[i];
            Map<Integer, Object[]> data = columnAndIndexHitAnalyzer.getColumnIndexHolder();

            data.forEach((k, v) -> {

                List<Integer> numberHitsPatterns = (List<Integer>) v[3];
                int currentWinningNumber = numberHitsPatterns.get(numberHitsPatterns.size() - 1);

                int[] indicies = IntStream.range(0, numberHitsPatterns.size() - 1)
                        .filter(index -> numberHitsPatterns.get(index) == currentWinningNumber).toArray();

                Map<Integer, Integer[]> companionNumberTracker = (Map<Integer, Integer[]>) v[4];

                for (int j = 0; j < indicies.length; j++) {

                    int nextWinningNumber = numberHitsPatterns.get(indicies[j] + 1);
                    if (!companionNumberTracker.containsKey(nextWinningNumber)) {
                        companionNumberTracker.put(nextWinningNumber, new Integer[]{1, 0});
                        NumberAnalyzer.incrementGamesOut(companionNumberTracker, nextWinningNumber);
                    } else {

                        Integer[] values = companionNumberTracker.get(nextWinningNumber);
                        values[0]++;
                        values[1] = 0;
                        NumberAnalyzer.incrementGamesOut(companionNumberTracker, nextWinningNumber);
                    }
                }
            });
        }
    }

    private void findRowAndColumnHits(Integer[][] betSlipDefinitions, List<StringBuilder> data) {

        Map<String, BetSlipDistributionAnalyzer> hitAndGameOutTracker = new HashMap<>();

        List<Integer[]> betSlipColumns = new ArrayList<>();
        betSlipColumns.addAll(Arrays.asList(betSlipDefinitions));

        for (int i = 0; i < data.size(); i++) {

            String[] drawString = data.get(i).toString().split("-");

            StringBuilder columnString = new StringBuilder();
            StringBuilder rowString = new StringBuilder();

            // now analyze each draw number to see how many rows an columns were needed
            for (int k = 0; k < drawString.length; k++) {

                int number = Integer.parseInt(drawString[k].trim());
                for (int j = 0; j < betSlipColumns.size(); j++) {

                    if (Arrays.asList(betSlipColumns.get(j)).contains(number)) {
                        columnString.append(j+1).append(",");
                        rowString.append(Arrays.asList(betSlipColumns.get(j)).indexOf(number)).append(",");
                        break;
                    }
                }
            }

            columnString.setCharAt(columnString.lastIndexOf(","), ' ');
            rowString.setCharAt(rowString.lastIndexOf(","), ' ');

            Integer[][] betslipMatrix = getBetSlipDefinitions(lottoGame);
            populateColumnAndIndexHits(columnString,rowString,betslipMatrix);

            Set<String> columnSet = new HashSet<>(Arrays.asList(columnString.toString().trim().split(",")));
            Set<String> rowSet = new HashSet<>(Arrays.asList(rowString.toString().trim().split(",")));

            String format = columnSet.size() + "Col" + " / " + rowSet.size() + "Row";

            if (!hitAndGameOutTracker.containsKey(format)) {

                hitAndGameOutTracker.put(format, new BetSlipDistributionAnalyzer(1, 0, new HashMap<>()));
                BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = hitAndGameOutTracker.get(format);
                betSlipDistributionAnalyzer.incrementGamesOutForAllOtherFormatOccurences(hitAndGameOutTracker, format);

                Map<String, Integer[]> winninNumberDistibutionMap = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder();
                if (!winninNumberDistibutionMap.containsKey(data.get(i).toString())) {

                    winninNumberDistibutionMap.put(data.get(i).toString(), new Integer[]{1, 0});
                    betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());
                } else {
                    Integer[] innerData = winninNumberDistibutionMap.get(data.get(i).toString());
                    innerData[0]++;
                    innerData[1] = 0;
                    betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());

                }
            } else {

                BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = hitAndGameOutTracker.get(format);
                int hits = betSlipDistributionAnalyzer.getFormatHits();

                betSlipDistributionAnalyzer.setFormatHits(++hits);
                betSlipDistributionAnalyzer.setFormatGamesOut(0);
                betSlipDistributionAnalyzer.incrementGamesOutForAllOtherFormatOccurences(hitAndGameOutTracker, format);

                Map<String, Integer[]> winninNumberDistibutionMap = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder();
                if (!winninNumberDistibutionMap.containsKey(data.get(i).toString())) {

                    winninNumberDistibutionMap.put(data.get(i).toString(), new Integer[]{1, 0});
                    betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());
                } else {

                    Integer[] innerData = winninNumberDistibutionMap.get(data.get(i).toString());
                    innerData[0]++;
                    innerData[1] = 0;
                    betSlipDistributionAnalyzer.incrementGamesOutForNonWinning(hitAndGameOutTracker, data.get(i).toString());
                }
            }
        }

        sortedEntries = new ArrayList<>(hitAndGameOutTracker.entrySet());
        sortedEntries.sort((o1, o2) -> {

            Integer o1Hits = o1.getValue().getFormatHits();
            Integer o2Hits = o2.getValue().getFormatHits();

            int result = o1Hits.compareTo( o2Hits );
            if(result > 0){return -1;}
            else if( result < 0){return 1;}
            else {return 0;}    }
            );

        //Collections.reverse(sortedEntries);

        sortedEntries.forEach(k -> {
            System.out.println(String.format("Pattern Helper: %s \tHits: %s \tGames Out: %s", k.getKey(), k.getValue().getFormatHits(), k.getValue().getFormatGamesOut()));
        });

    }


    private void populateColumnAndIndexHits(StringBuilder columnString, StringBuilder rowString, Integer[][] betslipMatrix) {

        final String[] columnIndices = columnString.toString().trim().split(",");
        final String[] rowIndices = rowString.toString().trim().split(",");

        for (int k = 0; k < columnAndIndexHitAnalyzers.length; k++) {

            ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer = columnAndIndexHitAnalyzers[k];

            Map<Integer, Object[]> colAndIndexData = columnAndIndexHitAnalyzer.getColumnIndexHolder();
            if (!colAndIndexData.containsKey(Integer.parseInt(columnIndices[k]))) {

                colAndIndexData.put(Integer.parseInt(columnIndices[k]), new Object[]{1, 0, new TreeMap<Integer, Integer[]>(),
                        new ArrayList<Integer>(), new TreeMap<Integer, Integer[]>(), new LinkedHashMap<Integer[],Integer[]>()});

                // new code starts here
                Map<Integer[],Integer[]> hitSumRangeMap = (Map<Integer[],Integer[]>)colAndIndexData.get(Integer.parseInt(columnIndices[k]))[5];

                if(hitSumRangeMap.size() < 1)
                    plugSumRangesIntoMap(hitSumRangeMap);

                // Continue to implement hit sum range here



                Map<Integer, Integer[]> rowInfo = (Map<Integer, Integer[]>) colAndIndexData.get(Integer.parseInt(columnIndices[k]))[2];
                columnAndIndexHitAnalyzer.incrementGamesOutForNonWinningColumns(colAndIndexData, columnIndices[k]);

                Integer[] winnincolumn = betslipMatrix[Integer.parseInt(columnIndices[k])-1];
                int winningDigit = winnincolumn[Integer.parseInt(rowIndices[k])];

                if (!rowInfo.containsKey(winningDigit)) {

                    List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get(Integer.parseInt(columnIndices[k]))[3];
                    winningNumberHolder.add(winningDigit);
                    rowInfo.put(winningDigit, new Integer[]{1, 0});
                    NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                } else {

                    List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get(Integer.parseInt(columnIndices[k]))[3];
                    winningNumberHolder.add(winningDigit);

                    Integer[] winningDigitData = rowInfo.get(winningDigit);
                    winningDigitData[0]++;
                    winningDigitData[1] = 0;
                    NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                }
            } else {

                Object[] colAndIndexDataTwo = colAndIndexData.get(Integer.parseInt(columnIndices[k]));
                colAndIndexDataTwo[0] = (int) colAndIndexDataTwo[0] + 1;
                colAndIndexDataTwo[1] = 0;
                columnAndIndexHitAnalyzer.incrementGamesOutForNonWinningColumns(colAndIndexData, columnIndices[k]);
                Map<Integer, Integer[]> rowInfo = (Map<Integer, Integer[]>) colAndIndexDataTwo[2];

                Integer[] winnincolumn = betslipMatrix[Integer.parseInt(columnIndices[k])-1];
                int winningDigit = winnincolumn[Integer.parseInt(rowIndices[k])];

                if (!rowInfo.containsKey(winningDigit)) {

                    List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get(Integer.parseInt(columnIndices[k]))[3];
                    winningNumberHolder.add(winningDigit);

                    rowInfo.put(winningDigit, new Integer[]{1, 0});
                    NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                } else {

                    List<Integer> winningNumberHolder = (List<Integer>) colAndIndexData.get(Integer.parseInt(columnIndices[k]))[3];
                    winningNumberHolder.add(winningDigit);

                    Integer[] winningDigitData = rowInfo.get(winningDigit);
                    winningDigitData[0]++;
                    winningDigitData[1] = 0;
                    NumberAnalyzer.incrementGamesOut(rowInfo, winningDigit);
                }
            }
        }

    }

    private void plugSumRangesIntoMap(Map<Integer[], Integer[]> hitSumRangeMap) {

        for(Iterator<Integer[]> iterator = sumRanges.iterator(); iterator.hasNext();){

            hitSumRangeMap.put(iterator.next(), new Integer[]{0,0});
        }
    }

    private Integer[][] getBetSlipDefinitions(LottoGame lottoGame) {

        if (betSlipDefinitions == null)
            betSlipDefinitions = new BetSlipDefinitions();

        return betSlipDefinitions.getDefinitionFile(lottoGame.getGameName());
    }

    private List<StringBuilder> formDrawStrings(int[][] drawNumbers, int index) {

        List<StringBuilder> data = new ArrayList<>();

        // add string builders to list for access later
        for (int i = 0; i < 1; i++)
            data.add(new StringBuilder());

        for (int i = 0; i < 1; i++) {

            StringBuilder builder = data.get(i);
            String appendVal;
            for (int k = 0; k < drawNumbers.length; k++) {


                builder.append(drawNumbers[k][index]).append("-");
            }

         builder.setCharAt( builder.toString().length()-1 , ' ');
        }

        return data;
    }
}
