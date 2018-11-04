package com.lottoanalysis.ui.gamesoutview;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.common.MenuBarHelper;
import com.lottoanalysis.controllers.GroupChartController;
import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.gameout.GameOutRange;
import com.lottoanalysis.models.gameout.NumberDistanceCalculator;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.models.technicalindicators.BollingerBand;
import com.lottoanalysis.ui.gamesoutview.cells.DistanceCell;
import com.lottoanalysis.ui.gamesoutview.cells.GameOutCell;
import com.lottoanalysis.ui.gamesoutview.cells.GameOutRangeViewCell;
import com.lottoanalysis.ui.gamesoutview.cells.GroupRangeGameOutViewCell;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.GameOutPresenter;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.*;
import java.util.stream.Collectors;

public class GameOutViewImpl extends BaseView<GameOutPresenter> implements GameOutView {

    private MenuBar menuBar;
    private Label positionLabel, analysisLabel, dayOfWeekLabel;

    private int gameRange;
    private String gameName;
    private int gameMaxValue;
    private String winninNumbers;

    public GameOutViewImpl() {
        super.setPrefWidth(1270);
        super.setPrefHeight(770);
        super.setStyle("-fx-background-color:#515B51;");
        getStylesheets().add("./src/com/lottoanalysis/styles/table_view.css");


        this.menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color:#dac6ac;");
        analysisLabel = new Label();

        this.positionLabel = new Label();
        dayOfWeekLabel = new Label();

    }

    public Label getPositionLabel() {
        return positionLabel;
    }

    public Label getAnalysisLabel() {
        return analysisLabel;
    }

    public Label getDayOfWeekLabel() {
        return dayOfWeekLabel;
    }

    @Override
    public void setUpUi() {
        GroupRangeGameOutViewCell.processed = false;
        getPresenter().handleViewEvent("load");

        buildMenuBar();
        buildHeader();
        buildScene();

        getPresenter().handleViewEvent("loadViews");
    }

    private void buildScene() {

        HBox hBox = new HBox();
        VBox infoVBox = new VBox();

        AnchorPane.setBottomAnchor(hBox, 5.0);
        AnchorPane.setLeftAnchor(hBox, 5.0);
        AnchorPane.setRightAnchor(hBox, 5.0);
        AnchorPane.setTopAnchor(hBox, 65.0);

        //hBox.setStyle("-fx-background-color: black;");

        for (int i = 0; i < 3; i++) {
            StackPane pane = new StackPane();
            pane.setId((i + 1) + "");
            //pane.setStyle("-fx-background-color: white");
            pane.setPrefWidth(600);
            pane.setPrefHeight(300);
            infoVBox.getChildren().add(pane);
        }

        infoVBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setPrefWidth(700);
        //vBox.setStyle("-fx-background-color: red;");
        HBox.setHgrow(vBox, Priority.ALWAYS);
        HBox.setMargin(infoVBox, new Insets(0, 0, .5, 0));

        setUpChartPanes(vBox);

        hBox.getChildren().addAll(infoVBox, vBox);
        hBox.setSpacing(10);
        getChildren().add(hBox);
    }

    private void setUpChartPanes(VBox vBox) {

        vBox.setSpacing(10);
        for (int i = 0; i < 3; i++) {
            StackPane stackPane = new StackPane();
            //stackPane.setStyle("-fx-background-color: white;");
            stackPane.setPrefWidth(vBox.getPrefWidth());

            switch (i) {
                case 0:
                    stackPane.setPrefWidth(vBox.getPrefWidth());
                    stackPane.setId("4");
                    stackPane.setPrefHeight(300);
                    vBox.getChildren().add(stackPane);
                    break;
                case 1:
                    stackPane.setPrefWidth(vBox.getPrefWidth());
                    stackPane.setId("5");
                    stackPane.setPrefHeight(200);
                    vBox.getChildren().add(stackPane);
                    break;
                case 2:
                    HBox hBox = new HBox();
                    createSideBySidePanes(hBox, vBox.getPrefWidth());
                    vBox.getChildren().add(hBox);
                    break;
            }
        }
    }

    private void createSideBySidePanes(HBox hBox, final double width) {

        hBox.setPrefWidth(width);
        //hBox.setStyle("-fx-background-color: green;");
        hBox.setPrefHeight(200);
        hBox.setSpacing(10);
        int id = 6;
        for (int i = 0; i < 2; i++) {
            StackPane stackPane = new StackPane();
            stackPane.setId((id++) + "");
           // stackPane.setStyle("-fx-background-color: white");
            stackPane.setPrefWidth(400);
            stackPane.setPrefHeight(200);
            //System.out.println("ID : " + stackPane.getId());
            hBox.getChildren().add(stackPane);
        }


    }

