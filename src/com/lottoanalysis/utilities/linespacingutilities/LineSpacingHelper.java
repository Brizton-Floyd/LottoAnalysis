package com.lottoanalysis.utilities.linespacingutilities;

import java.util.*;

/**
 * This class will aid in analyzing the spacing between all winning lottery numbers
 *
 */
public class LineSpacingHelper {

    private static Map<Integer[],Map<String,Integer[]>> lineSpacingHolder = new LinkedHashMap<>();
    private static List<String> lineSpacingSequences = new ArrayList<>();
    private static Map<Integer,Integer[]> hitLengthHolder = new TreeMap<>();
	private static Map<Integer,Map<Integer,Integer[]>> firstDigitHolder = new TreeMap<>();
    public static int num = 1;

    public static void determineMostProbableLineSpacing( List<Integer> numbers ){

        Integer[] data = Arrays.stream( getDifferenceBetweenLastTwoWinningNumbers( numbers ) ).boxed().toArray(Integer[]::new);
        lineSpacingHolder.put( data, new LinkedHashMap<>() );
        populateLineSpacingSequences( numbers );
        determineWhatHitsWithKeyMostOften( lineSpacingHolder, numbers );
		
		// new method here that will try to predict which sequence to play
		
        print();
    }
    private static void print(){ 
        System.out.println("CHART# " + num);
        for( Map.Entry<Integer[],Map<String,Integer[]>> data : lineSpacingHolder.entrySet()){

            System.out.println("Key: " + Arrays.toString(data.getKey()) + "\n");

            for(Map.Entry<String,Integer[]> innerData : data.getValue().entrySet()){
                System.out.println("Sequence:  " + innerData.getKey() + "Hits and Games out: " + Arrays.toString(innerData.getValue()));
            }
            predict();
        }
        lineSpacingHolder.clear();
        System.out.println("\n");
    }
	private static void printTwo(){
		
	}
	private static void predict(){
		System.out.println("\nHit Length Due For Chart: " + num);
		hitLengthHolder.forEach( (key, value) -> { 
		    System.out.println("\nHitLength: " + key + "\tHit and Games Out: " + Arrays.toString(value) + "\n");
		    if( firstDigitHolder.containsKey( key ) ){
				System.out.println("First number hit sequence for line lenght: " + key + "\n");
				Map<Integer,Integer[]> data = firstDigitHolder.get( key );
				data.forEach( (keyTwo,valueTwo) -> {
					System.out.println( "Hit Beginning Digit: " + keyTwo + "\tDigit Hits and Games Out" + Arrays.toString(valueTwo) );
				});
			}
		} );
	}
	private static void determineWhatHitsWithKeyMostOften( Map<Integer[],Map<String,Integer[]>> lineSpacingHolder, List<Integer> numbers ){

        for(Map.Entry<Integer[],Map<String,Integer[]>> data : lineSpacingHolder.entrySet()){

            String dataKey = Arrays.toString( data.getKey() );

            for( int i = 0; i < lineSpacingSequences.size()-1; i++ ){

                String sequence = lineSpacingSequences.get( i );
                if( sequence.equals(dataKey) ){

                    String nextSequence = lineSpacingSequences.get( i + 1);
                    Map<String,Integer[]> dataValue = data.getValue();

                    if( !dataValue.containsKey( nextSequence ) ){
                        dataValue.put(nextSequence, new Integer[]{1,0});
                        
                        incrementGamesOut( dataValue, nextSequence);
                        
                    }else{
                        Integer[] innerValue = dataValue.get( nextSequence );
                        innerValue[0]++;
                        innerValue[1] = 0;
  
                        incrementGamesOut(dataValue, nextSequence);
                    }
					
					// get length
					Integer[] vals = dataValue.get( nextSequence );
					String len = Integer.toString( vals[0] ).length() + "";
					if( !hitLengthHolder.containsKey( Integer.parseInt( len ) )){
						hitLengthHolder.put( Integer.parseInt( len), new Integer[]{1, 0} );
						incrementForLineSpacingPrediction( hitLengthHolder, Integer.parseInt( len ) );
					}else{
						Integer[] dd = hitLengthHolder.get( Integer.parseInt( len ) );
						dd[1]=0;
						dd[0]++;
						incrementForLineSpacingPrediction( hitLengthHolder, Integer.parseInt( len ) );
					}
					
					analyzeFirstDigitOfLineLenght( hitLengthHolder );
                }
            }
        }

    }

