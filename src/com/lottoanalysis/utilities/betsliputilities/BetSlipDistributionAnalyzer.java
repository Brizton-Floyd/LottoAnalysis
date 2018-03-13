package com.lottoanalysis.utilities.betsliputilities;

import java.util.*;

/**
 * 
 *
 */
@SuppressWarnings("unchecked")
public class BetSlipDistributionAnalyzer{

    private Integer formatHits; 
    private Integer formatGamesOut;
    private Map<String,Integer[]> winningNumberDistributionHolder;

    public BetSlipDistributionAnalyzer(int formatHits, int formatGamesOut, Map<String, Integer[]> winningNumberDistributionHolder){

        this.formatHits = formatHits; 
        this.formatGamesOut = formatGamesOut; 
        this.winningNumberDistributionHolder = winningNumberDistributionHolder; 
    }

    // setters
    public void setFormatHits( int val ){
        this.formatHits = val;
    }

    public void setFormatGamesOut( int val ){
        this.formatGamesOut = val;
    }

    public void setWinningNumberDistributionHolder( Map<String, Integer[]> winningNumberDistributionHolder){
        this.winningNumberDistributionHolder = winningNumberDistributionHolder;
    }

    // Getters 
    public Integer getFormatHits(){
        return this.formatHits; 
    }

    public Integer getFormatGamesOut(){
        return this.formatGamesOut; 
    }

    public Map<String,Integer[]> getWinningNumberDistributionHolder(){
        return this.winningNumberDistributionHolder;
    }

    /*
    *
    * 
    */
    public void incrementGamesOutForAllOtherFormatOccurences( Map<String, BetSlipDistributionAnalyzer> formats, String currentFormat){

        formats.forEach( (key,value) -> {

            if(!key.equals(currentFormat)){

                BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = value;
                int gamesOut = betSlipDistributionAnalyzer.getFormatGamesOut();
                betSlipDistributionAnalyzer.setFormatGamesOut( ++gamesOut );
            }
        });
    }
    /*
    *
    * 
    */
    public void incrementGamesOutForNonWinning(Map<String, BetSlipDistributionAnalyzer> formats,
                                               String winningDistro){

        formats.forEach( (k,v) -> {

            BetSlipDistributionAnalyzer betSlipDistributionAnalyzer = v;
            Map<String,Integer[]> data = betSlipDistributionAnalyzer.getWinningNumberDistributionHolder();

            for(Map.Entry<String, Integer[]> values : data.entrySet()){

                if(!values.getKey().equals(winningDistro)){

                    Integer[] vals = values.getValue(); 
                    vals[1]++;
                }
            }
        });

    }
}
