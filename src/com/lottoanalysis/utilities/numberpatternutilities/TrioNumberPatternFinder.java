package com.lottoanalysis.utilities.numberpatternutilities;

import java.util.*;

public class TrioNumberPatternFinder {

    private Set<Integer> numberSpanHolder;
    private List<Integer> winningLottoNumberHolder;
    private List<Range> rangeList = new ArrayList<>();
    private List<Integer[]> trioPatternHolder = new ArrayList<>();

    public TrioNumberPatternFinder( List<Integer> values )
    {
        winningLottoNumberHolder = new ArrayList<>(values);
        numberSpanHolder = new TreeSet<>( values );
        loadRanges();

        analyzeData();
    }

    private void analyzeData(){

        final int currentWinningNumber = winningLottoNumberHolder.get( winningLottoNumberHolder.size() - 1);
        final int previousWinningNumber = winningLottoNumberHolder.get( winningLottoNumberHolder.size() - 2);

        final int[] duoHolder = {previousWinningNumber, currentWinningNumber};

        for(int i = 0; i < winningLottoNumberHolder.size() - 2; i++){

            if(duoHolder[0] == winningLottoNumberHolder.get( i ) && duoHolder[1] == winningLottoNumberHolder.get(i+1)){

                Integer[] triHolder = new Integer[duoHolder.length + 1];
                triHolder[0] = duoHolder[0];
                triHolder[1] = duoHolder[1];
                triHolder[2] = winningLottoNumberHolder.get( i + 2);

                rangeList.forEach( range -> {

                    if(triHolder[2] >= range.getLowerAndUpperBounds().get(0) && triHolder[2] <= range.getLowerAndUpperBounds().get(1)){

                        int rangeHits = range.getHits();
                        range.setHit(++rangeHits);
                        range.setGamesOut( 0 );
                        System.out.println( Arrays.toString( triHolder ));
                        System.out.println( Arrays.toString( range.getLowerAndUpperBounds().toArray() ));
                        System.out.println( "Range Hits: " + rangeHits );
                        System.out.println( "Range Games Out: " + range.getGamesOut() );
                        System.out.println();

                        ;

                    }
                    else
                    {
                        int gamesOut = range.getGamesOut();
                        range.setGamesOut(++gamesOut);
                        System.out.println( Arrays.toString( range.getLowerAndUpperBounds().toArray() ));
                        System.out.println( "Range Hits: " + range.getHits() );
                        System.out.println( "Range Games Out: " + range.getGamesOut() );
                        System.out.println();
                    }
                });

                trioPatternHolder.add( triHolder );

            }
        }

        rangeList.forEach(range -> {
            //System.out.println( range.getGamesOut() );
            //range.insertGameOutLastSeen( range.getGamesOut() );
            //System.out.println( range.getGamesOut() );
        });
    }

    private void loadRanges(){
        int index = 1;
        for(int i = 1; i <= 100; i++){

            if( i % 5 == 0){

                rangeList.add( new Range(index, i));
                index = i + 1;
            }

        }
    }

    private class Range{

        private int lowerBound;
        private int upperBound;
        private int hits;
        private int gamesOut;
        private int gameOutHits;
        private int gameOutLastSeen;

        private Map<Integer,Integer[]> rangeGameOutMap = new TreeMap<>();
        private List<Integer> gameOutLastSeenHolder = new ArrayList<>();

        public Range( int lowerBound, int upperBound){

            this.lowerBound = lowerBound;
            this.upperBound = upperBound;

        }

        public List<Integer> getLowerAndUpperBounds(){

            return Arrays.asList( lowerBound, upperBound);
        }

        public void insertGameOutLastSeen(int gamesOutValue)
        {
            int lastSeen = getGameOutLastSeen();
            Integer[] gameOutHitData = rangeGameOutMap.get( gamesOutValue);
            gameOutHitData[2] = lastSeen;
        }

        // getters and setters
        public void setHit( int hits ){
            this.hits = hits;
        }
        public void setGamesOut( int gameOuts ){
            this.gamesOut = gameOuts;
            gameOutLastSeenHolder.add( this.gamesOut );

            if( !rangeGameOutMap.containsKey( this.gamesOut ))
            {
                rangeGameOutMap.put(this.gamesOut, new Integer[]{1, 0, 0});
            }
            else
            {
                Integer[] data = rangeGameOutMap.get( this.gamesOut );
                data[0]++;
                data[1] = 0;
            }

        }

        private int getGameOutLastSeen(){

            int lastSeen = Math.abs( gameOutLastSeenHolder.size() - gameOutLastSeenHolder.lastIndexOf( this.getGamesOut() ));

            return lastSeen;
        }

        public int getGamesOut(){
            return this.gamesOut;
        }
        public int getHits(){
            return this.hits;
        }

        public int getGameOutHits(){
            if( rangeGameOutMap.containsKey( this.gamesOut ) ){
                return rangeGameOutMap.get( this.gamesOut )[0];
            }

            return -1;
        }
    }
}

