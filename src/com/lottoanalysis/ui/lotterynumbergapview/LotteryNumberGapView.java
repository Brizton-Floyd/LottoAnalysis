package com.lottoanalysis.ui.lotterynumbergapview;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.lotterynumbergap.LotteryNumberGapModel;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.lotterynumbergapview.cell.LotteryNumberGapCell;
import com.lottoanalysis.ui.presenters.LotteryNumberGapPresenter;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;
import java.util.stream.Collectors;

public class LotteryNumberGapView extends BaseView<LotteryNumberGapPresenter>{

    private Label positionLabel, analysisLabel, dayOfWeekLabel;

    public LotteryNumberGapView(){
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

    private void buildScene(){
        buildPatternView();
        buildChartingView();
    }

    private void buildChartingView() {
        HBox hBox = (HBox) lookup("#1");
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setId("3");
        //vBox.setStyle("-fx-background-color:white");

        hBox.getChildren().add( vBox );

        HBox.setHgrow(vBox, Priority.ALWAYS);
    }

    public void injectDataIntoTable(List<LotteryNumberGapModel> lotteryNumberGapModels) {
        VBox box = (VBox)lookup(("#2"));
        box.setSpacing(12);
        StackPane pane = new StackPane();
        pane.setPrefHeight(750);

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();

        lotteryNumberGapModels.removeIf(lotteryNumberGapModel -> lotteryNumberGapModel.getHits() == 0);

        TableView<ObservableList<String>> tableView = new TableView<>();
        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(int i = 0; i < lotteryNumberGapModels.size(); i++){
            final int j = i;
            TableColumn<ObservableList<String>,String> column = new TableColumn<>(Arrays.toString(lotteryNumberGapModels.get(i).getValues()));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
            column.setSortable(false);
            tableView.getColumns().add( column );
            column.setCellFactory(param -> new TableCell<ObservableList<String>,String>(){
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (!isEmpty()) {

                        setText(item);

                        if (item.contains("X")) {
                            this.setTextFill(Color.YELLOW);
                            setStyle("-fx-font-weight: bold;");
                        }
                    }
                }
            });
        }

        for(int i = 0; i < lotteryNumberGapModels.get(0).getPatternHolder().size(); i++){

            ObservableList<String> row = FXCollections.observableArrayList();

            for(int j = 0; j < lotteryNumberGapModels.size(); j++){
                row.add(lotteryNumberGapModels.get(j).getPatternHolder().get(i));
            }
            data.add( row );
        }

        tableView.setItems( data );
        tableView.scrollTo( data.size()-1);
        pane.getChildren().setAll(tableView);

        StackPane statPane = buildStatPane( lotteryNumberGapModels );

        box.getChildren().setAll( pane, statPane );

    }

    private StackPane buildStatPane(List<LotteryNumberGapModel> lotteryNumberGapModels) {
        LotteryNumberGapCell.processed = false;

        StackPane stackPane = new StackPane();

        TableView<LotteryNumberGapModel> tableView = new TableView<>();

        ObservableList<LotteryNumberGapModel> lotteryNumberGapModelList = FXCollections.observableArrayList();

        TableColumn<LotteryNumberGapModel,String> gapColumn = new TableColumn<>(("Gap Range"));
        gapColumn.setCellValueFactory(param -> new SimpleStringProperty(Arrays.toString(param.getValue().getValues())));
        gapColumn.setCellFactory(param -> new LotteryNumberGapCell((this)));

        TableColumn<LotteryNumberGapModel,String> hitColumn = new TableColumn<>(("Hits"));
        hitColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHits()+""));
        hitColumn.setCellFactory(param -> new LotteryNumberGapCell((this)));

        TableColumn<LotteryNumberGapModel,String> gameOutColumn = new TableColumn<>(("Games Out"));
        gameOutColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGamesOut()+""));
        gameOutColumn.setCellFactory(param -> new LotteryNumberGapCell((this)));

        TableColumn<LotteryNumberGapModel,String> hitsAtColumn = new TableColumn<>(("Hits @ Game Out"));
        hitsAtColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getHitsAtGameOut()+""));
        hitsAtColumn.setCellFactory(param -> new LotteryNumberGapCell((this)));

        TableColumn<LotteryNumberGapModel,String> lastSeenColumn = new TableColumn<>(("Last Seen"));
        lastSeenColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getLastSeen()+""));
        lastSeenColumn.setCellFactory(param -> new LotteryNumberGapCell((this)));

        tableView.getColumns().setAll( gapColumn, hitColumn,gameOutColumn,hitsAtColumn,lastSeenColumn);

        lotteryNumberGapModelList.addAll(lotteryNumberGapModels);
        tableView.setItems( lotteryNumberGapModelList );

        stackPane.getChildren().setAll( tableView );

        return stackPane;
    }

    private void buildPatternView() {
        HBox box = new HBox();
        box.setId("1");
        //box.setStyle("-fx-background-color:black;");

        AnchorPane.setTopAnchor(box, 60.0);
        AnchorPane.setLeftAnchor(box, 5.0);
        AnchorPane.setRightAnchor(box, 5.0);
        AnchorPane.setBottomAnchor(box, 5.0);

        VBox patternVbox = new VBox();
        //patternVbox.setStyle("-fx-background-color:white;");
        patternVbox.setPrefHeight(box.getHeight());
        patternVbox.setPrefWidth(500);
        patternVbox.setId("2");

        box.getChildren().setAll(patternVbox);
        getChildren().add( box );
    }

    private void buildHeader() {

        HBox headerHbox = new HBox();
        final String gameName = getPresenter().getGameName();
        Region region = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);

        Label pageAndGameTitle = new Label();
        pageAndGameTitle.textProperty().bind(new SimpleStringProperty(

                String.format("Lottery Number Gap Chart: %s", gameName)
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

        final Menu analysisMenu = new Menu("", analysisLabel);
        analysisMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 100;");

        final Menu weekDayMenu = new Menu("", dayOfWeekLabel);
        weekDayMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 80;");

        menuBar.getMenus().addAll(drawPositionMenu, analyzeMethodMenu, dayOfWeekMenu, analysisMenu, weekDayMenu);

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        getChildren().add(menuBar);
    }

    private Menu buildPositionMenu() {

        Menu menu = new Menu("Draw Positions");
        final int gameRange = getPresenter().getNumberOfPositionsInGame();
        for (DrawPosition drawPosition : DrawPosition.values()) {

            if ((drawPosition.getIndex() + 1) <= gameRange) {

                MenuItem menuItem = new MenuItem(drawPosition.getText());
                menuItem.setOnAction(event -> {

                    DrawPosition position = Arrays.stream(DrawPosition.values())
                            .filter(val -> val.getText().equals(menuItem.getText())).findAny().orElse(null);
                    getPresenter().setDrawPositin( position );
                });
                menu.getItems().add(menuItem);
            }
        }

        return menu;
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

    public void injectNumbersIntoChart(List<Integer> chartPoints) {
        VBox vBox = (VBox)lookup("#3");
        StackPane stackPane = new StackPane();

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
       // dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        dataPoints.add((chartPoints.size() > 20) ? chartPoints.subList(chartPoints.size() - 20, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Performance Chart", 654, 346, 2);

        stackPane.getChildren().setAll(lc.getLineChart());
        vBox.getChildren().setAll(stackPane);
    }
}
