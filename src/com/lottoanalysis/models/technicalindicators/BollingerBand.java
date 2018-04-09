package com.lottoanalysis.models.technicalindicators;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class BollingerBand {

    private List<List<Integer>> bollingerBands = new LinkedList<>();
    private Integer sizeLimit;

    public BollingerBand(List<Integer> lottoNumbers, int periods, Integer limit){

        this.sizeLimit = limit;

        loadBandsIntoList();
        operateOnData(lottoNumbers, periods, limit);
    }


    public List<List<Integer>> getBollingerBands() {

        List<List<Integer>> dataPoints = new LinkedList<>();

        for(Iterator<List<Integer>> values = bollingerBands.iterator(); values.hasNext();){

            List<Integer> list = values.next();
            if(list.size() > 0) {
                List<Integer> data = (list.size() > sizeLimit) ? list.subList(list.size() - sizeLimit, list.size()) : list;
                dataPoints.add(data);
            }
            else{
                values.remove();
            }
        }
        return dataPoints;
    }

    private void loadBandsIntoList() {

        for(int i = 0; i < 3; i++){

            bollingerBands.add( new ArrayList<>());
            bollingerBands.add( new ArrayList<>());
            bollingerBands.add( new ArrayList<>());

        }
    }

    private void operateOnData(List<Integer> lottoNumbers, final int periods, final Integer limit) {

        List<Integer> lottoNumberSpanHolder = new ArrayList<>();

        for(int i = 0; i < lottoNumbers.size(); i++){

            if( lottoNumberSpanHolder.size() >= periods) {
                int simpleMovingAverage = calculateSimpleMovingAverage(lottoNumberSpanHolder);
                Integer[] deviations = calculateDeviations(simpleMovingAverage, lottoNumberSpanHolder);

                loadValuesIntoList(simpleMovingAverage, deviations);

                lottoNumberSpanHolder.remove(0);
                lottoNumberSpanHolder.add( lottoNumbers.get(i) );
            }
            else
            {
                lottoNumberSpanHolder.add( lottoNumbers.get(i) );
            }
        }
    }

    private void loadValuesIntoList(int simpleMovingAverage, Integer[] deviations) {

        for(int i = 0; i < bollingerBands.size(); i++)
        {
            switch (i){

                case 0:
                    List<Integer> upperBandValues = bollingerBands.get( i );
                    upperBandValues.add( deviations[0] );
                    break;

                case 1:
                    List<Integer> smaValues = bollingerBands.get( i );
                    smaValues.add( simpleMovingAverage );
                    break;
                case 2:
                    List<Integer> lowerBandValues = bollingerBands.get( i );
                    lowerBandValues.add( deviations[1] );
                    break;
            }
        }
    }

    private Integer[] calculateDeviations(int simpleMovingAverage, List<Integer> lottoNumberSpanHolder) {

        List<Integer> values = new ArrayList<>(lottoNumberSpanHolder);
        for(int i = 0; i < values.size(); i++){

            int difOne = values.get(i) - simpleMovingAverage;
            Double squaredValue = Math.pow(difOne, 2);
            values.set(i, squaredValue.intValue() );

        }

        Double avg = values.stream().collect(Collectors.averagingDouble(i -> i));
        double deviation = Math.sqrt( avg );

        int upperBandValue = (int) (simpleMovingAverage + (2 * deviation));
        int lowerBandValue = (int) (simpleMovingAverage - (2 * deviation));

        return new Integer[]{upperBandValue, lowerBandValue};
    }

    private Integer calculateSimpleMovingAverage(List<Integer> lottoNumberSpanHolder) {

        Double val = lottoNumberSpanHolder.stream().collect(Collectors.averagingInt( j -> j));

        return val.intValue();
    }

}
