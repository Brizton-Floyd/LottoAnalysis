package com.lottoanalysis.utilities.chartutility;

import java.util.*;
import java.util.stream.Collectors;

import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.PickFourLotteryGameImpl;
import com.lottoanalysis.models.lottogames.PickThreeLotteryGameImpl;
import com.lottoanalysis.utilities.analyzerutilites.NumberAnalyzer;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;

/**
 *
 *
 */
@SuppressWarnings("unchecked")
public class ChartHelperTwo {

    //First index in object array will be the number hits for the group,
    // second element will be group hits, third element will lottogames out, fourth element will be the hits at specified lottogames out
    // and the fifth element will be an arraylist holding all game out data.
    private static Map<Integer[],Object[]> groupHitInformation = new LinkedHashMap<>();

    private static LottoGame lottoGame;

    /**
     *This Method will take in a lottery game instance and all positional hits for a given lottery number position
     */
    public static void processIncomingData(LottoGame lotteryGame, int[] drawPositionNumbers, int groupSize){

        lottoGame = lotteryGame;

        // Deterimine how to split the number for a given lottery game
        splitLotteryGameDrawSizeIntoGroups( lotteryGame, groupSize );

        // start assigning numbers for the given drawPosition to the correct group ranges
        assignNumbersToGroups( drawPositionNumbers );

        // determine hits at lottogames out
        findHitsAtCurrentGamesOut();
		
		// analyze lottogames out information
		plugInNumbersForCurrentGamesOut();

        //print();
    }
	
	private static void plugInNumbersForCurrentGamesOut(){
	
		List<Integer> gamesOutIndexHolder = new ArrayList<>();
		groupHitInformation.forEach( (key,value) -> {
			
			Object[] data = value;
			List<Integer> arrayListData = (List<Integer>) data[4];
			for(int n = 0; n < arrayListData.size(); n++){
				if(arrayListData.get(n) == (int)data[2]){
			
					gamesOutIndexHolder.add(n);
					
				}
			}
			
			//System.out.println(Arrays.toString(gamesOutIndexHolder.toArray()));
			
			if(gamesOutIndexHolder.size() > 1 ){
		    	List<Integer> diferenceHolder = new ArrayList<>();
		    	for(int i = 0; i < gamesOutIndexHolder.size() - 1; i++){
		    	    
		    	    int dif = Math.abs(gamesOutIndexHolder.get( i + 1 ) - gamesOutIndexHolder.get(i)) - 1;
		    	    diferenceHolder.add(dif);
		    	    
		    	}
		    	
		    	// compute the averate
		    	double avg = 0.0; 
		    	int sum = 0; 
		    	
		    	for(int num : diferenceHolder){
		    	    sum += num;
		    	}
		    	
		    	avg = sum / diferenceHolder.size();
		    	value[5] = avg;
		    	
		    	for(int num : diferenceHolder){
		    	    if(num > avg)
		    	        value[6] = (int)value[6] + 1;
		    	    else if( num < avg)
		    	        value[7] = (int)value[7] + 1;
		    	    else
		    	        value[8] = (int)value[8] + 1;
		    	}
		    	
		    	int index = arrayListData.lastIndexOf( arrayListData.get( arrayListData.size() - 1) );
		    	int currentGamesOut = index - gamesOutIndexHolder.get(gamesOutIndexHolder.size() - 1 );
		    	value[9] = currentGamesOut;
		    	
				//System.out.println(Arrays.toString(diferenceHolder.toArray()));
				
			}
	
			gamesOutIndexHolder.clear();

		});
	}
	
    private static void print(){


        Map<String,Object[]> data = getGroupHitInformation();

        data.forEach( (key,value) -> {
            System.out.println("Key: " + key + "\t\tValue: " + Arrays.toString(value));
            System.out.println("\n");
        });

    }
    public static Map<String,Object[]> getGroupHitInformation(){

        Map<String,Object[]> data = new LinkedHashMap<>();

        groupHitInformation.forEach( (key,value) -> {

            Integer[] array;

            if(key.length > 1){
                array = new Integer[2];
                array[0] = key[0];
                array[1] = key[ key.length - 1];

            }else{
                array = key.clone();
            }

            data.put(Arrays.toString(array), value);
        });

        return data;
    }

