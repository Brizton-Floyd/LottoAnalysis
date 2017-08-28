package model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by briztonfloyd on 8/26/17.
 */
public class Drawing {

    private int drawNumber;
    private String drawDate;
    private List<String> drawResults;

    public Drawing(int drawId, String drawDate, String... numbers){

        this.drawNumber = drawId;
        this.drawDate = drawDate;
        drawResults = Arrays.asList(numbers);
    }

    public int getDrawNumber() {
        return drawNumber;
    }

    public void setDrawNumber(int drawNumber) {
        this.drawNumber = drawNumber;
    }

    public String getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(String drawDate) {
        this.drawDate = drawDate;
    }

    public List<String> getDrawResults() {
        return drawResults;
    }

    public void setDrawResults(List<String> drawResults) {
        this.drawResults = drawResults;
    }
}
