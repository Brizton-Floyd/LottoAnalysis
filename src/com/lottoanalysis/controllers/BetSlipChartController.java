package com.lottoanalysis.controllers;

import com.lottoanalysis.charts.LineChartWithHover;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.gapspacings.GameOutSpacing;
import com.lottoanalysis.models.numbertracking.FirstLastDigitTracker;
import com.lottoanalysis.utilities.betsliputilities.BetSlipAnalyzer;
import com.lottoanalysis.utilities.betsliputilities.ColumnAndIndexHitAnalyzer;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import com.lottoanalysis.utilities.gameoutviewutilities.GamesOutViewDepicter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unchecked")
public class BetSlipChartController {


    private BetSlipAnalyzer betSlipAnalyzer;
    private LottoGame lottoGame;
    private List<Object> lottoDrawData;
    private int[] currentDrawIndex = new int[1];
    private ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer;
    private Map<Integer,GameOutSpacing> gameOutSpacingMap;

    private int[][] currentDrawData;

    @FXML
    private MenuBar menuBar;

    @FXML
    private Label drawPosLbl, avgLbl;

    @FXML
    private StackPane totalHitPerformancePane, firstDigitHitPane, betSlipPane, columnHitStackPane, lottoNumberStackPane, lastDigitPane,
            directionStackPane;

    public void init(Object[] lottoGameData) {

        this.lottoGame = (LottoGame) lottoGameData[0];
        this.lottoDrawData = (List<Object>) lottoGameData[1];
        currentDrawData = (int[][]) lottoDrawData.get(0);

        initializeData();

        betSlipAnalyzer = new BetSlipAnalyzer();

        populatePanesPanes(DrawPosition.PositionOne, "10");
    }

    private void initializeData() {

        setUpMenuBar();
    }

    private void setUpMenuBar() {

        DrawPosition[] positions = {DrawPosition.PositionOne, DrawPosition.PositionTwo, DrawPosition.PositionThree,
                DrawPosition.PositionFour, DrawPosition.PositionFive, DrawPosition.BonusPosition};
        menuBar.getMenus().clear();

        Menu positionMenu = new Menu("Draw Positions");
        for (int i = 0; i < currentDrawData.length; i++) {

            final int index = i;
            MenuItem item = new MenuItem("Position " + (i + 1));
            item.setOnAction(e -> {

                currentDrawIndex[0] = positions[index].getIndex();
                drawPosLbl.textProperty().bind(new SimpleStringProperty(String.format("Currently Analyzing Position %s", positions[index].getIndex() + 1)));

                populatePanesPanes(positions[index], "10");
            });
            positionMenu.getItems().add(item);
        }

        Menu gameSpanMenu = new Menu("Game Span Range");
        for (int i = 5; i <= 40; i++) {

            MenuItem item;
            if (i % 5 == 0) {
                item = new MenuItem(Integer.toString(i));
                item.setOnAction(e -> {

                    for (DrawPosition drawPosition : positions) {

                        if (drawPosition.getIndex() == currentDrawIndex[0]) {

                            populatePanesPanes(drawPosition, item.getText());
                            break;
                        }
                    }
                });
                gameSpanMenu.getItems().add(item);
            }
        }

        Menu betSlipColumnMenu = new Menu("Bet Slip Column Number Analysis");
        MenuItem item;
        for (int i = 0; i < currentDrawData.length; i++) {

            MenuItem items = new MenuItem(String.format("Column %s", i + 1));
            items.setOnAction(event -> {

                List<Integer> numbers = new ArrayList<>();
                List<ColumnPosition> columnPositions = ColumnPosition.getPositions();
                boolean breakOuterLoop = false;
                for (ColumnPosition columnPosition : columnPositions) {

                    if (items.getText().contains(Integer.toString(columnPosition.getColumnIndex()))) {

                        for (Map.Entry<Integer, Object[]> entry : columnAndIndexHitAnalyzer.getEntries()) {

                            if (entry.getKey() == columnPosition.getColumnIndex()) {

                                List<Integer> data = new ArrayList<>(((Map<Integer, Integer[]>) entry.getValue()[2]).keySet());
                                columnAndIndexHitAnalyzer.getDigitHolder().forEach(digit -> {

                                    if (data.contains(digit)) {
                                        numbers.add(digit);
                                    }
                                });

                                breakOuterLoop = true;
                                break;
                            }
                        }
                    }

                    if (breakOuterLoop)
                        break;
                }

                gameOutSpacingMap = GamesOutViewDepicter.analyzeData( numbers );
                setUpRecentWinningNumberDirectionChart(GameOutSpacing.getHitDirectionIdHolder());
                setUpWinningNumberChart(numbers);
            });

            betSlipColumnMenu.getItems().add(items);
        }


        item = new MenuItem("All Column Numbers");
        item.setOnAction(event -> {

            gameOutSpacingMap = GamesOutViewDepicter.analyzeData( columnAndIndexHitAnalyzer.getDigitHolder() );
            setUpRecentWinningNumberDirectionChart(GameOutSpacing.getHitDirectionIdHolder());
            setUpWinningNumberChart(columnAndIndexHitAnalyzer.getDigitHolder());
        });
        betSlipColumnMenu.getItems().add(0, item);

        menuBar.getMenus().setAll(positionMenu, gameSpanMenu, betSlipColumnMenu);
    }

