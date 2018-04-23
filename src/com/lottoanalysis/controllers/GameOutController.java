package com.lottoanalysis.controllers;

import com.lottoanalysis.charts.LineChartWithHover;
import com.lottoanalysis.lottogames.*;
import com.lottoanalysis.models.gameoutanalyzers.GameOutHitGrouper;
import com.lottoanalysis.models.gameoutanalyzers.GameOutMapper;
import com.lottoanalysis.models.technicalindicators.BollingerBand;
import com.lottoanalysis.utilities.analyzerutilites.NumberPatternAnalyzer;
import com.lottoanalysis.utilities.analyzerutilites.TrendLineAnalyzer;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
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
import org.omg.PortableInterceptor.INACTIVE;

import java.lang.reflect.GenericArrayType;
import java.util.*;
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
    private TableView rangeHitTable, patternTable, gameOutHitTable, individualGameOutTable;

    @FXML
    private Label posLabel;

    @FXML
    private StackPane chartStackPane,numberMultpleTextArea;

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

        GameOutHitGrouper gameOutHitGrouper = gameOutMapper.getGameOutHitGrouper( currentDrawPosition );
        Map<String,Object[]> gameOutHitGrouperData = gameOutHitGrouper.getGameOutGroupHolderMap();
        populateGameOutTable( gameOutHitGrouperData );
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

                if(lottoGame instanceof PickFourLotteryGameImpl || lottoGame instanceof PickThreeLotteryGameImpl)
                    rangeComboBox.getSelectionModel().select(1);
                else
                    rangeComboBox.getSelectionModel().select(7);

                currentDrawPosition = pos;

                int[][]data = (int[][])lottoDrawData.get(0);

                ChartHelperTwo.clearGroupHitInformation();
                ChartHelperTwo.processIncomingData(lottoGame, data[currentDrawPosition], DEFAULT_DRAW_SIZE);

                Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
                populateGroupHitTable(positionData);

                GameOutHitGrouper gameOutHitGrouper = gameOutMapper.getGameOutHitGrouper( currentDrawPosition );
                Map<String,Object[]> gameOutHitGrouperData = gameOutHitGrouper.getGameOutGroupHolderMap();
                populateGameOutTable( gameOutHitGrouperData );

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


    private void populateGameOutTable(Map<String, Object[]> gameOutHitGrouperData) {

        gameOutHitTable.refresh();
        gameOutHitTable.getItems().clear();
        gameOutHitTable.getColumns().clear();

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        // Create columns
        String[] colNames = {"Out Group","Group Hits","Games Out"};
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

                                    if(val.toString().contains(",")){

                                        this.setOnMouseClicked( event -> {

                                            GameOutHitGrouper gameOutHitGrouper = gameOutMapper.getGameOutHitGrouper(currentDrawPosition);
                                            Map<Integer,Integer[]> gameOutHitGrouperData = gameOutHitGrouper.getGameOutTracker( val.toString() );
                                            populateIndividualLottonumberTable( gameOutHitGrouperData );

                                            List<Integer> chartPoints = (List<Integer>) gameOutHitGrouper.getGameOutGroupHolderMap().get(val.toString())[2];
                                            injectChartWithData( chartPoints );


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
            gameOutHitTable.getColumns().addAll(col);
        }


        /********************************
         * Data added to ObservableList *
         ********************************/
        //int size = positionData.size();
        for(Map.Entry<String,Object[]> data : gameOutHitGrouperData.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            String key = data.getKey();
            Object[] values = data.getValue();

            row.add(key);
            row.add(values[0]+"");
            row.add(values[1]+"");

            dataItems.add(row);
        }

        gameOutHitTable.setItems(dataItems);
        //groupInfoTable.scrollTo(size - 1);

    }

    private void injectChartWithData(List<Integer> chartPoints) {


        int[] nums = chartPoints.stream().mapToInt(Integer::intValue).toArray();
       // TrendLineAnalyzer.analyzeData(nums);

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>)data[0];


        //List<Integer> movingAverages = GroupChartController.calculateMovingAverage(specialList);
//        BollingerBand bollingerBand = new BollingerBand(chartPoints,5,100);
//        List<List<Integer>> data = bollingerBand.getBollingerBands();


//        data.forEach( val -> {
//            dataPoints.add(val);
//        });
        dataPoints.add((specialList.size() > 150) ? specialList.subList(specialList.size()- 150,specialList.size()) : specialList);
       // dataPoints.add((movingAverages.size() > 150) ? movingAverages.subList(movingAverages.size()-150,movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart",654,346);

        chartStackPane.getChildren().setAll( lc.getLineChart() );
        numberMultpleTextArea.getChildren().setAll(new TextArea((String)data[1]));
    }

    private void populateIndividualLottonumberTable(Map<Integer, Integer[]> gameOutHitGrouperData) {

        individualGameOutTable.refresh();
        individualGameOutTable.getItems().clear();
        individualGameOutTable.getColumns().clear();

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        // Create columns
        String[] colNames = {"Out Digit","Out Hits","Games Out"};
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

                                    if(val.toString().contains(",") ){

                                        this.setOnMouseClicked( event -> {

                                            GameOutHitGrouper gameOutHitGrouper = gameOutMapper.getGameOutHitGrouper(currentDrawPosition);
                                            Map<Integer,Integer[]> gameOutHitGrouperData = gameOutHitGrouper.getGameOutTracker( val.toString() );
                                            populateIndividualLottonumberTable( gameOutHitGrouperData );
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
            individualGameOutTable.getColumns().addAll(col);
        }


        /********************************
         * Data added to ObservableList *
         ********************************/
        //int size = positionData.size();
        for(Map.Entry<Integer,Integer[]> data : gameOutHitGrouperData.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer key = data.getKey();
            Integer[] values = data.getValue();

            row.add(key+"");
            row.add(values[0]+"");
            row.add(values[1]+"");

            dataItems.add(row);
        }

        individualGameOutTable.setItems(dataItems);
        //groupInfoTable.scrollTo(size - 1);
    }

    private void populatePatternMap(List<List<String>> values, String range){
        patternTable.refresh();
        patternTable.getItems().clear();
        patternTable.getColumns().clear();

        int[] rangeValues = computeRange(range);

        int minVal = rangeValues[0];

        final int maxVal = rangeValues[1];

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
                                else if( item.contains("P"))
                                    this.setTextFill(Color.valueOf("#409EB2"));
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

        String range = positionData.keySet().iterator().next();
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
                                            values.removeIf(list -> list.get(list.size() - 1).equals(Integer.toString(list.size())));

                                            populatePatternMap(values, val.toString());

                                            Map<Integer,Map<String,Integer[]>> currentGameOutCompanionMap =
                                                    getCurrentGameOutCompanionHits( values, computeRange(val.toString()) );


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



        List<List<String>> values = gameOutMapper.processDataRequest( range, currentDrawPosition );
        values.removeIf(list -> list.get(list.size() - 1).equals(Integer.toString(list.size())));

        populatePatternMap(values, range);
        Map<Integer,Map<String,Integer[]>> currentGameOutCompanionMap = getCurrentGameOutCompanionHits( values, computeRange(range) );

        rangeHitTable.setItems(dataItems);
        //  groupInfoTable.scrollTo(size - 1);

    }

    private int[] computeRange(String range) {

        int[] rangeTwo = new int[2];

        StringBuilder builder = new StringBuilder(range);
        builder.setCharAt(0,' ');
        builder.setCharAt(range.length() -1, ' ');

        String[] rangeValues = builder.toString().split(",");
        rangeTwo[0] = Integer.parseInt(rangeValues[0].trim());
        rangeTwo[1] = Integer.parseInt(rangeValues[1].trim());

        if(lottoGame instanceof PickFourLotteryGameImpl || lottoGame instanceof PickThreeLotteryGameImpl)
            rangeTwo[0] = 0;
        else{

            if(rangeTwo[0] == 0)
                rangeTwo[0] = 1;

        }

        rangeValues[0] = rangeTwo[0]+"";
        rangeValues[1] = rangeTwo[1]+"";

        return Arrays.stream(rangeValues).map(String::trim).mapToInt(Integer::parseInt).toArray();
    }

    private Map<Integer,Map<String,Integer[]>> getCurrentGameOutCompanionHits(List<List<String>> values, int[] range) {

        List<String> gameOutHolder = new ArrayList<>();

        List<Integer[]> indexHolderList = new ArrayList<>();
        for(List<String> list : values){

            String currentPattern = list.get( list.size() - 1);
            int[] indexes = IntStream.range(0,list.size() - 1).filter( pat -> list.get(pat).equals(currentPattern)).toArray();
            indexHolderList.add(Arrays.stream( indexes ).boxed().toArray(Integer[]::new));
        }

        Map<Integer, Map<String,Integer[]>> data = new LinkedHashMap<>();

        for(int i = range[0]; i <= range[1]; i++){

            if(!data.containsKey(i)){

                Map<String,Integer[]> innerData = new LinkedHashMap<>();

                data.put(i, innerData);
            }
        }

        int indexer = 0;
        for(Map.Entry<Integer,Map<String,Integer[]>> stringEntry : data.entrySet()){

            Map<String,Integer[]> valuess = stringEntry.getValue();
            Integer[] hitIndexes = indexHolderList.get(indexer);

            for(int i = 0; i < hitIndexes.length; i++){

                String val = values.get(indexer).get( hitIndexes[i] + 1);
                if(!valuess.containsKey(val)){

                    valuess.put(val, new Integer[]{1,0});
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(valuess,val);
                }
                else
                {
                    Integer[] d = valuess.get( val );

                    if(val.equals("##")){

                        gameOutHolder.add(d[1]+"");
                    }

                    d[0]++;
                    d[1] = 0;
                    NumberPatternAnalyzer.incrementGamesOutForMatrix(valuess,val);
                }
            }

            indexer++;
        }

        return data;
    }

    public void initialzie(){
        rangeHitTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        patternTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        gameOutHitTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        individualGameOutTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        drawPositoinHbox.getChildren().clear();

        rangeComboBox.getItems().addAll(2,3,5,6,7,8,9,10,15,16,20,24,30);

        if(lottoGame instanceof PickFourLotteryGameImpl || lottoGame instanceof PickThreeLotteryGameImpl)
            rangeComboBox.getSelectionModel().select(1);
        else
            rangeComboBox.getSelectionModel().select(7);

        rangeComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {

            int[][]data = (int[][])lottoDrawData.get(0);

            ChartHelperTwo.clearGroupHitInformation();
            ChartHelperTwo.processIncomingData(lottoGame, data[currentDrawPosition], (int)newValue);

            Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
            populateGroupHitTable(positionData);
        });
    }
}
