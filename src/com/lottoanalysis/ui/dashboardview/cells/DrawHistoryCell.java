package com.lottoanalysis.ui.dashboardview.cells;

import com.lottoanalysis.models.lottogames.drawing.Drawing;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;

public class DrawHistoryCell extends TableCell<Drawing, String> {

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        setText(item);
        this.setTextFill(Color.BEIGE);

        if(getTableColumn().getText().equals("Bonus"))
            setTextFill(Color.YELLOW);

        getTableView().getSelectionModel().select(getTableView().getItems().size()-1);
    }
}
