package model;

import javafx.beans.property.SimpleStringProperty;

import java.util.LinkedList;
import java.util.List;
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
    public static int drawSize;

    public static int getDrawSize() {
        return drawSize;
    }

    public Drawing(int drawNumber, String drawDate, String... numbers) {

        this.drawNumber = new SimpleStringProperty("" + drawNumber);
        this.drawDate = new SimpleStringProperty(drawDate);
        drawSize = numbers.length;

        initializeNumberPositions(numbers);
        loadData(numbers);
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

    public void setPosOne(String posOne) {
        this.posOne.set(posOne);
    }

    public String getPosTwo() {
        return posTwo.get();
    }

    public SimpleStringProperty posTwoProperty() {
        return posTwo;
    }

    public void setPosTwo(String posTwo) {
        this.posTwo.set(posTwo);
    }

    public String getPosThree() {
        return posThree.get();
    }

    public SimpleStringProperty posThreeProperty() {
        return posThree;
    }

    public void setPosThree(String posThree) {
        this.posThree.set(posThree);
    }

    public String getPosFour() {
        return posFour.get();
    }

    public SimpleStringProperty posFourProperty() {
        return posFour;
    }

    public void setPosFour(String posFour) {
        this.posFour.set(posFour);
    }

    public String getPosFive() {
        return posFive.get();
    }

    public SimpleStringProperty posFiveProperty() {
        return posFive;
    }

    public void setPosFive(String posFive) {
        this.posFive.set(posFive);
    }

    public String getBonusNumber() {
        return bonusNumber.get();
    }

    public SimpleStringProperty bonusNumberProperty() {
        return bonusNumber;
    }

    public void setBonusNumber(String bonusNumber) {
        this.bonusNumber.set(bonusNumber);
    }

    public String getDrawNumber() {
        return drawNumber.get();
    }

    public SimpleStringProperty drawNumberProperty() {
        return drawNumber;
    }

    public void setDrawNumber(String drawNumber) {
        this.drawNumber.set(drawNumber);
    }

    public String getDrawDate() {
        return drawDate.get();
    }

    public SimpleStringProperty drawDateProperty() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate.set(drawDate);
    }

//    public SimpleStringProperty[] getDrawResults() {
//        return drawResults;
//    }
//
//    public void setDrawResults(SimpleStringProperty[] drawResults) {
//        this.drawResults = drawResults;
//    }

    public String getDrawSum() {
        return drawSum.get();
    }

    public SimpleStringProperty drawSumProperty() {
        return drawSum;
    }

    public void setDrawSum(String drawSum) {
        this.drawSum.set(drawSum);
    }

    public String getOddEvenRatio() {
        return oddEvenRatio.get();
    }

    public SimpleStringProperty oddEvenRatioProperty() {
        return oddEvenRatio;
    }

    public void setOddEvenRatio(String oddEvenRatio) {
        this.oddEvenRatio.set(oddEvenRatio);
    }

}