	private static void analyzeFirstDigitOfLineLenght( Map<Integer,Integer[]> lineSpacingHolder){

		lineSpacingHolder.forEach( (key,value) -> {
			
			String len = Integer.toString( value[0] ).length() + "";
			int firstCharacterOfLenght = Character.getNumericValue( Integer.toString( value[0] ).toString().charAt(0));
			
			if( !firstDigitHolder.containsKey(Integer.parseInt(len))){
				
				firstDigitHolder.put( Integer.parseInt(len), new TreeMap<>() );
				
				Map<Integer,Integer[]> firstDigitHitHolder = firstDigitHolder.get( Integer.parseInt( len ) );
				
				if(!firstDigitHitHolder.containsKey( firstCharacterOfLenght ) ){
					firstDigitHitHolder.put( firstCharacterOfLenght, new Integer[]{1,0} );
				}else{
					Integer[] innerValue = firstDigitHitHolder.get( firstCharacterOfLenght );
					innerValue[0]++;
					innerValue[1] = 0;
					// create a gamesoutview incremented for innerValue map
					incrementGamesOut(firstDigitHitHolder, firstCharacterOfLenght );
			    }
				
			}else{
				Map<Integer,Integer[]> valueData = firstDigitHolder.get( Integer.parseInt( len ) );
				if(!valueData.containsKey( firstCharacterOfLenght ) ) {
					valueData.put( firstCharacterOfLenght, new Integer[]{1,0});
				}else{
					Integer[] dd = valueData.get( firstCharacterOfLenght );
					dd[0]++;
					dd[1] = 0;
					// create a gamesoutview incremented for innerValue map
					incrementGamesOut( valueData, firstCharacterOfLenght );
				}
			}
		});
	}
	private static void incrementGamesOut(Map<Integer,Integer[]> data, int number){
		
		data.forEach( (key,value) -> {
			
			if( key != number ){
				value[1]++;
			}
		});
	}
	private static void incrementForLineSpacingPrediction(Map<Integer,Integer[]> lineLengths, int len){

		lineLengths.forEach( (key, value) -> {
			
			if( key != len ){
				value[1]++;
			}
			
		});
	}


    public static Map<Integer[],Map<String,Integer[]>> getLineSpacingHolder(){
        return lineSpacingHolder;
    }

    private static void incrementGamesOut(Map<String,Integer[]> values, String winningSequence){

        for(Map.Entry<String,Integer[]> valueEntries : values.entrySet()){

            if(!valueEntries.getKey().equals( winningSequence )){
                Integer[] data = valueEntries.getValue();
                data[1]++;
            }
        }
        
    }

    private static void populateLineSpacingSequences(List<Integer> numbers ) {

        int difOne, difTwo;

        for(int i = 1; i < numbers.size(); i++){
            String recentNum = Integer.toString( numbers.get( i ) );
            String priorNum =  Integer.toString( numbers.get( i - 1 ) );

            if( recentNum.length() == 2 && priorNum.length() == 2 ){
                difOne = Math.abs( Character.getNumericValue( recentNum.charAt(0)) - Character.getNumericValue( priorNum.charAt(0) ) );
                difTwo = Math.abs( Character.getNumericValue( recentNum.charAt(1)) - Character.getNumericValue( priorNum.charAt(1) ) );

                lineSpacingSequences.add(Arrays.toString(new Integer[]{difOne,difTwo}));
            }
            else{

                if( recentNum.length() == 1 ){
                    recentNum = "0"+recentNum;
                }
                if( priorNum.length() == 1 ){
                    priorNum = "0"+priorNum;
                }
                difOne = Math.abs( Character.getNumericValue( recentNum.charAt(0)) - Character.getNumericValue( priorNum.charAt(0) ) );
                difTwo = Math.abs( Character.getNumericValue( recentNum.charAt(1)) - Character.getNumericValue( priorNum.charAt(1) ) );

                lineSpacingSequences.add(Arrays.toString(new Integer[]{difOne,difTwo}));
            }
        }
    }

    private static int[] getDifferenceBetweenLastTwoWinningNumbers( List<Integer> numbers ){

        String recentNum = Integer.toString( numbers.get( numbers.size() - 1 ) );
        String priorNum =  Integer.toString( numbers.get( numbers.size() - 2 ) );
        int difOne, difTwo;

        if( recentNum.length() == 2 && priorNum.length() == 2 ){
            difOne = Math.abs( Character.getNumericValue( recentNum.charAt(0)) - Character.getNumericValue( priorNum.charAt(0) ) );
            difTwo = Math.abs( Character.getNumericValue( recentNum.charAt(1)) - Character.getNumericValue( priorNum.charAt(1) ) );

            return new int[]{difOne, difTwo};
        }
        else{

            if( recentNum.length() == 1 ){
                recentNum = "0"+recentNum;
            }
            if( priorNum.length() == 1 ){
                priorNum = "0"+priorNum;
            }
            difOne = Math.abs( Character.getNumericValue( recentNum.charAt(0)) - Character.getNumericValue( priorNum.charAt(0) ) );
            difTwo = Math.abs( Character.getNumericValue( recentNum.charAt(1)) - Character.getNumericValue( priorNum.charAt(1) ) );

            return new int[]{difOne, difTwo};
        }
    }
}