    @Override
    public AnchorPane display() {
        return this;
    }

    private void setOnGamePositionChange(DrawPosition drawPosition) {

        getPresenter().setDrawPosition(drawPosition);
    }

    public void setGamePositionRange(int range) {
        this.gameRange = range;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void setGameMaxValue(int maxValue) {
        this.gameMaxValue = maxValue;
    }

    @Override
    public void setWinningDrawNumbers(String winningNumbes) {
        this.winninNumbers = winningNumbes;
    }

    private void buildMenuBar() {

        final Menu drawPositionMenu = buildPositionMenu();
        final Menu analyzeMethodMenu = buildAnalyzeMethodMenu();
        final Menu groupRangeMenu = buildGroupRangeMenu();
        final Menu dayOfWeekMenu = buildDayOfWeekMenu();

        Label label = new Label("Current Winning Numbers: " + winninNumbers);
        label.setStyle("-fx-text-fill: yellow; -fx-background-color: black;");
        final Menu winningNumberMenu = new Menu("", label);
        winningNumberMenu.setStyle("-fx-background-color: #0000; -fx-padding: 5 0 0 100;");

        analysisLabel.setStyle("-fx-text-fill: black;");
        final Menu analysisMenu = new Menu("", analysisLabel);
        analysisMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 100;");

        dayOfWeekLabel.setStyle("-fx-text-fill: black;");
        final Menu weekDayMenu = new Menu("", dayOfWeekLabel);
        weekDayMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 80;");

        menuBar.getMenus().addAll(drawPositionMenu, groupRangeMenu, analyzeMethodMenu, dayOfWeekMenu, winningNumberMenu, analysisMenu, weekDayMenu);

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        getChildren().add(menuBar);
    }

    private Menu buildDayOfWeekMenu() {

        Set<String> days = getPresenter().returnDaysOfWeek();
        Menu menu = new Menu("Day Of Week");
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (days.contains(dayOfWeek.getDay()) || DayOfWeek.ALL.equals(dayOfWeek)) {
                MenuItem menuItem = new MenuItem(dayOfWeek.getFullDayName());
                menuItem.setOnAction(event -> getPresenter().setDayOfWeek(dayOfWeek));
                menu.getItems().add(menuItem);
            }
        }

        return menu;
    }

    private Menu buildGroupRangeMenu() {

        final List<Integer> ranges = MenuBarHelper.buildRangeSizeList(gameMaxValue, gameName);
        Menu menu = new Menu("Group Range");
        // menu.setStyle("-fx-background-color: #0000;");

        for (Integer integer : ranges) {
            MenuItem menuItem = new MenuItem(integer.toString());
            menuItem.setOnAction(event -> {

                getPresenter().setGroupRange(menuItem.getText());
            });

            menu.getItems().add(menuItem);
        }

        return menu;
    }

