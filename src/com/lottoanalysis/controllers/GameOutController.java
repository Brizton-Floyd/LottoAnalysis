package com.lottoanalysis.controllers;

import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.gameoutanalyzers.GameOutMapper;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GameOutController {

    private LottoGame lottoGame;
    private List<Object> lottoDrawData;
    private int[] positionData;

    private GameOutMapper gameOutMapper;

    private int currentDrawPosition;
    private final int DEFAULT_DRAW_SIZE = 10;


    @FXML
    private HBox drawPositoinHbox;

    @FXML
    private ComboBox rangeComboBox;

    @FXML
    private TableView rangeHitTable, patternTable;

    @FXML
    private Label posLabel;

    public void init(Object[] lottoGameData){

        this.lottoGame = (LottoGame)lottoGameData[0];
        this.lottoDrawData = (List<Object>)lottoGameData[1];

        posLabel.textProperty().bind( new SimpleStringProperty("Currently Analyzing Position One" ));

        initialzie();

        addDrawPositionRadioButtons(((int[][])lottoDrawData.get(0)).length);

        int[][]data = (int[][])lottoDrawData.get(0);

        gameOutMapper = new GameOutMapper( lottoGame , data );

        ChartHelperTwo.clearGroupHitInformation();
        ChartHelperTwo.processIncomingData(lottoGame, data[currentDrawPosition], DEFAULT_DRAW_SIZE);

        Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
        populateGroupHitTable(positionData);
    }

    private void addDrawPositionRadioButtons(int length) {

        ToggleGroup toggleGroup = new ToggleGroup();
        for(int i = 0; i < length; i++){

            RadioButton radioButton = new RadioButton("P"+(i+1));
            radioButton.setStyle("-fx-text-fill: #dac6ac;");

            toggleGroup.getToggles().add( radioButton );

            drawPositoinHbox.getChildren().add( radioButton );

            final int pos = i;
            radioButton.setOnAction( action -> {

                rangeComboBox.getSelectionModel().select(1);

                currentDrawPosition = pos;

                int[][]data = (int[][])lottoDrawData.get(0);

                ChartHelperTwo.clearGroupHitInformation();
                ChartHelperTwo.processIncomingData(lottoGame, data[currentDrawPosition], DEFAULT_DRAW_SIZE);

                Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
                populateGroupHitTable(positionData);

                String poss = "";
                switch (currentDrawPosition){
                    case 0:
                        poss = "One";
                        break;
                    case 1:
                        poss = "Two";
                        break;
                    case 2:
                        poss = "Three";
                        break;
                    case 3:
                        poss = "Four";
                        break;
                    case 4:
                        poss = "Five";
                        break;
                    case 5:
                        poss = "Bonus";
                        break;
                }

                posLabel.textProperty().bind( new SimpleStringProperty("Currently Analyzing Position " + poss ));
            });
        }

        currentDrawPosition = 0;
        toggleGroup.getToggles().get(0).setSelected(true);
    }

    private void populatePatternMap(List<List<String>> values, String range){
        patternTable.refresh();
        patternTable.getItems().clear();
        patternTable.getColumns().clear();

        StringBuilder builder = new StringBuilder(range);
        builder.setCharAt(0,' ');
        builder.setCharAt(range.length() -1, ' ');

        String[] rangeValues = builder.toString().split(",");

        final int minVal = Integer.parseInt( rangeValues[0].trim() );
        final int maxVal = Integer.parseInt( rangeValues[1].trim() );

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        List<Integer> colNames = IntStream.range(minVal,maxVal+1).boxed().collect(Collectors.toList());

        int[] count = {0};
        for(int i = 0; i < colNames.size(); i++){

            final int j = i;
            TableColumn col = new TableColumn(colNames.get(i)+"");
            col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {

                @Override
                public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {

                    return new TableCell<ObservableList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {

                                setText(item);

                                if(item.equals("##"))
                                    this.setTextFill(Color.YELLOW);
                                else
                                    this.setTextFill(Color.BEIGE);

                                // System.out.println(param.getText());

//                                ObservableList observableList = getTableView().getItems().get(getIndex());
//
//                                if (observableList.get(2).toString().equalsIgnoreCase("0")) {
//                                    getTableView().getSelectionModel().select(getIndex());
//
//                                    if (getTableView().getSelectionModel().getSelectedItems().contains(observableList)) {
//
//                                        this.setTextFill(Color.valueOf("#76FF03"));
//                                    }
//
//                                    //System.out.println(getItem());
//                                    // Get fancy and change color based on data
//                                    if (item.equals("##"))
//                                     this.setTextFill(Color.valueOf("#76FF03"));
//                                }

                            }
                        }

                    };
                }
            });


            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())
            );

            col.setSortable(false);
            patternTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/

        for (int i = 0; i < values.get(0).size(); i++){

            ObservableList<String> row = FXCollections.observableArrayList();

            for(int j = 0; j < values.size(); j++){

                row.add( values.get(j).get(i) );
            }

            dataItems.add( row );
        }


        patternTable.setItems(dataItems);
        patternTable.scrollTo(values.get(0).size() - 1);

    }

    private void populateGroupHitTable(Map<String, Object[]> positionData) {
        rangeHitTable.refresh();
        rangeHitTable.getItems().clear();
        rangeHitTable.getColumns().clear();

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        // Create columns
        String[] colNames = {"Group","Hits","Gms Out","Hts At", "Avg Skps", "Hits Abv","Hits Blw","Eql To","Lst Seen"};
        for(int i = 0; i < colNames.length; i++){

            final int j = i;
            TableColumn col = new TableColumn(colNames[i]);
            col.setCellFactory(new Callback<TableColumn<ObservableList, String>, TableCell<ObservableList, String>>() {

                @Override
                public TableCell<ObservableList, String> call(TableColumn<ObservableList, String> param) {

                    return new TableCell<ObservableList, String>() {

                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (!isEmpty()) {

                                setText(item);
                                this.setTextFill(Color.BEIGE);


                                // System.out.println(param.getText());

                                ObservableList observableList = getTableView().getItems().get(getIndex());
                                if (observableList.get(2).toString().equalsIgnoreCase("0")) {
                                    getTableView().getSelectionModel().select(getIndex());

                                    if (getTableView().getSelectionModel().getSelectedItems().contains(observableList)) {

                                        this.setTextFill(Color.valueOf("#76FF03"));
                                    }

                                    //System.out.println(getItem());
                                    // Get fancy and change color based on data
                                    //if (item.contains("X"))
                                    //this.setTextFill(Color.valueOf("#EFA747"));
                                }

                                observableList.forEach( val -> {

                                    if(val.toString().contains("[")){

                                        this.setOnMouseClicked( event -> {

                                            List<List<String>> values = gameOutMapper.processDataRequest( val.toString(), currentDrawPosition );
                                            populatePatternMap(values, val.toString());
                                        });
                                    }
                                });
                            }
                        }

                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())
            );

            col.setSortable(false);
            rangeHitTable.getColumns().addAll(col);
        }


        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = positionData.size();
        for(Map.Entry<String,Object[]> data : positionData.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            String key = data.getKey();
            Object[] values = data.getValue();

            row.add(key);
            row.add(values[1]+"");
            row.add(values[2]+"");
            row.add(values[3]+"");
            row.add(values[5]+"");
            row.add(values[6]+"");
            row.add(values[7]+"");
            row.add(values[8]+"");
            row.add(values[9]+"");

            dataItems.add(row);
        }

        String range = positionData.keySet().iterator().next();

        List<List<String>> values = gameOutMapper.processDataRequest( range, currentDrawPosition );
        populatePatternMap(values, range);

        rangeHitTable.setItems(dataItems);
        //  groupInfoTable.scrollTo(size - 1);

    }

    public void initialzie(){
        rangeHitTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patternTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        drawPositoinHbox.getChildren().clear();

        rangeComboBox.getItems().addAll(2,3,5,6,7,8,9,10,15,16,20,24,30);
        rangeComboBox.getSelectionModel().select(1);

        rangeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            int[][]data = (int[][])lottoDrawData.get(0);

            ChartHelperTwo.clearGroupHitInformation();
            ChartHelperTwo.processIncomingData(lottoGame, data[currentDrawPosition], (int)newValue);

            Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
            populateGroupHitTable(positionData);
        });
    }
}