    private void populatePanesPanes(DrawPosition drawPosition, String span) {

        if (betSlipAnalyzer != null) {

            betSlipAnalyzer = null;
            System.gc();

        }

        betSlipAnalyzer = new BetSlipAnalyzer();
        betSlipAnalyzer.analyzeDrawData(currentDrawData, lottoGame, Integer.parseInt(span));

        avgLbl.textProperty().bind(new SimpleStringProperty(String.format("Average Winning Numbers In Game Span of %s is: %s ", span, betSlipAnalyzer.getAvgHits())));

        ColumnAndIndexHitAnalyzer[] columnAndIndexHitAnalyzers = betSlipAnalyzer.getColumnAndIndexHitAnalyzers();
        columnAndIndexHitAnalyzer = columnAndIndexHitAnalyzers[drawPosition.getIndex()];

        setUpTotalHitPane();
        setUpFirstDigitPane(columnAndIndexHitAnalyzer);
        setUpBetSlipColumnPane(columnAndIndexHitAnalyzer);
        setUpColumnHitTrackerChart(columnAndIndexHitAnalyzer.getColumnHitTracker());
        setUpWinningNumberChart(columnAndIndexHitAnalyzer.getDigitHolder());
        gameOutSpacingMap = GamesOutViewDepicter.analyzeData( columnAndIndexHitAnalyzer.getDigitHolder() );
        GamesOutViewDepicter.analyzeData( columnAndIndexHitAnalyzer.getDigitHolder() );
        setUpRecentWinningNumberDirectionChart( GameOutSpacing.getHitDirectionIdHolder() );
    }

    private void setUpRecentWinningNumberDirectionChart(List<Integer> lastDigitHolder) {

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(lastDigitHolder);
        List<Integer> minMaxVals = new ArrayList<>(lastDigitHolder);
        Collections.sort(minMaxVals);

        List<Integer> specialList = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(lastDigitHolder)[0];
        // List<Integer> movingAverages = GroupChartController.calculateMovingAverage( lastDigitHolder );

//        BollingerBand bollingerBand = new BollingerBand(bucketHitHolder,5,100);
//        List<List<Integer>> data = bollingerBand.getBollingerBands();


//        data.forEach( val -> {
//            dataPoints.add(val);
//        });
        dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        // dataPoints.add( (movingAverages.size() > 180) ? movingAverages.subList(movingAverages.size()-180,movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                -1,
                4, unique.toString(), "Recent Winning Number Trend Direction Chart", 1014, 316, 4);

        lastDigitPane.getChildren().setAll(lc.getLineChart());

