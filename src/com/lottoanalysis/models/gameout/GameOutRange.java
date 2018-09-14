package com.lottoanalysis.models.gameout;

import java.util.*;

public class GameOutRange extends Range {

    private List<List<String>> lottoNumberHitDistrubutions;
    private List<Integer[]> rows = new ArrayList<>();
    private GameOut gameOut = new GameOut(true);

    private GameOutRange() {
    }

    private GameOutRange(int range) {
        super(range, 0, 100);
    }

    GameOutRange(List<List<String>> lottoNumberHitDistrubutions) {
        this(20);

        this.lottoNumberHitDistrubutions = lottoNumberHitDistrubutions;
    }

    @Override
    public void analyze() {

        computeRangeUpperLowerBound();
        convertListToRows(lottoNumberHitDistrubutions);
        computeGameOutRangeHits();
        computeHitsAtGamesOut();
        findLastOccurenceOfGameOut();
    }

    @Override
    protected void computeRangeUpperLowerBound() {

        for (int i = getMinNumber(); i <= getMaxNumber(); i++) {

            GameOutRange groupRange = new GameOutRange(getRange());
            groupRange.setLowerBound(i);
            groupRange.setUpperBound((i + getRange()) - 1);
            groupRange.setIndex();
            getRanges().add(groupRange);

            setMinNumber(groupRange.getUpperBound());
            i = getMinNumber();
        }

        validateUpperBoundsForOverflow();

        GameOutRange gameOutRange = (GameOutRange) getRanges().get( getRanges().size() -1);
        GameOutRange gameOutRangeTwo = new GameOutRange(getRange());
        gameOutRangeTwo.setIndex();
        gameOutRangeTwo.setLowerBound( gameOutRange.getUpperBound() + 1);

        getRanges().add( gameOutRangeTwo );

        super.resetLowerUpperBound();
    }

