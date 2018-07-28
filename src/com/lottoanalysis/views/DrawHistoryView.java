package com.lottoanalysis.views;

import com.lottoanalysis.charts.LineChartWithHover;
import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.lottogames.drawing.Drawing;
import com.lottoanalysis.models.pastresults.*;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;

import java.util.*;

public class DrawHistoryView extends AnchorPane {

    private DrawHistoryAnalyzer pastDrawResult;
    private HBox viewComponentHolder = new HBox();
    private MenuBar menuBar = new MenuBar();
    private Label drawPositionBeingAnalyzed = new Label();
    private Label hitAvgInSpanLabel = new Label();
    private Label analyzeMethodLabel = new Label();
    HBox dayOfWeekRadioButtons;

    public DrawHistoryView(DrawHistoryAnalyzer pastDrawResult) {

        this.pastDrawResult = pastDrawResult;

        configureMenuBar();
        setUpGameHeaderInformation();
        performDefaultViewSetUp();
        setUpLottoInfoStackPanes();

    }

    // Getters
    public MenuBar getMenuBar() {
        return menuBar;
    }

    public HBox getDayOfWeekRadioButtons() {
        return dayOfWeekRadioButtons;
    }

    public Label getDrawPositionBeingAnalyzed() {
        return drawPositionBeingAnalyzed;
    }

    public Label getHitAvgInSpanLabel() {
        return hitAvgInSpanLabel;
    }

    public void setPastDrawResult(DrawHistoryAnalyzer pastDrawResult) {
        this.pastDrawResult = pastDrawResult;
    }

    public Label getAnalyzeMethodLabel() {
        return analyzeMethodLabel;
    }

    // Methods
    private void setUpGameHeaderInformation() {

        VBox box = new VBox();
        HBox hBox = new HBox();
        //hBox.setStyle("-fx-background-color:green;");

        AnchorPane.setTopAnchor(box, 30.0);
        AnchorPane.setRightAnchor(box, 5.0);
        AnchorPane.setLeftAnchor(box, 5.0);

        Label game = new Label();
        game.textProperty().bind(new SimpleStringProperty("Draw History Analysis For: " +
                Drawing.splitGameName(pastDrawResult.getGameName())));
        game.setFont(Font.font(22.0));
        game.setStyle("-fx-text-fill:#dac6ac;");

        drawPositionBeingAnalyzed.setAlignment(Pos.CENTER_RIGHT);
        drawPositionBeingAnalyzed.setFont(Font.font(22.0));
        drawPositionBeingAnalyzed.setStyle("-fx-text-fill:#dac6ac;");

        Region region1 = new Region();
        HBox.setHgrow(region1, Priority.ALWAYS);

        hBox.getChildren().addAll(game, region1, drawPositionBeingAnalyzed);

        Pane dividerPane = makeDivider();

        box.getChildren().addAll(hBox, dividerPane);


        getChildren().add(box);
    }

    private Pane makeDivider() {

        Pane dividerPane = new Pane();

        dividerPane.setPrefHeight(3.0);
        dividerPane.setStyle("-fx-background-color:#EFA747;");

        return dividerPane;
    }

    private void configureMenuBar() {

        setTopAnchor(menuBar, 0.0);
        setRightAnchor(menuBar, 0.0);
        setLeftAnchor(menuBar, 0.0);

        menuBar.setStyle("-fx-background-color:#dac6ac;");

        Menu historyMenu = setUpGameHistoryMenu();
        Menu drawPositionMenu = setUpDrawPositionMenu();
        Menu analyizationMethodMenu = analyizationMethodMenu();

        menuBar.getMenus().addAll(drawPositionMenu, historyMenu, analyizationMethodMenu);


        getChildren().add(menuBar);
    }

