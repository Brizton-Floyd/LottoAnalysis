package com.lottoanalysis.ui.positionhitsequenceview;

import com.lottoanalysis.controllers.GroupChartController;
import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.models.drawhistory.TotalWinningNumberTracker;
import com.lottoanalysis.models.drawresults.DrawResultAnalyzer;
import com.lottoanalysis.models.drawresults.DrawResultPosition;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.positionhitsequenceview.cell.DrawPositionHItCell;
import com.lottoanalysis.ui.presenters.PositionHitSequencePresenter;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.*;

public class PositionHitSequenceView extends BaseView<PositionHitSequencePresenter> {

    private Label positionLabel;
    public PositionHitSequenceView(){
        super.setPrefWidth(1270);
        super.setPrefHeight(770);
        super.setStyle("-fx-background-color:#515B51;");
        getStylesheets().add("./src/com/lottoanalysis/styles/table_view.css");

        positionLabel = new Label();
        positionLabel.setStyle("-fx-text-fill: black;");
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

    private void buildScene(){
        buildMainContainer();
        buildDrawResultDisplay();
        buildGameOutAndLotteryNumberDisplay();
        buildPositionHitInfoPane();
    }

    private void buildGameOutAndLotteryNumberDisplay() {
        HBox hBox = (HBox)lookup("#1");
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20,0,0,0));
        vBox.setSpacing(20);

        //vBox.setStyle("-fx-background-color: black");
        vBox.setId("4");
        vBox.getChildren().setAll(buildLotteryChartStackpane(), buildGameOutChartStackPane());

        HBox.setHgrow(vBox,Priority.ALWAYS);

