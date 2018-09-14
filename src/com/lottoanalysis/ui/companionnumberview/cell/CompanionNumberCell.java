package com.lottoanalysis.ui.companionnumberview.cell;

import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.ui.companionnumberview.CompanionNumberView;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class CompanionNumberCell extends TableCell<Range,String> {
    private static boolean processed = false;
    private CompanionNumberView companionNumberView;

    public CompanionNumberCell(CompanionNumberView companionNumberView){
        this.companionNumberView = companionNumberView;
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!isEmpty()){

            setText(item);
            this.setTextFill(Color.BEIGE);

            Range range = getTableView().getItems().get(getIndex());
            if(range.getGamesOut() == 0){
                processed = true;
                getTableView().getSelectionModel().select(getIndex());
                companionNumberView.renderToCompanionChartPane( range.getRangeWinningNumberHolder() );
            }

            this.setOnMouseClicked(event -> {
                companionNumberView.renderToCompanionChartPane( range.getRangeWinningNumberHolder() );
            });
        }
    }
}
