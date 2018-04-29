package com.lottoanalysis.models.gapspacings;

import java.util.*;

public class GameOutSpacing {

    private static int idCounter = 1;

    private int id;
    private int winningLottoNumber;
    private double avgGameOutSpacing;
    private int gamesOut;
    private int hitsAtGamesOut;
    private int outLastSeen;
    private int hits;

    private List<Integer> gameOutHolder = new ArrayList<>();
    private List<Integer> gapSpacings = new ArrayList<>();
    private Map<String,GameOutSpacing> directionCountHolder = new LinkedHashMap<>();
    private static List<Integer> hitDirectionIdHolder = new ArrayList<>();

    public GameOutSpacing(boolean populateDirectionMap){
        if(populateDirectionMap) {
            populateDirectionsIntoMap();
            idCounter = 1;
        }
        else
        {

            this.id = idCounter;
            idCounter++;
        }
    }

    private void populateDirectionsIntoMap() {

        String[] directions = {"Above","Below"};
        for(int i = 0; i < directions.length; i++)
            directionCountHolder.put( directions[i], new GameOutSpacing(Boolean.FALSE));

    }

    public int getHits() {
        return hits;
    }

    public void setHits(int hits) {
        this.hits = hits;
    }

    public int getId() {
        return id;
    }

    public int getOutLastSeen() {
        return outLastSeen;
    }

    public void setOutLastSeen(int outLastSeen) {
        this.outLastSeen = outLastSeen;
    }

    public int getWinningLottoNumber() {
        return winningLottoNumber;
    }

    public void setWinningLottoNumber(int winningLottoNumber) {
        this.winningLottoNumber = winningLottoNumber;
    }

    public int getGamesOut() {
        return gamesOut;
    }

    public void setGamesOut(int gamesOut) {
        this.gamesOut = gamesOut;
    }

    public static List<Integer> getHitDirectionIdHolder() {
        return hitDirectionIdHolder;
    }

    public int getHitsAtGamesOut() {
        return hitsAtGamesOut;
    }

    public void setHitsAtGamesOut(int hitsAtGamesOut) {
        this.hitsAtGamesOut = hitsAtGamesOut;
    }

    public List<Integer> getGameOutHolder() {
        return gameOutHolder;
    }

    public void setGameOutHolder(List<Integer> gameOutHolder) {
        this.gameOutHolder = gameOutHolder;
    }

    public double getAvgGameOutSpacing() {
        return avgGameOutSpacing;
    }

    public void setAvgGameOutSpacing(double avgGameOutSpacing) {
        this.avgGameOutSpacing = avgGameOutSpacing;
    }

    public List<Integer> getGapSpacings() {
        return gapSpacings;
    }

    public void setGapSpacings(List<Integer> gapSpacings) {
        this.gapSpacings = gapSpacings;
    }

    public Map<String, GameOutSpacing> getDirectionCountHolder() {
        return directionCountHolder;
    }

    public void setDirectionCountHolder(Map<String, GameOutSpacing> directionCountHolder) {
        this.directionCountHolder = directionCountHolder;
    }

    public void populateDirectionMap(int[] numberHitIndexes, Integer winninLottoNumber, List<Integer> winningNumbers) {

        this.winningLottoNumber = winninLottoNumber;
        for(int i = 1; i < numberHitIndexes.length; i++){

            int dif = Math.abs(numberHitIndexes[i-1] - numberHitIndexes[i]);
            getGapSpacings().add(dif);
        }

        List<Integer>  spacingList = getGapSpacings();
        double avg = spacingList.stream().mapToDouble(num -> num).average().orElse(Double.NaN);
        setAvgGameOutSpacing(round(avg, 2));

        findHitDirection(numberHitIndexes, winningNumbers);
        findHitsAtGamesOut();
    }

    private void findHitsAtGamesOut() {

        getDirectionCountHolder().forEach((k,v) -> {

            int currentGamesOut = v.getGamesOut();
            Long hitsAtGames = v.getGameOutHolder().stream().filter(num -> num == currentGamesOut).count();
            v.setHitsAtGamesOut( hitsAtGames.intValue() );

            int lastSeen = Math.abs( v.getGameOutHolder().lastIndexOf(currentGamesOut) - v.getGameOutHolder().size());
            v.setOutLastSeen(lastSeen);
        });
    }

    private void findHitDirection(int[] indexes , List<Integer> winningNumbers) {

        final double currentAvg = getAvgGameOutSpacing();
        GameOutSpacing gameOutSpacing = null;
        getHitDirectionIdHolder().clear();

        for(int num  : indexes){

            int nextWinninNumber = winningNumbers.get(num + 1);
            if(nextWinninNumber > winningLottoNumber){

                gameOutSpacing = getDirectionCountHolder().get("Above");
                getHitDirectionIdHolder().add(gameOutSpacing.getId());
                int hits = gameOutSpacing.getHits();
                gameOutSpacing.setHits(++hits);

                gameOutSpacing.getGameOutHolder().add( gameOutSpacing.getGamesOut() );
                gameOutSpacing.setGamesOut(0);

                incrementGamesOut(getDirectionCountHolder(), "Below");
            }
            else if( nextWinninNumber <= winningLottoNumber)
            {

                gameOutSpacing = getDirectionCountHolder().get("Below");
                getHitDirectionIdHolder().add(gameOutSpacing.getId());

                int hits = gameOutSpacing.getHits();
                gameOutSpacing.setHits(++hits);

                gameOutSpacing.getGameOutHolder().add( gameOutSpacing.getGamesOut() );
                gameOutSpacing.setGamesOut(0);

                incrementGamesOut(getDirectionCountHolder(), "Above");

            }
//            else{
//
//                gameOutSpacing = getDirectionCountHolder().get("Equal");
//                getHitDirectionIdHolder().add(gameOutSpacing.getId());
//
//                int hits = gameOutSpacing.getHits();
//                gameOutSpacing.setHits(++hits);
//
//                gameOutSpacing.getGameOutHolder().add( gameOutSpacing.getGamesOut() );
//                gameOutSpacing.setGamesOut(0);
//
//                incrementGamesOut(getDirectionCountHolder(), "Above","Below");
//            }
        }
    }

    private void incrementGamesOut(Map<String,GameOutSpacing> gameOutSpacingMap, String... directions){

        for(String string : directions){

            GameOutSpacing gameOutSpacing = gameOutSpacingMap.get(string);
            int gamesOut = gameOutSpacing.getGamesOut();
            gameOutSpacing.setGamesOut(++gamesOut);
        }

    }
    private double round(double value, int places){

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);

        return (double) tmp / factor;
    }
}
