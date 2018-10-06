package com.lottoanalysis.models.drawresults;

public interface GamesInPast {
    enum Span {
        FIVE(5),
        TEN(10),
        FIFTEEN(15),
        TWENTY(20),
        THIRTY(30);

        private int spanValue;
        Span(int val){
            this.spanValue = val;
        }

        int getSpanValue(){
            return spanValue;
        }
    }

    Span getSpan();
}
