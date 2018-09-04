package com.lottoanalysis.ui.gamesoutview.cells;

import com.lottoanalysis.models.gameout.GameOutRange;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class GameOutCell extends TableCell<GameOutRange.GameOut,String > {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!isEmpty()){

            setText(item);
            this.setTextFill(Color.BEIGE);
        }
    }
}
