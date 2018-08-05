package com.lottoanalysis.controllers;

import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.models.lottogames.LottoGame;
import com.lottoanalysis.models.lottogames.SixDigitLotteryGameImpl;
import com.lottoanalysis.models.lottogames.drawing.Drawing;
import com.lottoanalysis.models.gapspacings.GapSpacingAnalyzer;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import com.lottoanalysis.utilities.companionnumberutilities.CompanionNumberHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.IntStream;

public class GapSpacingController {

    private LottoGame lottoGame;
    private List<Object> lottoDrawData;

    private int[][] currentDrawData;
    private int[] bonuesData;

    private GapSpacingAnalyzer gapSpacingAnalyzer;

    private int currentDrawPosistion;

    private final int DRAW_SIZE = 10;
    private int indexPosition = 0;

    @FXML
    private MenuBar gameMenuBar;

    @FXML
    private TableView groupInfoTable, spacingTable, lineSpacingTable;

    @FXML
    private Label lbl_analyzedPosistion, groupHitOutlookLabel, lbl_analyzedNumber;

    @FXML
    private StackPane chartPane;


    public void init(Object[] lottoGameData) {

        initialize();

        this.lottoGame = (LottoGame)lottoGameData[0];
        this.lottoDrawData = (List<Object>)lottoGameData[1];
        gapSpacingAnalyzer = new GapSpacingAnalyzer();

        setUpMenuBar();
        assignValuesToLabels();


        populateChart();
    }

    private void populateChart() {

        currentDrawData = (int[][])lottoDrawData.get( indexPosition );
        //System.out.println(currentDrawData[currentDrawPosistion] );
        int[] v = currentDrawData[currentDrawPosistion];
        System.out.println(v[v.length-1]);
        ChartHelperTwo.clearGroupHitInformation();
        ChartHelperTwo.processIncomingData(lottoGame, currentDrawData[currentDrawPosistion], DRAW_SIZE);

        Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
        setUpGroupInfoTable( positionData );

        List<Integer> numList = (List<Integer>)positionData.get(positionData.keySet().iterator().next())[0];
        int[] data = numList.stream().mapToInt( i-> i).toArray();

        gapSpacingAnalyzer.formHitBuckets(0, 9);
        gapSpacingAnalyzer.analyzeSpacings( data );

        String phrase = (bonuesData != null) ? "Bonus Number" : "Lotto Number";

        lbl_analyzedNumber.textProperty().bind(
                new SimpleStringProperty(String.format("Currently Analyzing Spacings Against Current Winning %s: %s",phrase, gapSpacingAnalyzer.getWinningNumber())
                ));
        setUpSpacingHitTable( gapSpacingAnalyzer.getLineSpacingBuckets() );
        injectChartWithData( gapSpacingAnalyzer.getBucketHitHolder() );

    }

