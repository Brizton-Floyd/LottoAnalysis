package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.numbertracking.FirstLastDigitTracker;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SuppressWarnings("unchecked")
public class BetSlipAnalyzer {

    private BetSlipDefinitions betSlipDefinitions;
    private ColumnAndIndexHitAnalyzer[] columnAndIndexHitAnalyzers;
    List<Map.Entry<String, BetSlipDistributionAnalyzer>> sortedEntries;
    private LottoGame lottoGame;
    private List<List<Integer>> posData = new ArrayList<>();
    private Map<String, BetSlipDistributionAnalyzer> hitAndGameOutTracker = new LinkedHashMap<>();
    private Map<Integer,Integer[]> totalNumberPresentTracker = new TreeMap<>();
    private List<Integer> avgValueHolder = new ArrayList<>();

    private Integer[][] customBetSlip;
    private int betSlipRange = 5;
    private double avgHits;

    private static Set<Integer[]> lastDigitHolder = new LinkedHashSet<>();
    static {
        lastDigitHolder.add( new Integer[]{0,1});
        lastDigitHolder.add( new Integer[]{2,3});
        lastDigitHolder.add( new Integer[]{4,5});
        lastDigitHolder.add( new Integer[]{6,7});
        lastDigitHolder.add( new Integer[]{8,9});
    }

    public ColumnAndIndexHitAnalyzer[] getColumnAndIndexHitAnalyzers() {
        return columnAndIndexHitAnalyzers;
    }

    public Object[] getBetSlipData() {
        return new Object[]{columnAndIndexHitAnalyzers, hitAndGameOutTracker};
    }

    public void analyzeDrawData(int[][] drawNumbers, LottoGame lottoGame, int span) {

        this.lottoGame = lottoGame;
        betSlipRange = span;

        columnAndIndexHitAnalyzers = new ColumnAndIndexHitAnalyzer[drawNumbers.length];
        for (int i = 0; i < columnAndIndexHitAnalyzers.length; i++){
            columnAndIndexHitAnalyzers[i] = new ColumnAndIndexHitAnalyzer();
            posData.add( new ArrayList<>() );
        }


        for(int i = betSlipRange + 1; i < drawNumbers[0].length; i++) {

            if( posData.get(0).size() >= betSlipRange)
            {

                List<StringBuilder> data = formDrawStrings(drawNumbers, i);
//                if(data.get(0).toString().trim().equals("2-7-17-19-36"))
//                    System.out.println("yes");

                Integer[][] betSlipDefinitions = makeCustomBetSlip( convertPosDataToArrayOfArrays(posData) );
                findRowAndColumnHits(betSlipDefinitions, data);

                int numbersPresent = scanPastResultsForHits( data );
                avgValueHolder.add(numbersPresent);
                populateNumbersPresentMap(numbersPresent);
                // remove first index from each list so it stays at a size of 20
                posData.forEach( list -> {

                    list.remove(0);
                });

                String[] vals = data.get(0).toString().split("-");
                for(int p = 0; p < vals.length; p++){

                    List<Integer> dd = posData.get(p);
                    dd.add(Integer.parseInt(vals[p].trim()));
                }
            }
            else
            {

                for(int j = 0; j < betSlipRange; j++ ){

                    for(int k = 0; k < posData.size(); k++){

                        List<Integer> listData = posData.get( k );
                        listData.add( drawNumbers[k][j] );

                    }
                }
                i--;
            }
        }
        storeCurrentRelevantNumbers();
        findWinningNumberCompaionHits();
        analyzeFirstDigits();
        sortColumnAndIndexHits();
        populateLastDigitHolder();
        sortData();
        computeAvgHits();
    }

    public double getAvgHits() {
        return avgHits;
    }

    public Map<Integer, Integer[]> getTotalNumberPresentTracker() {
        return totalNumberPresentTracker;
    }

    private void computeAvgHits() {

        int sum = 0;
       // double roundOff = (double) Math.round(a * 100) / 100;
        sum = avgValueHolder.stream().mapToInt(Integer::intValue).sum();

        avgHits =  (double) sum / avgValueHolder.size();
        avgHits =  (double) Math.round(avgHits * 100) / 100;
    }

    private void analyzeFirstDigits() {

        for(ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer : columnAndIndexHitAnalyzers){

            List<Integer> numHolder =columnAndIndexHitAnalyzer.getDigitHolder();
            FirstLastDigitTracker tracker = columnAndIndexHitAnalyzer.getFirstLastDigitTracker();
            for(int num : numHolder){

                tracker.analyzeFirstDigitAndInsert( num );
            }
        }
    }

