package com.lottoanalysis.ui.companionnumberview.cell;

import com.lottoanalysis.models.companionnumber.CompanionNumber;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.ui.companionnumberview.CompanionNumberView;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class MainNumberCell extends TableCell<Range, String> {

    public static boolean processed = false;
    private CompanionNumberView companionNumberView;

    public MainNumberCell(CompanionNumberView companionNumberView){
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
                //int value = range.getRangeWinningNumberHolder().get(range.getRangeWinningNumberHolder().size()-1);
                //companionNumberView.passOffToPresenter(value);
                companionNumberView.renderToMainChartPane( range.getRangeWinningNumberHolder() );
            }

            this.setOnMouseClicked(event -> {
                int value = range.getRangeWinningNumberHolder().get(range.getRangeWinningNumberHolder().size()-1);
                companionNumberView.passOffToPresenter(value);
                companionNumberView.renderToMainChartPane( range.getRangeWinningNumberHolder() );
            });
        }
    }
}
