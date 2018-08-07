package com.lottoanalysis.models.lottogames.drawing;

import com.lottoanalysis.models.lottogames.LottoGame;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

import java.util.*;

/**
 * Created by briztonfloyd on 8/26/17.
 */
public class Drawing {

    private SimpleIntegerProperty drawId;
    private SimpleStringProperty drawNumber;
    private SimpleStringProperty drawDate;
    private SimpleStringProperty posOne, posTwo, posThree, posFour, posFive, bonusNumber;
    private SimpleStringProperty drawSum;
    private SimpleStringProperty oddEvenRatio;
    private Map<String,String> monthNumbers = new HashMap<>();
    private Set<String> dayOfWeek = new HashSet<>();
    private static List<Drawing> unModifiedDrawData = new ArrayList<>();
    private String[] nums;
    private List<SimpleStringProperty> drawNumbers1 = new ArrayList<>();

    public static int drawSize;

    public static int getDrawSize() {
        return drawSize;
    }

    public Drawing(int drawNumber, String drawDate, String... numbers) {

        nums = numbers;
        loadHashMap();
        this.drawNumber = new SimpleStringProperty("" + drawNumber);
        String[] pp = drawDate.split("\\s");
        dayOfWeek.add(pp[0]);

        this.drawDate = new SimpleStringProperty(String.format(" %1s %2s / %3s / %4s",pp[0], monthNumbers.get(pp[1]),pp[2].substring(0,2),pp[3]));
        drawSize = numbers.length;

        initializeNumberPositions(numbers);
        loadData(numbers);
        loadDrawNumbers();
    }

    private void loadDrawNumbers() {

        SimpleStringProperty[] values = {posOne, posTwo, posThree, posFour, posFive, bonusNumber};

        for(SimpleStringProperty string : values){
            if(string != null){
                drawNumbers1.add( string );
            }
        }
    }

    public int getDrawId() {
        return drawId.get();
    }

    public SimpleIntegerProperty drawIdProperty() {
        return drawId;
    }

    public void setDrawId(int drawId) {

        if(this.drawId == null)
            this.drawId = new SimpleIntegerProperty(drawId);

    }

    public List<SimpleStringProperty> getDrawNumbers() {
        return drawNumbers1;
    }

    public static List<Drawing> getUnModifiedDrawData() {
        return unModifiedDrawData;
    }

    public String[] getNums() {
        return nums;
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

    public static String splitGameName( String gameName )
    {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < gameName.length(); i++)
        {
            if(Character.isUpperCase( gameName.charAt(i) ))
            {
                if( i != 0)
                    builder.append(" ").append(gameName.charAt(i));
                else
                    builder.append(gameName.charAt(i));
            }
            else
            {
                builder.append(gameName.charAt(i));
            }
        }

        return builder.toString();
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

    public static List<Integer> extractBonusNumbersFromDrawing(LottoGame lottoGame) {

        List<Integer> bonusNumbers = new ArrayList<>();

        lottoGame.getDrawingData().forEach( val -> {

            bonusNumbers.add( Integer.parseInt(val.getBonusNumber()) );
        });

        return bonusNumbers;
    }

    public static Set<String> extractDays(ObservableList<Drawing> drawingData) {

        Set<String> dayOfWeek = new HashSet<>();

        for(Drawing drawing : drawingData){
            String[] pp = drawing.getDrawDate().split("\\s");
            dayOfWeek.add( pp[1] );
        }

        return dayOfWeek;
    }

    public static int[][] convertDrawDataTo2DArray(List<Drawing> drawResults) {

        Drawing drawing = drawResults.iterator().next();

        int[][] data = new int[drawing.getDrawNumbers().size()][drawResults.size()];

        int indexer = 0;
        for(int i = 0; i < drawResults.size(); i++){

            Drawing drawing1 = drawResults.get(i);
            for(int j = 0; j < drawing1.getDrawNumbers().size(); j++){

                data[j][indexer] = Integer.parseInt( drawing1.getDrawNumbers().get(j).get().trim() );
            }

            indexer++;
        }

        return data;
    }
}
