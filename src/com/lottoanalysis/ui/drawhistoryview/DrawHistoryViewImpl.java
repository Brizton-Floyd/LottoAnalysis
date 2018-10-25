package com.lottoanalysis.ui.drawhistoryview;

import com.lottoanalysis.models.charts.LineChartWithHover;
import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.models.drawhistory.*;
import com.lottoanalysis.ui.drawhistoryview.cells.SumGroupCell;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.DrawHistoryPresenter;
import com.lottoanalysis.utilities.chartutility.ChartHelperTwo;
import javafx.beans.property.SimpleStringProperty;
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

public class DrawHistoryViewImpl extends BaseView<DrawHistoryPresenter> implements DrawHistoryView {

    private VBox lottoGameStatsVBox = new VBox();
    private HBox dayOfWeekRadioButtons;
    private HBox viewComponentHolder = new HBox();
    private MenuBar menuBar = new MenuBar();
    private Label drawPositionBeingAnalyzed = new Label();
    private Label hitAvgInSpanLabel = new Label();
    private Label analyzeMethodLabel = new Label();
    private Label lottoHitAbrLabel;

    private Set<String> days;
    private int numberOfPositions;
    private String gameName;
    private boolean dayOfWeekPopulationNeeded;

    public DrawHistoryViewImpl(){
    }

    private void addActionHandlerToUiElements() {

        configureMenuBar();
        performDefaultViewSetUp();


        if (dayOfWeekPopulationNeeded) {
            dayOfWeekRadioButtons = setUpDayOfWeekRadioButtons();
            dayOfWeekRadioButtons.setPadding(new Insets(-30, 0, 0, 0));

        }

        addActionsToMenuBarItems();

    }

    /**
     * Method is responsible for adding actions to all menu menu items
     */
    private void addActionsToMenuBarItems() {

        ObservableList<Menu> menus = menuBar.getMenus();

        for (Menu menu : menus) {
            switch (menu.getText()) {
                case "Draw Position":
                    placeActionsOnDrawPositions(menu);
                    break;
                case "Game Span":
                    placeActionOnGameSpanOptions(menu);
                    break;
                case "Analyze Methods":
                    placeActionsOnAnalysisMethods(menu);
                    break;
                case "Group Analysis":
                    placeActionOnGroupAnalysisMethods(menu);
                    break;
            }
        }
    }

    /**
     * Method will place the appropriate actions on all analysis menu items
     *
     * @param menu
     */
    private void placeActionOnGroupAnalysisMethods(Menu menu) {

        for (MenuItem menuItem : menu.getItems()) {

            menuItem.setOnAction(event -> {

                AnalyzeMethod drawPosition = Arrays.stream(AnalyzeMethod.values()).filter(val -> val.getTitle()
                        .equals(menuItem.getText())).findAny().orElse(null);
                notifyListenerOfAnalysisChange(drawPosition);
            });
        }
    }

    /**
     * Method will place actions on all game span options
     *
     * @param menu
     */
    private void placeActionOnGameSpanOptions(Menu menu) {

        for (MenuItem menuItem : menu.getItems()) {

            menuItem.setOnAction(event -> {

                final int span = Integer.parseInt(menuItem.getText().trim());
                notifyListenerOfGameSpanChange(span);
            });
        }
    }

    /**
     * Method will place the appropriate actions on all analysis menu items
     *
     * @param menu
     */
    private void placeActionsOnAnalysisMethods(Menu menu) {


        for (MenuItem menuItem : menu.getItems()) {

            menuItem.setOnAction(event -> {

                AnalyzeMethod drawPosition = Arrays.stream(AnalyzeMethod.values()).filter(val -> val.getTitle()
                        .equals(menuItem.getText())).findAny().orElse(null);
                notifyListenerOfAnalysisChange(drawPosition);
            });
        }
    }

    /**
     * Method will place the appropriate actions on all draw positions menu items
     *
     * @param menu
     */
    private void placeActionsOnDrawPositions(Menu menu) {

        for (MenuItem menuItem : menu.getItems()) {

            menuItem.setOnAction(event -> {

                DrawPosition drawPosition = Arrays.stream(DrawPosition.values()).filter(val -> val.getText()
                        .equals(menuItem.getText())).findAny().orElse(null);
                notifyListenerOfDrawPositionChange(drawPosition);
            });
        }
    }

    @Override
    public void setUpUi() {
        addActionHandlerToUiElements();
        notifyListenerOfPageLoad();
    }

    @Override
    public AnchorPane display() {
        return this;
    }

    @Override
    public void notifyListenerOfDrawPositionChange(DrawPosition drawPosition) {
        getPresenter().onDrawPositionChange(drawPosition);
    }

