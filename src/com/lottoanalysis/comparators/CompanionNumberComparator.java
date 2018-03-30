package com.lottoanalysis.comparators;

import com.lottoanalysis.models.toplevelcharting.CompanionNumberTracker;

import java.util.Comparator;
import java.util.*;

public class CompanionNumberComparator implements Comparator<Map.Entry<Integer,CompanionNumberTracker.CompanionNumberData>> {

    @Override
    public int compare(Map.Entry<Integer, CompanionNumberTracker.CompanionNumberData> o1, Map.Entry<Integer, CompanionNumberTracker.CompanionNumberData> o2) {

        Integer o1Hits = o1.getValue().getHits();
        Integer o2Hits = o2.getValue().getHits();

        int result = o1Hits.compareTo( o2Hits );
        if(result > 0){return -1;}
        else if( result < 0){return 1;}
        else {return 0;}
    }
}