    private static void findHitsAtCurrentGamesOut(){

        groupHitInformation.forEach( (key,value) -> {

            int currentGameOut = (int) value[2];
            //System.out.println(currentGameOut);
            List<Integer> gameOutNumbers = (List<Integer>) value[4];
            gameOutNumbers.forEach( number -> {

                if(number == currentGameOut){
                    value[3] = (int) value[3] + 1;
                }
            });

        });

    }

    /**
     * Method assigns numbers for a given winning lottery position to perspective draw group
     */
    private static void assignNumbersToGroups( int[] drawPositionNumbers ){

        groupHitInformation.forEach( (key,value) -> {

            List<Integer> data = Arrays.asList(key);
            plugInNumbers(data,drawPositionNumbers,value);
        });
    }
    private static void plugInNumbers(List<Integer> arrayAsList, int[] drawNumbers, Object[] groupData){

        for(int i = 0; i < drawNumbers.length; i++){

            if(arrayAsList.contains( drawNumbers[i] ) ){
                // first array list will be numbers to hit in group
                ((ArrayList<Integer>)groupData[0]).add(drawNumbers[i]);
                groupData[1] = (int)groupData[1] + 1;
                // second array list will be the game out hits for the given group
                ((ArrayList<Integer>)groupData[4]).add((int)groupData[2]);
                groupData[2] = 0;
                //incrementGamesOut(arrayAsList,groupData,false);

            }else{
                incrementGamesOut(arrayAsList,groupData);
            }
        }
    }
    private static void incrementGamesOut(List<Integer> groupNumbers, Object[] groupData){

        groupHitInformation.forEach( (key,value) -> {

            String arrayAsString = Arrays.toString(key);
            Integer[] data = groupNumbers.toArray(new Integer[groupNumbers.size()]);
            String listAsArrayString = Arrays.toString(data);

            if(arrayAsString.equals( listAsArrayString ) ){
                value[2] = (int) value[2] + 1;
            }

        });

    }
    /**
     *Clears all contents from the group hit map
     */
    public static void clearGroupHitInformation(){

        if(groupHitInformation != null)
            groupHitInformation.clear();
    }

    /**
     * This method will take in a given lottery game and determine the drawsize. Once draw size is determined distict groups
     * will then be placed into a map.
     */
    private static void splitLotteryGameDrawSizeIntoGroups(LottoGame game, int groupSize){

        int minNumber = game.getMinNumber();
        int maxNumber = game.getMaxNumber();
        int count = 0;
        int currentNumberInIteration = 0;
        boolean numbersRemaining = true;

        // get the max number which will determine how to split the group size.
        int groupDivisor;

        if(lottoGame instanceof PickThreeLotteryGameImpl || lottoGame instanceof PickFourLotteryGameImpl){

            if(groupSize > maxNumber){

                groupDivisor = groupSize;
                numbersRemaining = false;
            }
            else{
                groupDivisor = (maxNumber >= groupSize) ? groupSize : maxNumber / 2;
            }
        }
        else {
            groupDivisor = (maxNumber >= groupSize) ? groupSize : maxNumber / 2;
        }
        List<Integer> numberHolderList = new ArrayList<>();

        // start setting up to populate group hit map
        for(int i = 0; i <= maxNumber + 1; i++){

            if(count == groupDivisor){

                Integer[] groupArray = new Integer[groupDivisor];
                currentNumberInIteration = numberHolderList.get( numberHolderList.size() - 1 );

                for(int j = 0; j < numberHolderList.size(); j++){
                    groupArray[j] = numberHolderList.get(j);
                }
                // add array to groupmap. 0 index in object array will be the number hits for the group,
                // 1 index will be group hits, 2 index will lottogames out, 3 index will be the hits at specified lottogames out
                // and the 4 index will be an arraylist holding all game out data, index 5 will out avg skips, index 6 will be hits above avg,
                // index 7 will be hits below avg, index 8 will be
				// current lottogames out for the specified out.
                groupHitInformation.put(groupArray,new Object[]{new ArrayList<Integer>(),0,0,0,new ArrayList<Integer>(),0.0,0,0,0,0});

                // reset counter backt to 1 and clear the list
                count = 0;
                numberHolderList.clear();
            }

            numberHolderList.add( i );
            count++;
        }

        if(numbersRemaining) {
            // plug in last remaining numbers if there are any;
            int dif = Math.abs(currentNumberInIteration - maxNumber);
            Integer[] groupArray = new Integer[dif];
            for (int i = 0; i < groupArray.length; i++) {
                currentNumberInIteration++;
                groupArray[i] = currentNumberInIteration;
            }

            groupHitInformation.put(groupArray,new Object[]{new ArrayList<Integer>(),0,0,0,new ArrayList<Integer>(),0.0,0,0,0,0});

        }
    }
	
