package com.lottoanalysis.ui.companionnumberview;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.models.companionnumber.CompanionNumber;
import com.lottoanalysis.models.companionnumber.LotteryNumberRange;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.gameout.Range;
import com.lottoanalysis.ui.companionnumberview.cell.CompanionNumberCell;
import com.lottoanalysis.ui.companionnumberview.cell.MainNumberCell;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.CompanionNumberPresenter;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

import java.util.*;
import java.util.stream.Collectors;

public class CompanionNumberView extends BaseView<CompanionNumberPresenter> {

    private Label positionLabel, analysisLabel, dayOfWeekLabel, gameSpanLabel;

    public CompanionNumberView(){
        super.setPrefWidth(1270);
        super.setPrefHeight(770);
        super.setStyle("-fx-background-color:#515B51;");
        getStylesheets().add("./src/com/lottoanalysis/styles/table_view.css");

        positionLabel = new Label();
        positionLabel.setStyle("-fx-text-fill: black;");
        analysisLabel = new Label();
        analysisLabel.setStyle("-fx-text-fill: black;");
        dayOfWeekLabel= new Label();
        dayOfWeekLabel.setStyle("-fx-text-fill: black;");
        gameSpanLabel = new Label();
        gameSpanLabel.setStyle("-fx-text-fill: black;");
    }

    @Override
    public void setUpUi() {
        buildMenuBar();
        buildHeader();
        buildScene();
    }

    @Override
    public AnchorPane display() {
        return null;
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

    public Label getGameSpanLabel() {
        return gameSpanLabel;
    }

    private void buildScene(){
        buildSceneMainContainer();
        buildInfoDisplay();
        buildMainPositionGroupRangeDisplay();
        buildCompanionPositionGroupRangeDisplay();
        buildMainPositionInfoDisplay();
        buildCharPanes();
    }

    private void buildCharPanes() {
        HBox hBox = (HBox)lookup("#1");
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(20,0,0,0));
        HBox.setHgrow(vBox,Priority.ALWAYS);

        StackPane mainChartPane = new StackPane();
        mainChartPane.setId("7");
        mainChartPane.setStyle("-fx-background-color: black");
        mainChartPane.setPrefHeight(300);

        StackPane companionChartPane = new StackPane();
        companionChartPane.setStyle("-fx-background-color: black");
        companionChartPane.setId("8");
        companionChartPane.setPrefHeight(300);

        VBox.setVgrow(companionChartPane,Priority.ALWAYS);

        vBox.getChildren().setAll( mainChartPane, companionChartPane );
        hBox.getChildren().add(vBox);
    }

    private void buildMainPositionInfoDisplay() {
        VBox vBox = (VBox)lookup("#2");

        Label label = new Label("Main Position Information Display");
        label.setFont(Font.font(15));
        label.setTextFill(Color.valueOf("#EFA747"));
        label.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(label);

        StackPane mainPositionPane = new StackPane();
        mainPositionPane.setId("6");
        mainPositionPane.setStyle("-fx-background-color: black");
        mainPositionPane.setPrefHeight(150);
        vBox.getChildren().add( mainPositionPane );
    }

    private void buildCompanionPositionGroupRangeDisplay() {
        VBox vBox = (VBox)lookup("#2");

        Label label = new Label("Companion Position Group Range Hit Table");
        label.setFont(Font.font(15));
        label.setTextFill(Color.valueOf("#EFA747"));
        label.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(label);

        StackPane companionPositionPane = new StackPane();
        companionPositionPane.setId("5");
        companionPositionPane.setStyle("-fx-background-color: black");
        companionPositionPane.setPrefHeight(175);
        vBox.getChildren().add( companionPositionPane );
    }

    private void buildMainPositionGroupRangeDisplay() {
        VBox vBox = (VBox)lookup("#2");

        Label label = new Label("Main Position Group Range Hit Table");
        label.setFont(Font.font(15));
        label.setTextFill(Color.valueOf("#EFA747"));
        label.setPadding(new Insets(60,0,0,0));
        vBox.getChildren().add(label);

        StackPane mainPositionPane = new StackPane();
        mainPositionPane.setId("4");
        mainPositionPane.setStyle("-fx-background-color: black");
        mainPositionPane.setPrefHeight(175);
        vBox.getChildren().add( mainPositionPane );
    }