        setUpRecentWinninNumberTable();
    }

    private void setUpRecentWinninNumberTable() {


        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        Map<Integer, Integer[]> totalNumberPresentTracker = betSlipAnalyzer.getTotalNumberPresentTracker();

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {"ID", "Direction", "Hits","Gms Out","Hts @ Out","Lst Seen"};

        for (int i = 0; i < colNames.length; i++) {

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
            tableView.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        GameOutSpacing gameOutSpacing = gameOutSpacingMap.entrySet().iterator().next().getValue();
        for (Map.Entry<String, GameOutSpacing> data : gameOutSpacing.getDirectionCountHolder().entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer id = data.getValue().getId();
            String direction = data.getKey();

            row.add(String.format("%s", id));
            row.add(direction);
            row.add(data.getValue().getHits()+"");
            row.add(data.getValue().getGamesOut()+"");
            row.add(data.getValue().getHitsAtGamesOut()+"");
            row.add(data.getValue().getOutLastSeen()+"");


            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);
        directionStackPane.getChildren().setAll(tableView);
    }

    private void setUpWinningNumberChart(List<Integer> digitHolder) {
        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(digitHolder);
        List<Integer> minMaxVals = new ArrayList<>(digitHolder);
        Collections.sort(minMaxVals);

        List<Integer> specialList = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(digitHolder)[0];
        // List<Integer> movingAverages = GroupChartController.calculateMovingAverage( digitHolder );

//        BollingerBand bollingerBand = new BollingerBand(bucketHitHolder,5,100);
//        List<List<Integer>> data = bollingerBand.getBollingerBands();


//        data.forEach( val -> {
//            dataPoints.add(val);
//        });
        //dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        dataPoints.add((digitHolder.size() > 100) ? digitHolder.subList(digitHolder.size() - 100, digitHolder.size()) : digitHolder);
        // dataPoints.add( (movingAverages.size() > 180) ? movingAverages.subList(movingAverages.size()-180,movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Winning Number Chart", 1014, 316, 10);

        lottoNumberStackPane.getChildren().setAll(lc.getLineChart());


    }

    private void setUpColumnHitTrackerChart(List<Integer> columnHitTracker) {


        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(columnHitTracker);
        List<Integer> minMaxVals = new ArrayList<>(columnHitTracker);
        Collections.sort(minMaxVals);

        List<Integer> specialList = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(columnHitTracker)[0];
        // List<Integer> movingAverages = GroupChartController.calculateMovingAverage( columnHitTracker );

//        BollingerBand bollingerBand = new BollingerBand(bucketHitHolder,5,100);
//        List<List<Integer>> data = bollingerBand.getBollingerBands();


//        data.forEach( val -> {
//            dataPoints.add(val);
//        });
        dataPoints.add((specialList.size() > 180) ? specialList.subList(specialList.size() - 180, specialList.size()) : specialList);
        //dataPoints.add( (movingAverages.size() > 180) ? movingAverages.subList(movingAverages.size()-180,movingAverages.size()) : movingAverages);

        //dataPoints.add((bucketHitHolder.size() > 180) ? bucketHitHolder.subList(bucketHitHolder.size()-180,bucketHitHolder.size()) : bucketHitHolder);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Column Hit Chart", 1014, 320, 6);

        columnHitStackPane.getChildren().setAll(lc.getLineChart());

    }

    private void setUpBetSlipColumnPane(ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer) {

        VBox box = new VBox();
        StackPane upperTable = setUpColumnHitInformationTable(columnAndIndexHitAnalyzer);

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        List<Integer> columns = new ArrayList<>();
        for (Map.Entry<Integer, Object[]> entry : columnAndIndexHitAnalyzer.getEntries()) {

            columns.add(entry.getKey());
        }

        String[] colNames = columns.stream().map(string -> Integer.toString(string)).toArray(String[]::new);

        for (int i = 0; i < colNames.length; i++) {

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

//                                ObservableList observableList = getTableView().getItems().get(getIndex());
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
//                                    //if (item.contains("X"))
//                                    //this.setTextFill(Color.valueOf("#EFA747"));
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
            tableView.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = currentDrawData.length;
        List<List<String>> columnData = new ArrayList<>();
        for (Map.Entry<Integer, Object[]> data : columnAndIndexHitAnalyzer.getEntries()) {

            //Iterate Row
            List<String> list = new ArrayList<>();
            Iterator<Integer> iterator = ((LinkedHashMap) data.getValue()[2]).keySet().iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next() + "");
            }
            List<Integer> intList = list.stream().map(Integer::parseInt).collect(Collectors.toList());
            Collections.sort(intList);

            list = intList.stream().map(num -> Integer.toString(num)).collect(Collectors.toList());
            columnData.add(list);
        }

        int[] max = {Integer.MIN_VALUE};
        columnData.forEach(list -> {

            max[0] = Math.max(list.size(), max[0]);
        });

        columnData.forEach(list -> {

            while (list.size() < max[0])
                list.add(0, "-1");

        });


        for (int i = 0; i < columnData.get(0).size(); i++) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            for (int j = 0; j < columnData.size(); j++) {

                row.add(columnData.get(j).get(i) + "");
            }

            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);

        box.getChildren().addAll(upperTable, tableView);
        betSlipPane.getChildren().setAll(box);
    }

    private StackPane setUpColumnHitInformationTable(ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer) {

        StackPane pane = new StackPane();
        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        String[] colNames = {"Column Number", "Hits", "Games Out"};

        for (int i = 0; i < colNames.length; i++) {

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
            tableView.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = currentDrawData.length;
        for (Map.Entry<Integer, Object[]> data : columnAndIndexHitAnalyzer.getEntries()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer key = data.getKey();
            Object[] values = data.getValue();

            row.add(key + "");
            row.add(values[0] + "");
            row.add(values[1] + "");


            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);
        pane.getChildren().setAll(tableView);

        return pane;
    }

    private void setUpFirstDigitPane(ColumnAndIndexHitAnalyzer columnAndIndexHitAnalyzer) {

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        FirstLastDigitTracker firstLastDigitTracker = columnAndIndexHitAnalyzer.getFirstLastDigitTracker();

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {"First Digit", "Hits", "Games Out"};

        for (int i = 0; i < colNames.length; i++) {

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
            tableView.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = currentDrawData.length;
        for (Map.Entry<Integer, Integer[]> data : firstLastDigitTracker.getDigitTracker().entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer key = data.getKey();
            Integer[] values = data.getValue();

            row.add(key + "");
            row.add(values[0] + "");
            row.add(values[1] + "");


            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        firstDigitHitPane.getChildren().setAll(tableView);

    }

    private void setUpTotalHitPane() {

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        Map<Integer, Integer[]> totalNumberPresentTracker = betSlipAnalyzer.getTotalNumberPresentTracker();

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {"Correct Winning #'s", "Hits", "Games Out"};

        for (int i = 0; i < colNames.length; i++) {

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

//                                ObservableList observableList = getTableView().getItems().get(getIndex());
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
//                                    //if (item.contains("X"))
//                                    //this.setTextFill(Color.valueOf("#EFA747"));
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
            tableView.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        int size = currentDrawData.length;
        for (Map.Entry<Integer, Integer[]> data : totalNumberPresentTracker.entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer key = data.getKey();
            Integer[] values = data.getValue();

            row.add(String.format("%s / %s", key, size));
            row.add(values[0] + "");
            row.add(values[1] + "");


            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);
        totalHitPerformancePane.getChildren().setAll(tableView);
    }
}

enum DrawPosition {

    PositionOne(0), PositionTwo(1), PositionThree(2), PositionFour(3), PositionFive(4), BonusPosition(5);

    private int index;

    DrawPosition(int index) {
        this.index = index;
    }

    public int getIndex() {

        return index;
    }
}

enum ColumnPosition {

    ColumnOne(1), ColumnTwo(2), ColumnThree(3), ColumnFour(4), ColumnFive(5), ColumnSix(6);

    private static List<ColumnPosition> positions = Arrays.asList(ColumnOne, ColumnTwo, ColumnThree, ColumnFour, ColumnFive, ColumnSix);
    private int pos;

    ColumnPosition(int pos) {
        this.pos = pos;
    }

    public int getColumnIndex() {
        return pos;
    }

    public static List<ColumnPosition> getPositions() {
        return positions;
    }
}