    @Override
    public void notifyListenerOfAnalysisChange(AnalyzeMethod analyzeMethod) {
        getPresenter().onAnalysisMethodChange(analyzeMethod);
    }

    @Override
    public void notifyListenerOfGameSpanChange(int span) {
        getPresenter().onGameSpanChange(span);
    }

    @Override
    public void notifyListenerOfTableCellSelectionChange(String value, int gamesOut) {
        getPresenter().onTableCellSelectionChange(value, gamesOut);
    }

    @Override
    public void notifyListenerOfPageLoad() {
        getPresenter().onPageLoad();
    }

    @Override
    public void setHeaderInformation(String position) {
        setUpGameHeaderInformation(position);
    }

    @Override
    public void setAbbreviationLabel(String value) {
        lottoHitAbrLabel = new Label(value);
    }

    @Override
    public void setAnalyzeLabel(String analyzeLabelValue) {

        analyzeMethodLabel.setStyle("-fx-text-fill:#dac6ac;");
        analyzeMethodLabel.setFont(Font.font(15.0));
        analyzeMethodLabel.textProperty().bind(new SimpleStringProperty(
                String.format("Analyzing Mode: \" %s \"", analyzeLabelValue)));

        AnchorPane.setTopAnchor(analyzeMethodLabel, 67.0);
        AnchorPane.setRightAnchor(analyzeMethodLabel, 10.0);

        int indexTwo = getChildren().indexOf(analyzeMethodLabel);
        if (indexTwo > -1) {
            getChildren().remove(indexTwo);
            getChildren().add(indexTwo, analyzeMethodLabel);
        } else {
            getChildren().add(analyzeMethodLabel);
        }
    }

    @Override
    public void setAverageAndSpan(int span, Double avg) {

        hitAvgInSpanLabel.setStyle("-fx-text-fill:#dac6ac;");
        hitAvgInSpanLabel.setFont(Font.font(15.0));
        hitAvgInSpanLabel.textProperty().bind(new SimpleStringProperty(
                "The average winning numbers in a game span of " + span +
                        " is " + avg + ""));

        AnchorPane.setTopAnchor(hitAvgInSpanLabel, 67.0);
        AnchorPane.setLeftAnchor(hitAvgInSpanLabel, 10.0);

        int index = getChildren().indexOf(hitAvgInSpanLabel);
        if (index > -1) {
            getChildren().remove(index);
            getChildren().add(index, hitAvgInSpanLabel);
        } else {
            getChildren().add(hitAvgInSpanLabel);
        }
    }

    @Override
    public void setDrawDays(Set<String> days) {
        this.days = new HashSet<>(days);
    }

    @Override
    public void setNumberOfPositions(int numbers) {
        this.numberOfPositions = numbers;
    }

    @Override
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    @Override
    public void setDayOfWeekPopulationNeeded(boolean dayOfWeekPopulationNeeded) {
        this.dayOfWeekPopulationNeeded = dayOfWeekPopulationNeeded;
    }

    @Override
    public void injectTotalWinningNumbers(Map<String, TotalWinningNumberTracker> totalWinningNumberTrackerMap) {

       // lottoGameStatsVBox.getChildren().clear();
        StackPane hitHistoryStackPane = new StackPane(setUpHitsInLastStackPane(totalWinningNumberTrackerMap));

        Label winningNumberPresentTableTitle = new Label("Total Winning Numbers Present Table");
        winningNumberPresentTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        winningNumberPresentTableTitle.setFont(Font.font(15.0));
        winningNumberPresentTableTitle.setPadding(new Insets(5, 0, 0, 0));

        if (lottoGameStatsVBox.getChildren().size() == 0) {
            lottoGameStatsVBox.getChildren().add(winningNumberPresentTableTitle);
            lottoGameStatsVBox.getChildren().add(hitHistoryStackPane);
        } else {
            lottoGameStatsVBox.getChildren().set(1, hitHistoryStackPane);
        }
    }

    @Override
    public void injectFirstDigitNumbers(Map<Integer, Integer[]> firstDigitNumbers) {

        Label winningFirstDigitTableTitle = new Label("Most Winning First Digit Table For Current Position Being Analyzed");
        winningFirstDigitTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        winningFirstDigitTableTitle.setFont(Font.font(15.0));
        winningFirstDigitTableTitle.setPadding(new Insets(10, 0, 0, 0));

        StackPane mostPopularFirstDigitPositionPane = new StackPane(setUpPopularFirstDigitStackPane(firstDigitNumbers));

        if (lottoGameStatsVBox.getChildren().size() == 2) {
            lottoGameStatsVBox.getChildren().add(winningFirstDigitTableTitle);
            lottoGameStatsVBox.getChildren().add(mostPopularFirstDigitPositionPane);
        } else {
            lottoGameStatsVBox.getChildren().set(3, mostPopularFirstDigitPositionPane);
        }
    }

