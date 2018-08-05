package com.lottoanalysis.controllers;

import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.numbertracking.NumberMultipleAnalyzer;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import com.lottoanalysis.utilities.gameoutviewutilities.GameOutLottoHitFinder;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;

public class NumberMultipleController {

    private LottoGame lottoGame;
    private List<Object> lottoDrawData;
    private int[][] allPositionNumbers;
    private int[] positionData;
    private int currentDrawPosition;
    private Map<Integer,List<Integer>> multipleHolderMap;

    private NumberMultipleAnalyzer numberMultipleAnalyzer;

    private final int DEFAULT_DRAW_SIZE = 10;

    @FXML
    private HBox positionButtonHbox, components;

    @FXML
    private StackPane numberMultipleStackPane, multpleHitDirectionPane;

    @FXML
    private Label lbl_analyzedPosistion,groupHitOutlookLabel;

    @FXML
    private TableView groupInfoTable, gameOutTable;

    @FXML
    private ComboBox rangeComboBox;

    @FXML
    private RadioButton group;

    @FXML
    private TextArea multipleHitTextArea;

    public void init(Object[] lottoGame){

        this.lottoGame = (LottoGame)lottoGame[0];
        this.lottoDrawData = (List<Object>)lottoGame[1];
        numberMultipleAnalyzer = new NumberMultipleAnalyzer(this.lottoGame);

        int drawPositions = ((int[][])lottoDrawData.get(1)).length;
        this.allPositionNumbers = (int[][])lottoDrawData.get(0);

        populateDrawPositionsIntoHBox( drawPositions );
        initialize();

        addActionToComboBoxItems();

        analyzeData( currentDrawPosition );


        positionData = allPositionNumbers[currentDrawPosition];
        numberMultipleAnalyzer.analyzeDrawData( positionData );
        multipleHolderMap = numberMultipleAnalyzer.getMultipleHolderMap();

        for (int num : positionData)
            numberMultipleAnalyzer.analyzeLottoNumber(num);

        numberMultipleAnalyzer.computeHitsAtGamesOutAndLastAppearance();

        multipleHitTextArea.setText( numberMultipleAnalyzer.getFormattedOutPut() );

        setUpMultipleDirectionChart(NumberMultipleAnalyzer.getMultipleList());
        addActionOnMultipleButtons();
    }

    private void addActionOnMultipleButtons() {

        ObservableList<Toggle> multipleToggles = group.getToggleGroup().getToggles();

        multipleToggles.forEach( toggle -> {

            ((RadioButton)toggle).setOnAction( event -> {

                if(toggle.isSelected()){

                    String[] text = ((RadioButton) toggle).getText().split("");

                    int multiple = Integer.parseInt( text[0] );
                    List<Integer> values = multipleHolderMap.get( multiple );

                    List<Integer> minMaxVals = new ArrayList<>(values);
                    Collections.sort(minMaxVals);

                    injectChartWithData( values, Integer.toString(multiple) );

                    GameOutLottoHitFinder gameOutLottoHitFinder = new GameOutLottoHitFinder(minMaxVals.get(0),minMaxVals.get(minMaxVals.size() - 1),values);
                    setUpGameOutPatternChart( gameOutLottoHitFinder.getLottoNumberHitTracker() );

                }
            });
        });

        List<Integer> values = multipleHolderMap.get( 7 );
        injectChartWithData( values, Integer.toString(7) );

        List<Integer> minMaxVals = new ArrayList<>(values);
        Collections.sort(minMaxVals);

        GameOutLottoHitFinder gameOutLottoHitFinder = new GameOutLottoHitFinder(minMaxVals.get(0),minMaxVals.get(minMaxVals.size() - 1),values);
        setUpGameOutPatternChart( gameOutLottoHitFinder.getLottoNumberHitTracker() );
    }

    private void injectChartWithData(List<Integer> values, String title) {

        List<List<Integer>> dataPoints = new ArrayList<>();
        Set<Integer> range = new TreeSet<>(values);

        List<Integer> list = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(values)[0];

        dataPoints.add( (list.size() > 100) ? list.subList(list.size() - 100,list.size()) : list );
        //dataPoints.add( (values.size() > 250) ? values.subList(values.size() - 250,values.size()) : values );

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                lottoGame.getMinNumber(),
                lottoGame.getMaxNumber(), null, title + " Multiple",1223,297,8);


