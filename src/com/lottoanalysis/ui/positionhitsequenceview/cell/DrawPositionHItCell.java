package com.lottoanalysis.ui.positionhitsequenceview.cell;

import com.lottoanalysis.models.drawresults.DrawResultPosition;
import com.lottoanalysis.ui.positionhitsequenceview.PositionHitSequenceView;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class DrawPositionHItCell extends TableCell<DrawResultPosition,String> {

    private PositionHitSequenceView positionHitSequenceView;

    public DrawPositionHItCell(PositionHitSequenceView positionHitSequenceView) {
        this.positionHitSequenceView = positionHitSequenceView;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if(!isEmpty()){

            setText(item);
            this.setTextFill(Color.BEIGE);

            DrawResultPosition drawResultPosition = getTableView().getItems().get(getIndex());
            if(drawResultPosition.getDrawPositionGamesOut() == 0){
                getTableView().getSelectionModel().select(getIndex());
                positionHitSequenceView.injectValuesToLotteryNumberChart( drawResultPosition.getLotteryResultPositionList() );
                positionHitSequenceView.injectValuesToGameOutChart( drawResultPosition.getGameOutHolderList(), drawResultPosition.getDrawPositionIndex());

            }
            this.setOnMouseClicked(event -> {
                positionHitSequenceView.injectValuesToLotteryNumberChart( drawResultPosition.getLotteryResultPositionList() );
                positionHitSequenceView.injectValuesToGameOutChart( drawResultPosition.getGameOutHolderList(), drawResultPosition.getDrawPositionIndex() );
            });
        }
    }
}
