package com.lottoanalysis.lottodashboard;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.Main;
import com.lottoanalysis.chartanalysis.ChartAnalysisController;
import com.lottoanalysis.screenloader.MainController;
import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.lottoinfoandgames.*;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import com.lottoanalysis.utilities.GamesOutViewAnalyzer;
import com.lottoanalysis.utilities.GamesOutViewDepicter;
import com.lottoanalysis.utilities.LottoBetSlipAnalyzer;
import com.lottoanalysis.utilities.NumberPatternAnalyzer;

import java.io.IOException;
import java.util.*;

public class LottoDashboardController  {

    private MainController mainController;
    private LotteryGame lotteryGame;
    private static int universalCount = 0;
    private int[][] positionalNumbers, deltaNumberForLastDraw, positionalSums, lineSpacings, remainder;
    private static LotteryGame classLevelLotteryGame;
    private static List<Object> numbersForChartDisplay = new ArrayList<>();

    public static LotteryGame getClassLevelLotteryGame() {
        return classLevelLotteryGame;
    }

    private static void setClassLevelLotteryGame(LotteryGame classLevelLotteryGame) {
        LottoDashboardController.classLevelLotteryGame = classLevelLotteryGame;
    }

    public static List<Object> getNumbersForChartDisplay() {
        return numbersForChartDisplay;
    }

    private static void setNumbersForChartDisplay(List<Object> numbersForChartDisplay) {
        LottoDashboardController.numbersForChartDisplay = numbersForChartDisplay;
    }
    @FXML
    private AnchorPane pane, infoPane, infoPane1, predictedNumbersPane;

    @FXML
    private JFXButton btn_close;

    @FXML
    private Label lottoDashboard, predictedNumbersLabel;

    @FXML
    private TableView<Drawing> drawNumberTable;

    @FXML
    private BarChart barChart;

    @FXML
    private HBox hBox;
    @FXML
    private VBox vBox;

    @FXML
    FontAwesomeIconView helpIconLottoDashboard;

    private ChoiceBox choiceBox;
    private BarChartExt<String, Number> bc;

    @FXML
    public void initialize() {


        // this is a hack to make the tableview stop scrolling horizontally
        drawNumberTable.addEventFilter(ScrollEvent.SCROLL, new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {
                if (event.getDeltaX() != 0) {
                    event.consume();
                }
            }
        });

        choiceBox = new ChoiceBox();
        choiceBox.setStyle("-fx-focus-color: transparent;");
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.tickLabelFontProperty().set(Font.font(8));
        //xAxis.setTickLabelRotation(20);
        xAxis.setStyle("-fx-tick-label-fill: #dac6ac");

        NumberAxis yAxis = new NumberAxis();
        yAxis.tickLabelFontProperty().set(Font.font(8));
        yAxis.setStyle("-fx-tick-label-fill: #dac6ac");
        yAxis.setAutoRanging(true);

        bc = new BarChartExt<>(xAxis, yAxis);
        hBox.getChildren().addAll(bc, choiceBox);
        vBox.setPadding(new Insets(20, 0, 50, 0));
        this.mainController = mainController;