    private Menu buildAnalyzeMethodMenu() {

        Menu menu = new Menu("Analysis Methods");
        //menu.setStyle("-fx-background-color: #0000;");

        List<AnalyzeMethod> analyzeMethods = Arrays.stream(AnalyzeMethod.values()).collect(Collectors.toList());
        if (gameName.equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                gameName.equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {
            analyzeMethods.remove(AnalyzeMethod.DELTA_NUMBERS);
            analyzeMethods.remove(AnalyzeMethod.FIRST_DIGIT);
        }

        for (AnalyzeMethod analyzeMethod : analyzeMethods) {

            final MenuItem menuItem = new MenuItem(analyzeMethod.getTitle());
            menuItem.setOnAction(event -> getPresenter().setAnalyzeMethod(analyzeMethod));
            menu.getItems().add(menuItem);
        }

        return menu;
    }

    private Menu buildPositionMenu() {

        Menu menu = new Menu("Draw Positions");
        //menu.setStyle("-fx-background-color: #0000;");
        for (DrawPosition drawPosition : DrawPosition.values()) {

            if ((drawPosition.getIndex() + 1) <= gameRange) {

                MenuItem menuItem = new MenuItem(drawPosition.getText());
                menuItem.setOnAction(event -> {

                    DrawPosition position = Arrays.stream(DrawPosition.values())
                            .filter(val -> val.getText().equals(menuItem.getText())).findAny().orElse(null);

                    setOnGamePositionChange(position);

                });
                menu.getItems().add(menuItem);
            }
        }

        return menu;
    }


    private void buildHeader() {

        HBox headerHbox = new HBox();

        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        Label pageAndGameTitle = new Label();
        pageAndGameTitle.textProperty().bind(new SimpleStringProperty(

                String.format("Game Out View Chart: %s", gameName)
        ));
        pageAndGameTitle.setFont(Font.font(20.0));
        pageAndGameTitle.setStyle("-fx-text-fill:#dac6ac;");

        positionLabel.setFont(Font.font(20.0));
        positionLabel.setStyle("-fx-text-fill:#dac6ac;");

        headerHbox.getChildren().addAll(pageAndGameTitle, region, positionLabel);

        AnchorPane.setTopAnchor(headerHbox, 25.0);
        AnchorPane.setLeftAnchor(headerHbox, 5.0);
        AnchorPane.setRightAnchor(headerHbox, 5.0);

        Pane dividerPane = new Pane();
        dividerPane.setPrefHeight(3.0);
        dividerPane.setStyle("-fx-background-color:#EFA747;");

        AnchorPane.setTopAnchor(dividerPane, 54.0);
        AnchorPane.setLeftAnchor(dividerPane, 5.0);
        AnchorPane.setRightAnchor(dividerPane, 5.0);

        getChildren().addAll(headerHbox, dividerPane);
    }

    public <T extends Range> void populateRangeTableView(T groupRange) {

        ObservableList<Range> values = FXCollections.observableArrayList();

        StackPane stackPane = (StackPane) lookup(("#1"));

        TableView<Range> rangeTableView = new TableView<>();
        //rangeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Range, String> rangeColumn = new TableColumn<>("Range");
        rangeColumn.setCellValueFactory(param -> new SimpleStringProperty(Arrays.toString(param.getValue().getUpperLowerBoundAsArray())));
        rangeColumn.setCellFactory(param -> new GroupRangeGameOutViewCell(this));

        TableColumn<Range, String> hitCol = new TableColumn<>("Hits");
        hitCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHits() + ""));
        hitCol.setCellFactory(param -> new GroupRangeGameOutViewCell(this));

