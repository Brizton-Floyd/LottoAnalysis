package com.lottoanalysis.controllers;

import com.lottoanalysis.charts.LineChartWithHover;
import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.lottogames.LottoGame;
import com.lottoanalysis.models.toplevelcharting.ChartDataBuilder;
import com.lottoanalysis.utilities.chartutility.ChartHelper;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import com.lottoanalysis.utilities.gameoutviewutilities.GamesOutAnalyzerHelper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.Collectors;

public class ChartAnalysisController {

    private LottoGame lotteryGame;
    private int[][] drawNumbers;
    private int currentDrawPosition = 0;

    private static List<DrawPosition> drawPositions = new ArrayList<>();

    private List<Object> lottoDrawData;
    private ChartDataBuilder dataBuilder = new ChartDataBuilder();

    @FXML
    private Label gameTitle, analyzedPosition, hotHits, hotOut, warmHits, warmOut, coldHits, coldOut, warmHitsAtGamesOut,
            hotHitsAtGamesOut, coldHitsAtGamesOut, hotOutLastSeen, warmOutLastSeen, coldOutLastSeen, hotOutLastSeenHits,
            warmOutLastSeenHits, coldOutLastSeenHits;
    @FXML
    private Label lastHitPos, lottoGamesOut, posHits, lottoNum;
    @FXML
    private StackPane chartOneAnchorPane, chartTwoAnchorPane, chartThreeAnchorPane, tablePane, gameOutChartPane;

    @FXML
    private MenuButton menuButton;
    @FXML
    private RadioButton rdbFullLottoButton;

    @FXML
    private MenuBar menuBar;

    public LottoGame getLotteryGame() {
        return lotteryGame;
    }

    public void setLotteryGame(LottoGame lotteryGame) {
        this.lotteryGame = lotteryGame;
    }

    public int[][] getDrawNumbers() {
        return drawNumbers;
    }

    public void init(Object[] drawNumbers) {
        this.lotteryGame = (LottoGame) drawNumbers[0];
        this.lottoDrawData = (List<Object>) drawNumbers[1];

        this.drawNumbers = (int[][]) lottoDrawData.get(0);

    }

    public void intialize() {

        menuBar.getMenus().clear();

        Menu posistionMenu = new Menu("Draw Positions");

        for (int i = 0; i < drawNumbers.length; i++) {

            final int index = i;
            MenuItem menuItem = new MenuItem("Position " + (i + 1));
            menuItem.setOnAction(event -> {

                processDataForChartRendering(drawPositions.get(index).getIndex());
                makeInvisible();

            });

            posistionMenu.getItems().add(menuItem);
        }

        Menu elementMenu = new Menu("Index Positions");
        String[] elements = {"Element One", "Element Two", "Full Lotto Number"};

        for (String ele : elements) {

            MenuItem item = new MenuItem(ele);
            item.setOnAction(event -> {

                dataBuilder.processData(drawNumbers[currentDrawPosition], item.getText());
                List<ChartDataBuilder.ChartData> chartData = dataBuilder.getChartData();
                setUpTopCharts(chartData);
            });

            elementMenu.getItems().add(item);

        }
        menuBar.getMenus().addAll(posistionMenu, elementMenu);

    }

    private void makeVisible() {
        Label[] data = {lastHitPos, lottoGamesOut, posHits, lottoNum};

        for (Label label : data) {
            label.setVisible(true);
        }
    }

    private void makeInvisible() {
        Label[] data = {lastHitPos, lottoGamesOut, posHits, lottoNum};

        for (Label label : data) {
            label.setVisible(false);
        }
    }

    private void processDataForChartRendering(int position) {

        currentDrawPosition = position;

        setUpHeaders(position);

        int[] positionArray = drawNumbers[position];
        loadMenuButtonDropDown(positionArray, position);
        // set up top level charts
        getDataForTopChartRendering(positionArray);

    }

    private void getDataForTopChartRendering(int[] posisitionArray) {

        dataBuilder.processData(posisitionArray, LotteryGameConstants.FULL_NUM);
        List<ChartDataBuilder.ChartData> chartData = dataBuilder.getChartData();

        setUpTopCharts(chartData);
    }

    private void loadMenuButtonDropDown(int[] positionArray, int position) {

        menuButton.getItems().clear();

        List<Integer> nums = Arrays.asList(Arrays.stream(positionArray).boxed().toArray(Integer[]::new));
        Set<Integer> numbers = new TreeSet<>(nums);

        for (Iterator<Integer> num = numbers.iterator(); num.hasNext(); ) {

            MenuItem item = new MenuItem(num.next() + "");
            item.setOnAction(e -> {
                GamesOutAnalyzerHelper.analyze(drawNumbers, position, item.getText());
                Object[] data = GamesOutAnalyzerHelper.getLottoNumSpecificData();
                populateTextFields(data, item.getText());
            });
            menuButton.getItems().add(item);
        }
    }

