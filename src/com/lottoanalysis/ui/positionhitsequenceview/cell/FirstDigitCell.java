package com.lottoanalysis.ui.positionhitsequenceview.cell;

import com.lottoanalysis.models.drawresults.DrawResultPosition;
import com.lottoanalysis.models.drawresults.FirstDigitAdjacentNumberAnalyzer;
import com.lottoanalysis.ui.positionhitsequenceview.PositionHitSequenceView;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.util.List;

public class FirstDigitCell extends TableCell<FirstDigitAdjacentNumberAnalyzer,String> {

    private PositionHitSequenceView positionHitSequenceView;

    public FirstDigitCell(PositionHitSequenceView positionHitSequenceView) {
        this.positionHitSequenceView = positionHitSequenceView;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if(!isEmpty()){

            setText(item);
            this.setTextFill(Color.BEIGE);

            FirstDigitAdjacentNumberAnalyzer firstDigitAdjacentNumberAnalyzer = getTableView().getItems().get( getIndex() );
            if(firstDigitAdjacentNumberAnalyzer.getGamesOut() == 0) {
                  getTableView().getSelectionModel().select(getIndex());
                  //positionHitSequenceView.injectValuesToGameOutChart( firstDigitAdjacentNumberAnalyzer.getAdjacentNumberHolderList(), getIndex()+1 );
            }
            this.setOnMouseClicked(event -> {
                //positionHitSequenceView.injectValuesToGameOutChart( firstDigitAdjacentNumberAnalyzer.getAdjacentNumberHolderList(), getIndex()+1 );
            });
        }
    }
}
