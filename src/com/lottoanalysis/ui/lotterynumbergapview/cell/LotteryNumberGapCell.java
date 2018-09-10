package com.lottoanalysis.ui.lotterynumbergapview.cell;

import com.lottoanalysis.models.lotterynumbergap.LotteryNumberGapModel;
import com.lottoanalysis.ui.lotterynumbergapview.LotteryNumberGapView;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class LotteryNumberGapCell extends TableCell<LotteryNumberGapModel,String> {

    public static boolean processed;
    private LotteryNumberGapView lotteryNumberGapView;

    public LotteryNumberGapCell( LotteryNumberGapView lotteryNumberGapView ){
        this.lotteryNumberGapView = lotteryNumberGapView;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!isEmpty()){

            setText(item);
            this.setTextFill(Color.BEIGE);

            LotteryNumberGapModel lotteryNumberGapModel = getTableView().getItems().get(getIndex());
            if(lotteryNumberGapModel.getGamesOut() == 0 && !processed){
                getTableView().getSelectionModel().select( getIndex() );
                lotteryNumberGapView.injectNumbersIntoChart( lotteryNumberGapModel.getGapNumberHolder() );
                System.out.println("Out zero");
                processed = true;
            }

            this.setOnMouseClicked(event -> {
                lotteryNumberGapView.injectNumbersIntoChart( lotteryNumberGapModel.getGapNumberHolder() );
            });
        }
    }
}