    @Override
    public void injectLottoNumberHits(Map<Integer, LottoNumberGameOutTracker> lottoNumberGameOutTrackerMap) {

        Label lottoNumberHitTableTitle = new Label("Lotto Number Hit Information Table");
        lottoNumberHitTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        lottoNumberHitTableTitle.setFont(Font.font(15.0));
        lottoNumberHitTableTitle.setPadding(new Insets(10, 0, 0, 0));

        StackPane lottoNumberHitPositionPane = new StackPane(setUpLottoNumberHitInfoPane(lottoNumberGameOutTrackerMap));

        if (lottoGameStatsVBox.getChildren().size() == 4) {
            lottoGameStatsVBox.getChildren().add(lottoNumberHitTableTitle);
            lottoGameStatsVBox.getChildren().add(lottoNumberHitPositionPane);
        } else {
            lottoGameStatsVBox.getChildren().set(5, lottoNumberHitPositionPane);
        }
    }

    @Override
    public void injectSumGroupHits(Map<Integer[], SumGroupAnalyzer> sumGroupAnalyzerMap) {

        Label sumHitGroupTableTitle = new Label("Lotto Number Sum Group Table");
        sumHitGroupTableTitle.setStyle("-fx-text-fill:#dac6ac;");
        sumHitGroupTableTitle.setFont(Font.font(15.0));
        sumHitGroupTableTitle.setPadding(new Insets(10, 0, 0, 0));

        StackPane sumGroupHitPane = new StackPane(setUpLottoNumberSumGroupPane(sumGroupAnalyzerMap));

        if (lottoGameStatsVBox.getChildren().size() == 6) {
            lottoGameStatsVBox.getChildren().add(sumHitGroupTableTitle);
            lottoGameStatsVBox.getChildren().add(sumGroupHitPane);
        } else {
            lottoGameStatsVBox.getChildren().set(7, sumGroupHitPane);
        }

    }

    @Override
    public void injectLottoAndGameOutValues(List<Integer> lottoNumbers, List<Integer> gameOutValues) {

        VBox chartingVbox = new VBox();
        chartingVbox.setPadding(new Insets(6, 0, 0, 0));
        chartingVbox.setPrefWidth(750);
        chartingVbox.setSpacing(3);
        //chartingVbox.setStyle("-fx-background-color:black;");

        StackPane lottoNumberPane = setUpLottoNumberChartPane(lottoNumbers);
        StackPane sumGroupLottoNumberPane = setUpLottoNumberGameOutChartPane(gameOutValues);

        chartingVbox.getChildren().setAll(dayOfWeekRadioButtons, lottoNumberPane, sumGroupLottoNumberPane);
        viewComponentHolder.getChildren().setAll(lottoGameStatsVBox, chartingVbox);
        HBox.setMargin(chartingVbox, new Insets(20, 0, 0, 20));
    }

    @Override
    public void injectLottoNumberValues(List<Integer> lottoNumbers) {

        StackPane lottoNumberPane = setUpLottoNumberChartPane(lottoNumbers);

        injectViewHolderWithCorrectPane(lottoNumberPane);
    }

    // Methods
    private HBox setUpDayOfWeekRadioButtons() {

        HBox hBox = new HBox();
        ToggleGroup toggleGroup = new ToggleGroup();

        hBox.setSpacing(10);
        RadioButton button = new RadioButton("All Days");
        button.setStyle("-fx-text-fill:#dac6ac;");
        button.setUserData("All Days");
        button.setToggleGroup(toggleGroup);
        button.setSelected(true);

        hBox.getChildren().add(button);

        days.removeIf( d -> d.length() == 0 || d.equals(""));
        for (Iterator<String> iterator = days.iterator(); iterator.hasNext(); ) {

            String val = iterator.next();
            RadioButton button1 = new RadioButton(val);
            button1.setStyle("-fx-text-fill:#dac6ac;");
            button1.setUserData(val);
            button1.setToggleGroup(toggleGroup);

            hBox.getChildren().add(button1);
        }

        toggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {

            for (DayOfWeek dayOfWeek : DayOfWeek.values()) {

                if (dayOfWeek.getDay().equals(newVal.getUserData().toString())) {

                    getPresenter().onRadioButtonChange(dayOfWeek);

                }
            }

        });