    private void buildSceneMainContainer() {

        HBox hBox = new HBox();
        hBox.setId("1");
       // hBox.setStyle("-fx-background-color: white");
        AnchorPane.setTopAnchor(hBox,65.0);
        AnchorPane.setLeftAnchor(hBox,5.0);
        AnchorPane.setBottomAnchor(hBox,5.0);
        AnchorPane.setRightAnchor(hBox,5.0);

        getChildren().add(hBox);
    }

    private void buildInfoDisplay() {
        VBox vBox = new VBox();
        vBox.setId("2");
        vBox.setPrefWidth(500);

        Label label = new Label("Information Display");
        label.setTextFill(Color.valueOf("#EFA747"));
        label.setFont(Font.font(15));
        vBox.getChildren().add(label);

        StackPane infoDisplayStackPane = new StackPane();
        infoDisplayStackPane.setId("3");
        infoDisplayStackPane.setStyle("-fx-background-color: black");
        infoDisplayStackPane.setPrefHeight(50);

        vBox.getChildren().add(infoDisplayStackPane);

        HBox hBox = (HBox)lookup("#1");
        hBox.getChildren().add(vBox);
    }

    private void buildHeader() {

        HBox headerHbox = new HBox();
        final String gameName = getPresenter().getGameName();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        Label pageAndGameTitle = new Label();
        pageAndGameTitle.textProperty().bind(new SimpleStringProperty(

                String.format("Companion Number Hit Chart: %s", gameName)
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
    private void buildMenuBar() {

        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color:#dac6ac;");

        final Menu drawPositionMenu = buildPositionMenu();
        final Menu analyzeMethodMenu = buildAnalyzeMethodMenu();
        final Menu dayOfWeekMenu = buildDayOfWeekMenu();
        final Menu gameSpanMenu = setUpGameHistoryMenu();

        final Menu analysisMenu = new Menu("", analysisLabel);
        analysisMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 100;");

        final Menu weekDayMenu = new Menu("", dayOfWeekLabel);
        weekDayMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 80;");

        final Menu gameSpan = new Menu("", gameSpanLabel);
        gameSpan.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 80;");

        menuBar.getMenus().addAll(drawPositionMenu, analyzeMethodMenu, gameSpanMenu, dayOfWeekMenu, gameSpan, analysisMenu, weekDayMenu);

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        getChildren().add(menuBar);
    }

    private Menu buildPositionMenu() {

        Menu menu = new Menu("Draw Positions");
        final int gameRange = getPresenter().getNumberOfPositionsInGame();
        for (DrawPosition drawPosition : DrawPosition.values()) {

            if ((drawPosition.getIndex() + 1) <= gameRange-1) {

                MenuItem menuItem = new MenuItem(drawPosition.getText());
                menuItem.setOnAction(event -> {

                    DrawPosition position = Arrays.stream(DrawPosition.values())
                            .filter(val -> val.getText().equals(menuItem.getText())).findAny().orElse(null);
                    getPresenter().setDrawPosition( position );
                });
                menu.getItems().add(menuItem);
            }
        }

        return menu;
    }
    private Menu setUpGameHistoryMenu() {

        Menu historyMenu = new Menu("Games Out Span");
        for (int number : Arrays.asList(5, 7, 10, 12, 15, 20, 25, 30)) {
            MenuItem menuItem = new MenuItem(Integer.toString(number));
            menuItem.setOnAction(event -> getPresenter().setGameSpan(Integer.parseInt(menuItem.getText())));
            historyMenu.getItems().add(menuItem);
        }
        return historyMenu;
    }
    private Menu buildAnalyzeMethodMenu() {

        Menu menu = new Menu("Analysis Methods");
        final String gameName = getPresenter().getGameName();

        List<AnalyzeMethod> analyzeMethods = Arrays.stream(AnalyzeMethod.values()).collect(Collectors.toList());
        if (gameName.equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                gameName.equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {
            analyzeMethods.remove(AnalyzeMethod.DELTA_NUMBERS);
        }

        for (AnalyzeMethod analyzeMethod : analyzeMethods) {

            if(AnalyzeMethod.POSITIONAL_SUMS.equals(analyzeMethod) || AnalyzeMethod.DRAW_POSITION.equals(analyzeMethod) ||
                    AnalyzeMethod.DELTA_NUMBERS.equals(analyzeMethod)) {
                final MenuItem menuItem = new MenuItem(analyzeMethod.getTitle());
                menuItem.setOnAction(event -> getPresenter().setAnalysisMethod(analyzeMethod));
                menu.getItems().add(menuItem);
            }
        }

        return menu;
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

    public void refreshViews(CompanionNumber model) {
        refreshInfoPane(model);
        refreshMainPositionTable(model);
        refreshCompanionTable(model);
        refreshMainInfoDisplay(model);
    }

    private void refreshMainInfoDisplay(CompanionNumber model) {
        StackPane stackPane = (StackPane)lookup("#6");
        VBox box = new VBox();
        box.setPadding(new Insets(10,0,0,10));
        GridPane gridPane = new GridPane();
        gridPane.setHgap(18.0);

        Label currentWinningNumberLbl = new Label("Current Winning Number");
        currentWinningNumberLbl.underlineProperty().setValue(true);
        currentWinningNumberLbl.setTextFill(Color.valueOf("#EFA747"));

        Label positionHitLbl = new Label("Position Hits");
        positionHitLbl.underlineProperty().setValue(true);
        positionHitLbl.setTextFill(Color.valueOf("#EFA747"));

        Label gameOutLbl = new Label("Games Out");
        gameOutLbl.underlineProperty().setValue(true);
        gameOutLbl.setTextFill(Color.valueOf("#EFA747"));

        Label currentWinningNumber = new Label(model.getLotteryNumberRange().getRangeWinningNumber()+"");
        currentWinningNumber.setFont(Font.font(15));
        currentWinningNumber.setTextFill(Color.GREEN);

        Label totalPositionHits = new Label(model.getTotalWinningNumberAnalyzer().getTotalWinninPositionHits()+"");
        totalPositionHits.setFont(Font.font(15));
        totalPositionHits.setTextFill(Color.GREEN);

        Label gamesOut = new Label(model.getTotalWinningNumberAnalyzer().getLottoNumberGamesOut()+"");
        gamesOut.setFont(Font.font(15));
        gamesOut.setTextFill(Color.GREEN);

        GridPane.setHalignment(currentWinningNumber,HPos.CENTER);
        GridPane.setHalignment(totalPositionHits,HPos.CENTER);
        GridPane.setHalignment(gamesOut,HPos.CENTER);

        gridPane.add(currentWinningNumberLbl,0,0);
        gridPane.add(positionHitLbl,1,0);
        gridPane.add(gameOutLbl,2,0);

        gridPane.add(currentWinningNumber,0,1);
        gridPane.add(totalPositionHits,1,1);
        gridPane.add(gamesOut,2,1);

        box.getChildren().setAll(gridPane);
        stackPane.getChildren().setAll(box);

    }

    private void refreshMainPositionTable(CompanionNumber model) {
        StackPane stackPane = (StackPane)lookup("#4");
        MainNumberCell.processed = false;

        LotteryNumberRange lotteryNumberRange = model.getLotteryNumberRange().getLotteryNumberRangeList().get(0);
        lotteryNumberRange.getRanges().sort((o1,o2) -> Integer.compare(o2.getHits(),o1.getHits()));

        ObservableList<Range> rangeObservableList = FXCollections.observableArrayList();
        rangeObservableList.addAll( lotteryNumberRange.getRanges() );
        rangeObservableList.removeIf(range -> range.getHits() == 0);

        TableView<Range> tableView = new TableView<>();
        TableColumn<Range,String> rangeColumn = new TableColumn<>("Group Range");
        rangeColumn.setCellValueFactory(param -> new SimpleStringProperty(Arrays.toString(param.getValue().getUpperLowerBoundAsArray())));
        rangeColumn.setCellFactory(param -> new MainNumberCell((this)));

        TableColumn<Range,String> hitColumn = new TableColumn<>("Hits");
        hitColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHits()+""));
        hitColumn.setCellFactory(param -> new MainNumberCell((this)));

        TableColumn<Range,String> gameOutColumn = new TableColumn<>("Games Out");
        gameOutColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut()+""));
        gameOutColumn.setCellFactory(param -> new MainNumberCell((this)));

        TableColumn<Range,String> hitsAtOutColumn = new TableColumn<>("Hits @ Games Out");
        hitsAtOutColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHitsAtGamesOut()+""));
        hitsAtOutColumn.setCellFactory(param -> new MainNumberCell((this)));

