package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created by briztonfloyd on 8/26/17.
 */
public class Drawing {

    private SimpleIntegerProperty drawNumber;
    private SimpleStringProperty drawDate;
    private SimpleStringProperty[] drawResults;
    private SimpleIntegerProperty drawSum;
    private SimpleStringProperty oddEvenRatio;

    public Drawing(int drawNumber, String drawDate, String... numbers) {

        this.drawNumber = new SimpleIntegerProperty(drawNumber);
        this.drawDate = new SimpleStringProperty(drawDate);
        drawResults = new SimpleStringProperty[numbers.length];
        loadData(numbers);
    }

    private void loadData(String[] numbers) {

        int index = 0;
        int sum = 0;
        int oddCount = 0;
        int evenCount = 0;

        for(String number : numbers){
            drawResults[index] = new SimpleStringProperty(number);

            sum += Integer.parseInt(number);

            if(Integer.parseInt(number) % 2 == 0)
                evenCount++;
            else
                oddCount++;

            index++;
        }

        drawSum = new SimpleIntegerProperty(sum);
        oddEvenRatio = new SimpleStringProperty(evenCount + " / " + oddCount);
    }



    public int getDrawSum() {
        return drawSum.get();
    }

    public SimpleIntegerProperty drawSumProperty() {
        return drawSum;
    }

    public void setDrawSum(int drawSum) {
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

    public int getDrawNumber() {
        return drawNumber.get();
    }

    public SimpleIntegerProperty drawNumberProperty() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
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

    public SimpleStringProperty[] getDrawResults() {
        return drawResults;
    }

    public void setDrawResults(SimpleStringProperty[] drawResults) {
        this.drawResults = drawResults;
    }
}