	public static List<Integer> extractAppropriatePosition(Map<String,Object[]> data, String group){

		List<Integer> numbers = new ArrayList<>();

		if( group.equals("1") ){

			Map.Entry<String,Object[]> res = data.entrySet().iterator().next();	

			return (List<Integer>) res.getValue()[0];

		}		

		for(Map.Entry<String,Object[]> positionData : data.entrySet()){

			if(positionData.getKey().equals(group)){

				numbers = (List<Integer>) positionData.getValue()[0];
				break;
			}
		}

		return numbers;
	}

	public static void formCompanionHitTrio( List<Integer> values)
    {
        int currentWinningNumber = values.get( values.size() - 1);
        int previousWinningNumber = values.get( values.size() - 2);
        Integer[] holder = {previousWinningNumber, currentWinningNumber};
        List<Integer[]> patternHolder = new ArrayList<>();

        Map<Integer[],Integer> f = new HashMap<>();
        f.put( new Integer[]{previousWinningNumber , currentWinningNumber}, 0);

        for(int i = 0; i < values.size() - 2; i++)
        {

            if(holder[0] == values.get(i) && holder[1] == values.get( i + 1)){

                Integer[] d = new Integer[holder.length + 1];
                d[0] = holder[0];
                d[1] = holder[1];
                d[2] = values.get(i + 2);
                patternHolder.add(d);
//
//                for(Map.Entry<Integer[],Integer> p : f.entrySet()){
//
//                    if(Arrays.equals( p.getKey(), holder)){
//
//                        Integer val = p.getValue();
//                        val++;
//
//                        f.put(p.getKey(), val );
//                    }
//                }

            }

        }
    }

	public static Object[] getRepeatedNumberList(List<Integer> values){

	    if(values.size() < 2)
	        return new Object[]{new ArrayList<>()};

	    int currentWinningNumber = values.get(values.size()-1);
	    List<Integer> list = new ArrayList<>();
        List<Integer> indexHolder = new ArrayList<>();
        Map<String,Integer[]> patternHolderMap = new LinkedHashMap<>();
        List<String> patterns = new ArrayList<>();

	    for(int i = 0; i < values.size()-1; i++){

	        if(values.get(i) == currentWinningNumber){

	            int nextWinningNumber = values.get(i +1);
	            String pattern = String.format("%s-%s",currentWinningNumber,nextWinningNumber);
	            patterns.add(pattern);

                list.add(currentWinningNumber);
                list.add(nextWinningNumber);
	            list.add(currentWinningNumber);
            }

        }

//        String[] output = {""};
//
//        String currentWinningPattern = patterns.get( patterns.size() - 1);
//        int[] indexes = IntStream.range(0, patterns.size() - 1).filter( pat -> patterns.get(pat).equals(currentWinningPattern)).toArray();
//
//        for(int indx : indexes){
//
//
//            String nextWinningPattern = patterns.get( indx + 1);
//
//            if(!patternHolderMap.containsKey(nextWinningPattern)){
//
//                patternHolderMap.put(nextWinningPattern, new Integer[]{1,0});
//                NumberPatternAnalyzer.incrementGamesOutForMatrix(patternHolderMap, nextWinningPattern);
//            }
//            else{
//
//                Integer[] data = patternHolderMap.get( nextWinningPattern );
//                data[0]++;
//                data[1] = 0;
//                NumberPatternAnalyzer.incrementGamesOutForMatrix(patternHolderMap, nextWinningPattern);
//
//            }
//        }
//
//        List<Map.Entry<String,Integer[]>> sorted = new ArrayList<>(patternHolderMap.entrySet());
//        sorted.sort( ((o1, o2) -> {
//
//            Integer o1Hits = o1.getValue()[0];
//            Integer o2Hits = o2.getValue()[0];
//
//            int result = o1Hits.compareTo( o2Hits );
//            if(result > 0){return -1;}
//            else if( result < 0){return 1;}
//            else {return 0;}
//
//        }));
//
//        patternHolderMap.clear();
//
//        sorted.forEach( val -> {
//
//            patternHolderMap.put(val.getKey(), val.getValue());
//        });
//
//
//        output[0] += String.format("%s %15s %20s","Pattern","Hits","Games Out");
//        patternHolderMap.forEach( (k,v) -> {
//
//            output[0] += String.format("\n%5s %15s %20s",k,v[0],v[1]);
//        });

        Object[] vals = new Object[]{list};

	    return vals;
    }

