package com.lottoanalysis.models.drawhistory;

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
