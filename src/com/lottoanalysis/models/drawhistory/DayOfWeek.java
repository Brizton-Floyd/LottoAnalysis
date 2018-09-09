package com.lottoanalysis.models.drawhistory;

public enum DayOfWeek {

    ALL("All Days","All Days"),
    MON("Mon.","Monday"),
    TUE("Tue.","Tuesday"),
    WED("Wed.","Wednesday"),
    THU("Thu.","Thursday"),
    FRI("Fri.","Friday"),
    SAT("Sat.","Saturday"),
    SUN("Sun.","Sunday");

    private String dayOfWeek;
    private String fullDayName;

    DayOfWeek( String dayOfWeek, String fullDayName){
        this.dayOfWeek = dayOfWeek;
        this.fullDayName = fullDayName;
    }

    public String getDay(){
        return dayOfWeek;
    }

    public String getFullDayName() {
        return fullDayName;
    }
}
