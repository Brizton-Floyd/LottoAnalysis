package com.lottoanalysis.ui.gamesoutview.cells;

import com.lottoanalysis.models.gameout.GameOutRange;
import com.lottoanalysis.models.gameout.NumberDistanceCalculator;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.util.List;

public class DistanceCell extends TableCell<NumberDistanceCalculator, String> {

    private GameOutViewImpl gameOutView;
    public static boolean processed = false;
    public DistanceCell(GameOutViewImpl gameOutView) {
        this.gameOutView = gameOutView;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!isEmpty()){
            setText(item);
            this.setTextFill(Color.BEIGE);

            NumberDistanceCalculator gameOutRange = getTableView().getItems().get( getIndex() );
            if(gameOutRange.getGamesOut() == 0 && !processed){
                getTableView().getSelectionModel().select(getIndex());
                List<Integer> distanceValues = gameOutRange.getDistanceValues();
                gameOutView.injectDistanceValues( distanceValues );
                processed = true;
            }

            this.setOnMouseClicked(event -> {

                List<Integer> distanceValues = gameOutRange.getDistanceValues();
                gameOutView.injectDistanceValues( distanceValues );

            });
        }
    }
}
