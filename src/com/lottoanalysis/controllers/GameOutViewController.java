package com.lottoanalysis.controllers;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.lottogames.PickFourLotteryGameImpl;
import com.lottoanalysis.lottogames.PickThreeLotteryGameImpl;
import com.lottoanalysis.lottogames.drawing.Drawing;
import com.lottoanalysis.patternmakers.LottoNumberPatternMaker;
import com.lottoanalysis.patternmakers.PatternMaker;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GameOutViewController {

    private Map<Integer,Object[]> lottoNumberPatternData;
    private PatternMaker patternMaker;
    private LottoGame lottoGame;
    @FXML
    private TableView tbl_lottoNumberPattern;


    public void init(LottoGame lottoGame){

        this.lottoGame = lottoGame;
        patternMaker = new LottoNumberPatternMaker();
        patternMaker.constructGameOutHitPattern( lottoGame );
        patternMaker.extractValuesFromMap();

        lottoNumberPatternData = patternMaker.getLottoNumberPatternMap();

        initialize();
        loadPatternTable();
    }

    public void initialize() {

        if(lottoGame instanceof PickFourLotteryGameImpl || lottoGame instanceof PickThreeLotteryGameImpl){
            tbl_lottoNumberPattern.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }
        else
         tbl_lottoNumberPattern.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
    }
    private void loadPatternTable() {

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();
        List<Integer> numberRanges = new ArrayList<>(lottoNumberPatternData.keySet());
        String[] headers = {"Draw Number","Draw Date"};

        // Create columns
        for(int i = 0; i < numberRanges.size() + 2; i++){

            final int j = i;
            TableColumn col;

            if( j == 0 || j == 1)
                col = new TableColumn(headers[j]);
            else
                col = new TableColumn(numberRanges.get(i-2)+"");

            col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {

                @Override
                public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {
                    return new TableCell<ObservableList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {
                                this.setTextFill(Color.BEIGE);
                                // Get fancy and change color based on data
                                if (item.contains("P"))
                                    this.setTextFill(Color.valueOf("#00FF48"));
                                setText(item);
                            }
                        }
                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())



            );

            col.setSortable(false);
            tbl_lottoNumberPattern.getColumns().addAll(col);
        }


        List<List<String>> mapValues = patternMaker.getValue();
        for(int i = 0; i < mapValues.get(0).size(); i++){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            ObservableList<Drawing> drawings = lottoGame.getDrawingData();
            Drawing drawing = drawings.get(i);

            row.add( drawing.getDrawNumber());
            row.add( drawing.getDrawDate());
            for(int j = 0; j < mapValues.size(); j++){


                List<String> values = mapValues.get(j);
                //Iterate Column
                row.add(values.get(i));
                // System.out.println("Row [1] added " + row);

            }

            dataItems.add(row);
        }

        tbl_lottoNumberPattern.setItems(dataItems);
        tbl_lottoNumberPattern.scrollTo(mapValues.get(0).size() - 1);

    }
}
