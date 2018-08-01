package com.lottoanalysis.utilities.chartutility;

import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class ChartHelper {

    private static Map<Integer,Integer[]> overallGamesOutTracker = new TreeMap<>();
    private static Map<Integer,Map<Integer,Integer[]>> recentWinningNumberCompanionHitTracker = new TreeMap<>();
    private static Map<Integer,Map<Integer,Integer[]>> recentWinningNumberLineLengthDue = new TreeMap<>();
    private static Map<Integer,Map<Integer,Map<Integer,Integer[]>>> remainderDueForLineLength = new TreeMap<>();

    public static List<Object[]> setUpTopLevelCharts(int[] data, String length){

        List<Object[]> numberPoints = new LinkedList<>();

        groupNumbers(data, numberPoints, length);

        return numberPoints;

    }

    public static Map<Integer, Map<Integer, Map<Integer, Integer[]>>> getRemainderDueForLineLength() {
        return remainderDueForLineLength;
    }

    public static Map<Integer, Map<Integer, Integer[]>> getRecentWinningNumberLineLengthDue() {
        return recentWinningNumberLineLengthDue;
    }

    public static Map<Integer, Map<Integer, Integer[]>> getRecentWinningNumberCompanionHitTracker() {
        return recentWinningNumberCompanionHitTracker;
    }

    public static void clearStaticCharts(){
        if(recentWinningNumberCompanionHitTracker != null)
            recentWinningNumberCompanionHitTracker.clear();

        if(recentWinningNumberLineLengthDue != null)
            recentWinningNumberLineLengthDue.clear();

        if(remainderDueForLineLength != null)
            remainderDueForLineLength.clear();
    }
    public static void plugNumbersIntoRecentWinningNumberCompanionMap(List<Integer> winningNumbers){

        //recentWinningNumberCompanionHitTracker.clear();
        // recent winning digit
        int num = winningNumbers.get( winningNumbers.size() - 1 );
		
		for(int i = 0; i < winningNumbers.size() - 1; i++){
			
			if(winningNumbers.get(i) == num){
				// extract the nextWinningNumber
			    int nextWinningNumber = winningNumbers.get( i + 1 );
				
				// first check to see if recentWinningNumberCompanionHitTracker contains key for recent winning digit
				if(!recentWinningNumberCompanionHitTracker.containsKey( num ) ){
					
					// place number and new TreeMap instance into map
					recentWinningNumberCompanionHitTracker.put( num, new TreeMap<>() );
					
					// retrieve the inner tree map for further processing
					Map<Integer,Integer[]> innerTreeMap = recentWinningNumberCompanionHitTracker.get( num );
					
					// Check to see if inneer tree map contains next winning number. if it doesnt add it						
					if(!innerTreeMap.containsKey( nextWinningNumber ) ){

						innerTreeMap.put( nextWinningNumber, new Integer[]{1,0} );
						NumberAnalyzer.incrementGamesOut( innerTreeMap, nextWinningNumber );
						
					}else{
						
						Integer[] data = innerTreeMap.get( nextWinningNumber );
						data[0]++;
						data[1] = 0;
						NumberAnalyzer.incrementGamesOut( innerTreeMap, nextWinningNumber );
						
					}
				}else{
					
					Map<Integer,Integer[]> innerTreeMap = recentWinningNumberCompanionHitTracker.get( num );
					if( !innerTreeMap.containsKey( nextWinningNumber ) ){
						innerTreeMap.put( nextWinningNumber, new Integer[]{1,0});
						NumberAnalyzer.incrementGamesOut( innerTreeMap, nextWinningNumber );
					}
					else{
						Integer[] data = innerTreeMap.get( nextWinningNumber );
						data[0]++;
						data[1] = 0;
						NumberAnalyzer.incrementGamesOut( innerTreeMap, nextWinningNumber );
					}
				}
				// start code here 
				determineLineLengthDueForRecentWinningDigit ( recentWinningNumberCompanionHitTracker );
				
			}
		}
    }
    
private static void determineLineLengthDueForRecentWinningDigit( Map<Integer,Map<Integer,Integer[]>> companionNumbers ){
		
		companionNumbers.forEach( (key,value) -> {
			
			if(!recentWinningNumberLineLengthDue.containsKey( key ) ){
				recentWinningNumberLineLengthDue.put(key, new TreeMap<>());
			}
			
			Map<Integer,Integer[]> data = recentWinningNumberLineLengthDue.get( key );
			value.forEach( (keyTwo, valueTwo) -> {
				
				if(valueTwo[1] == 0){
					int len = Integer.parseInt( Integer.toString( valueTwo[0] ).length() + "" );
					if( !data.containsKey(len) ){
						data.put( len, new Integer[]{1,0});
						NumberAnalyzer.incrementGamesOut(data,len);
					}
					else{
						Integer[] dataTwo = data.get( len );
						dataTwo[0]++;
						dataTwo[1] = 0; 
						NumberAnalyzer.incrementGamesOut(data,len);
					}
					//map to populateremainder map
                    setUpRemainderMap( data, value, key);
				}
				
			});			
			
		});
	}

    private static void setUpRemainderMap(Map<Integer, Integer[]> lineLengthData, Map<Integer, Integer[]> companionNumberData,
                                          int winningNumber) {

        //remainderDueForLineLength
        for( Iterator<Integer> iterator = lineLengthData.keySet().iterator(); iterator.hasNext();){

            int numLen = iterator.next();
            companionNumberData.forEach( (key,value) -> {

                int len = Integer.parseInt( Integer.toString(value[0]).length()+"");
                if( len == numLen ){
                    int remainder = value[1] % 3;
                    if( !remainderDueForLineLength.containsKey(winningNumber)){

                        remainderDueForLineLength.put(winningNumber,new TreeMap<>());
                        Map<Integer,Map<Integer,Integer[]>> data  = remainderDueForLineLength.get(winningNumber);
                        if(!data.containsKey(numLen)){
                            data.put(numLen, new TreeMap<>());
                            Map<Integer,Integer[]> dataTwo = data.get( numLen );
                            if( !dataTwo.containsKey(remainder)){

                                dataTwo.put(remainder, new Integer[]{1,0});
                                NumberAnalyzer.incrementGamesOut(dataTwo,remainder);
                            }
                        }else{

                            Map<Integer,Map<Integer,Integer[]>> dataVal = remainderDueForLineLength.get(winningNumber);
                            Map<Integer,Integer[]> dataValTwo = dataVal.get(numLen);
                            Integer[] values = dataValTwo.get( remainder );
                            values[0]++;
                            values[1] = 0;
                            NumberAnalyzer.incrementGamesOut(dataValTwo,remainder);
                        }
                    }
                    else{
                        Map<Integer,Map<Integer,Integer[]>> data = remainderDueForLineLength.get(winningNumber);
                        if(!data.containsKey(numLen)){
                            data.put(numLen, new TreeMap<>());
                            Map<Integer,Integer[]> dataTwo = data.get(numLen);
                            if(!dataTwo.containsKey(remainder)){
                                dataTwo.put(remainder, new Integer[]{1,0});
                                NumberAnalyzer.incrementGamesOut(dataTwo,remainder);
                            }
                        }
                        else {
                            Map<Integer,Integer[]> vals= data.get(numLen);
                            if(!vals.containsKey(remainder)){
                                vals.put(remainder,new Integer[]{1,0});
                                NumberAnalyzer.incrementGamesOut(vals,remainder);
                            }else {
                                Integer[] values = vals.get(remainder);
                                values[0]++;
                                values[1] = 0;
                                NumberAnalyzer.incrementGamesOut(vals, remainder);
                            }
                        }
                    }
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    private static void groupNumbers(int[] data, List<Object[]> numberPoints, String length) {

        Map<Integer[],Object[]> lastDigitGroupingsAndHitCounter = new HashMap<>();
        lastDigitGroupingsAndHitCounter.put(new Integer[]{0,1,2,3}, new Object[]{0,0,new ArrayList<Integer>(),0,0,new ArrayList<>(),0,"",0});
        lastDigitGroupingsAndHitCounter.put(new Integer[]{4,5,6}, new Object[]{0,0,new ArrayList<Integer>(),0,0,new ArrayList<>(),0,"",0});
        lastDigitGroupingsAndHitCounter.put(new Integer[]{7,8,9}, new Object[]{0,0,new ArrayList<Integer>(),0,0,new ArrayList<>(),0,"",0});

        for(int number = 0; number < data.length; number++){

            for(Map.Entry<Integer[],Object[]> mapData : lastDigitGroupingsAndHitCounter.entrySet()){

                Integer[] keyData = mapData.getKey();
                List<Integer> keyDataList = Arrays.asList( keyData );

                int lastDigit;
                if( Integer.parseInt(length) > 1)
                    lastDigit = (Integer.toString(data[number]).length() > 1) ? Character.getNumericValue( Integer.toString(data[number]).charAt(1)) :
                                                                                data[number];
                else
                    lastDigit = data[number];

                if(keyDataList.contains( lastDigit )){

                    Object[] valueData = mapData.getValue();

                    // store current gamesoutview in list
                    ((ArrayList<Integer>)valueData[5]).add((int)valueData[1]);

                    populateUniversalGameOutHitTracker( (int)valueData[1] );
                    valueData[0] = (int)valueData[0] + 1;
                    valueData[1] = 0;
                    ((ArrayList<Integer>)valueData[2]).add(data[number]);

                    NumberAnalyzer.incrementGamesOut(lastDigitGroupingsAndHitCounter, keyData);

                }
            }

        }

        //find hits at current lottogames out
        findHitsAtCurrentGamesOut( lastDigitGroupingsAndHitCounter );

        //find min and max
        findMinAndMax(lastDigitGroupingsAndHitCounter);

        // associate lottogames out historical values with charts
        fillInGamesOutHistoricalValues( lastDigitGroupingsAndHitCounter );

        orderChartHitsLeastToGreatest(numberPoints, lastDigitGroupingsAndHitCounter);

    }

    private static void fillInGamesOutHistoricalValues(Map<Integer[], Object[]> lastDigitGroupingsAndHitCounter) {
        // index 7 and 8
        for(Map.Entry<Integer[],Object[]> data : lastDigitGroupingsAndHitCounter.entrySet()){
            Object[] dataValues = data.getValue();
            Map<Integer, Integer[]> gamesOutTracker = overallGamesOutTracker;
            if( gamesOutTracker.containsKey( dataValues[1])){

                Integer[] trackerData = gamesOutTracker.get(dataValues[1]);
                dataValues[7] = trackerData[1] + "";
                dataValues[8] = trackerData[0];
            }else{
                dataValues[7] = "Never";
                dataValues[8] = 0;
            }
        }
    }

    private static void populateUniversalGameOutHitTracker( int value ){

        if(!overallGamesOutTracker.containsKey( value ) ){
            overallGamesOutTracker.put(value, new Integer[]{1,0});
            NumberAnalyzer.incrementGamesOut(overallGamesOutTracker, value );
        }
        else{
            Integer[] number = overallGamesOutTracker.get( value );
            number[0]++;
            number[1] = 0;
            overallGamesOutTracker.put(value, number);
            NumberAnalyzer.incrementGamesOut(overallGamesOutTracker, value );

        }
    }
    private static void findHitsAtCurrentGamesOut(Map<Integer[], Object[]> lastDigitGroupingsAndHitCounter) {

        for(Map.Entry<Integer[],Object[]> mapData : lastDigitGroupingsAndHitCounter.entrySet()){

            Object[] mapValues = mapData.getValue();

            List<Integer> hitValues = new ArrayList<> ( ((List<Integer>)mapValues[5]) );
            hitValues.forEach( e -> {

                if(e == (int)mapValues[1]){

                    mapValues[6] = (int)mapValues[6] + 1;
                }
            });
        }
    }

    private static void orderChartHitsLeastToGreatest(List<Object[]> numberPoints, Map<Integer[], Object[]> lastDigitGroupingsAndHitCounter) {
        int count=0;
        int[] placeHolder = new int[3];

        for(Map.Entry<Integer[],Object[]> mapData : lastDigitGroupingsAndHitCounter.entrySet()){

            Object[] valueData = mapData.getValue();
            placeHolder[count++] = (int)valueData[0];

        }

        Arrays.sort(placeHolder);
        List<Integer> arrayData = Arrays.stream(placeHolder).boxed().collect(Collectors.toList());
        Collections.reverse(arrayData);
        count = 0;

        for(int i = 0; i < arrayData.size(); i++) {

            for (Map.Entry<Integer[], Object[]> mapData : lastDigitGroupingsAndHitCounter.entrySet()) {

                Object[] valueData = mapData.getValue();
                if (arrayData.get(i) == (int)valueData[0]) {
                    numberPoints.add(valueData);
                    break;
                }
            }
        }
    }

    private static void findMinAndMax(Map<Integer[], Object[]> lastDigitGroupingsAndHitCounter) {

        for(Map.Entry<Integer[],Object[]> mapData : lastDigitGroupingsAndHitCounter.entrySet()){

            Object[] valueData = mapData.getValue();
            List<Integer> dataa = new ArrayList<>( (ArrayList<Integer>)valueData[2]);

            Collections.sort( dataa );

            if(dataa.size() > 0) {
                valueData[3] = dataa.get(0);
                valueData[4] = dataa.get(dataa.size() - 1);
            }
            else {
                valueData[3] = 0;
                valueData[4] = 0;
            }

        }

    }

    public static int[] generateListOfAllWinningNumbersAfterCurrentWinningNumber(int[] positionArray) {

        int currentWinningNum = positionArray[ positionArray.length - 1 ];

        List<Integer> numHolder = new LinkedList<>();

        for(int i = 0; i < positionArray.length - 1; i++){

            if(positionArray[i] == currentWinningNum && i < positionArray.length){
                int nextWinningNumber = positionArray[i + 1];
                numHolder.add(nextWinningNumber);
            }
        }

        int[] data = new int[numHolder.size()];
        for(int i = 0; i < data.length; i++){
            data[i] = numHolder.get(i);
        }

        return data;

    }

    public static List<Integer> getListOfNumbersBasedOnCurrentWinningNumber(List<Integer> points) {

        List<Integer> getListOfIndexes = getIndexes( points);

        List<Integer> precedingWinningNumbers = new ArrayList<>();

        for(int i = 0; i < getListOfIndexes.size()-1; i++){

            precedingWinningNumbers.add( points.get( (getListOfIndexes.get(i) + 1) ));
        }
        return precedingWinningNumbers;
    }

    private static List<Integer> getIndexes(List<Integer> points) {

        int currentWinningNumber = points.get( points.size() - 1);

        List<Integer> indexes = new ArrayList<>();

        for(int i = 0; i < points.size(); i++){

            if( points.get(i) == currentWinningNumber){
                indexes.add(i);
            }
        }

        return indexes;
    }

    public static int[] returnNumbersAtIndex(int[] posisitionArray, String index) {
        int[] numberArray = new int[posisitionArray.length];
        int count = 0;

        if(index != null){
            int ind = Integer.parseInt(index);
            for(int num : posisitionArray){
                String numAsString = (Integer.toString(num).length() > 1) ? num + "" : "0"+num;
                int number = Character.getNumericValue( numAsString.charAt(ind));
                numberArray[count++] = number;
            }
        }else{

            for(int num : posisitionArray){
                numberArray[count++] = num;
            }

        }
        return numberArray;
    }
}