    private int scanPastResultsForHits(List<StringBuilder> data) {

        int[] count = {0};
        String[] results = data.get(0).toString().trim().split("-");

        Iterator<String> iterator = Arrays.stream(results).collect(Collectors.toList()).iterator();

        while (iterator.hasNext()){

            int val = Integer.parseInt( iterator.next() );

            for(List list : posData){

                if(list.contains(val)){

                    count[0]++;
                    break;
                }
            }

        }

        return count[0];

    }

    private void populateLastDigitHolder() {


        for(ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer : columnAndIndexHitAnalyzers){

            ColumnAndIndexHitAnalyzer analyzer = columnAndIndexHitAnalyzer;
            Map<Integer, Object[]> colAndIndexData = analyzer.getColumnIndexHolder();

            colAndIndexData.forEach((k,v) -> {

                List<Integer> listData = (List<Integer>)v[3];
                listData.forEach( num -> {

                   // System.out.println(listData.get(listData.size()-1));
                    String numString = Integer.toString(num);
                    int winninDigitSum = (numString.length() > 1) ? Character.getNumericValue(numString.charAt(0)) +
                            Character.getNumericValue(numString.charAt(1)): Integer.parseInt(numString);

                    int lastDigitOfWinningSum = (Integer.toString(num).length() > 1) ? Character.getNumericValue((num+"").charAt(1)):
                            Character.getNumericValue(Integer.toString(num).charAt(0));

                    Map<Integer[],Integer[]> hitSumRangeMap = (Map<Integer[],Integer[]>)v[5];

                    for(Map.Entry<Integer[],Integer[]> entry : hitSumRangeMap.entrySet() ){

                        if(lastDigitOfWinningSum >= entry.getKey()[0] && lastDigitOfWinningSum <= entry.getKey()[1]) {

                            Integer[] data = entry.getValue();
                            data[0]++;
                            data[1] = 0;

                            incrementGamesOut(hitSumRangeMap, entry.getKey());
                        }
                    }
                });

            });
        }

    }

    private void incrementGamesOut(Map<Integer[], Integer[]> hitSumRangeMap, Integer[] key) {


        hitSumRangeMap.forEach((k,v) -> {

            if(!Arrays.equals(k,key)){

                v[1]++;
            }
        });
    }

    private void storeCurrentRelevantNumbers() {

        //for(int i = 0; i < customBetSlip.length)

        int indexer = 0;

        for(ColumnAndIndexHitAnalyzer analyzer : columnAndIndexHitAnalyzers){

            Map<Integer, Object[]> colAndIndexData = analyzer.getColumnIndexHolder();

            for(Map.Entry<Integer,Object[]> entry : colAndIndexData.entrySet()){

                Map<Integer,Integer[]> data = (Map<Integer, Integer[]>) entry.getValue()[2];
                List<Integer> listData = (List<Integer>) entry.getValue()[3];
                data.clear();

                for(int i = 0; i < customBetSlip[indexer].length; i++){


                    if(!data.containsKey( customBetSlip[indexer][i] )){

                        int winningDigit = customBetSlip[indexer][i];

                        Long hitCount = listData.stream().filter( num -> num == winningDigit).count();
                        int gamesOut = Math.abs( listData.lastIndexOf( winningDigit ) - listData.size() ) - 1;
                        data.put(winningDigit, new Integer[]{hitCount.intValue(), gamesOut});

                    }
                }
                indexer++;
            }
            indexer = 0;
        }
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

        List<List<Integer>> columnData = new LinkedList<>();

        for(int i = 0; i < numbers.length; i++){

            List<Integer> data = Arrays.stream( numbers[i] ).boxed().collect(Collectors.toList());
            for(Iterator<Integer> iterator = data.iterator(); iterator.hasNext();){

                int number = iterator.next();
                int[] winningIndexes = IntStream.range(0, data.size()).filter( k -> data.get(k) == number).toArray();

                if(winningIndexes.length > 1 && number > -1)
                {
                    List<Integer> p = Arrays.stream(winningIndexes).boxed().collect(Collectors.toList());
                    p.remove( p.size() - 1);

                    p.forEach( v -> {

                        data.set(v,-1);
                    });
                }
            }
            //data.removeIf( val -> val < 0);
            columnData.add(data);
        }

        extractRepeatedNumbers( columnData );

        customBetSlip = new Integer[columnData.size()][];
        int[] index = {0};
        columnData.forEach( list -> {

            Integer[] col = new Integer[list.size()];
            for(int i = 0; i < list.size(); i++){

                col[i] = list.get(i);
            }
            //Arrays.sort(col);
            customBetSlip[index[0]++] = col;
        });


        return customBetSlip;
    }