    private void populateTextFields(Object[] data, String num) {
        makeVisible();
        lottoNum.setText("Lotto#: " + num);
        posHits.setText("Pos Hits: " + data[0] + "");
        lottoGamesOut.setText("Games Out: " + data[1] + "");
        lastHitPos.setText("Last Hit: " + data[2] + "");

    }

    @SuppressWarnings("unchecked")
    private void setUpTopCharts(List<ChartDataBuilder.ChartData> chartData) {

        StackPane[] panes = {chartOneAnchorPane, chartTwoAnchorPane, chartThreeAnchorPane};

        String[] colors = {"#FF0000", "#FF0000", "#FF0000"};

        for (int i = 0; i < chartData.size(); i++) {

            List<Integer> points = chartData.get(i).getWinningLottoNumberHolder();

            if (points.size() > 0) {
                //List<Integer> pointTwo = (points.size() > 0) ? ChartHelper.getListOfNumbersBasedOnCurrentWinningNumber(points) : new ArrayList<>();
                Set<Integer> unique = new HashSet<>(points);

                List<Integer> special = (List<Integer>) ChartHelperTwo.getRepeatedNumberList(points)[0];
                List<List<Integer>> dataPoints = new ArrayList<>();
                dataPoints.add((special.size() > 140) ? special.subList(special.size() - 140, special.size()) : special);
                //dataPoints.add((points.size() > 140) ? points.subList(points.size() - 140, points.size()) : points);

                int min = points.stream().min(Integer::compare).get();
                int max = points.stream().max(Integer::compare).get();

                // if(pointTwo.size() > 0)
                //dataPoints.add((pointTwo.size() > 100) ? pointTwo.subList(pointTwo.size() - 30, pointTwo.size()) : pointTwo);

                //LineSpacingHelper.determineMostProbableLineSpacing(points);
                // LineSpacingHelper.num++;

                LineChartWithHover lc = new LineChartWithHover(dataPoints,
                        colors[i],
                        min,
                        max, unique.toString(), null, 1230, 112, 6);

                panes[i].getChildren().setAll(lc.getLineChart());
            }
        }

        setUpChartHitTable(chartData);
        setUpChart(chartData);
    }

    private void setUpChart(List<ChartDataBuilder.ChartData> chartData) {

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(ChartDataBuilder.getGameOutHolderList());
        List<Integer> minMaxVals = new ArrayList<>(ChartDataBuilder.getGameOutHolderList());
        Collections.sort(minMaxVals);

        List<Integer> currentGamesOutAmongstAllCharts = chartData.stream().map(ChartDataBuilder.ChartData::getGamesOut).collect(Collectors.toList());
        ChartDataBuilder.getGameOutHolderList().removeIf( number -> !currentGamesOutAmongstAllCharts.contains(number));

        Object[] data = ChartHelperTwo.getRepeatedNumberList(ChartDataBuilder.getGameOutHolderList());

        List<Integer> specialList = (List<Integer>) data[0];

//        dataPoints.add((ChartDataBuilder.getGameOutHolderList().size() > 80) ?
//                ChartDataBuilder.getGameOutHolderList().subList(ChartDataBuilder.getGameOutHolderList().size()  - 80,
//                        ChartDataBuilder.getGameOutHolderList().size() ) : ChartDataBuilder.getGameOutHolderList());
         dataPoints.add((specialList.size() > 70) ? specialList.subList(specialList.size()-70,specialList.size()) : specialList);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart", 654, 346, 7);

        gameOutChartPane.getChildren().setAll(lc.getLineChart());
    }

    private void setUpChartHitTable(List<ChartDataBuilder.ChartData> upperChartData) {

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        //Map<Integer, Integer[]> totalNumberPresentTracker = betSlipAnalyzer.getTotalNumberPresentTracker();

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {"Chart Indicator", "Hits", "Games Out", "Hits @ GOut", "Last Seen"};

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

            col.setSortable(true);
            tableView.getColumns().addAll(col);
        }

        /********************************
         * Data added to ObservableList *
         ********************************/
        for (ChartDataBuilder.ChartData chartData : upperChartData) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            row.add(chartData.getHitIndicator());
            row.add(chartData.getHits() + "");
            row.add(chartData.getGamesOut() + "");
            row.add(chartData.getHitsAtGamesOut() + "");
            row.add(chartData.getLastSeen() + "");

            dataItems.add(row);

        }

        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);
        tablePane.getChildren().setAll(tableView);

    }

    private void setUpHeaders(int position) {

        gameTitle.textProperty().bind(new SimpleStringProperty(String.format("Chart Analysis: %s", lotteryGame.getGameName())));
        analyzedPosition.textProperty().bind(new SimpleStringProperty(String.format("Currently Analyzing Position " + (position + 1))));

    }

    public void start() {

        drawPositions.add(DrawPosition.PositionOne);
        drawPositions.add(DrawPosition.PositionTwo);
        drawPositions.add(DrawPosition.PositionThree);
        drawPositions.add(DrawPosition.PositionFour);
        drawPositions.add(DrawPosition.PositionFive);
        drawPositions.add(DrawPosition.BonusPosition);

        intialize();


        processDataForChartRendering(currentDrawPosition);
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
}
