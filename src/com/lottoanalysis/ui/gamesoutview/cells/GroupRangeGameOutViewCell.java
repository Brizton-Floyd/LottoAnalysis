package com.lottoanalysis.ui.gamesoutview.cells;

import com.lottoanalysis.models.gameout.GroupRange;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.ui.gamesoutview.GameOutViewImpl;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

public class GroupRangeGameOutViewCell extends TableCell<Range, String> {

    private GameOutViewImpl gameOutView;
    public static boolean processed = false;

    public GroupRangeGameOutViewCell(GameOutViewImpl gameOutView) {
        this.gameOutView = gameOutView;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (!isEmpty()) {

            setText(item);
            this.setTextFill(Color.BEIGE);


            GroupRange range = (GroupRange) getTableView().getItems().get(getIndex());
            if (range.getGamesOut() == 0 && !processed) {
                getTableView().getSelectionModel().select(getIndex());
                this.setTextFill(Color.valueOf("#76FF03"));
                gameOutView.displayNumberDistrubution( range.getLottoNumberMap());
                gameOutView.alertPresenterOfIndexChange(range.getRangeIndex());
                processed = true;
            }

            this.setOnMouseClicked(event -> {
                gameOutView.displayNumberDistrubution( range.getLottoNumberMap());
                gameOutView.alertPresenterOfIndexChange( range.getRangeIndex() );
            });
        }
    }
}