        styleChart();
        setTextStyleForAllLabels();
        loadDefaultGameForView();
    }

    public int[][] getDeltaNumberForLastDraw() {
        return deltaNumberForLastDraw;
    }

    public int[][] getPositionalSums() {
        return positionalSums;
    }

    public static int getUniversalCount() {
        return universalCount;
    }

    private static void setCount(int val) {

        universalCount = val;
    }

    public int[][] getRemainder() {
        return remainder;
    }

    public int[][] getLineSpacings() {
        return lineSpacings;
    }

    public int[][] getPositionalNumbers() {
        return positionalNumbers;
    }

    public void loadChoicesIntoChoiceBox() {


        if (choiceBox.getItems().size() > 0)
            choiceBox.getItems().clear();


        int count = 0;
        List<String> choiceBoxItems = new LinkedList<>();

        List<TableColumn> columns = new ArrayList<>(drawNumberTable.getColumns());
        for (TableColumn column : columns) {
            String columnName = column.getText();
            if (columnName.contains("Pos") || columnName.contains("Bonus")) {
                if (count < 5)
                    choiceBoxItems.add("Position " + (++count));
                else
                    choiceBoxItems.add("Bonus Position");
            }
        }

        setCount(count);
        positionalNumbers = new int[count][drawNumberTable.getItems().size()];
        loadUpPositionalNumbers(positionalNumbers, lotteryGame.getDrawingData());
        deltaNumberForLastDraw = NumberPatternAnalyzer.findDeltaNumbers(positionalNumbers);
        positionalSums = NumberPatternAnalyzer.findPositionalSums(positionalNumbers);
        lineSpacings = NumberPatternAnalyzer.lineSpacings(positionalNumbers);
        remainder = NumberPatternAnalyzer.computeRemainders(positionalNumbers);

        GamesOutViewAnalyzer gamesOutViewAnalyzer = new GamesOutViewAnalyzer(positionalNumbers, lotteryGame);
        Map<String, Map<String, Integer[]>> res = gamesOutViewAnalyzer.analyzeWinningNumberDistrubution();
        setUpInfoPanel(res);

        List<Object> chartPoints = new ArrayList<>();
        chartPoints.add( positionalNumbers);
        chartPoints.add(deltaNumberForLastDraw);
        chartPoints.add(positionalSums);
        chartPoints.add(lineSpacings);
        chartPoints.add(remainder);

        setNumbersForChartDisplay( chartPoints );

        choiceBox.setItems(FXCollections.observableArrayList(choiceBoxItems));
        choiceBox.getSelectionModel().selectFirst();
    }

    public void performOperationOnChoiceboxValue() {

        choiceBox.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {

            final Object[] data;
            int number = (int) newValue;
            Map<Integer[], Object[]> chartData = null;
            Map<Integer, Integer> chartDataTwo = null;
            List<Object> rowInformation = null;

            switch (number) {
                case 0:
                    LottoBetSlipAnalyzer analyzerPosOne = new LottoBetSlipAnalyzer(lotteryGame, 2);
                    rowInformation = analyzerPosOne.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);

//                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[0]);
//                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[0], positionalNumbers.length,
//                            (int) data[1], (int) data[2], lotteryGame);

                    break;
                case 1:
                    LottoBetSlipAnalyzer analyzerPosTwo = new LottoBetSlipAnalyzer(lotteryGame, 3);
                    rowInformation = analyzerPosTwo.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);

//                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[1]);
//                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[1], positionalNumbers.length,
//                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 2:
                    LottoBetSlipAnalyzer analyzerPosThree = new LottoBetSlipAnalyzer(lotteryGame, 4);
                    rowInformation = analyzerPosThree.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);

//                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[2]);
//                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[2], positionalNumbers.length,
//                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 3:
                    LottoBetSlipAnalyzer analyzerPosFour = new LottoBetSlipAnalyzer(lotteryGame, 5);
                    rowInformation = analyzerPosFour.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);

//                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[3]);
//                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[3], positionalNumbers.length,
//                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 4:
                    LottoBetSlipAnalyzer analyzerPosFive = new LottoBetSlipAnalyzer(lotteryGame, 6);
                    rowInformation = analyzerPosFive.getAllInformationForRowAndPosition();
                    chartDataTwo = extractInformationForBarChart(rowInformation);