        TableColumn<Range, String> gamesOut = new TableColumn<>("Games Out");
        gamesOut.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut() + ""));
        gamesOut.setCellFactory(param -> new GroupRangeGameOutViewCell(this));

        TableColumn<Range, String> hitsAtGamesOut = new TableColumn<>("Hts @Games Out");
        hitsAtGamesOut.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHitsAtGamesOut() + ""));
        hitsAtGamesOut.setCellFactory(param -> new GroupRangeGameOutViewCell(this));

        TableColumn<Range, String> gameOutLastSeen = new TableColumn<>("Games Out Last Seen");
        gameOutLastSeen.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameOutLastSeen() + ""));
        gameOutLastSeen.setCellFactory(param -> new GroupRangeGameOutViewCell(this));

        rangeTableView.getColumns().setAll(rangeColumn, hitCol, gamesOut, hitsAtGamesOut, gameOutLastSeen);

        groupRange.getRanges().removeIf(range -> range.getHits() == 0);
        values.addAll(groupRange.getRanges());

        rangeTableView.setItems(values);
        stackPane.getChildren().setAll(rangeTableView);

    }

    public <T extends Range> void populateGameOutStackPane(T gameOutRange) {

        StackPane stackPane = (StackPane) lookup("#2");
        List<T> ranges = (List<T>) gameOutRange.getRanges();
        ranges.removeIf(range -> range.getHits() == 0);

        TableView<T> tableView = new TableView<>();
        //List<String> columnTitles = new ArrayList<>(Arrays.asList(, "Hits", "Games Out", "Hits @Games Out", "Last Seen"));

        TableColumn<T, String> gameRangeCol = new TableColumn<>("Game Out Group");
        gameRangeCol.setCellValueFactory(param -> new SimpleStringProperty(Arrays.toString(param.getValue().getUpperLowerBoundAsArray())));
        gameRangeCol.setCellFactory(param -> new GameOutRangeViewCell((this)));

        TableColumn<T, String> gameOutHitsCol = new TableColumn<>("Hits");
        gameOutHitsCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHits() + ""));
        gameOutHitsCol.setCellFactory(param -> new GameOutRangeViewCell((this)));

        TableColumn<T, String> gameOutCol = new TableColumn<>("Games Out");
        gameOutCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut() + ""));
        gameOutCol.setCellFactory(param -> new GameOutRangeViewCell((this)));

        TableColumn<T, String> hitAtGameOutCol = new TableColumn<>("Hits @Games Out");
        hitAtGameOutCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHitsAtGamesOut() + ""));
        hitAtGameOutCol.setCellFactory(param -> new GameOutRangeViewCell((this)));

        TableColumn<T, String> lastSeenCol = new TableColumn<>("Last Seen");
        lastSeenCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameOutLastSeen() + ""));
        lastSeenCol.setCellFactory(param -> new GameOutRangeViewCell((this)));

        tableView.getColumns().setAll(gameRangeCol, gameOutHitsCol, gameOutCol, hitAtGameOutCol, lastSeenCol);

        ObservableList<T> rangeObservableList = FXCollections.observableArrayList();
        rangeObservableList.setAll(ranges);

        tableView.setItems(rangeObservableList);

        stackPane.getChildren().setAll(tableView);

    }

    public void displayNumberDistrubution(Map<Integer, List<String>> lottoNumberDistroMap) {

        StackPane stackPane = (StackPane) lookup("#4");

        ObservableList<ObservableList<String>> observableList = FXCollections.observableArrayList();

        Iterator<List<String>> iterator = lottoNumberDistroMap.values().iterator();
        while (iterator.hasNext()) {
            List<String> data = iterator.next();
            if (Character.isDigit(data.get(data.size() - 1).charAt(0))) {
                if (Integer.parseInt(data.get(data.size() - 1)) == data.size())
                    iterator.remove();
            }
        }

        TableView<ObservableList<String>> numberDistrotTableView = new TableView<>();
        if(getPresenter().getGameMaxValue() > 9) {
            numberDistrotTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        }

        List<Integer> colNames = new ArrayList<>(lottoNumberDistroMap.keySet());

        for (int i = 0; i < colNames.size(); i++) {
            final int j = i;
            TableColumn<ObservableList<String>, String> tableColumn = new TableColumn<>(colNames.get(i).toString());
            tableColumn.setSortable(false);
            tableColumn.setResizable(true);
            tableColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
            tableColumn.setCellFactory(new Callback<TableColumn<ObservableList<String>, String>, TableCell<ObservableList<String>, String>>() {
                @Override
                public TableCell<ObservableList<String>, String> call(TableColumn<ObservableList<String>, String> param) {
                    return new TableCell<ObservableList<String>, String>() {

                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (!isEmpty()) {

                                setText(item);

                                if (item.contains("##")) {
                                    this.setTextFill(Color.YELLOW);
                                    setStyle("-fx-font-weight: bold;");
                                } else if (item.equals("P1"))
                                    this.setTextFill(Color.valueOf("#2243B6"));
                                else if (item.contains("->"))
                                    this.setTextFill(Color.ORANGE);
                                else if (item.equals("P2"))
                                    this.setTextFill(Color.GREENYELLOW);
                                else if (item.equals("P3"))
                                    this.setTextFill(Color.PINK);
                                else if (item.equals("P4"))
                                    this.setTextFill(Color.AQUA);
                                else if (item.equals("P5"))
                                    this.setTextFill(Color.CYAN);
                                else
                                    this.setTextFill(Color.BEIGE);
                            }
                        }
                    };
                }
            });

            numberDistrotTableView.getColumns().add(tableColumn);
        }

        List<List<String>> vals = new ArrayList<>(lottoNumberDistroMap.values());

        for (int i = 0; i < vals.get(0).size(); i++) {

            ObservableList<String> row = FXCollections.observableArrayList();
            for (int j = 0; j < vals.size(); j++) {

                row.add(vals.get(j).get(i));
            }

            observableList.add(row);
        }

        numberDistrotTableView.scrollTo(vals.get(0).size() - 1);
        numberDistrotTableView.setItems(observableList);
        stackPane.getChildren().setAll(numberDistrotTableView);
    }

    public void alertPresenterOfIndexChange(int rangeIndex) {
        getPresenter().setRangeIndex(rangeIndex);
    }

    public void injectGameOutValuesIntoChart(List<Integer> chartPoints) {

        StackPane stackPane = (StackPane) lookup("#5");

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];

        //dataPoints.add((specialList.size() > 160) ? specialList.subList(specialList.size() - 160, specialList.size()) : specialList);