    private void convertListToRows(List<List<String>> lottoNumberHitDistrubutions) {

        System.out.println("Inside Convert List to Rows Method");
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < lottoNumberHitDistrubutions.get(0).size(); i++) {

            Integer[] row = new Integer[lottoNumberHitDistrubutions.size()];

            for (int j = 0; j < lottoNumberHitDistrubutions.size(); j++) {
                String gameOutPattern = lottoNumberHitDistrubutions.get(j).get(i);
                if (gameOutPattern.contains("##")) {
                    row[j] = 0;
                } else if (gameOutPattern.startsWith("P") && !gameOutPattern.contains("##")) {
                    row[j] = -1;
                } else {
                    row[j] = Integer.parseInt(gameOutPattern);
                }
            }
            rows.add(row);
        }
        long stopTime = System.currentTimeMillis();
        long elap = stopTime - startTime;
        System.out.println("Time in MilliSeconds: " + elap);
    }

    private void computeGameOutRangeHits() {

        System.out.println("Inside Compute Game Out Range Hits");
        long startTime = System.currentTimeMillis();
        List<List<Integer>> previousRowHolder = new ArrayList<>();

        for (int i = 0; i < rows.size(); i++) {

            List<Integer> row = new ArrayList<>(Arrays.asList(rows.get(i)));
            if (row.contains(0) && previousRowHolder.size() > 0) {
                final int index = row.indexOf(0);

                List<Integer> row2 = previousRowHolder.get(i - 1);
                final int previousValueAtIndex = row2.get(index);

                incrementHitsForAppropriateRange(previousValueAtIndex);
                gameOut.incrementLoosingGameOutResetCurrentGameOut(getRanges(), previousValueAtIndex);
                previousRowHolder.add(row);

            } else {
                if (row.contains(0)) {
                    incrementHitsForAppropriateRange(0);
                    gameOut.incrementLoosingGameOutResetCurrentGameOut(getRanges(), 0);
                }

                previousRowHolder.add(row);
            }
        }
        long stopTime = System.currentTimeMillis();
        long elap = stopTime - startTime;
        System.out.println("Time in MilliSeconds: " + elap);

    }

    public GameOut getGameOut() {
        return gameOut;
    }

    public class GameOut {

        private List<GameOut> gameOutList = new ArrayList<>();
        private List<Integer> gameOutHolder = new ArrayList<>();
        private int gameOutNumber;
        private int gamesOut;
        private int gameOutHits;
        private int gameOutLastSeen;

        GameOut() {
        }

        GameOut(boolean populate) {
            if (populate)
                populateList();
        }

        private void populateList() {

            for (int i = getMinNumber(); i <= getMaxNumber()+1; i++) {
                GameOut gameOut = new GameOut();
                gameOut.setGameOutNumber(i);
                gameOutList.add(gameOut);
            }
        }
        private int binarySearchGameOutList(List<GameOut> gameOuts,int min,int max, int key){

            if(max < min)
                return -1;

            int middle = (min + max) / 2;

            if(key == gameOuts.get(middle).gameOutNumber){
                return middle;
            }
            else if(key < gameOuts.get(middle).gameOutNumber){
                return binarySearchGameOutList(gameOuts, min, middle-1, key);
            }
            else{
                return binarySearchGameOutList(gameOuts, middle+1, max,key);
            }
        }
        void incrementLoosingGameOutResetCurrentGameOut(List<Range> ranges, int value) {

            for (Range range : ranges) {

                GameOutRange gameOutRange = (GameOutRange) range;
                List<GameOut> gameOutList = gameOutRange.getGameOut().getGameOutList();

                // The below logic will aid in determining if a new GameOut object should be added
                // to the appropriate range based on the value being passed in
                int max = gameOutList.size()-1;
                if(value >= max && gameOutRange.getUpperBound() == 0) {
                    int val = binarySearchGameOutList(gameOutList, 0, max, value);
                    if (val < 0) {
                        GameOut newGameOut = new GameOut();
                        newGameOut.setGameOutNumber(value);
                        gameOutList.add(newGameOut);
                        gameOutList.sort(Comparator.comparingInt(GameOut::getGameOutNumber));
                    }
                }

                List<GameOut> filteredList = new ArrayList<>();
                for (GameOut gameOut : gameOutList) {
                    if (gameOut.gameOutNumber >= range.getLowerBound() && gameOut.gameOutNumber <= range.getUpperBound()) {
                        filteredList.add(gameOut);
                    }
                    else if(gameOut.gameOutNumber >= range.getLowerBound() && range.getUpperBound()==0){
                        filteredList.add(gameOut);
                    }
                }
                incrementGameOutResetCurrentGameOut(filteredList, value);
            }
        }

        private void incrementGameOutResetCurrentGameOut(List<GameOut> list, int gameOutValue) {

            // LOOP for gameOut in gameOutList
            for (GameOut gameOut : list) {
                // IF gameOut.gameOutNumber == gameOutValue
                if (gameOut.getGameOutNumber() == gameOutValue) {
                    // SET hits = gameOut.getOutHits
                    int hits = gameOut.getGameOutHits();
                    // SET gameOut.setGameOutHits( ++hits )
                    gameOut.setGameOutHits(++hits);
                    // ADD gameOut.gameOutHolder.add( game.getGamesOut() )
                    gameOut.gameOutHolder.add(gameOut.getGamesOut());
                    // SET gameOut.gamesOut( 0 )
                    gameOut.setGamesOut(0);

                    //END IF
                }
                //ELSE
                else {
                    // SET gameOut = gameOut.getGamesOut
                    int out = gameOut.getGamesOut();
                    // SET gameOut.setGamesOut( ++gameOut )
                    gameOut.setGamesOut(++out);
                    // ADD gameOut.gameOutHolder.add( game.getGamesOut() )
                    gameOut.getGameOutHolder().add(gameOut.getGamesOut());
                    //END ELSE
                }
                //END LOOP
            }

        }

        public List<Integer> getGameOutHolder() {
            return gameOutHolder;
        }

        public List<GameOut> getGameOutList() {
            return gameOutList;
        }

        public int getGameOutNumber() {
            return gameOutNumber;
        }

        void setGameOutNumber(int gameOutNumber) {
            this.gameOutNumber = gameOutNumber;
        }

        public int getGamesOut() {
            return gamesOut;
        }

        public void setGamesOut(int gamesOut) {
            this.gamesOut = gamesOut;
        }

        public int getGameOutHits() {
            return gameOutHits;
        }

        void setGameOutHits(int gameOutHits) {
            this.gameOutHits = gameOutHits;
        }

        public int getGameOutLastSeen() {
            return gameOutLastSeen;
        }

        public void setGameOutLastSeen(int gameOutLastSeen) {
            this.gameOutLastSeen = gameOutLastSeen;
        }
    }
}
