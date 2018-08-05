package com.lottoanalysis.utilities.analyzerutilites;

import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.collections.ObservableList;

import java.util.*;
import java.util.function.Function;

public class DayGrouperAnalyzer {

    private Map<Integer,Map<String,Object[]>> data = new TreeMap<>();
    private List<List<Integer>> postionNumbers = new LinkedList<>();

    public void analzye(LottoGame game){

        fillMapData(game.getDrawingData());
        findAboveAndBelowAvgHits();
    }

    private void findAboveAndBelowAvgHits() {

        Function<List<Integer>,Integer> res = (list) -> {
            int sum = 0;
            for(int i : list){
                sum+= i;
            }
            return Math.abs( sum / list.size());
        };

        for(int i = 0; i < postionNumbers.size(); i++){

            int sum = 0;
            for(int k = 0; k < postionNumbers.get(i).size(); k++){
                sum += postionNumbers.get(i).get(k);
            }

            int avg = Math.abs( sum / postionNumbers.get(i).size() );

            Map<String,Object[]> d = data.get(i+1);
            d.forEach( (k,v) -> {

                List<Integer> dd = (ArrayList<Integer>)v[2];
                v[0] = avg;
                v[1] = res.apply(dd);
            });
        }
    }

    @SuppressWarnings("unchecked")
    private void fillMapData(ObservableList<Drawing> drawingData) {

        for(int i = 0; i < drawingData.get(0).getNums().length; i++){
            data.put((i+1),new HashMap<>());
            postionNumbers.add(new LinkedList<>());
        }

        for(int i = 0; i < drawingData.size(); i++){

            Drawing drawing = drawingData.get(i);
            String[] numbers = drawing.getNums();
            for(int k = 0; k < numbers.length; k++){

                List<Integer> p = postionNumbers.get(k);
                p.add(Integer.parseInt(numbers[k]));

                String dayOfWeek = drawing.getDrawDate().substring(0,drawing.getDrawDate().indexOf(".")).trim();
                Map<String,Object[]> positionData = data.get(k+1);

                if(!positionData.containsKey(dayOfWeek)){
                    positionData.put(dayOfWeek, new Object[]{0,0,new ArrayList<>()});
                    List<Integer> nums = (ArrayList<Integer>) positionData.get(dayOfWeek)[2];
                    nums.add(Integer.parseInt(numbers[k]));
                }
                else
                {
                    List<Integer> nums = (ArrayList<Integer>) positionData.get(dayOfWeek)[2];
                    nums.add(Integer.parseInt(numbers[k]));
                }
            }
        }
    }
}