        numberMultipleStackPane.getChildren().add( lc.getLineChart());
    }

    private void addActionToComboBoxItems() {

        int[][] positionDataTwo =  (int[][])lottoDrawData.get(0);

        rangeComboBox.getSelectionModel().selectedItemProperty().addListener( (observable, oldValue, newValue) -> {

            ChartHelperTwo.clearGroupHitInformation();
            ChartHelperTwo.processIncomingData(lottoGame, positionDataTwo[currentDrawPosition], (int)newValue);

            Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
            populateGroupHitTable(positionData);

        });
    }

    private void setUpGameOutPatternChart(Map<Integer, Integer[]> lottoNumberHitTracker) {


        gameOutTable.refresh();

        gameOutTable.getItems().clear();
        gameOutTable.getColumns().clear();

        // Create columns
        String[] colNames = {"Lotto#","Hits","Prv G Out","Gms Out","Gm Out Hts","Game Out Lst Ht"};
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
                                if (observableList.get(3).toString().equalsIgnoreCase("0")) {
                                    getTableView().getSelectionModel().select(getIndex());

                                    if (getTableView().getSelectionModel().getSelectedItems().contains(observableList)) {

                                        this.setTextFill(Color.valueOf("#76FF03"));
                                    }

                                    //System.out.println(getItem());
                                    // Get fancy and change color based on data
                                    //if (item.contains("X"))
                                    //this.setTextFill(Color.valueOf("#EFA747"));
                                }
                            }
                        }
                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())



            );

            col.setSortable(false);
            gameOutTable.getColumns().addAll(col);
        }

        ObservableList<ObservableList> items = FXCollections.observableArrayList();
        /********************************
         * Data added to ObservableList *
         ********************************/
        for(Map.Entry<Integer,Integer[]> data : lottoNumberHitTracker.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer key = data.getKey();
            Integer[] values = data.getValue();

            row.add(key+"");
            row.add(values[0]+"");
            row.add(values[4]+"");
            row.add(values[1]+"");
            row.add(values[2]+"");
            row.add(values[3]+"");

            items.add(row);
        }

        gameOutTable.setItems(items);
    }

    public void initialize() {
        groupInfoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        gameOutTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        rangeComboBox.getItems().clear();
        rangeComboBox.getItems().addAll(2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30);
    }

    private void analyzeData(int currentDrawPosition) {

        int[][] positionDataTwo =  (int[][])lottoDrawData.get(0);

        ChartHelperTwo.clearGroupHitInformation();
        ChartHelperTwo.processIncomingData(lottoGame, positionDataTwo[currentDrawPosition], DEFAULT_DRAW_SIZE);

        Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();

        populateGroupHitTable( positionData );
    }

    private void populateGroupHitTable(Map<String, Object[]> positionData) {

        groupInfoTable.refresh();
        groupInfoTable.getItems().clear();
        groupInfoTable.getColumns().clear();

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        // Create columns
        String[] colNames = {"Group","Hits","Games Out","Hits At", "Avg Skips", "Hits Above","Hits Below","Equal To","Last Seen"};
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
                            }
                        }
                    };
                }
            });

            col.setCellValueFactory((Callback<TableColumn.CellDataFeatures<ObservableList, String>,
                    ObservableValue<String>>) param -> new SimpleStringProperty(param.getValue().get(j).toString())



            );

            col.setSortable(false);
            groupInfoTable.getColumns().addAll(col);
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

        groupInfoTable.setItems(dataItems);
        //  groupInfoTable.scrollTo(size - 1);
    }

    private void populateDrawPositionsIntoHBox(int drawPositions) {

        positionButtonHbox.getChildren().clear();

        ToggleGroup positionToggleGroup = new ToggleGroup();

        for(int i = 0; i < drawPositions; i++){

            final int val = i;

            RadioButton radioButton = new RadioButton("P"+(i+1));
            radioButton.setStyle("-fx-text-fill: #dac6ac;");
            radioButton.setOnAction( e -> {

                currentDrawPosition = val;
                String pos = "";
                switch (val){
                    case 0:
                        pos = "One";
                        break;
                    case 1:
                        pos = "Two";
                        break;
                    case 2:
                        pos = "Three";
                        break;
                    case 3:
                        pos = "Four";
                        break;
                    case 4:
                        pos = "Five";
                        break;
                    case 5:
                        pos = "Bonus";
                        break;
                }

                lbl_analyzedPosistion.textProperty().bind(new SimpleStringProperty("Currently Analyzing Position " + pos));
                groupHitOutlookLabel.textProperty().bind(new SimpleStringProperty("Group Hit Outlook Position " + pos));



                int[][] positionDataTwo =  (int[][])lottoDrawData.get(0);

                ChartHelperTwo.clearGroupHitInformation();
                ChartHelperTwo.processIncomingData(lottoGame, positionDataTwo[currentDrawPosition], DEFAULT_DRAW_SIZE);

                Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();

                rangeComboBox.getSelectionModel().select( DEFAULT_DRAW_SIZE -2);
                populateGroupHitTable(positionData);

                numberMultipleAnalyzer.analyzeDrawData( allPositionNumbers[currentDrawPosition] );
                multipleHolderMap = numberMultipleAnalyzer.getMultipleHolderMap();

                List<Integer> values = multipleHolderMap.get( 7 );
                injectChartWithData(values, Integer.toString(7));

                group.setSelected(true);

                NumberMultipleAnalyzer.getMultipleList().clear();
                numberMultipleAnalyzer.clear();
                for (int num : allPositionNumbers[currentDrawPosition])
                    numberMultipleAnalyzer.analyzeLottoNumber(num);

                numberMultipleAnalyzer.computeHitsAtGamesOutAndLastAppearance();

                multipleHitTextArea.setText( numberMultipleAnalyzer.getFormattedOutPut() );

                setUpMultipleDirectionChart(NumberMultipleAnalyzer.getMultipleList());

                addActionOnMultipleButtons();
            });

            positionToggleGroup.getToggles().add(radioButton);

            positionButtonHbox.getChildren().addAll( radioButton );

        }

        Toggle toggle = positionToggleGroup.getToggles().get(0);
        toggle.setSelected(true);

        lbl_analyzedPosistion.textProperty().bind(new SimpleStringProperty("Currently Analyzing Position One"));
        groupHitOutlookLabel.textProperty().bind(new SimpleStringProperty("Group Hit Outlook Position One"));

        currentDrawPosition = 0;
    }

    private void setUpMultipleDirectionChart(List<Integer> values) {

        List<List<Integer>> dataPoints = new ArrayList<>();
        Set<Integer> range = new TreeSet<>(values);

        List<Integer> list = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(values)[0];

        dataPoints.add( (list.size() > 100) ? list.subList(list.size() - 100,list.size()) : list );
        //dataPoints.add( (values.size() > 250) ? values.subList(values.size() - 250,values.size()) : values );

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                0,
                8, null, "Multiple Trend Direction Chart",1223,297,6);


        multpleHitDirectionPane.getChildren().add( lc.getLineChart());
    }


}
