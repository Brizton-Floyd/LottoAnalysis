package com.lottoanalysis.utilities.betsliputilities;

import java.util.*;

public class ColumnAndIndexHitAnalyzer {

    private Map<Integer, Object[]> columnIndexHolder = new TreeMap<>();
    private List<Map.Entry<Integer,Object[]>> entries = new ArraysList<>();

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
