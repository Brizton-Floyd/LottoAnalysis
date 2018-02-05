package com.lottoanalysis.companionnumbers.companionnumberhelpers;

import com.lottoanalysis.utilities.NumberAnalyzer;

import java.util.*;

@SuppressWarnings("unchecked")
public class CompanionNumberHelper {

    private static Map<String,Object[]> patternHolder = new LinkedHashMap<>();
    private static Map<Integer,String> columnHeaders = new TreeMap<>();
    private static List<List<String>> listHolder = new LinkedList<>();
    private static Map<Integer,Integer[]> gameOutHolderMap = new LinkedHashMap<>();

    public static Map<Integer, Integer[]> getGameOutHolderMap() {
        return gameOutHolderMap;
    }

    public static List<List<String>> getListHolder() {
        return listHolder;
    }

    public static Map<Integer, String> getColumnHeaders() {
        return columnHeaders;
    }

    public static Map<String, Object[]> getPatternHolder() {
        return patternHolder;
    }

    public static void analyze(int lottoDigit, List<Integer> currentPos, List<Integer> companionNumbers, List<Integer> pairingNumbers){

        clear();
        loadPatternIntoMap(lottoDigit, pairingNumbers);
        analyzePositionData(currentPos,companionNumbers,lottoDigit);
    }
    @SuppressWarnings("unchecked")
    private static void analyzePositionData(List<Integer> currentPos, List<Integer> companionNumbers, int lottoDigit) {


        for(int i = 0; i < currentPos.size(); i++){

            if(currentPos.get(i) == lottoDigit){
                int num = companionNumbers.get(i);
                String pattern = lottoDigit + "-" + num;
                if(patternHolder.containsKey(pattern)){
                    Object[] data = patternHolder.get(pattern);
                    data[0] = (int) data[0] + 1;

                    if(!gameOutHolderMap.containsKey((Integer)data[1])){

                        gameOutHolderMap.put((Integer)data[1],new Integer[]{1,0});
                        NumberAnalyzer.incrementGamesOut(gameOutHolderMap,(int)data[1]);
                    }
                    else{
                        Integer[] dataaa = gameOutHolderMap.get((Integer)data[1]);
                        dataaa[0]++;
                        dataaa[1] = 0;
                        NumberAnalyzer.incrementGamesOut(gameOutHolderMap,(int)data[1]);
                    }

                    data[1] = 0;

                    List<String> list = (LinkedList<String>)data[2];
                    String val = ((int)data[1] == 0) ? "X" : data[1] + "";
                    list.add(val);


                    NumberAnalyzer.incrementGamesOut(patternHolder, pattern);

                    patternHolder.forEach((k,v) -> {

                        if(!k.equals(pattern)){
                            ((List<String>)v[2]).add(v[1]+"");
                        }
                    });
                }
            }
        }

        patternHolder.forEach((k,v) -> {

            List<String> dd = (LinkedList<String>)v[2];
            addListToArray(dd);

            int currentGamesOut = (int)v[1];
            if(gameOutHolderMap.containsKey(currentGamesOut)){

                int hits = gameOutHolderMap.get(currentGamesOut)[0];
                int lastSeen = gameOutHolderMap.get(currentGamesOut)[1];

                v[3] = hits;
                v[4] = lastSeen;
            }
        });


    }

    private static void addListToArray(List<String> dd) {

        listHolder.add(dd);
    }

    private static void loadPatternIntoMap(int lottoDigit, List<Integer> pairingNumbers) {

        for(int i = 0; i < pairingNumbers.size(); i++)
        {
            String pattern = lottoDigit + "-" + pairingNumbers.get(i);
            patternHolder.put(pattern,new Object[]{0,0, new LinkedList<String>(),0,0});
            columnHeaders.put(i,pattern);
        }
    }

    private static void clear(){
        patternHolder.clear();
        listHolder.clear();
    }
}
