package com.lottoanalysis.lottogames.drawing;

import javafx.beans.property.SimpleStringProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by briztonfloyd on 8/26/17.
 */
public class Drawing {

    private SimpleStringProperty drawNumber;
    private SimpleStringProperty drawDate;
    private SimpleStringProperty posOne, posTwo, posThree, posFour, posFive, bonusNumber;
    private SimpleStringProperty drawSum;
    private SimpleStringProperty oddEvenRatio;
    private Map<String,String> monthNumbers = new HashMap<>();
    public static int drawSize;

    public static int getDrawSize() {
        return drawSize;
    }

    public Drawing(int drawNumber, String drawDate, String... numbers) {

        loadHashMap();
        this.drawNumber = new SimpleStringProperty("" + drawNumber);
        String[] pp = drawDate.split("\\s");

        this.drawDate = new SimpleStringProperty(String.format("%1s / %2s / %3s",monthNumbers.get(pp[1]),pp[2].substring(0,2),pp[3]));
        drawSize = numbers.length;


        initializeNumberPositions(numbers);
        loadData(numbers);
    }

    private void loadHashMap() {
        monthNumbers.put("Jan","01");
        monthNumbers.put("Feb","02");
        monthNumbers.put("Mar","03");
        monthNumbers.put("Apr","04");
        monthNumbers.put("May","05");
        monthNumbers.put("Jun","06");
        monthNumbers.put("Jul","07");
        monthNumbers.put("Aug","08");
        monthNumbers.put("Sep","09");
        monthNumbers.put("Oct","10");
        monthNumbers.put("Nov","11");
        monthNumbers.put("Dec","12");
    }

    private void initializeNumberPositions(String[] numbers) {

        for(int i = 0; i <numbers.length; i++){

            switch (i){
                case 0:
                    posOne = new SimpleStringProperty(numbers[i]);
                    break;
                case 1:
                    posTwo = new SimpleStringProperty(numbers[i]);
                    break;
                case 2:
                    posThree = new SimpleStringProperty(numbers[i]);
                    break;
                case 3:
                    posFour = new SimpleStringProperty(numbers[i]);
                    break;
                case 4:
                    posFive = new SimpleStringProperty(numbers[i]);
                    break;
                case 5:
                    bonusNumber = new SimpleStringProperty(numbers[i]);
                    break;

            }
        }
    }

    private void loadData(String[] numbers) {

        int index = 0;
        int sum = 0;
        int oddCount = 0;
        int evenCount = 0;

        int count = 1;
        for (String number : numbers) {
           // drawResults[index] = new SimpleStringProperty(number);

            sum += Integer.parseInt(number);

            if (Integer.parseInt(number) % 2 == 0)
                evenCount++;
            else
                oddCount++;

            index++;
        }

        drawSum = new SimpleStringProperty("" + sum);
        oddEvenRatio = new SimpleStringProperty(evenCount + " / " + oddCount);
    }

    public String getPosOne() {
        return posOne.get();
    }

    public SimpleStringProperty posOneProperty() {
        return posOne;
    }

    public String getPosTwo() {
        return posTwo.get();
    }

    public SimpleStringProperty posTwoProperty() {
        return posTwo;
    }

    public String getPosThree() {
        return posThree.get();
    }

    public SimpleStringProperty posThreeProperty() {
        return posThree;
    }

    public String getPosFour() {
        return posFour.get();
    }

    public SimpleStringProperty posFourProperty() {
        return posFour;
    }

    public String getPosFive() {
        return posFive.get();
    }

    public SimpleStringProperty posFiveProperty() {
        return posFive;
    }

    public String getBonusNumber() {
        return bonusNumber.get();
    }

    public SimpleStringProperty bonusNumberProperty() {
        return bonusNumber;
    }

}