    private Menu analyizationMethodMenu() {

        Menu historyMenu = new Menu("Analyze Methods");
        List<String> menuItemTitles = new LinkedList<>(Arrays.asList("Positional Numbers","Delta Number","Positional Sums","Line Spacings","Remainder","Last Digit","Multiples"));

        if(pastDrawResult.getLottoGame().getGameName().equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                pastDrawResult.getLottoGame().getGameName().equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {
            menuItemTitles.remove(AnalyzeMethod.DELTA_NUMBERS.getTitle());
        }

        for(String item : menuItemTitles)
        {
            MenuItem menuItem = new MenuItem(item);
            historyMenu.getItems().add(menuItem);
        }

        return historyMenu;
    }

    private Menu setUpDrawPositionMenu() {

        Menu drawPositonMenu = new Menu("Draw Position");
        for (int i = 0; i < pastDrawResult.getDrawResultSize(); i++) {
            MenuItem drawMenuItem = new MenuItem("Position " + (i + 1));
            drawPositonMenu.getItems().add(drawMenuItem);
        }

        return drawPositonMenu;
    }

    private Menu setUpGameHistoryMenu() {

        Menu historyMenu = new Menu("Game Span");
        for (int number : Arrays.asList(5, 7, 10, 12, 15, 20, 25, 30)) {
            MenuItem menuItem = new MenuItem(Integer.toString(number));
            historyMenu.getItems().add(menuItem);
        }
        return historyMenu;
    }

    public void setUpLottoInfoStackPanes() {

        VBox lottoGameStatsVBox = new VBox();


        StackPane hitHistoryStackPane = new StackPane(setUpHitsInLastStackPane());

        hitAvgInSpanLabel.setStyle("-fx-text-fill:#dac6ac;");
        hitAvgInSpanLabel.setFont(Font.font(15.0));
        hitAvgInSpanLabel.textProperty().bind(new SimpleStringProperty(
                "The average winning numbers in a game span of " + pastDrawResult.getGameSpan() +
                        " is " + TotalWinningNumberTracker.getAverage() + ""));

        analyzeMethodLabel.setStyle("-fx-text-fill:#dac6ac;");
        analyzeMethodLabel.setFont(Font.font(15.0));
        analyzeMethodLabel.textProperty().bind(new SimpleStringProperty(
                String.format("Analyzing Mode: \" %s \"", pastDrawResult.getAnalyzeMethod().getTitle())));

        Label winningNumberPresentTableTitle = new Label("Total Winning Numbers Present Table");
        winningNumberPresentTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        winningNumberPresentTableTitle.setFont(Font.font(15.0));
        winningNumberPresentTableTitle.setPadding(new Insets(5, 0, 0, 0));

        Label winningFirstDigitTableTitle = new Label("Most Winning First Digit Table For Current Position Being Analyzed");
        winningFirstDigitTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        winningFirstDigitTableTitle.setFont(Font.font(15.0));
        winningFirstDigitTableTitle.setPadding(new Insets(10, 0, 0, 0));

        Label lottoNumberHitTableTitle = new Label("Lotto Number Hit Information Table");
        lottoNumberHitTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        lottoNumberHitTableTitle.setFont(Font.font(15.0));
        lottoNumberHitTableTitle.setPadding(new Insets(10, 0, 0, 0));

        Label sumHitGroupTableTitle = new Label("Lotto Number Sum Group Table");
        sumHitGroupTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        sumHitGroupTableTitle.setFont(Font.font(15.0));
        sumHitGroupTableTitle.setPadding(new Insets(10, 0, 0, 0));

        StackPane mostPopularFirstDigitPositionPane = new StackPane(setUpPopularFirstDigitStackPane());
        StackPane lottoNumberHitPositionPane = new StackPane(setUpLottoNumberHitInfoPane());
        StackPane sumGroupHitPane = new StackPane(setUpLottoNumberSumGroupPane());

        VBox chartingVbox = new VBox();
        chartingVbox.setPadding(new Insets(6, 0, 0, 0));
        chartingVbox.setPrefWidth(750);
        chartingVbox.setSpacing(12);
        chartingVbox.setStyle("-fx-background-color:black;");

        if(pastDrawResult.dayOfWeekPopulationNeeded) {
            dayOfWeekRadioButtons = setUpDayOfWeekRadioButtons();
            dayOfWeekRadioButtons.setPadding(new Insets(-30, 0, 0, 0));
        }

        StackPane lottoNumberPane = setUpLottoNumberChartPane(pastDrawResult.extractDefaultResults());

        StackPane sumGroupLottoNumberPane = setUpLottoNumberGameOutChartPane( pastDrawResult.getSumGroupAnalyzer().getGameOutHitHolder());

        chartingVbox.getChildren().setAll(dayOfWeekRadioButtons, lottoNumberPane, sumGroupLottoNumberPane);

        lottoGameStatsVBox.getChildren().setAll(winningNumberPresentTableTitle, hitHistoryStackPane,
                winningFirstDigitTableTitle, mostPopularFirstDigitPositionPane,
                lottoNumberHitTableTitle, lottoNumberHitPositionPane, sumHitGroupTableTitle, sumGroupHitPane);

        AnchorPane.setTopAnchor(hitAvgInSpanLabel, 67.0);
        AnchorPane.setLeftAnchor(hitAvgInSpanLabel, 10.0);

        AnchorPane.setTopAnchor(analyzeMethodLabel,67.0);
        AnchorPane.setRightAnchor(analyzeMethodLabel,10.0);

        int index = getChildren().indexOf(hitAvgInSpanLabel);
        if (index > -1) {
            getChildren().remove(index);
            getChildren().add(index, hitAvgInSpanLabel);
        } else {
            getChildren().add(hitAvgInSpanLabel);
        }

        int indexTwo = getChildren().indexOf(analyzeMethodLabel);
        if (indexTwo > -1) {
            getChildren().remove(indexTwo);
            getChildren().add(indexTwo, analyzeMethodLabel);
        } else {
            getChildren().add(analyzeMethodLabel);
        }

        viewComponentHolder.getChildren().setAll(lottoGameStatsVBox, chartingVbox);
        HBox.setMargin(chartingVbox, new Insets(20, 0, 0, 20));
    }

    private HBox setUpDayOfWeekRadioButtons() {

        HBox hBox = new HBox();

        hBox.setSpacing(10);
        RadioButton button = new RadioButton("All Days");
        button.setUserData("All Days");

        if(pastDrawResult.getDayOfWeek().getDay().equals("All Days"))
            button.setSelected(true);

        hBox.getChildren().add(button);


        Set<String> days = pastDrawResult.getLottoGame().extractDaysOfWeekFromResults( pastDrawResult.getLottoGame().getDrawingData());

        for (Iterator<String> iterator = days.iterator(); iterator.hasNext();){
            String val = iterator.next();
            RadioButton button1 = new RadioButton(val);
            button1.setUserData(val);

            if(button1.getUserData().toString().equals(pastDrawResult.getDayOfWeek().getDay()))
                button1.setSelected(true);

            hBox.getChildren().add(button1);
        }

        return hBox;
    }

    private StackPane setUpLottoNumberGameOutChartPane(List<Integer> chartPoints) {

        StackPane historyStackPane = new StackPane();
        historyStackPane.setStyle("-fx-background-color:black;");
        historyStackPane.setPrefWidth(500);
        historyStackPane.setPrefHeight(303);

        //historyStackPane.setPadding(new Insets(100,0,0,0));

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];
        dataPoints.add((specialList.size() > 50) ? specialList.subList(specialList.size() - 50, specialList.size()) : specialList);
        //dataPoints.add((chartPoints.size() > 60) ? chartPoints.subList(chartPoints.size()-60,chartPoints.size()) : chartPoints);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Lotto Number Game Out Trend Chart", 654, 346, 7);

        historyStackPane.getChildren().setAll(lc.getLineChart());

        return historyStackPane;
    }

