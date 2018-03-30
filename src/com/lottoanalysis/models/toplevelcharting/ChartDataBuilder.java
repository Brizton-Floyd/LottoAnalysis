package com.lottoanalysis.models.toplevelcharting;

import com.lottoanalysis.comparators.ChartDataComparator;

import java.util.*;

public class ChartDataBuilder {

    private int[] positionalWinningNumbers;
    private Map<Set<Integer>, ChartData> lastDigitMapHolder = new LinkedHashMap<>();

    public ChartDataBuilder( int[] positionalWinningNumbers ) {

        this.positionalWinningNumbers = positionalWinningNumbers;

        lastDigitMapHolder.put(new TreeSet<>(Arrays.asList(0, 1, 2, 3)), new ChartData());
        lastDigitMapHolder.put(new TreeSet<>(Arrays.asList(4, 5, 6)), new ChartData());
        lastDigitMapHolder.put(new TreeSet<>(Arrays.asList(7, 8, 9)), new ChartData());

        analyzeDrawData();
        buildCompanionNumberMapForRecentWinningDigit( lastDigitMapHolder );
        findHitsAtGamesOut();
        sortData();

        printData();
    }

    private void printData() {

        int[] count = {0};
        String[] groups = {"Hot","Warm","Cold"};
        lastDigitMapHolder.forEach( (k,v)->{

            System.out.printf("\n%s Group %12s %4s %18s %4s %18s %4s %18s %4s",groups[count[0]++],"Hits:",v.getHits(),"Games Out:",v.getGamesOut(),"Hits @ G Out",
                    v.getHitsAtGamesOut(),"Last Seen",v.getLastSeen());

            CompanionNumberTracker companionNumberTracker = v.getCompanionNumberTracker();
            Map<Integer,Map<Integer,CompanionNumberTracker.CompanionNumberData>> data = companionNumberTracker.getCompanionNumberTracker();
            data.forEach((key,value) -> {

                System.out.printf("\nRECENT WINNING LOTTERY NUMBER: %s\n", key);
                value.forEach((keyTwo,valueTwo) -> {
                    System.out.printf("\n\t\tCompanion Lottery Number: %2s %12s %3s %15s %3s\n", keyTwo,"Hits:",valueTwo.getHits(),"Games Out:",valueTwo.getGamesOut());
                });

            });

            GameHitLenghtTracker gameHitLenghtTracker = companionNumberTracker.getGameHitLenghtTracker();
            Map<Integer,GameHitLenghtTracker.LenTrackerData> gameLenData = gameHitLenghtTracker.getLengthTracker();
            gameLenData.forEach((keyTwo,valueTwo) -> {
                System.out.printf("\n\t\tHit Length %s %10s %3s %15s %3s\n",keyTwo,"Hits:",valueTwo.getHits(),"Games Out:",valueTwo.getGamesOut() );
                System.out.printf("\n\t\tRemainder Performance Outlook For Hit Length: %s\n",keyTwo);
                GameHitLenghtTracker.LenTrackerData.LenRemainderTracker tracker = valueTwo.getLenRemainderTracker();
                Map<Integer,Integer[]> remainderData = tracker.getHitValueMap();
                remainderData.forEach( (keyThree,valueThree) -> {

                    System.out.printf("\n\t\t\t\tRemainder: %s %10s %3s %15s %3s\n",keyThree,"Hits:",valueThree[0],"Games Out:",valueThree[1]);

                });
            });
        });
    }

    private void sortData() {
        List<Map.Entry<Set<Integer>, ChartData>> sortedData = new ArrayList<>(lastDigitMapHolder.entrySet());
        sortedData.sort(new ChartDataComparator());

        lastDigitMapHolder.clear();
        for(Map.Entry<Set<Integer>,ChartData> dataEntry : sortedData){

            lastDigitMapHolder.put(dataEntry.getKey(),dataEntry.getValue());
        }

        lastDigitMapHolder.forEach((k,v) -> {

            CompanionNumberTracker companionNumberTracker = v.getCompanionNumberTracker();
            companionNumberTracker.sortMapData( v.companionNumberTracker.getCompanionNumberTracker() );
        });
    }

