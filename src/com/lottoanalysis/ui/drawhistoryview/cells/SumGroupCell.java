package com.lottoanalysis.ui.drawhistoryview.cells;

import com.lottoanalysis.models.drawhistory.SumGroupAnalyzer;
import com.lottoanalysis.ui.drawhistoryview.DrawHistoryView;
import javafx.scene.control.TableCell;
import javafx.scene.paint.Color;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SumGroupCell extends TableCell<SumGroupAnalyzer, String> {

    private DrawHistoryView drawHistoryView;

    public SumGroupCell(DrawHistoryView drawHistoryView) {
        this.drawHistoryView = drawHistoryView;
    }

    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (!isEmpty()) {

            setText(item);
            this.setTextFill(Color.BEIGE);
            // System.out.println(param.getText());

            SumGroupAnalyzer sumGroupAnalyzer = getTableView().getItems().get(getIndex());
            if (sumGroupAnalyzer.getGroupGamesOut() == 0) {
                getTableView().getSelectionModel().select(getIndex());

                final String values = getUniqueValueString(sumGroupAnalyzer);
                drawHistoryView.notifyListenerOfTableCellSelectionChange(values);

                if (getTableView().getSelectionModel().getSelectedItems().contains(sumGroupAnalyzer)) {

                    this.setTextFill(Color.valueOf("#76FF03"));
                }

                //System.out.println(getItem());
                // Get fancy and change color based on data
                //if (item.contains("X"))
                //this.setTextFill(Color.valueOf("#EFA747"));
            }


            this.setOnMouseClicked(event -> {

                final String values = getUniqueValueString(sumGroupAnalyzer);
                drawHistoryView.notifyListenerOfTableCellSelectionChange(values);

            });


        }
    }

    private String getUniqueValueString(SumGroupAnalyzer sumGroupAnalyzer) {
        Set<Integer> valuess = new HashSet<>(sumGroupAnalyzer.getLottoNumberInSumRangeHolder());
        return Arrays.toString( valuess.toArray() );
    }
}
