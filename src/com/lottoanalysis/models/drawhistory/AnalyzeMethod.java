package com.lottoanalysis.models.drawhistory;

public enum AnalyzeMethod {

    DRAW_POSITION(0,"Positional Numbers","Lotto"),
    DELTA_NUMBERS(1, "Delta Number","Delta"),
    POSITIONAL_SUMS(2,"Positional Sums","Lotto Sums"),
    LINE_SPACINGS(3, "Line Spacings","Spacing"),
    REMAINDER(4, "Remainder","Remainder"),
    LAST_DIGIT(5,"Last Digit","Last Digit"),
    MULTIPLES(6,"Multiples","Lotto"),
    GROUP_ANALYSIS(7,"Number Group", "Group"),
    LAST_DIGIT_GROUPING(8,"Last Digit Grouping","Last Digit Group"),
    FIRST_DIGIT(7,"First Digit","First Digit");

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
