package com.lottoanalysis.models.gameout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GameOutRange extends Range {

    private List<List<String>> lottoNumberHitDistrubutions;
    private List<Integer[]> rows = new ArrayList<>();
    private GameOut gameOut = new GameOut(true);

    private GameOutRange(){}
    private GameOutRange(int range){
        super(range, 0, 100);
    }
    GameOutRange(List<List<String>> lottoNumberHitDistrubutions) {
        this(15);

        this.lottoNumberHitDistrubutions = lottoNumberHitDistrubutions;
    }

    @Override
    void analyze() {

        computeRangeUpperLowerBound();
        convertListToRows( lottoNumberHitDistrubutions );
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
        super.resetLowerUpperBound();
    }

    private void convertListToRows(List<List<String>> lottoNumberHitDistrubutions) {

        for(int i = 0; i < lottoNumberHitDistrubutions.get(0).size(); i++) {

            Integer[] row = new Integer[lottoNumberHitDistrubutions.size()];

            for(int j = 0; j < lottoNumberHitDistrubutions.size(); j++) {
                String gameOutPattern = lottoNumberHitDistrubutions.get(j).get(i);
                if(gameOutPattern.contains("##")) {
                    row[j] = 0;
                }
                else if(gameOutPattern.startsWith("P") && !gameOutPattern.contains("##")) {
                    row[j] = -1;
                }
                else {
                    row[j] = Integer.parseInt( gameOutPattern );
                }
            }
            rows.add( row );
        }
    }

    private void computeGameOutRangeHits() {

        for(int i = 1; i < rows.size(); i++) {
            List<Integer> list = new ArrayList<>(Arrays.asList(rows.get(i)));
            if(list.contains(0)) {
                final int index = list.indexOf(0);
                List<Integer> previousList = new ArrayList<>(Arrays.asList(rows.get(i-1)));
                int value = previousList.get( index );
                if(value == -1)
                    value = 0;
                incrementHitsForAppropriateRange( value );
                gameOut.incrementLoosingGameOutResetCurrentGameOut( getRanges(), value );
            }
        }
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

        GameOut(){}
        GameOut(boolean populate){
            if(populate)
                populateList();
        }

        private void populateList() {

            for(int i = getMinNumber(); i <= getMaxNumber(); i++){
                GameOut gameOut = new GameOut();
                gameOut.setGameOutNumber(i);
                gameOutList.add(gameOut);
            }
        }
        public void incrementLoosingGameOutResetCurrentGameOut(List<Range> ranges, int value) {

            for(Range range : ranges){

                GameOutRange gameOutRange = (GameOutRange)range;
                List<GameOut> gameOutList = gameOutRange.getGameOut().getGameOutList();
                List<GameOut> filteredList = new ArrayList<>();
                for(GameOut gameOut : gameOutList){
                    if(gameOut.gameOutNumber >= range.getLowerBound() && gameOut.gameOutNumber <= range.getUpperBound()){
                        filteredList.add(gameOut);
                    }
                }
                incrementGameOutResetCurrentGameOut(filteredList, value);
            }
        }
        private void incrementGameOutResetCurrentGameOut(List<GameOut> list, int gameOutValue){

            // LOOP for gameOut in gameOutList
            for(GameOut gameOut : list) {
                // IF gameOut.gameOutNumber == gameOutValue
                if(gameOut.getGameOutNumber() == gameOutValue) {
                    // SET hits = gameOut.getOutHits
                    int hits = gameOut.getGameOutHits();
                    // SET gameOut.setGameOutHits( ++hits )
                    gameOut.setGameOutHits(++hits);
                    // ADD gameOut.gameOutHolder.add( game.getGamesOut() )
                    gameOut.gameOutHolder.add( gameOut.getGamesOut() );
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
                    gameOut.getGameOutHolder().add( gameOut.getGamesOut() );
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
