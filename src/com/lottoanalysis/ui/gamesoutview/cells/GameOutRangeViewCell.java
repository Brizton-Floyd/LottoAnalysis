package com.lottoanalysis.ui.gamesoutview.cells;

import com.lottoanalysis.models.gameout.GameOutRange;
import com.lottoanalysis.models.gameout.NumberDistanceCalculator;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
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

                NumberDistanceCalculator numberDistanceCalculator = new NumberDistanceCalculator( gameOutRange );
                List<NumberDistanceCalculator> maps = new ArrayList<>( numberDistanceCalculator.numberDistributionData() );
                gameOutViewEmpl.injectValuesIntoDistanceTable( maps );

                List<GameOutRange.GameOut> list = new ArrayList<>();
                for(GameOutRange.GameOut gameOut : gameOutRange.getGameOut().getGameOutList()){
                    if(gameOutRange.getUpperBound() > 0){
                        if(gameOut.getGameOutNumber() >= gameOutRange.getLowerBound() && gameOut.getGameOutNumber() <= gameOutRange.getUpperBound()){
                            list.add( gameOut );
                        }
                    }
                    else if(gameOut.getGameOutNumber() >= gameOutRange.getLowerBound()){
                        list.add( gameOut );
                    }
                }
                list.sort((o1, o2) -> Integer.compare(o2.getGameOutHits(), o1.getGameOutHits()));
                gameOutViewEmpl.injectValuesIntoGameOutTable( list );

            }

            this.setOnMouseClicked(event -> {
                gameOutViewEmpl.injectGameOutValuesIntoChart( gameOutRange.getGameOutHolder() );

                NumberDistanceCalculator numberDistanceCalculator = new NumberDistanceCalculator( gameOutRange );
                List<NumberDistanceCalculator> maps = new ArrayList<>( numberDistanceCalculator.numberDistributionData() );
                gameOutViewEmpl.injectValuesIntoDistanceTable( maps );

                List<GameOutRange.GameOut> list = new ArrayList<>();
                for(GameOutRange.GameOut gameOut : gameOutRange.getGameOut().getGameOutList()){
                    if(gameOutRange.getUpperBound() > 0){
                        if(gameOut.getGameOutNumber() >= gameOutRange.getLowerBound() && gameOut.getGameOutNumber() <= gameOutRange.getUpperBound()){
                            list.add( gameOut );
                        }
                    }
                    else if(gameOut.getGameOutNumber() >= gameOutRange.getLowerBound()) {
                        list.add( gameOut );
                    }
                }

                list.sort((o1, o2) -> Integer.compare(o2.getGameOutHits(), o1.getGameOutHits()));
                gameOutViewEmpl.injectValuesIntoGameOutTable( list );

            });
        }
    }
}
