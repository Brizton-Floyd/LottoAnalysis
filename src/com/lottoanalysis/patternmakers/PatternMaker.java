package com.lottoanalysis.patternmakers;

import com.lottoanalysis.interfaces.MapValueEntry;
import com.lottoanalysis.lottogames.LottoGame;
import java.util.*;

public abstract class PatternMaker implements MapValueEntry<List<String>> {

    private Map<Integer, Object[]> lottoNumberPatternMap = new TreeMap<>();
    private List<List<String>> mapValues = new LinkedList<>();

    public abstract void constructGameOutHitPattern(LottoGame game);

    public Map<Integer, Object[]> getLottoNumberPatternMap() {
        return lottoNumberPatternMap;
    }

    @SuppressWarnings("unchecked")
    public void extractValuesFromMap(){

        lottoNumberPatternMap.forEach((k,v) -> {

            mapValues.add( (List<String>)v[1] );
        });
    }

    @Override
    public List<List<String>> getValue() {
        return mapValues;
    }
}
