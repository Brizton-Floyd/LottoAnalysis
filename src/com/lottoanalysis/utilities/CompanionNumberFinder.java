/*
*
*/
public class CompanionNumberFinder{
	
	private static Map<Integer,Map<Integer,Integer[]>> companionNumberHitTrackerMap = new TreeMap<>();
	
	/*
	*
	*	Method will start the analyzing process for all incoming numbers
	*/
	public static void analyzeIncomingInformation( List<Integer> positionNumbers ){
		
		clearMap();
		findRecentWinningLottoNumber( positionNumbers );
		
	}
	
	private static void clearMap(){
		
		if(companionNumberHitTrackerMap.size() > 0){
			companionNumberHitTrackerMap.clear();
		}
	}
	private static void findRecentWinningLottoNumber( List<Integer> numbers ){
		
		final int recentWinningNumber = numbers.get( numbers.size() - 1 );
		
		plugCompanionNumbersIntoMap( recentWinningNumber, numbers );
	}
	
	private static void plugCompanionNumbersIntoMap(int recentWinningNumber, List<Integer> numbers){
		
		companionNumberHitTrackerMap.put(recentWinningNumber, new TreeMap<>() );
		
		for(int i = 0; i < numbers.size() - 1; i ++){
			
			if( numbers.get(i) == recentWinningNumber ){
				
				int companionNumber = numbers.get( i + 1 );
				Map<Integer,Integer[]> companionNumberData = companionNumberHitTrackerMap.get(recentWinningNumber);
				
				if( !companionNumberData.containsKey(companionNumber) ){
					
					companionNumberData.put(companionNumber, new Integer[]{1,0} );
					incrementGamesOut( companionNumberData, companionNumber );					
				}
				else{
					
					Integer[] companionNumberCountData = companionNumberData.get( companionNumber );
					companionNumberCountData[0]++;
					companionNumberCountData[1] = 0;
					incrementGamesOut( companionNumberData, companionNumber );
				}
			}
			
		}
		
	}
	
	private static void incrementGamesOut(Map<Integer,Integer[]> companionNumberData, int companionNumber){
		
		companionNumberData.forEach( (key,value) -> {
			
			if(key != companionNumber){
				value[1]++;
			}
		});
	}
}