    private void extractRepeatedNumbers(List<List<Integer>> columnData) {

        for(int i = 1; i < columnData.size(); i++){

            List<Integer> previousList = columnData.get(i-1);
            List<Integer> currentList = columnData.get(i);

            for(int k = 0; k < previousList.size(); k++){

                if(currentList.contains(previousList.get(k)) && previousList.get(k) != -1){

                    int index = previousList.indexOf( previousList.get(k));
                    previousList.set(index, -1);
                }
            }
        }
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

        List<Integer[]> betSlipColumns = new ArrayList<>();
        betSlipColumns.addAll(Arrays.asList(betSlipDefinitions));

        for (int i = 0; i < data.size(); i++) {

            String[] drawString = data.get(i).toString().split("-");

            StringBuilder columnString = new StringBuilder();
            StringBuilder rowString = new StringBuilder();
            int[] index = null;

            // now analyze each draw number to see how many rows an columns were needed
            for (int k = 0; k < drawString.length; k++) {

                int number = Integer.parseInt(drawString[k].trim());

                index = findCorrectColumnAndRowHitIndex( number, betSlipColumns);

                if(index == null)
                    break;

                columnString.append(index[0]).append(",");
                rowString.append(index[1]).append(",");

            }

            if( index == null)
                break;

            columnString.setCharAt(columnString.lastIndexOf(","), ' ');
            rowString.setCharAt(rowString.lastIndexOf(","), ' ');

            //Integer[][] betslipMatrix = betSlipDefinitions;

            Set<String> columnSet = new HashSet<>(Arrays.asList(columnString.toString().trim().split(",")));
            Set<String> rowSet = new HashSet<>(Arrays.asList(rowString.toString().trim().split(",")));

            populateColumnAndIndexHits(columnString,rowString,betSlipDefinitions);

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

    }

    private int[] findCorrectColumnAndRowHitIndex(int number, List<Integer[]> betSlipColumns) {

        int indexer = 0;
        int[] dataToReturn = {Integer.MIN_VALUE,Integer.MIN_VALUE};

        List<List<Integer>> betSlipDataPoints = new ArrayList<>();
        for(int i = 0; i < betSlipColumns.size(); i++){

            betSlipDataPoints.add( Arrays.stream(betSlipColumns.get(i)).collect(Collectors.toList()));
        }

        for(int i = 0; i < betSlipDataPoints.size(); i++){

            List<Integer> data = betSlipDataPoints.get(i);
            if(data.contains(number)){

                int hitIndex = data.indexOf(number);

                if(hitIndex > dataToReturn[1]) {

                    dataToReturn[indexer++] = i + 1;
                    dataToReturn[indexer] = hitIndex;
                    break;

                }

                indexer = 0;
            }

        }

        // now check to see which col has the most recent hit occurence of number
        List<Integer> values = Arrays.stream(dataToReturn).boxed().collect(Collectors.toList());
        if(values.get(0) < 0)
            return null;

        return dataToReturn;
    }

    private void populateNumbersPresentMap(int hitCounter) {

        List<Integer> vals = null;

        if(hitCounter > 0)
            vals = IntStream.range(1, hitCounter + 1).boxed().collect(Collectors.toList());
        else
            vals = IntStream.of(0).boxed().collect(Collectors.toList());

        if(hitCounter > 0) {

            for (int i = hitCounter; i > 0; i--) {

                if (!totalNumberPresentTracker.containsKey(i))
                {

                    totalNumberPresentTracker.put(i, new Integer[]{1, 0});
                    specialGamesOutIncrementer( totalNumberPresentTracker, vals);

                }
                else
                {

                    Integer[] data = totalNumberPresentTracker.get(i);
                    data[0]++;
                    specialGamesOutIncrementer(totalNumberPresentTracker, vals);

                }
            }
        }
        else
        {

            if(!totalNumberPresentTracker.containsKey(hitCounter)){

                totalNumberPresentTracker.put(hitCounter, new Integer[]{1,0});
                specialGamesOutIncrementer(totalNumberPresentTracker, vals);
            }
            else{

                Integer[] data = totalNumberPresentTracker.get( hitCounter );
                data[0]++;
                specialGamesOutIncrementer(totalNumberPresentTracker, vals);
            }
        }
    }

    private void specialGamesOutIncrementer(Map<Integer, Integer[]> totalNumberPresentTracker, List<Integer> vals) {

        totalNumberPresentTracker.forEach( (k,v) -> {

            if(!vals.contains(k)){

                v[1] ++;
            }
            else{
                v[1] = 0;
            }
        });
    }

    private void sortData() {

        sortedEntries= new ArrayList<>(hitAndGameOutTracker.entrySet());
        sortedEntries.sort((o1, o2) -> {

            Integer o1Hits = o1.getValue().getFormatHits();
            Integer o2Hits = o2.getValue().getFormatHits();

            int result = o1Hits.compareTo( o2Hits );
            if(result > 0){return -1;}
            else if( result < 0){return 1;}
            else {return 0;}
        });


    }


    private void populateColumnAndIndexHits(StringBuilder columnString, StringBuilder rowString, Integer[][] betslipMatrix) {

        int columIndexConter = 0;
        final String[] columnIndices = columnString.toString().trim().split(",");
        final String[] rowIndices = rowString.toString().trim().split(",");

        for (int k = 0; k < columnAndIndexHitAnalyzers.length; k++) {

            ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer = columnAndIndexHitAnalyzers[k];
            List<Integer> columnHitHolder = columnAndIndexHitAnalyzer.getColumnHitTracker();

            Map<Integer, Object[]> colAndIndexData = columnAndIndexHitAnalyzer.getColumnIndexHolder();
            List<Integer> digitHolder = columnAndIndexHitAnalyzer.getDigitHolder();
            List<Integer> lastDigitHolder = columnAndIndexHitAnalyzer.getLastDigitHolder();

            if (!colAndIndexData.containsKey(Integer.parseInt(columnIndices[k]))) {

                columnHitHolder.add(Integer.parseInt(columnIndices[k]));
                colAndIndexData.put(Integer.parseInt(columnIndices[k]), new Object[]{1, 0, new LinkedHashMap<Integer,Integer[]>(),
                        new ArrayList<Integer>(), new TreeMap<Integer, Integer[]>(), new LinkedHashMap<Integer[],Integer[]>()});



                // new code starts here
                Map<Integer[],Integer[]> hitSumRangeMap = (Map<Integer[],Integer[]>)colAndIndexData.get(Integer.parseInt(columnIndices[k]))[5];

                if(hitSumRangeMap.size() < 1)
                    plugSumRangesIntoMap(hitSumRangeMap);

                Map<Integer, Integer[]> rowInfo = (Map<Integer, Integer[]>) colAndIndexData.get(Integer.parseInt(columnIndices[k]))[2];
                columnAndIndexHitAnalyzer.incrementGamesOutForNonWinningColumns(colAndIndexData, columnIndices[k]);

                Integer[] winnincolumn = betslipMatrix[Integer.parseInt(columnIndices[k])-1];
                int winningDigit = winnincolumn[Integer.parseInt(rowIndices[k])];
                int lastDigit = (Integer.toString(winningDigit).length() > 1) ? Character.getNumericValue(Integer.toString(winningDigit).charAt(1)):
                        Character.getNumericValue(Integer.toString(winningDigit).charAt(0));

                digitHolder.add(winningDigit);
                lastDigitHolder.add(lastDigit);

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

                columnHitHolder.add(Integer.parseInt(columnIndices[k]));

                Object[] colAndIndexDataTwo = colAndIndexData.get(Integer.parseInt(columnIndices[k]));
                colAndIndexDataTwo[0] = (int) colAndIndexDataTwo[0] + 1;
                colAndIndexDataTwo[1] = 0;
                columnAndIndexHitAnalyzer.incrementGamesOutForNonWinningColumns(colAndIndexData, columnIndices[k]);
                Map<Integer, Integer[]> rowInfo = (Map<Integer, Integer[]>) colAndIndexDataTwo[2];

                Integer[] winnincolumn = betslipMatrix[Integer.parseInt(columnIndices[k])-1];
                int winningDigit = winnincolumn[Integer.parseInt(rowIndices[k])];
                int lastDigit = (Integer.toString(winningDigit).length() > 1) ? Character.getNumericValue(Integer.toString(winningDigit).charAt(1)):
                        Character.getNumericValue(Integer.toString(winningDigit).charAt(0));

                digitHolder.add(winningDigit);
                lastDigitHolder.add(lastDigit);

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

        for(Iterator<Integer[]> iterator = lastDigitHolder.iterator(); iterator.hasNext();){

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