    private void setUpSpacingHitTable(Map<Integer, Object[]> lineSpacingBuckets) {

        spacingTable.refresh();

        spacingTable.getItems().clear();
        spacingTable.getColumns().clear();

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        String[] colNames = {"ID","Spacing Range","Hits","Games Out","Hits @ Games Out","Out Last Seen"};

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

                                observableList.forEach( val -> {

                                    if(val.toString().contains("[")){

                                        this.setOnMouseClicked( event -> {

                                            Map<Integer,Object[]> data = gapSpacingAnalyzer.getLineSpacingBuckets();
                                            String[] values = CompanionNumberHelper.stripBrackets(val.toString());
                                            data.forEach( (k,v) -> {

                                                GapSpacingAnalyzer.GameSpacingHitTracker da = (GapSpacingAnalyzer.GameSpacingHitTracker) v[0];

                                                if(da.getLineSpacingHitTracker().getLineSpacingHitTracker().containsKey(Integer.parseInt( values[0].trim() ))){

                                                    setUpSpacingTable(da);

                                                }
                                            });
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
            spacingTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = lineSpacingBuckets.size();
        for(Map.Entry<Integer,Object[]> data : lineSpacingBuckets.entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();
            GapSpacingAnalyzer.GameSpacingHitTracker gapSpacingAnlz = (GapSpacingAnalyzer.GameSpacingHitTracker) data.getValue()[0];
            List<Integer> range = (List<Integer>) data.getValue()[1];

            String id = gapSpacingAnlz.getId() + "";
            Object[] values = data.getValue();

            row.add(id);
            row.add(Arrays.toString(range.toArray()));
            row.add(gapSpacingAnlz.getHits() + "");
            row.add(gapSpacingAnlz.getGamesOut() + "");
            row.add(gapSpacingAnlz.getHitsAtGamesOut() + "");
            row.add(gapSpacingAnlz.getOutLastSeen() + "");

            dataItems.add(row);
        }

        spacingTable.setItems(dataItems);
        //  groupInfoTable.scrollTo(size - 1);
    }

    private void setUpSpacingTable(GapSpacingAnalyzer.GameSpacingHitTracker da) {

        lineSpacingTable.refresh();

        lineSpacingTable.getItems().clear();
        lineSpacingTable.getColumns().clear();

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        String[] colNames = {"Spacing","Hits","Games Out","Hits @ Games Out","Out Last Seen"};

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
            lineSpacingTable.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = da.getLineSpacingHitTracker().getLineSpacingHitTracker().size();
        for(Map.Entry<Integer,GapSpacingAnalyzer.LineSpacingHitTracker> data : da.getLineSpacingHitTracker().getLineSpacingHitTracker().entrySet()){

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();


            String id = data.getKey() + "";

            row.add(id);
            row.add(data.getValue().getHits() + "");
            row.add(data.getValue().getGamesOut() + "");
            row.add(data.getValue().getHitsAtGamesOut() + "");
            row.add(data.getValue().getLastSeen() + "");

            dataItems.add(row);
        }

        lineSpacingTable.setItems(dataItems);

    }

    /**
     *
     * @param bucketHitHolder
     */
    private void injectChartWithData(List<Integer> bucketHitHolder) {

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(bucketHitHolder);
        List<Integer> minMaxVals = new ArrayList<>(bucketHitHolder);
        Collections.sort(minMaxVals);

        List<Integer> specialList = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(bucketHitHolder)[0];

//        BollingerBand bollingerBand = new BollingerBand(bucketHitHolder,5,100);
//        List<List<Integer>> data = bollingerBand.getBollingerBands();


//        data.forEach( val -> {
//            dataPoints.add(val);
//        });
        dataPoints.add((specialList.size() > 180) ? specialList.subList(specialList.size()-180,specialList.size()) : specialList);
        //dataPoints.add((bucketHitHolder.size() > 180) ? bucketHitHolder.subList(bucketHitHolder.size()-180,bucketHitHolder.size()) : bucketHitHolder);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Line Spacing Hit ID Chart",789,364,6);

        chartPane.getChildren().setAll( lc.getLineChart() );
    }

    private void initialize(){

        groupInfoTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        spacingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        lineSpacingTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
    /**
     *
     * @param positionData
     */
    private void setUpGroupInfoTable(Map<String, Object[]> positionData) {

        groupInfoTable.refresh();

        groupInfoTable.getItems().clear();
        groupInfoTable.getColumns().clear();

        int[] index = {0};

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

                                observableList.forEach( val -> {

                                    int[] count = {0};
                                    if(val.toString().contains("[")){

                                        this.setOnMouseClicked( event -> {

                                            String[] values = CompanionNumberHelper.stripBrackets(val.toString());
                                            gapSpacingAnalyzer.formHitBuckets( Integer.parseInt(values[0].trim()), Integer.parseInt(values[1].trim()));

                                            List<Integer> numList = (List<Integer>)positionData.get(val.toString())[0];
                                            int[] data = numList.stream().mapToInt( i-> i).toArray();

                                            gapSpacingAnalyzer.analyzeSpacings( data );
                                            lbl_analyzedNumber.textProperty().bind(
                                                    new SimpleStringProperty(String.format("Currently Analyzing Spacings Against Current Winning Lotto Number: %s", gapSpacingAnalyzer.getWinningNumber())
                                                    ));
                                            setUpSpacingHitTable( gapSpacingAnalyzer.getLineSpacingBuckets() );
                                            injectChartWithData( gapSpacingAnalyzer.getBucketHitHolder() );

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

        String[] values = CompanionNumberHelper.stripBrackets( positionData.keySet().iterator().next() );
        gapSpacingAnalyzer.formHitBuckets( Integer.parseInt(values[0].trim()), Integer.parseInt(values[1].trim()));

        List<Integer> numList = (List<Integer>)positionData.get(positionData.keySet().iterator().next() )[0];
        int[] data = numList.stream().mapToInt( i-> i).toArray();

        gapSpacingAnalyzer.analyzeSpacings( data );
        lbl_analyzedNumber.textProperty().bind(
                new SimpleStringProperty(String.format("Currently Analyzing Spacings Against Current Winning Lotto Number: %s", gapSpacingAnalyzer.getWinningNumber())
                ));
        injectChartWithData( gapSpacingAnalyzer.getBucketHitHolder() );
        setUpSpacingHitTable( gapSpacingAnalyzer.getLineSpacingBuckets() );

    }

    /**
     *
     */
    private void assignValuesToLabels() {
        lbl_analyzedPosistion.setPadding( new Insets(0.0,0.0,0.0,2.0));
        lbl_analyzedPosistion.textProperty().bind(
                new SimpleStringProperty(String.format("Currently Analyzing %s",getPosistionAsString(currentDrawPosistion)))
        );

        groupHitOutlookLabel.textProperty().bind(
                new SimpleStringProperty(String.format("Group Hit Outlook %s",getPosistionAsString(currentDrawPosistion)))
        );
    }

    /**
     *
     */
    private void setUpMenuBar() {

        // clear all existing menus
        gameMenuBar.getMenus().clear();

        List<MenuItem> positionMenuItems = new ArrayList<>();


        Menu positionMenu = new Menu("Draw Positions");
        int size = ((int[][])lottoDrawData.get(0)).length;
        int incrementSize = (lottoGame instanceof SixDigitLotteryGameImpl) ? 1:0;

        for(int i = 0; i < size + incrementSize; i++){

            MenuItem item = new MenuItem(getPosistionAsString(i));
            positionMenuItems.add( item );
            positionMenu.getItems().add( item );
        }

        Menu analysisMethod = new Menu("Analysis Method");
        String[] methods = {"Position Numbers","Position Sums","Position Remainders","Position Last Digits","Delta Numbers"};
        for(int i = 0; i < methods.length; i++){

            final int pos = i;
            MenuItem item = new MenuItem(methods[i]);

            item.setOnAction( e -> {

                populateChart(item.getText());

            });

            analysisMethod.getItems().add(item);
        }

        Menu rangeMenu = new Menu("Range");
        for(int i = 5; i <= 70; i++){
            if(i % 5 == 0 ){

                MenuItem item = new MenuItem(i + "");
                item.setOnAction( e -> {

                    int[] data = (bonuesData == null) ? currentDrawData[currentDrawPosistion] : bonuesData;

                    ChartHelperTwo.clearGroupHitInformation();
                    ChartHelperTwo.processIncomingData(lottoGame, data, Integer.parseInt(item.getText()));

                    Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
                    setUpGroupInfoTable( positionData );


                });
                rangeMenu.getItems().add( item );
            }
        }

        placeActionsOnMenuItems( positionMenuItems );

        gameMenuBar.getMenus().addAll( positionMenu, rangeMenu, analysisMethod );

    }

    private void populateChart(String text) {

       // "Position Numbers","Position Sums","Position Remainders","Position Last Digits"
        switch (text){

            case "Position Numbers":
                indexPosition = 0;
                break;
            case "Position Sums":
                indexPosition = 2;
                break;
            case "Position Remainders":
                indexPosition = 4;
                break;
            case "Position Last Digits":
                indexPosition = 5;
                break;
            case"Delta Numbers":
                indexPosition = 1;
                break;

        }
        populateChart();
    }

    /**
     *
     * @param positionMenuItems
     */
    private void placeActionsOnMenuItems(List<MenuItem> positionMenuItems) {

        IntStream.range(0, positionMenuItems.size()).forEach( num -> {

            MenuItem menuItem = positionMenuItems.get( num );


            if(menuItem.getText().equals("Bonus Number")){

                menuItem.setOnAction(e -> {

                    currentDrawPosistion = num;
                    List<Integer> bonusData = Drawing.extractBonusNumbersFromDrawing( lottoGame );
                    bonuesData = bonusData.stream().mapToInt(i -> i).toArray();

                    ChartHelperTwo.clearGroupHitInformation();
                    ChartHelperTwo.processIncomingData(lottoGame, bonuesData, DRAW_SIZE);

                    Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
                    setUpGroupInfoTable(positionData);

                    gapSpacingAnalyzer.formHitBuckets(0, 9);
                    gapSpacingAnalyzer.analyzeSpacings( bonuesData );
                    injectChartWithData(gapSpacingAnalyzer.getBucketHitHolder());

                    String phrase = (bonuesData != null) ? "Bonus Number" : "Lotto Number";

                    lbl_analyzedNumber.textProperty().bind(
                            new SimpleStringProperty(String.format("Currently Analyzing Spacings Against Current Winning %s: %s",phrase, gapSpacingAnalyzer.getWinningNumber())
                            ));
                    assignValuesToLabels();

                });

            }
            else {

                menuItem.setOnAction(e -> {

                    bonuesData = null;
                    currentDrawPosistion = num;
                    assignValuesToLabels();

                    ChartHelperTwo.clearGroupHitInformation();
                    ChartHelperTwo.processIncomingData(lottoGame, currentDrawData[currentDrawPosistion], DRAW_SIZE);

                    Map<String, Object[]> positionData = ChartHelperTwo.getGroupHitInformation();
                    setUpGroupInfoTable(positionData);

                    populateChart();
                });

            }
        });

    }

    /**
     *
     * @param posistion
     * @return
     */
    private String getPosistionAsString( int posistion ){

        switch (posistion ){

            case 0:
                return "Position One";
            case 1:
                return "Position Two";
            case 2:
                return "Position Three";
            case 3:
                return "Position Four";
            case 4:
                return "Position Five";
            case 5:
                return "Bonus Number";
            default:
                return "";
        }
    }
}