//                    data = NumberPatternAnalyzer.computePositionalAvgAboveBelowHits(positionalNumbers[4]);
//                    chartData = NumberPatternAnalyzer.findNumberGroupLikelyToHit(positionalNumbers[4], positionalNumbers.length,
//                            (int) data[1], (int) data[2], lotteryGame);
                    break;
                case 5:
                    LottoBetSlipAnalyzer analyzerBonus = new LottoBetSlipAnalyzer(lotteryGame, 7);
                    System.out.print("Bonus Number");
                    break;
            }

            // Plot data on bar chart
            if (number < 5 && number >= 0) {
                int size = vBox.getChildren().size();
                if (size > 1)
                    vBox.getChildren().remove(1);

                setUpChart(number + 1, chartDataTwo);
            }

        });
    }

    private void setUpInfoPanel(Map<String, Map<String, Integer[]>> data) {

        ScrollPane pane = new ScrollPane();
        //pane.setPrefSize(300,300);
        AnchorPane.setTopAnchor(pane, 5.0);
        AnchorPane.setLeftAnchor(pane, 0.0);
        AnchorPane.setRightAnchor(pane, 0.0);
        AnchorPane.setBottomAnchor(pane, 0.0);
        pane.setFocusTraversable(false);
        pane.setStyle("-fx-background: #515b51; -fx-focus-color: transparent; -fx-border-color: black;");
        double wi = infoPane1.getPrefWidth();

        Pane p1 = panelOneSetUp(data);

        Label label1 = new Label();
        Pane p2 = new Pane();
        p2.setStyle("-fx-background: #16CAC3;");
        p2.setPrefSize(340, 200);
        p2.getChildren().addAll(label1);

        Pane p3 = new Pane();
        p3.setStyle("-fx-background: #16CAC3;");
        p3.setPrefSize(340, 200);

        VBox box = new VBox();
        box.setSpacing(120.0);
        box.getChildren().addAll(p1, p2, p3);


        pane.setContent(box);
        infoPane1.getChildren().add(pane);
    }

    private Pane panelOneSetUp(Map<String, Map<String, Integer[]>> data) {
        Label label = new Label("Winning Number Distribution Between Ranges");
        label.setStyle("-fx-text-fill: #DAC6AC;");
        Pane p1 = new Pane();
        p1.setPrefSize(340, 200);
        VBox v = new VBox();
        v.setPadding(new Insets(20, 0, 0, 0));
        p1.getChildren().addAll(label);
        label = new Label("Range\t" + "Hits\t\t" + "Games Out");
        label.setPadding(new Insets(20, 0, 0, 0));
        p1.getChildren().addAll(label);

        for (Map.Entry<String, Map<String, Integer[]>> d : data.entrySet()) {
            Map<String, Integer[]> dd = d.getValue();
            Label l = new Label(d.getKey());
            l.setStyle("-fx-text-fill: #DAC6AC;");
            l.setPadding(new Insets(20, 0, 0, 0));
            v.getChildren().addAll(l);

            for (Map.Entry<String, Integer[]> ddd : dd.entrySet()) {
                if (Integer.toString(ddd.getValue()[0]).length() == 4) {
                    l = new Label(String.format("%1s %2$8s %3$10s", ddd.getKey(), ddd.getValue()[0], ddd.getValue()[1]));
                    v.getChildren().add(l);
                } else {
                    l = new Label(String.format("%1s %2$7s %3$12s", ddd.getKey(), ddd.getValue()[0], ddd.getValue()[1]));
                    v.getChildren().add(l);

                }
            }
        }

        p1.getChildren().addAll(v);
        return p1;
    }


    private Map<Integer, Integer> extractInformationForBarChart(List<Object> rowInformation) {

        List<Integer> columnHitCounter = new LinkedList<>();

        Map<Integer, Object[]> chartData = (Map<Integer, Object[]>) rowInformation.get(1);

        for (Map.Entry<Integer, Object[]> dd : chartData.entrySet()) {

            Object[] value = dd.getValue();
            Map<Integer, Integer[]> numberAndHitCounter = (Map<Integer, Integer[]>) value[0];
            for (Map.Entry<Integer, Integer[]> ddd : numberAndHitCounter.entrySet()) {

                Integer[] data = ddd.getValue();
                columnHitCounter.add(ddd.getKey());
                columnHitCounter.add(data[0]);
            }

        }

        Map<Integer, Integer> sortedNums = new TreeMap<>();

        for (int num = 0; num < columnHitCounter.size(); num++) {

            if (num % 2 == 0 && num < columnHitCounter.size() - 1)
                sortedNums.put(columnHitCounter.get(num), columnHitCounter.get(num + 1));

        }

        return sortedNums;
    }


    /**
     * Method will be used to set up the bar chart with the appropriate data
     *
     * @param position
     */
    private void setUpChart(int position, Map<Integer, Integer> chartData) {

        bc.getData().clear();

        bc.setTitle(String.format("Best Group For Position %1s", position));
        bc.getXAxis().setLabel("Predicted Numbers");
        bc.getYAxis().setLabel("Hits in Pos " + position);
        bc.legendVisibleProperty().setValue(false);
        bc.setAnimated(false);

        if (chartData != null) {

            XYChart.Series<String, Number> series = new XYChart.Series<>();

            for (Map.Entry<Integer, Integer> data : chartData.entrySet()) {

                String num = Integer.toString(data.getKey());
                series.getData().add(new XYChart.Data<>(num, data.getValue()));

            }
            bc.getData().add(series);


        }
    }

    private void styleChart() {

        bc.getStylesheets().addAll("/com/lottoanalysis/styles/bar_chart.css");

    }

    public static void loadPositionalNumbers(int[][] positions, ObservableList<Drawing> drawings) {
        loadUpPositionalNumbers(positions, drawings);
    }

    /**
     * Loader that injects values into perspective draw positions from a Lotterygame object.
     *
     * @param positionalNumbers
     * @param drawingData
     */
    private static void loadUpPositionalNumbers(int[][] positionalNumbers, ObservableList<Drawing> drawingData) {

        for (int i = 0; i < drawingData.size(); i++) {

            if (positionalNumbers.length == 3) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
            } else if (positionalNumbers.length == 4) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
            } else if (positionalNumbers.length == 5) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
                positionalNumbers[4][i] = Integer.parseInt(drawingData.get(i).posFiveProperty().get());
            } else if (positionalNumbers.length == 6) {
                positionalNumbers[0][i] = Integer.parseInt(drawingData.get(i).posOneProperty().get());
                positionalNumbers[1][i] = Integer.parseInt(drawingData.get(i).posTwoProperty().get());
                positionalNumbers[2][i] = Integer.parseInt(drawingData.get(i).posThreeProperty().get());
                positionalNumbers[3][i] = Integer.parseInt(drawingData.get(i).posFourProperty().get());
                positionalNumbers[4][i] = Integer.parseInt(drawingData.get(i).posFiveProperty().get());
                positionalNumbers[5][i] = Integer.parseInt(drawingData.get(i).bonusNumberProperty().get());

            }


        }
    }

    public void setGameLabels(String gameName) {

        if (!gameName.equalsIgnoreCase("update database")) {

            if (gameName.equals("Super Lotto Plus")) {

                this.lottoDashboard.setText(gameName + " Dashboard");

            } else {

                this.lottoDashboard.setText(gameName + " Lotto Dashboard");

            }

            this.predictedNumbersLabel.setText("Historical Draw Table For: " + gameName.split(":")[1]);
        }

    }

    public void showPane() {
        pane.setVisible(true);
    }

    public void hidePane() {
        pane.setVisible(false);
    }

    /**
     * Method will take in any lottery game and and set up the number draw tableview. Method also calls the necessary methods
     * to aid in populating the choicebox in the lotto dashboard section.
     *
     * @param lotteryGame
     */
    public void setUpTableView(LotteryGame lotteryGame) {

        positionalNumbers = null;
        drawNumberTable.getItems().clear();
        drawNumberTable.getColumns().clear();
        drawNumberTable.setVisible(true);
        predictedNumbersLabel.setVisible(true);

        this.lotteryGame = lotteryGame;

        ObservableList<Drawing> data = lotteryGame.getDrawingData();
        List<String> tableColumnName = new LinkedList<>();
        tableColumnName.add("Draw #");
        tableColumnName.add("Draw Date");

        TableColumn[] tableColumns = null;
        Map<SimpleStringProperty, String> newData = null;


        tableColumns = new TableColumn[4 + Drawing.drawSize];

        for (int i = 0; i < Drawing.drawSize; i++) {

            if (Drawing.drawSize > 5 && i == Drawing.drawSize - 1)
                tableColumnName.add("Bonus ");

            else
                tableColumnName.add("Pos " + (i + 1));

        }

        tableColumnName.add("Sum");
        tableColumnName.add("E/O");

        drawNumberTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        for (int i = 0; i < tableColumnName.size(); i++) {
            tableColumns[i] = new TableColumn<>(tableColumnName.get(i));
            if (i == 1)
                tableColumns[i].setMaxWidth(1f * Integer.MAX_VALUE * 70);
            else
                tableColumns[i].setMaxWidth(1f * Integer.MAX_VALUE * 30);

            if (tableColumnName.size() > 9 && i == 7) {
                tableColumns[i].setCellFactory(new Callback<TableColumn<Drawing, String>, TableCell<Drawing, String>>() {

                    @Override
                    public TableCell<Drawing, String> call(TableColumn<Drawing, String> param) {
                        return new TableCell<Drawing, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!isEmpty()) {
                                    this.setTextFill(Color.valueOf("#EFA747"));
                                    // Get fancy and change color based on data
                                    if (item.contains("@"))
                                        this.setTextFill(Color.BLUEVIOLET);
                                    setText(item);
                                }
                            }
                        };
                    }
                });
            } else {
                tableColumns[i].setCellFactory(new Callback<TableColumn<Drawing, String>, TableCell<Drawing, String>>() {

                    @Override
                    public TableCell<Drawing, String> call(TableColumn<Drawing, String> param) {
                        return new TableCell<Drawing, String>() {

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (!isEmpty()) {
                                    this.setTextFill(Color.BEIGE);
                                    // Get fancy and change color based on data
                                    if (item.contains("@"))
                                        this.setTextFill(Color.BLUEVIOLET);
                                    setText(item);
                                }
                            }
                        };
                    }
                });
            }
            tableColumns[i].setSortable(false);
            drawNumberTable.getColumns().add(tableColumns[i]);
        }
        lottoDashboard.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");

        setUpCellValueFactories(tableColumns, Drawing.drawSize);
        drawNumberTable.scrollTo(lotteryGame.getDrawingData().size() - 1);
        drawNumberTable.setItems(lotteryGame.getDrawingData());
    }

    private void setUpCellValueFactories(TableColumn[] tableColumns, int drawSize) {

        tableColumns[0].setCellValueFactory(new PropertyValueFactory("drawNumber"));
        tableColumns[1].setCellValueFactory(new PropertyValueFactory("drawDate"));

        switch (drawSize) {
            case 3:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;
            case 4:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("posFour"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[7].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;
            case 5:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("posFour"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("posFive"));
                tableColumns[7].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[8].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;

            case 6:
                tableColumns[2].setCellValueFactory(new PropertyValueFactory("posOne"));
                tableColumns[3].setCellValueFactory(new PropertyValueFactory("posTwo"));
                tableColumns[4].setCellValueFactory(new PropertyValueFactory("posThree"));
                tableColumns[5].setCellValueFactory(new PropertyValueFactory("posFour"));
                tableColumns[6].setCellValueFactory(new PropertyValueFactory("posFive"));
                tableColumns[7].setCellValueFactory(new PropertyValueFactory("bonusNumber"));
                tableColumns[8].setCellValueFactory(new PropertyValueFactory("drawSum"));
                tableColumns[9].setCellValueFactory(new PropertyValueFactory("oddEvenRatio"));
                break;

        }
    }

    private void setTextStyleForAllLabels() {

        List<Node> children = pane.getChildren();
        for (Node node : children) {
            if (node instanceof Label) {
                Label label = (Label) node;
                label.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
            }
        }
    }

    private void loadDefaultGameForView() {

        choiceBox.getItems().clear();

        LotteryGameManager manager = LotteryGameManagerImpl.getInstance();

        String gameName = "CA: Fantasy Five";
        //this.lotteryGame = new FiveDigitLotteryGame(gameName);
        String name = "Fantasy Five";
        String newName = "";

        for (int i = 0; i < name.length(); i++) {

            if (Character.isUpperCase(name.charAt(i)) && i == 0)

                newName = Character.toString(name.charAt(i));

            else if (Character.isUpperCase(name.charAt(i)) && i > 0) {
                newName += " " + Character.toString(name.charAt(i));
            } else {
                newName += Character.toString(name.charAt(i));
            }

        }

        predictedNumbersLabel.setText("Historical Draw Table For: " + newName);
        lottoDashboard.setText(gameName + "Lotto Dashboard");
        this.lotteryGame = manager.loadLotteryData(gameName, "fantasy_five_results", 5);

        setClassLevelLotteryGame(lotteryGame);

        setUpTableView(lotteryGame);
        loadChoicesIntoChoiceBox();
        performOperationOnChoiceboxValue();

        LottoBetSlipAnalyzer analyzerPosOne = new LottoBetSlipAnalyzer(lotteryGame, 2);
        List<Object> rowInformation = analyzerPosOne.getAllInformationForRowAndPosition();
        Map<Integer, Integer> chartDataTwo = extractInformationForBarChart(rowInformation);
        setUpChart(1, chartDataTwo);

    }

    public LotteryGame getLotteryGame() {
        return lotteryGame;
    }

    public void displayChartAnalysisScreen() {

        try {

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("/com/lottoanalysis/view/ChartAnalysis.fxml"));
            AnchorPane pane = loader.load();

            ChartAnalysisController chartAnalysisController = loader.getController();
//            chartAnalysisController.init(mainController);
//            chartAnalysisController.clearButtonBox();
//            chartAnalysisController.setNumbers(positionalNumbers);
//            chartAnalysisController.setGame(lotteryGame);
            //chartAnalysisController.setUpChart();
            //chartAnalysisController.loadDataIntoPerspectivePanes();

            GamesOutViewDepicter depicter = new GamesOutViewDepicter(positionalNumbers);

            Scene scene = new Scene(pane);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setResizable(false);
            stage.setTitle("Chart Analysis");
            stage.show();

//            stage.setOnCloseRequest(e -> {
//                mainController.lottoAnalysisHomeController.enableChartButton();
//            });


        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