//        dataPoints.add((DrawModel.getAllDayDrawResults().size() > 100) ? DrawModel.getAllDayDrawResults()
//                    .subList(DrawModel.getAllDayDrawResults().size() - 100, DrawModel.getAllDayDrawResults().size()) :
//                DrawModel.getAllDayDrawResults());
//        BollingerBand bollingerBand = new BollingerBand(chartPoints,5,100);
//        List<List<Integer>> bollingerBands = bollingerBand.getBollingerBands();
        //List<Integer> movingAverages = GroupChartController.calculateMovingAverage(chartPoints);
        dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
       // dataPoints.add((chartPoints.size() > 20) ? chartPoints.subList(chartPoints.size() - 20, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart", 654, 346, 11);

        stackPane.getChildren().setAll(lc.getLineChart());
    }

    public void injectValuesIntoGameOutTable(List<GameOutRange.GameOut> list) {

        StackPane stackPane = (StackPane) lookup("#3");

        TableView<GameOutRange.GameOut> tableView = new TableView<>();

        TableColumn<GameOutRange.GameOut, String> gameOutNumCol = new TableColumn<>("Game Out Number");
        gameOutNumCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameOutNumber() + ""));
        gameOutNumCol.setCellFactory(param -> new GameOutCell());

        TableColumn<GameOutRange.GameOut, String> gameOutNumHitCol = new TableColumn<>("Hits");
        gameOutNumHitCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameOutHits() + ""));
        gameOutNumHitCol.setCellFactory(param -> new GameOutCell());

        TableColumn<GameOutRange.GameOut, String> gameOutNumOutCol = new TableColumn<>("Games Out");
        gameOutNumOutCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut() + ""));
        gameOutNumOutCol.setCellFactory(param -> new GameOutCell());

        tableView.getColumns().setAll(gameOutNumCol, gameOutNumHitCol, gameOutNumOutCol);

        ObservableList<GameOutRange.GameOut> observableList = FXCollections.observableArrayList();
        observableList.setAll(list);

        tableView.setItems(observableList);

        stackPane.getChildren().setAll(tableView);

    }

    public void injectValuesIntoDistanceTable(List<NumberDistanceCalculator> numberDistanceCalculatorList) {
        StackPane stackPane = (StackPane) lookup("#6");

        TableView<NumberDistanceCalculator> tableView = new TableView<>();

        TableColumn<NumberDistanceCalculator, String> rangeCol = new TableColumn<>("Dist Range");
        rangeCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getUpperLowerAsArrayString()));
        rangeCol.setCellFactory(param -> new DistanceCell((this)));

        TableColumn<NumberDistanceCalculator, String> hitCol = new TableColumn<>("Hits");
        hitCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHits() + ""));
        hitCol.setCellFactory(param -> new DistanceCell((this)));

        TableColumn<NumberDistanceCalculator, String> gameOutCol = new TableColumn<>("Games Out");
        gameOutCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut() + ""));
        gameOutCol.setCellFactory(param -> new DistanceCell((this)));

        tableView.getColumns().setAll(rangeCol, hitCol, gameOutCol);

        ObservableList<NumberDistanceCalculator> observableList = FXCollections.observableArrayList();
        observableList.setAll( numberDistanceCalculatorList );

        tableView.setItems(observableList);

        stackPane.getChildren().setAll( tableView );
    }

    public void injectDistanceValues(List<Integer> chartPoints) {
        StackPane stackPane = (StackPane) lookup("#7");

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];

        //dataPoints.add((specialList.size() > 160) ? specialList.subList(specialList.size() - 160, specialList.size()) : specialList);
//        dataPoints.add((DrawModel.getAllDayDrawResults().size() > 100) ? DrawModel.getAllDayDrawResults()
//                    .subList(DrawModel.getAllDayDrawResults().size() - 100, DrawModel.getAllDayDrawResults().size()) :
//                DrawModel.getAllDayDrawResults());
//        BollingerBand bollingerBand = new BollingerBand(chartPoints,5,100);
//        List<List<Integer>> bollingerBands = bollingerBand.getBollingerBands();
        //List<Integer> movingAverages = GroupChartController.calculateMovingAverage(chartPoints);
         dataPoints.add((specialList.size() > 60) ? specialList.subList(specialList.size() - 60, specialList.size()) : specialList);
        //dataPoints.add((chartPoints.size() > 40) ? chartPoints.subList(chartPoints.size() - 40, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart", 654, 346, 3);

        stackPane.getChildren().setAll(lc.getLineChart());
    }
}
