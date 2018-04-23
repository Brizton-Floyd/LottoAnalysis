package com.lottoanalysis.utilities.betsliputilities;

import com.lottoanalysis.models.numbertracking.FirstDigitTracker;

import java.util.*;

public class ColumnAndIndexHitAnalyzer {

    private Map<Integer, Object[]> columnIndexHolder = new TreeMap<>();
    private List<Map.Entry<Integer,Object[]>> entries = new ArrayList<>();
    private List<Integer> columnHitTracker = new ArrayList<>();
    private FirstDigitTracker firstDigitTracker = new FirstDigitTracker();
    private List<Integer> digitHolder = new ArrayList<>();

    public List<Integer> getColumnHitTracker() {
        return columnHitTracker;
    }

    public List<Integer> getDigitHolder() {
        return digitHolder;
    }

    public void setDigitHolder(int digit) {
        this.digitHolder.add(digit);
    }

    public FirstDigitTracker getFirstDigitTracker() {
        return firstDigitTracker;
    }

    public Map<Integer, Object[]> getColumnIndexHolder() {
        return columnIndexHolder;
    }

    public List<Map.Entry<Integer,Object[]>> getEntries(){

        return entries;
    }
    public void setColumnIndexHolder(Map<Integer, Object[]> columnIndexHolder) {
        this.columnIndexHolder = columnIndexHolder;
    }

    public void incrementGamesOutForNonWinningColumns(Map<Integer, Object[]> colAndIndexData, String columnIndex) {

        int col = Integer.parseInt(columnIndex);
        colAndIndexData.forEach( (k,v) -> {

            if( k != col){
                v[1] = (int)v[1] +1;
            }
        });
    }
}
