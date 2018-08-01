package com.lottoanalysis.models.drawhistory;

public enum DayOfWeek {

    ALL("All Days"),MON("Mon."),TUE("Tue."),WED("Wed."),THU("Thu."),FRI("Fri."),SAT("Sat."),SUN("Sun.");

    private String dayOfWeek;

    DayOfWeek( String dayOfWeek){
        this.dayOfWeek = dayOfWeek;
    }

    public String getDay(){
        return dayOfWeek;
    }
}