    private StackPane setUpLottoNumberChartPane(List<Integer> chartPoints) {

        StackPane historyStackPane = new StackPane();
        historyStackPane.setStyle("-fx-background-color:black;");
        historyStackPane.setPrefWidth(500);
        historyStackPane.setPrefHeight(303);

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];

        dataPoints.add((specialList.size() > 50) ? specialList.subList(specialList.size() - 50, specialList.size()) : specialList);
        //dataPoints.add((chartPoints.size() > 60) ? chartPoints.subList(chartPoints.size()-60,chartPoints.size()) : chartPoints);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Lotto Number Performance Chart", 654, 346, 3);

        historyStackPane.getChildren().setAll(lc.getLineChart());

        return historyStackPane;
    }

    private StackPane setUpLottoNumberSumGroupPane() {

        StackPane sumGroupInfoPane = new StackPane();
        sumGroupInfoPane.setStyle("-fx-background-color:black;");
        sumGroupInfoPane.setPrefHeight(125);
        sumGroupInfoPane.setPrefWidth(500);

        // logic implementation here
        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        TableView tableView = new TableView();
        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {"Sum Group", "Hits", "Games Out", "Lotto#'s"};

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

                                for(int i = 0; i < observableList.size(); i++)
                                {
                                    if(i == 3)
                                    {
                                        final int index = i;
                                        this.setOnMouseClicked( event -> {

                                            final String values = observableList.get(index).toString();
                                            pastDrawResult.getSumGroupAnalyzer().getGroupAnalyzerMap().forEach((k,v) -> {

                                                Set<Integer> numericValues = new HashSet<>(v.getLottoNumberInSumRangeHolder());
                                                if(Arrays.toString(numericValues.toArray()).equals(values))
                                                {
                                                    final StackPane pane = setUpLottoNumberChartPane(v.getLottoNumberInSumRangeHolder());
                                                    injectViewHolderWithCorrectPane( pane );
                                                }
                                            });
                                        });
                                    }
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
        //int size = currentDrawData.length;
        pastDrawResult.getSumGroupAnalyzer().getGroupAnalyzerMap().entrySet().removeIf(v -> v.getValue().getGroupHits() == 0);
        for (Map.Entry<Integer[], SumGroupAnalyzer> data : pastDrawResult.getSumGroupAnalyzer().getGroupAnalyzerMap().entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer[] key = data.getKey();
            SumGroupAnalyzer value = data.getValue();

              row.add(Arrays.toString( key ));
              row.add(value.getGroupHits() + "");
              row.add(value.getGroupGamesOut() + "");
              row.add(Arrays.toString( new HashSet(value.getLottoNumberInSumRangeHolder()).toArray()) + "");

            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        sumGroupInfoPane.getChildren().setAll(tableView);

        return sumGroupInfoPane;
    }

    private void injectViewHolderWithCorrectPane(StackPane pane) {

       VBox children = (VBox) viewComponentHolder.getChildren().get(1);
       children.getChildren().set(1,pane);
       //System.out.println(children.getChildren().size());
    }

    private StackPane setUpLottoNumberHitInfoPane() {

        StackPane lottoNumberHitInfoPane = new StackPane();
        //lottoNumberHitInfoPane.setStyle("-fx-background-color:black;");
        lottoNumberHitInfoPane.setPrefHeight(125);
        lottoNumberHitInfoPane.setPrefWidth(500);

        // logic implementation here
        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {String.format("%s#",pastDrawResult.getAnalyzeMethod().getAbbr()), "Position Hits", "Games Out", "G-Out In Pos"};

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
        //int size = currentDrawData.length;
        for (Map.Entry<Integer, LottoNumberGameOutTracker> data : pastDrawResult.getLottoNumberGameOutTrackerMap().entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            Integer key = data.getKey();
            LottoNumberGameOutTracker value = data.getValue();

            row.add(key + "");
            row.add(value.getLottoNumberPositionHits() + "");
            row.add(value.getLottoNumberGamesOut() + "");
            row.add(value.getLottoNumberGamesOutInPosition() + "");

            dataItems.add(row);
        }

        tableView.setItems(dataItems);
        lottoNumberHitInfoPane.getChildren().setAll(tableView);

        return lottoNumberHitInfoPane;
    }

    private StackPane setUpPopularFirstDigitStackPane() {

        StackPane popularFirstDigitStackPane = new StackPane();
        //popularFirstDigitStackPane.setStyle("-fx-background-color:black;");
        popularFirstDigitStackPane.setPrefHeight(125);
        popularFirstDigitStackPane.setPrefWidth(500);

        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

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
        //int size = currentDrawData.length;
        for (Map.Entry<Integer, Integer[]> data : pastDrawResult.getFirstDigitValueHolderMap().entrySet()) {

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
        popularFirstDigitStackPane.getChildren().setAll(tableView);

        return popularFirstDigitStackPane;
    }

    private StackPane setUpHitsInLastStackPane() {

        StackPane historyStackPane = new StackPane();
        historyStackPane.setPrefWidth(500);
        historyStackPane.setPrefHeight(150);

        // Begin implementation
        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        Map<String, TotalWinningNumberTracker> totalNumberPresentTracker = pastDrawResult.getTotalWinningNumberTracker().getTotalWinningNumberTrackerMap();

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
        int size = pastDrawResult.getHistoricalDrawData().length;
        for (Map.Entry<String, TotalWinningNumberTracker> data : totalNumberPresentTracker.entrySet()) {

            //Iterate Row
            ObservableList<String> row = FXCollections.observableArrayList();

            row.add(data.getKey());
            row.add(data.getValue().getTotalHits()+"");
            row.add(data.getValue().getGamesOut()+"");

            dataItems.add(row);
        }

        tableView.refresh();
        tableView.setItems(dataItems);
        tableView.scrollTo(tableView.getItems().size() - 1);
        historyStackPane.getChildren().setAll(tableView);

        return historyStackPane;
    }

    private void performDefaultViewSetUp() {
        setPrefWidth(1270);
        setPrefHeight(770);
        setStyle("-fx-background-color:#515B51;");

        setTopAnchor(viewComponentHolder, 85.0);
        setLeftAnchor(viewComponentHolder, 5.0);
        setBottomAnchor(viewComponentHolder, 5.0);

        //viewComponentHolder.setStyle("-fx-background-color:black;");
        viewComponentHolder.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 3;" +
                "-fx-border-color: black;");
        viewComponentHolder.setPrefWidth(1260);

        getStylesheets().add("src/com/lottoanalysis/styles/table_view.css");

        getChildren().add(viewComponentHolder);
    }

}
