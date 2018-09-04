package com.lottoanalysis.ui.gamesoutview.cells;

import com.lottoanalysis.models.gameout.GameOutRange;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class GameOutRangeViewCell<T> extends TableCell<T,String> {

    private GameOutViewImpl gameOutViewEmpl;

    public GameOutRangeViewCell(GameOutViewImpl gameOutViewImpl){
        this.gameOutViewEmpl = gameOutViewImpl;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if(!isEmpty()){
            setText(item);
            this.setTextFill(Color.BEIGE);

            GameOutRange gameOutRange = (GameOutRange) getTableView().getItems().get( getIndex() );
            if(gameOutRange.getGamesOut() == 0){
                getTableView().getSelectionModel().select( getIndex() );
                gameOutViewEmpl.injectGameOutValuesIntoChart( gameOutRange.getGameOutHolder() );

                List<GameOutRange.GameOut> list = gameOutRange.getGameOut().getGameOutList().stream().filter(gameOut -> gameOut.getGameOutNumber() >= gameOutRange.getLowerBound() &&
                        gameOut.getGameOutNumber() <= gameOutRange.getUpperBound()).collect(Collectors.toList());
                gameOutViewEmpl.injectValuesIntoGameOutTable( list );

            }

            this.setOnMouseClicked(event -> {
                gameOutViewEmpl.injectGameOutValuesIntoChart( gameOutRange.getGameOutHolder() );

                List<GameOutRange.GameOut> list = gameOutRange.getGameOut().getGameOutList().stream().filter(gameOut -> gameOut.getGameOutNumber() >= gameOutRange.getLowerBound() &&
                        gameOut.getGameOutNumber() <= gameOutRange.getUpperBound()).collect(Collectors.toList());
                gameOutViewEmpl.injectValuesIntoGameOutTable( list );

            });
        }
    }
}