        hBox.getChildren().add( vBox );
    }

    private void buildPositionHitInfoPane(){
        VBox vBox = (VBox)lookup("#4");

        Label label = new Label("");
        label.setTextFill(Color.valueOf("#EFA747"));
        label.setPadding(new Insets(30,0,0,0));
        vBox.getChildren().add(label);

        HBox hBox = new HBox();
        hBox.setSpacing(10);
        label.setPadding(new Insets(0,60,0,0));

        StackPane stackPane = new StackPane();
        StackPane totalHitStackPane = new StackPane();
        totalHitStackPane.setId("8");
        totalHitStackPane.setStyle("-fx-background-color: blue");
        totalHitStackPane.setPrefWidth(500);
        totalHitStackPane.setPrefHeight(200);

        stackPane.setId("7");
        stackPane.setPrefWidth(500);
        stackPane.setPrefHeight(200);

        hBox.getChildren().setAll( stackPane, totalHitStackPane);


        vBox.getChildren().add(hBox);

    }
    private StackPane buildGameOutChartStackPane() {
        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: black");
        stackPane.setId("6");
        stackPane.setPrefWidth(600);
        stackPane.setPrefHeight(200);

        return stackPane;
    }

    private StackPane buildLotteryChartStackpane() {

        StackPane stackPane = new StackPane();
        stackPane.setStyle("-fx-background-color: black");
        stackPane.setId("5");
        stackPane.setPrefWidth(600);
        stackPane.setPrefHeight(200);

        return stackPane;
    }

    private void buildDrawResultDisplay() {
        VBox vBox = new VBox();
        vBox.setId("2");
        vBox.setPrefWidth(500);

        Label label = new Label("Draw Result Display");
        label.setTextFill(Color.valueOf("#EFA747"));
        label.setFont(Font.font(15));
        vBox.getChildren().add(label);

        StackPane infoDisplayStackPane = new StackPane();
        infoDisplayStackPane.setId("3");
        infoDisplayStackPane.setStyle("-fx-background-color: black");
        infoDisplayStackPane.setPrefHeight(800);

        HBox hBox = (HBox)lookup("#1");
        vBox.getChildren().add(infoDisplayStackPane);

        hBox.getChildren().add(vBox);
    }
    private void buildMainContainer() {
        HBox hBox = new HBox();
        hBox.setId("1");
        //hBox.setStyle("-fx-background-color: white");
        AnchorPane.setTopAnchor(hBox,65.0);
        AnchorPane.setLeftAnchor(hBox,5.0);
        AnchorPane.setBottomAnchor(hBox,5.0);
        AnchorPane.setRightAnchor(hBox,5.0);

        getChildren().add(hBox);
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
        final Menu gameSpanMenu = buildGameSpanMenu();

        menuBar.getMenus().addAll(drawPositionMenu, gameSpanMenu);

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        getChildren().add(menuBar);
    }

    private Menu buildGameSpanMenu() {
        Menu menu = new Menu("Game Span");
        List<MenuItem> menuItems = new ArrayList<>();
        for(Integer integer : Arrays.asList(5,10,15,20,25,30)){
            MenuItem menuItem = new MenuItem(integer+"");
            menuItem.setOnAction(event -> getPresenter().setGameSpan( Integer.parseInt( menuItem.getText() )));
            menu.getItems().add( menuItem );
        }

        return menu;
    }

    private Menu buildPositionMenu() {

        Menu menu = new Menu("Draw Positions");
        //menu.setStyle("-fx-background-color: #0000;");
        int gameRange = getPresenter().getAmountOfDrawPositionsAllowed();
        for (DrawPosition drawPosition : DrawPosition.values()) {

            if ((drawPosition.getIndex() + 1) <= gameRange) {

                MenuItem menuItem = new MenuItem(drawPosition.getText());
                menuItem.setOnAction(event -> {

                    DrawPosition position = Arrays.stream(DrawPosition.values())
                            .filter(val -> val.getText().equals(menuItem.getText())).findAny().orElse(null);

                    getPresenter().setDrawPosition(position);

                });
                menu.getItems().add(menuItem);
            }
        }
        return menu;
    }

    public void reloadViews(DrawResultAnalyzer model) {
        loadDataIntoDrawResultView(model);
        loadDataIntoWinningDrawPositionTable( model );
        loadTotalWinningNumberTable( model.getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap() );
    }

    private void loadTotalWinningNumberTable(Map<String, TotalWinningNumberTracker> totalWinningNumberTrackerMap) {

        StackPane historyStackPane = (StackPane) lookup("#8");

        // Begin implementation
        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

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
        for (Map.Entry<String, TotalWinningNumberTracker> data : totalWinningNumberTrackerMap.entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            row.add(data.getKey());
            row.add(data.getValue().getTotalHits() + "");
            row.add(data.getValue().getGamesOut() + "");

            dataItems.add(row);
        }

        tableView.refresh();
        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);
        historyStackPane.getChildren().setAll(tableView);
    }

    private void loadDataIntoWinningDrawPositionTable(DrawResultAnalyzer model) {
        StackPane pane = (StackPane) lookup("#7");

        List<DrawResultPosition> drawResultPositions = new ArrayList<>(model.getDrawResultPosition().getDrawPositionColumnMap().values());
        ObservableList<DrawResultPosition> drawResultPositionObservableList = FXCollections.observableList(drawResultPositions);

        TableView<DrawResultPosition> tableView = new TableView();

        TableColumn<DrawResultPosition,String> drawPositionCol = new TableColumn<>("Draw Position");
        drawPositionCol.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDrawPositionIndex()+""));
        drawPositionCol.setCellFactory(param -> new DrawPositionHItCell((this)));

        TableColumn<DrawResultPosition,String> drawPositionHits = new TableColumn<>("Position Hits");
        drawPositionHits.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDrawPositionHits()+""));
        drawPositionHits.setCellFactory(param -> new DrawPositionHItCell((this)));

        TableColumn<DrawResultPosition,String> drawPositionGamesOut = new TableColumn<>("Position Games Out");
        drawPositionGamesOut.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getDrawPositionGamesOut()+""));
        drawPositionGamesOut.setCellFactory(param ->  new DrawPositionHItCell((this)));

        tableView.getColumns().addAll(drawPositionCol, drawPositionHits, drawPositionGamesOut);
        tableView.setItems(drawResultPositionObservableList);

        pane.getChildren().setAll( tableView );
    }

    private void loadDataIntoDrawResultView(DrawResultAnalyzer model) {
        StackPane vBox = (StackPane) lookup("#3");
        List<DrawResultPosition> drawPositionList = model.getDrawResultPositions();

        TableView<ObservableList<String>> tableView = new TableView<>();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for(int i = 0; i < drawPositionList.size(); i++){
            final int j = i;
            TableColumn<ObservableList<String>,String> column = new TableColumn<>(("Position "+(drawPositionList.get(j).getDrawPositionIndex()+1)));
            column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().get(j)));
            column.setCellFactory( param ->  new TableCell<ObservableList<String>,String>(){

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if(!isEmpty()){

                        setText(item);
                        this.setTextFill(Color.BEIGE);

                    }
                }
            });
            tableView.getColumns().add(column);
        }

        ObservableList<ObservableList<String>> data = FXCollections.observableArrayList();
        for(int i = drawPositionList.get(0).getLotteryResultPositionList().size()-200; i < drawPositionList.get(0).getLotteryResultPositionList().size(); i++){
            ObservableList<String> row = FXCollections.observableArrayList();
            for(int j = 0; j < drawPositionList.size(); j++){
                row.add(drawPositionList.get(j).getLotteryResultPositionList().get(i)+"");
            }
            data.add(row);
        }

        tableView.setItems(data);
        tableView.scrollTo(data.size()-1);
        tableView.getSelectionModel().select(tableView.getItems().size() - model.getGameSpan());
        vBox.getChildren().setAll( tableView );
    }

    public void injectValuesToGameOutChart(List<Integer> chartPoints, int drawPositionIndex) {

        StackPane pane = (StackPane)lookup("#6");

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
        List<Integer> movingAverages = GroupChartController.calculateMovingAverage(chartPoints);
        //dataPoints.add((specialList.size() > 200) ? specialList.subList(specialList.size() - 200, specialList.size()) : specialList);
        dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size() - 100, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Game Out Trend Analysis For Draw Position " + drawPositionIndex, 654, 346, 3);

        pane.getChildren().setAll( lc.getLineChart() );
    }

    public void injectValuesToLotteryNumberChart(List<Integer> chartPoints) {

        StackPane pane = (StackPane)lookup("#5");

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
        //dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size() - 100, chartPoints.size()) : chartPoints);
        //dataPoints.add((movingAverages.size() > 100) ? movingAverages.subList(movingAverages.size() - 100, movingAverages.size()) : movingAverages);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Lottery Number Hit Trend Chart ", 654, 346, 3);

        pane.getChildren().setAll( lc.getLineChart() );
    }

    public void refresh(DrawResultPosition drawResultPosition) {
        getPresenter().refresh(drawResultPosition);
    }
}
