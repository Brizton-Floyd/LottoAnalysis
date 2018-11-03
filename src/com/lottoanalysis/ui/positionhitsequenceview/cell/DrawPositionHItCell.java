package com.lottoanalysis.ui.positionhitsequenceview.cell;

import com.lottoanalysis.models.drawresults.DrawResultPosition;
import com.lottoanalysis.ui.positionhitsequenceview.PositionHitSequenceView;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

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
                positionHitSequenceView.refresh(drawResultPosition);
                positionHitSequenceView.injectValuesToLotteryNumberChart( drawResultPosition.getLotteryResultPositionList() );
                List<Integer> spacingBetweenNumberList = getSpacingsBetweenNumbers();
                positionHitSequenceView.injectValuesToGameOutChart( spacingBetweenNumberList, drawResultPosition.getDrawPositionIndex());

            }
            this.setOnMouseClicked(event -> {
                positionHitSequenceView.refresh(drawResultPosition);
                positionHitSequenceView.injectValuesToLotteryNumberChart( drawResultPosition.getLotteryResultPositionList() );
                List<Integer> spacingBetweenNumberList = getSpacingsBetweenNumbers();
                positionHitSequenceView.injectValuesToGameOutChart( spacingBetweenNumberList, drawResultPosition.getDrawPositionIndex() );
            });
        }
    }

    private List<Integer> getSpacingsBetweenNumbers() {
        List<Integer> columns = new ArrayList<>( DrawResultPosition.compainonWinningColumnList );
        Object[] data = ChartHelperTwo.getRepeatedNumberListVersionTwo(columns);

        List<Integer> specialList = (List<Integer>) data[0];
//
//        List<Integer> spacings = new ArrayList<>();
//        for(int i = 1; i < specialList.size(); i++){
//            int spacing = Math.abs( specialList.get(i-1) - specialList.get(i));
//            spacings.add( spacing );
//        }
        return specialList;
    }
}
