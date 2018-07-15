package com.lottoanalysis.models.pastresults;

public enum AnalyzeMethod {

    DRAW_POSITION(0,"Positional Numbers","Lotto"),
    DELTA_NUMBERS(1, "Delta Number","Delta"),
    POSITIONAL_SUMS(2,"Positional Sums","Lotto Sums"),
    LINE_SPACINGS(3, "Line Spacings","Spacing"),
    REMAINDER(4, "Remainder","Remainder"),
    LAST_DIGIT(5,"Last Digit","Last Digit"),
    MULTIPLES(6,"Multiples","Lotto");

    private int index;
    private String title;
    private String abbr;

    AnalyzeMethod(int index, String title, String abbr)
    {
        this.index = index;
        this.title = title;
        this.abbr = abbr;
    }

    public int getIndex()
    {
        return index;
    }

    public String getTitle() {
        return title;
    }

    public String getAbbr() {
        return abbr;
    }

}
