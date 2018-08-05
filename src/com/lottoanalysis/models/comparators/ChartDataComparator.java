package com.lottoanalysis.models.comparators;

import com.lottoanalysis.models.toplevelcharting.ChartDataBuilder;
import java.util.*;

import java.util.Comparator;

public class ChartDataComparator implements Comparator<Map.Entry<Set<Integer>,ChartDataBuilder.ChartData>> {


    @Override
    public int compare(Map.Entry<Set<Integer>, ChartDataBuilder.ChartData> o1, Map.Entry<Set<Integer>, ChartDataBuilder.ChartData> o2) {
        Integer o1Hits = o1.getValue().getHits();
        Integer o2Hits = o2.getValue().getHits();

        int result = o1Hits.compareTo( o2Hits );
        if(result > 0){return -1;}
        else if( result < 0){return 1;}
        else {return 0;}
    }
}