    private static void incrementApproriateRange(Map<String, Integer[]> gapSpacingRanges, int dif) {

	    gapSpacingRanges.forEach((k,v) -> {

	        List<Integer> list = Arrays.stream( k.split(",")).map(Integer::parseInt).collect(Collectors.toList());
	        if(list.contains(dif)){

	            v[0]++;
	            v[1] = 0;
                NumberPatternAnalyzer.incrementGamesOutForMatrix(gapSpacingRanges,k);

            }

        });
    }

    public static Map<String,Object[]> getPatternData(List<Integer> integers,String text) {

	    if(integers.size() < 2)
	        return new HashMap<>();

	    List<String> patterns = new LinkedList<>();

	    int min = 0;
	    int max = 0;
        int recentWinningDigit = integers.get(integers.size() - 1);
        //int prevWinningDigit = integers.get(integers.size() - 2);

        String[] values = getMinMax(text);

        min = Integer.parseInt(values[0]);
        max = Integer.parseInt(values[1]);

        for(int i = min; i <= max; i++)
        {
            patterns.add(recentWinningDigit + "-" + i);
        }

        Map<Integer,Integer[]> gameOutHitMap = new TreeMap<>();
        Map<String,Object[]> data = new LinkedHashMap<>();
        for(String string : patterns)
            data.put(string, new Object[]{0,0,0,0});

        for(int i = 0; i < integers.size() - 1; i++){

            if(integers.get(i) == recentWinningDigit){
                int nextNum = integers.get(i+1);
                String pattern = recentWinningDigit + "-" + nextNum;
                if(data.containsKey(pattern)){

                    Object[] value = data.get(pattern);
                    value[0] = (int) value[0] + 1;

                    if(!gameOutHitMap.containsKey(value[1])){

                        gameOutHitMap.put((Integer)value[1],new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(gameOutHitMap,(int)value[1]);
                    }
                    else{

                        Integer[] dataa = gameOutHitMap.get( value[1]);
                        dataa[0]++;
                        dataa[1] = 0;
                        NumberAnalyzer.incrementGamesOut(gameOutHitMap,(int)value[1]);
                    }
                    value[1] = 0;
                    NumberAnalyzer.incrementGamesOut(data,pattern);
                }
            }
        }

        // iterate
        data.forEach((k,v) -> {

            int gamesOut = (int)v[1];
            if(gameOutHitMap.containsKey(gamesOut)){

                Integer[] goOutData = gameOutHitMap.get(gamesOut);
                v[2] = goOutData[0];
                v[3] = goOutData[1];
            }
        });

	    return data;
    }

    public static String[] getMinMax(String text) {
        StringBuilder builder = new StringBuilder(text);
        int i = builder.indexOf("[");
        int ii = builder.indexOf("]");

        builder.setCharAt(i,' ');
        builder.setCharAt(ii,' ');
        String val = builder.toString();
        return val.trim().split("[\\s ,]+");
    }

}
