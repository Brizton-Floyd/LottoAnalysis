package com.lottoanalysis.models.drawhistory;

import com.lottoanalysis.lottogames.LottoGame;

import java.util.*;
import java.util.stream.Collectors;

public class LottoNumberGameOutTracker {

    private int lottoNumberGamesOut;
    private int lottoNumberPositionHits;
    private int lottoNumberGamesOutInPosition;
    private Map<Integer,LottoNumberGameOutTracker> lottoNumberGameOutInfoMap = new LinkedHashMap<>();
    private static Map<Integer,LottoNumberGameOutTracker> mainLottoGameOutTracker = new LinkedHashMap<>();
    private List<Integer> gameOutHitHolder = new ArrayList<>();

    // Getters and Setters
    public  List<Integer> getGameOutHitHolder() {
        return gameOutHitHolder;
    }

    public int getLottoNumberGamesOut() {
        return lottoNumberGamesOut;
    }

    private void setLottoNumberGamesOut(int lottoNumberGamesOut) {
        this.lottoNumberGamesOut = lottoNumberGamesOut;
    }

    public int getLottoNumberPositionHits() {
        return lottoNumberPositionHits;
    }

    private void setLottoNumberPositionHits(int lottoNumberPositionHits) {
        this.lottoNumberPositionHits = lottoNumberPositionHits;
    }

    public int getLottoNumberGamesOutInPosition() {
        return lottoNumberGamesOutInPosition;
    }

    private void setLottoNumberGamesOutInPosition(int lottoNumberGamesOutInPosition) {
        this.lottoNumberGamesOutInPosition = lottoNumberGamesOutInPosition;
    }

    public Map<Integer, LottoNumberGameOutTracker> getLottoNumberGameOutInfoMap() {
        return lottoNumberGameOutInfoMap;
    }

    private void setLottoNumberGameOutInfoMap(Map<Integer, LottoNumberGameOutTracker> lottoNumberGameOutInfoMap) {
        this.lottoNumberGameOutInfoMap = lottoNumberGameOutInfoMap;
    }

    // Methods
    public void analyzeGamesOutForLottoNumbers(final int[][] lottoNumbers, final LottoGame lottoGame){

        populateLottoNumberInfoMap( lottoGame );

        for(int i = 0; i < lottoNumbers[0].length; i++)
        {
            final Integer[] drawResults = new Integer[lottoNumbers.length];
            for( int j = 0; j < lottoNumbers.length; j++)
            {
                drawResults[j] = lottoNumbers[j][i];

            }

            incrementGamesOutForAppropriateLottoNumbers( drawResults);
        }
    }

    private void incrementGamesOutForAppropriateLottoNumbers(Integer[] drawResults) {

        final List<Integer> currentWinningNumbers = Arrays.stream( drawResults ).collect(Collectors.toList());

        lottoNumberGameOutInfoMap.forEach( (number, data) -> {

            if( !currentWinningNumbers.contains( number ) )
            {
                LottoNumberGameOutTracker lottoNumberGameOutTracker = data;
                int gamesOut = lottoNumberGameOutTracker.getLottoNumberGamesOut();
                lottoNumberGameOutTracker.setLottoNumberGamesOut(++gamesOut);
            }
            else{
                gameOutHitHolder.add(data.getLottoNumberGamesOut());
                data.setLottoNumberGamesOut(0);

            }
        });
    }

    private void populateLottoNumberInfoMap(LottoGame lottoGame) {

        lottoNumberGameOutInfoMap.clear();

        final int min = 0;
        final int max = lottoGame.getMaxNumber();

        for(int i = min; i <= max; i++)
        {
            lottoNumberGameOutInfoMap.put(i, new LottoNumberGameOutTracker());
        }
    }

    public void getLottoNumbersBasedOnGameSpan( final int gameSpan )
    {

        //List<Map.Entry<Integer,LottoNumberGameOutTracker>> lottoNumberGameOutEntrySet = new ArrayList<>(lottoNumberGameOutInfoMap.entrySet());
        List<Map.Entry<Integer,LottoNumberGameOutTracker>> modifiedEntries = lottoNumberGameOutInfoMap.entrySet().stream()
                                                                                .sorted(Map.Entry.comparingByValue(
                                                                                        (v1,v2) -> v2.getLottoNumberPositionHits() - v1.getLottoNumberPositionHits())
                                                                                )
                                                                                .filter( data -> data.getValue().getLottoNumberGamesOut() <= gameSpan )
                                                                                .collect(Collectors.toList());

        lottoNumberGameOutInfoMap.clear();

        modifiedEntries.forEach( v -> {
            lottoNumberGameOutInfoMap.put( v.getKey(), v.getValue());
        });
    }

    public void analyzePositionalHitsAndGamesOut(int[] historicalDrawData) {


        for(Map.Entry<Integer,LottoNumberGameOutTracker> entrySet : lottoNumberGameOutInfoMap.entrySet())
        {

            final int hitCount = (int) Arrays.stream( historicalDrawData ).boxed().filter(number  -> number == entrySet.getKey()).count();
            entrySet.getValue().setLottoNumberPositionHits(hitCount);

            final List<Integer> lottoNumberNumericValues = Arrays.stream(historicalDrawData).boxed().collect(Collectors.toList());
            final int lastSeen = Math.abs( lottoNumberNumericValues.lastIndexOf(entrySet.getKey()) - lottoNumberNumericValues.size() )-1;
            entrySet.getValue().setLottoNumberGamesOutInPosition(lastSeen);
        }

    }

    public void extractNumbersBasedOnGameSpan(List<Integer> multipleHolderList, int gameSpan) {

        Set<Integer> validNumbers = new HashSet<>(multipleHolderList);

        for(Iterator<Integer> iterator = validNumbers.iterator(); iterator.hasNext();)
        {
            final int val = iterator.next();
            for(Map.Entry<Integer,LottoNumberGameOutTracker> lottoNumberGameOutTrackerEntry : mainLottoGameOutTracker.entrySet()){

                if( val == lottoNumberGameOutTrackerEntry.getKey() && lottoNumberGameOutTrackerEntry.getValue().getLottoNumberGamesOut() > gameSpan)
                {
                    iterator.remove();
                }
            }
        }


        multipleHolderList.removeIf( value -> !validNumbers.contains(value));
    }

    public void extractNumbersNotMeetingGameSpanCriteria(Collection<SumGroupAnalyzer> sumGroupAnalyzers, int gameSpan) {

        Set<Integer> numberHolder;
        for(SumGroupAnalyzer sumGroupAnalyzer : sumGroupAnalyzers){
            // Make all numbers in hit list unique by adding to set
            numberHolder = new HashSet<>(sumGroupAnalyzer.getLottoNumberInSumRangeHolder());
            int size = 0;
            final Iterator<Integer> numHolderIterator = numberHolder.iterator();
            while (numHolderIterator.hasNext()){

                final int number = numHolderIterator.next();
                if(!lottoNumberGameOutInfoMap.containsKey(number)){
                    sumGroupAnalyzer.getLottoNumberInSumRangeHolder().removeIf(val -> Objects.equals(val, number));
                }
            }
        }
    }
}
