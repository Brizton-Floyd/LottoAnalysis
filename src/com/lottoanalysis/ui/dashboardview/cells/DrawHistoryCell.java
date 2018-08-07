package com.lottoanalysis.ui.dashboardview.cells;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.ui.dashboardview.LottoDashBoardListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.EventHandler;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class DrawHistoryCell extends TableCell<Drawing, String> {

    private LottoDashBoardListener lottoDashBoardListener;
    private TableView<Drawing> drawingTableView;
    public DrawHistoryCell(){}

    public DrawHistoryCell(LottoDashBoardListener lottoDashBoardListener, TableView<Drawing> drawingTableView){
        this.lottoDashBoardListener = lottoDashBoardListener;
        this.drawingTableView = drawingTableView;
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        setText(item);
        this.setTextFill(Color.BEIGE);

        if(getTableColumn().getText().equals("Bonus"))
            setTextFill(Color.YELLOW);

        setOnMouseClicked(event -> {

            if(event.getClickCount() == 2){

                drawingTableView.setDisable(true);
                getTableView().getSelectionModel().select( getIndex() );

                Drawing drawing = getTableView().getItems().get( getIndex() );
                lottoDashBoardListener.loadEditableDrawView( drawing );
            }
        });

        //getTableView().getSelectionModel().select(getTableView().getItems().size()-1);
    }
}
