package com.lottoanalysis.models.drawhistory;

import com.lottoanalysis.lottogames.drawing.Drawing;

import java.util.Arrays;
import java.util.List;

public enum DrawPositions {

    POS_ONE(0,"Position 1"),
    POS_TWO(1,"Position 2"),
    POS_THREE(2,"Position 3"),
    POS_FOUR(3,"Position 4"),
    POS_FIVE(4,"Position 5"),
    BONUS(5,"Bonus Number");

    private String text;
    private int index;

    DrawPositions(int index, String text){
        this.index = index;
        this.text = text;
    }

    public String getText(){
        return text;
    }

    public int getIndex(){
        return index;
    }

}