        TableColumn<Range,String> lastSeenColumn = new TableColumn<>("Last Seen");
        lastSeenColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameOutLastSeen()+""));
        lastSeenColumn.setCellFactory(param -> new MainNumberCell((this)));

        tableView.getColumns().setAll( rangeColumn, hitColumn, gameOutColumn, hitsAtOutColumn,lastSeenColumn );
        tableView.setItems( rangeObservableList );

        stackPane.getChildren().setAll( tableView );

        System.out.println();

    }

    private void refreshCompanionTable(CompanionNumber model){
        StackPane stackPane = (StackPane)lookup("#5");
        MainNumberCell.processed = false;

        LotteryNumberRange lotteryNumberRange = model.getLotteryNumberRange().getLotteryNumberRangeList().get(1);
        lotteryNumberRange.getRanges().sort((o1,o2) -> Integer.compare(o2.getHits(),o1.getHits()));

        ObservableList<Range> rangeObservableList = FXCollections.observableArrayList();
        rangeObservableList.addAll( lotteryNumberRange.getRanges() );
        rangeObservableList.removeIf(range -> range.getHits() == 0);

        TableView<Range> tableView = new TableView<>();
        TableColumn<Range,String> rangeColumn = new TableColumn<>("Group Range");
        rangeColumn.setCellValueFactory(param -> new SimpleStringProperty(Arrays.toString(param.getValue().getUpperLowerBoundAsArray())));
        rangeColumn.setCellFactory(param -> new CompanionNumberCell((this)));

        TableColumn<Range,String> hitColumn = new TableColumn<>("Hits");
        hitColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHits()+""));
        hitColumn.setCellFactory(param -> new CompanionNumberCell((this)));

        TableColumn<Range,String> gameOutColumn = new TableColumn<>("Games Out");
        gameOutColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut()+""));
        gameOutColumn.setCellFactory(param -> new CompanionNumberCell((this)));

        TableColumn<Range,String> hitsAtOutColumn = new TableColumn<>("Hits @ Games Out");
        hitsAtOutColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHitsAtGamesOut()+""));
        hitsAtOutColumn.setCellFactory(param -> new CompanionNumberCell((this)));

        TableColumn<Range,String> lastSeenColumn = new TableColumn<>("Last Seen");
        lastSeenColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGameOutLastSeen()+""));
        lastSeenColumn.setCellFactory(param -> new CompanionNumberCell((this)));

        tableView.getColumns().setAll( rangeColumn, hitColumn, gameOutColumn,hitsAtOutColumn,lastSeenColumn );
        tableView.setItems( rangeObservableList );

        stackPane.getChildren().setAll( tableView );
    }
    private void refreshInfoPane(CompanionNumber model) {
        StackPane infoPane = (StackPane)lookup(("#3"));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(30);
        gridPane.setPadding(new Insets(10,0,0,5));
        Label currentWinningPositionLbl = new Label("Current Winning Position");
        currentWinningPositionLbl.underlineProperty().setValue(true);
        currentWinningPositionLbl.setTextFill(Color.valueOf("#EFA747"));

        Label companionPositionLbl = new Label("Companion Position");
        companionPositionLbl.underlineProperty().setValue(true);
        companionPositionLbl.setTextFill(Color.valueOf("#EFA747"));

        Label totalWinningNumbersAvgLbl = new Label("Avg Winning Nums @ Games Out");
        totalWinningNumbersAvgLbl.underlineProperty().setValue(true);
        totalWinningNumbersAvgLbl.setTextFill(Color.valueOf("#EFA747"));

        Label currentWinningPosition = new Label(model.getDrawPosition()+"");
        currentWinningPosition.setFont(Font.font(15));
        currentWinningPosition.setTextFill(Color.GREEN);

        Label companionPosition = new Label((model.getDrawPosition()+1) + "");
        companionPosition.setFont(Font.font(15));
        companionPosition.setTextFill(Color.GREEN);

        Label avgWinningNumbers = new Label((model.getTotalWinningNumberAnalyzer().getAverageWinningNumberAtGamesOut()) + "");
        avgWinningNumbers.setFont(Font.font(15));
        avgWinningNumbers.setTextFill(Color.GREEN);

        gridPane.add(currentWinningPositionLbl,0,0);
        gridPane.add(companionPositionLbl,1,0);
        gridPane.add(totalWinningNumbersAvgLbl,2,0);

        gridPane.add(currentWinningPosition,0,1);
        gridPane.add(companionPosition,1,1);
        gridPane.add(avgWinningNumbers,2,1);

        GridPane.setHalignment(currentWinningPosition, HPos.CENTER);
        GridPane.setHalignment(companionPosition, HPos.CENTER);
        GridPane.setHalignment(avgWinningNumbers, HPos.CENTER);

        infoPane.getChildren().setAll(gridPane);
    }

    public void renderToCompanionChartPane(List<Integer> chartPoints) {
        StackPane pane = (StackPane)lookup("#8");

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];

        // dataPoints.add((specialList.size() > 160) ? specialList.subList(specialList.size() - 160, specialList.size()) : specialList);