        return hBox;
    }

    private void setUpGameHeaderInformation(final String position) {

        VBox box = new VBox();
        HBox hBox = new HBox();
        //hBox.setStyle("-fx-background-color:green;");

        AnchorPane.setTopAnchor(box, 30.0);
        AnchorPane.setRightAnchor(box, 5.0);
        AnchorPane.setLeftAnchor(box, 5.0);

        Label game = new Label();
        game.textProperty().bind(new SimpleStringProperty("Draw History Analysis For: " +
                gameName));
        game.setFont(Font.font(22.0));
        game.setStyle("-fx-text-fill:#dac6ac;");

        drawPositionBeingAnalyzed.setAlignment(Pos.CENTER_RIGHT);
        drawPositionBeingAnalyzed.setFont(Font.font(22.0));
        drawPositionBeingAnalyzed.setStyle("-fx-text-fill:#dac6ac;");

        drawPositionBeingAnalyzed.textProperty().bind(
                new SimpleStringProperty("Analyzing Position " + (Integer.parseInt(position) + 1))
        );

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
        Menu drawPositionMenu = setUpDrawPositionMenu( numberOfPositions );
        Menu analyizationMethodMenu = analyizationMethodMenu( gameName );
        Menu numberGroupMenu = setUpNumberGroupMenu();

        menuBar.getMenus().addAll(drawPositionMenu, historyMenu, analyizationMethodMenu, numberGroupMenu);


        getChildren().add(menuBar);
    }

    private Menu setUpNumberGroupMenu() {

        Menu numberGroupMenu = new Menu("Group Analysis");

        MenuItem menuItem = new MenuItem("Number Group");

        numberGroupMenu.getItems().add(menuItem);

        return numberGroupMenu;
    }

    private Menu analyizationMethodMenu(String gameName) {

        Menu historyMenu = new Menu("Analyze Methods");
        List<String> menuItemTitles = new LinkedList<>(Arrays.asList("Positional Numbers", "Delta Number", "Positional Sums", "Line Spacings", "Remainder", "Last Digit", "Multiples"));

        if (gameName.equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                gameName.equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {
            menuItemTitles.remove(AnalyzeMethod.DELTA_NUMBERS.getTitle());
        }

        for (String item : menuItemTitles) {
            MenuItem menuItem = new MenuItem(item);
            historyMenu.getItems().add(menuItem);
        }

        return historyMenu;
    }

    private Menu setUpDrawPositionMenu(int size) {

        Menu drawPositonMenu = new Menu("Draw Position");
        for (int i = 0; i < size; i++) {
            MenuItem drawMenuItem = new MenuItem( DrawPosition.values()[i].getText() );
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

    private StackPane setUpLottoNumberGameOutChartPane(List<Integer> chartPoints) {

        StackPane historyStackPane = new StackPane();
        historyStackPane.setId("1");
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
        dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        //dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size()-100,chartPoints.size()) : chartPoints);

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

        dataPoints.add((specialList.size() > 160) ? specialList.subList(specialList.size() - 160, specialList.size()) : specialList);
//        dataPoints.add((DrawModel.getAllDayDrawResults().size() > 100) ? DrawModel.getAllDayDrawResults()
//                    .subList(DrawModel.getAllDayDrawResults().size() - 100, DrawModel.getAllDayDrawResults().size()) :
//                DrawModel.getAllDayDrawResults());
        //dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size()-100,chartPoints.size()) : chartPoints);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Lotto Number Performance Chart", 654, 346, 4);

        historyStackPane.getChildren().setAll(lc.getLineChart());

        return historyStackPane;
    }

    private StackPane setUpLottoNumberSumGroupPane(Map<Integer[], SumGroupAnalyzer> sumGroupAnalyzerMap) {

        StackPane sumGroupInfoPane = new StackPane();
        sumGroupInfoPane.setStyle("-fx-background-color:black;");
        sumGroupInfoPane.setPrefHeight(125);
        sumGroupInfoPane.setPrefWidth(500);

        // logic implementation here
        sumGroupAnalyzerMap.values().removeIf(val -> val.getGroupHits() == 0 || val.getGroupGamesOut() > 30);
        ObservableList<SumGroupAnalyzer> dataItems = FXCollections.observableArrayList( sumGroupAnalyzerMap.values() );

        TableView<SumGroupAnalyzer> tableView = new TableView();
        //tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<SumGroupAnalyzer,String> groupCol = new TableColumn<>("Sum Group");
        groupCol.setSortable(false);
        groupCol.setCellValueFactory( param -> new SimpleStringProperty(param.getValue().getArrayAsString()));
        groupCol.setCellFactory( param -> new SumGroupCell(this));

        TableColumn<SumGroupAnalyzer,String> hitsCol = new TableColumn<>("Hits");
        hitsCol.setSortable(false);
        hitsCol.setCellValueFactory( param -> new SimpleStringProperty( (param.getValue().getGroupHits()+"" )));
        hitsCol.setCellFactory( param -> new SumGroupCell(this));

        TableColumn<SumGroupAnalyzer,String> gameOutCol = new TableColumn<>("Games Out");
        gameOutCol.setSortable(false);
        gameOutCol.setCellValueFactory( param -> new SimpleStringProperty( (param.getValue().getGroupGamesOut()+"" )));
        gameOutCol.setCellFactory( param -> new SumGroupCell(this));

        TableColumn<SumGroupAnalyzer,String> numberCol = new TableColumn<>("Lotto#'s");
        numberCol.setCellValueFactory( param -> new SimpleStringProperty(
                    Arrays.toString(new HashSet(param.getValue().getLottoNumberInSumRangeHolder()).toArray())
        ));
        numberCol.setSortable(false);
        numberCol.setCellFactory( param -> new SumGroupCell(this));
        numberCol.prefWidthProperty().bind(
                                            tableView.widthProperty()
                                                    .subtract( groupCol.widthProperty())
                                                    .subtract( hitsCol.widthProperty())
                                                    .subtract(gameOutCol.widthProperty()).subtract(6));

        tableView.getColumns().setAll(groupCol,hitsCol,gameOutCol,numberCol);

        tableView.setItems(dataItems);
        sumGroupInfoPane.getChildren().setAll(tableView);

        return sumGroupInfoPane;
    }

    private void injectViewHolderWithCorrectPane(StackPane pane) {

        VBox children = (VBox) viewComponentHolder.getChildren().get(1);
        children.getChildren().set(1, pane);
        //System.out.println(children.getChildren().size());
    }

    private StackPane setUpLottoNumberHitInfoPane(Map<Integer, LottoNumberGameOutTracker> lottoNumberGameOutTrackerMap) {

        StackPane lottoNumberHitInfoPane = new StackPane();
        //lottoNumberHitInfoPane.setStyle("-fx-background-color:black;");
        lottoNumberHitInfoPane.setPrefHeight(125);
        lottoNumberHitInfoPane.setPrefWidth(500);

        // logic implementation here
        ObservableList<ObservableList> dataItems = FXCollections.observableArrayList();

        TableView tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        String[] colNames = {String.format("%s#", lottoHitAbrLabel.getText()), "Position Hits", "Games Out", "G-Out In Pos"};

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
        for (Map.Entry<Integer, LottoNumberGameOutTracker> data : lottoNumberGameOutTrackerMap.entrySet()) {

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

    private StackPane setUpPopularFirstDigitStackPane(Map<Integer, Integer[]> firstDigitNumbers) {

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
        for (Map.Entry<Integer, Integer[]> data : firstDigitNumbers.entrySet()) {

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

    private StackPane setUpHitsInLastStackPane(Map<String, TotalWinningNumberTracker> totalWinningNumberTrackerMap) {

        StackPane historyStackPane = new StackPane();
        historyStackPane.setPrefWidth(500);
        historyStackPane.setPrefHeight(150);

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

        getStylesheets().add("com/lottoanalysis/styles/table_view.css");

        getChildren().add(viewComponentHolder);
    }

    public void injectGameOutValues(List<Integer> chartPoints) {

        //historyStackPane.setPadding(new Insets(100,0,0,0));
        StackPane historyStackPane = (StackPane) lookup("#1");

        List<List<Integer>> dataPoints = new ArrayList<>();

        Set<Integer> unique = new HashSet<>(chartPoints);
        List<Integer> minMaxVals = new ArrayList<>(chartPoints);
        Collections.sort(minMaxVals);

        Object[] data = ChartHelperTwo.getRepeatedNumberList(chartPoints);

        List<Integer> specialList = (List<Integer>) data[0];
        //dataPoints.add((specialList.size() > 100) ? specialList.subList(specialList.size() - 100, specialList.size()) : specialList);
        dataPoints.add((chartPoints.size() > 100) ? chartPoints.subList(chartPoints.size()-100,chartPoints.size()) : chartPoints);

        LineChartWithHover lc = new LineChartWithHover(dataPoints,
                null,
                minMaxVals.get(0),
                minMaxVals.get(minMaxVals.size() - 1), unique.toString(), "Lotto Number Game Out Trend Chart", 654, 346, 7);

        historyStackPane.getChildren().setAll(lc.getLineChart());

    }
}