    private void findHitsAtGamesOut() {

        lastDigitMapHolder.forEach( (k,v) -> {

            List<Integer> gameOutHolder = v.getGameOutHolder();
            long hits  = gameOutHolder.stream().filter( i -> i == v.getGamesOut()).count();
            v.setHitsAtGamesOut((int)hits);

            int lastSeen = Math.abs( gameOutHolder.size() - gameOutHolder.lastIndexOf( v.getGamesOut() ));
            v.setLastSeen(lastSeen);
        });
    }

    private void buildCompanionNumberMapForRecentWinningDigit( Map<Set<Integer>, ChartData> lastDigitMapHolder ) {

        lastDigitMapHolder.forEach( (k,v) -> {

            List<Integer> winningNumbers = v.getWinningLottoNumberHolder();
            v.getCompanionNumberTracker().findCompanionNumberForRecentWinningNumber( winningNumbers );

        });
    }

    private void analyzeDrawData() {

        for(int lottoNumber : positionalWinningNumbers){

            String lottoNumberAsString = lottoNumber + "";
            int digit = (lottoNumberAsString.length() > 1) ? Character.getNumericValue(lottoNumberAsString.charAt(1)) :
                                                             Character.getNumericValue(lottoNumberAsString.charAt(0));

            for(Map.Entry<Set<Integer>,ChartData> data : lastDigitMapHolder.entrySet()){

                if(data.getKey().contains(digit)){

                    ChartData chartData = data.getValue();
                    int hits = chartData.getHits();
                    chartData.setHits(++hits);
                    chartData.gameOutHolder.add( chartData.getGamesOut() );
                    chartData.setGamesOut(0);
                    chartData.addWinningLottoNumber(lottoNumber);

                    Integer[] d = Arrays.stream( data.getKey().toArray() ).toArray(Integer[]::new);
                    incrementGamesOut(lastDigitMapHolder, d);
                    break;
                }
            }
        }
    }

    private void incrementGamesOut(Map<Set<Integer>,ChartData> data, Integer[] array){

        data.forEach((k,v) -> {

            Integer[] arr = Arrays.stream(k.toArray()).toArray(Integer[]::new);
            if(!Arrays.equals(arr,array)){

                int gamesOut = v.getGamesOut();
                v.setGamesOut( ++gamesOut );
            }
        });
    }

    public class ChartData {

        private int hits;
        private int gamesOut;
        private int hitsAtGamesOut;
        private int lastSeen;
        private List<Integer> gameOutHolder = new ArrayList<>();
        private List<Integer> winningLottoNumberHolder = new ArrayList<>();

        private CompanionNumberTracker companionNumberTracker = new CompanionNumberTracker();

        public CompanionNumberTracker getCompanionNumberTracker() {
            return companionNumberTracker;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }

        public int getGamesOut() {
            return gamesOut;
        }

        public void setGamesOut(int gamesOut) {
            this.gamesOut = gamesOut;
        }

        public int getHitsAtGamesOut() {
            return hitsAtGamesOut;
        }

        public void setHitsAtGamesOut(int hitsAtGamesOut) {
            this.hitsAtGamesOut = hitsAtGamesOut;
        }

        public int getLastSeen() {
            return lastSeen;
        }

        public void setLastSeen(int lastSeen) {
            this.lastSeen = lastSeen;
        }

        public List<Integer> getGameOutHolder() {
            return gameOutHolder;
        }

        public void setGameOutHolder(List<Integer> gameOutHolder) {
            this.gameOutHolder = gameOutHolder;
        }

        public List<Integer> getWinningLottoNumberHolder() {
            return winningLottoNumberHolder;
        }

        public void addWinningLottoNumber(int winningLottoNumberHolder) {
            this.winningLottoNumberHolder.add(winningLottoNumberHolder);
        }
    }
}