//        dataPoints.add((DrawModel.getAllDayDrawResults().size() > 100) ? DrawModel.getAllDayDrawResults()
//                    .subList(DrawModel.getAllDayDrawResults().size() - 100, DrawModel.getAllDayDrawResults().size()) :
//                DrawModel.getAllDayDrawResults());
//        BollingerBand bollingerBand = new BollingerBand(chartPoints,5,100);
//        List<List<Integer>> bollingerBands = bollingerBand.getBollingerBands();
        //List<Integer> movingAverages = GroupChartController.calculateMovingAverage(chartPoints);
        dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        //dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size() - 100, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart", 654, 346, 3);

        pane.getChildren().setAll( lc.getLineChart() );
    }

    public void renderToMainChartPane(List<Integer> chartPoints) {
        StackPane pane = (StackPane)lookup("#7");

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];

        // dataPoints.add((specialList.size() > 160) ? specialList.subList(specialList.size() - 160, specialList.size()) : specialList);
//        dataPoints.add((DrawModel.getAllDayDrawResults().size() > 100) ? DrawModel.getAllDayDrawResults()
//                    .subList(DrawModel.getAllDayDrawResults().size() - 100, DrawModel.getAllDayDrawResults().size()) :
//                DrawModel.getAllDayDrawResults());
//        BollingerBand bollingerBand = new BollingerBand(chartPoints,5,100);
//        List<List<Integer>> bollingerBands = bollingerBand.getBollingerBands();
        //List<Integer> movingAverages = GroupChartController.calculateMovingAverage(chartPoints);
        dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        //dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size() - 100, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart", 654, 346, 3);

        pane.getChildren().setAll( lc.getLineChart() );
    }

    public void passOffToPresenter(int value) {
        getPresenter().setWinningNumberBasedOnRange(value);
    }

    public void refreshCompanionView(CompanionNumber model) {
        refreshCompanionTable(model);
        refreshMainInfoDisplay(model);
    }
}
