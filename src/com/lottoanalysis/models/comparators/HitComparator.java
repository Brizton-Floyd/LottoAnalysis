package com.lottoanalysis.models.comparators;

import java.util.Comparator;
import java.util.Map;

public class HitComparator implements Comparator<Map.Entry<Integer,Integer[]>> {
    @Override
    public int compare(Map.Entry<Integer, Integer[]> o1, Map.Entry<Integer, Integer[]> o2) {
        Integer o1Hits = o1.getValue()[0];
        Integer o2Hits = o2.getValue()[0];

        int result = o1Hits.compareTo( o2Hits );
        if(result > 0){return -1;}
        else if( result < 0){return 1;}
        else {return 0;

        }

    }
}
