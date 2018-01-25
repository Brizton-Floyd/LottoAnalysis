package com.lottoanalysis.chartanalysis;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.utilities.*;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;

import java.util.*;

public class ChartAnalysisController {

    private LotteryGame lotteryGame;
    private int[][] drawNumbers;
    private int initialPosition = 0;

    public LotteryGame getLotteryGame() {
        return lotteryGame;
    }

    public void setLotteryGame(LotteryGame lotteryGame) {
        this.lotteryGame = lotteryGame;
    }

    public int[][] getDrawNumbers() {
        return drawNumbers;
    }

    public void setDrawNumbers(int[][] drawNumbers) {
        this.drawNumbers = drawNumbers;
    }

    @FXML
    private Label gameTitle, analyzedPosition, hotHits, hotOut, warmHits, warmOut, coldHits, coldOut, warmHitsAtGamesOut,
            hotHitsAtGamesOut, coldHitsAtGamesOut, hotOutLastSeen, warmOutLastSeen, coldOutLastSeen, hotOutLastSeenHits,
            warmOutLastSeenHits, coldOutLastSeenHits;

    @FXML
    private Label lastHitPos, lottoGamesOut, posHits, lottoNum;

    @FXML
    private AnchorPane chartOneAnchorPane, chartTwoAnchorPane, chartThreeAnchorPane, areaChartAnchorPane;

    @FXML
    private HBox buttonHbox;

    @FXML
    private MenuButton menuButton;

    @FXML
    private RadioButton rdbFullLottoButton;

    @FXML
    public void intialize() {


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

        // clear the compainion hit tracker first before rendering any new chart data
        ChartHelper.clearStaticCharts();
        rdbFullLottoButton.selectedProperty().setValue(true);

        // set up headers
        setUpHeaders(position);

        int[] positionArray = drawNumbers[position];
        loadMenuButtonDropDown(positionArray, position);
        // set up top level charts
        getDataForTopChartRendering(positionArray);

        // print recent winning number companion hits
        print(ChartHelper.getRecentWinningNumberCompanionHitTracker());
    }

    private void getDataForTopChartRendering(int[] posisitionArray) {

        ObservableList<Toggle> toggles = rdbFullLottoButton.getToggleGroup().getToggles();
        for (Toggle toggle : toggles) {

            RadioButton radioButton = (RadioButton) toggle;
            radioButton.setOnAction(e -> {
                switch (radioButton.getText()) {

                    case LotteryGameConstants.ELE_ONE:
                        if (radioButton.isSelected()) {
                            ChartHelper.clearStaticCharts();
                            int[] eleOneData = ChartHelper.returnNumbersAtIndex(posisitionArray, "0");
                            List<Object[]> upperChartData = ChartHelper.setUpTopLevelCharts(eleOneData,"1");
                            setUpTopCharts(upperChartData);
                            //Groupings.analyze( eleOneData );
                            print(ChartHelper.getRecentWinningNumberCompanionHitTracker());
                        }
                        break;
                    case LotteryGameConstants.ELE_TWO:
                        if(radioButton.isSelected()) {
                            ChartHelper.clearStaticCharts();
                            int[] eleTwoData = ChartHelper.returnNumbersAtIndex(posisitionArray, "1");
                            List<Object[]> upperChartDataTwo = ChartHelper.setUpTopLevelCharts(eleTwoData,"1");
                            setUpTopCharts(upperChartDataTwo);
                            //Groupings.analyze( eleTwoData );
                            print(ChartHelper.getRecentWinningNumberCompanionHitTracker());

                        }
                        break;
                    case LotteryGameConstants.FULL_NUM:
                        if(radioButton.isSelected()) {
                            ChartHelper.clearStaticCharts();
                            int[] fullNumData = ChartHelper.returnNumbersAtIndex(posisitionArray, null);
                            List<Object[]> upperChartDataFullNum = ChartHelper.setUpTopLevelCharts(fullNumData,"2");
                            setUpTopCharts(upperChartDataFullNum);
                            print(ChartHelper.getRecentWinningNumberCompanionHitTracker());
                            ChartHelperTwo.processIncomingData(lotteryGame,fullNumData,10);

                        }
                        break;
                    default:
                        break;
                }

            });
        }
        int[] eleOneData = ChartHelper.returnNumbersAtIndex(posisitionArray, null);
        List<Object[]> upperChartData = ChartHelper.setUpTopLevelCharts(eleOneData,"2");
        setUpTopCharts(upperChartData);
        ChartHelperTwo.processIncomingData(lotteryGame,eleOneData,10);
        Map<String,Object[]> dd = ChartHelperTwo.getGroupHitInformation();
        System.out.println();

    }

    private void print(Map<Integer, Map<Integer, Integer[]>> mapData) {

        mapData.forEach((key, value) -> {
            System.out.println("\nRecent Winning Lotto Number: " + key + "\n");
            value.forEach((keyTwo, valueTwo) -> {
                System.out.println("Companion Number: " + keyTwo + "\tHits and Games Out: " + Arrays.toString(valueTwo));
            });
            System.out.println("\nHit Length Due for Lotto Number: " + key);
            ChartHelper.getRecentWinningNumberLineLengthDue().get(key).forEach((keyThree, valueThree) -> {
                System.out.println("Hit Length: " + keyThree + "\tHits and Games Out: " + Arrays.toString(valueThree));

                Map<Integer, Map<Integer, Integer[]>> data = ChartHelper.getRemainderDueForLineLength().get(key);
                Map<Integer, Integer[]> dataTwo = data.get(keyThree);
                System.out.println("\nRemainder Due For Hit Length: " + keyThree);
                dataTwo.forEach((keyFour, keyFive) -> {
                    System.out.println("\nRemainder Number: " + keyFour + "\tHits and Games Out: " + Arrays.toString(keyFive));
                });
                System.out.println("\n");
            });
        });
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
    private void setUpTopCharts(List<Object[]> upperChartData) {

        AnchorPane[] panes = {chartOneAnchorPane, chartTwoAnchorPane, chartThreeAnchorPane};
        Label[][] labelData = {{hotHits, hotOut}, {warmHits, warmOut}, {coldHits, coldOut}};
        Label[] hitAtGamesOut = {hotHitsAtGamesOut, warmHitsAtGamesOut, coldHitsAtGamesOut};
        Label[] outLastSeen = {hotOutLastSeen, warmOutLastSeen, coldOutLastSeen};
        Label[] outLastSeenHits = {hotOutLastSeenHits, warmOutLastSeenHits, coldOutLastSeenHits};

        String[] colors = {"#FF0000", "#FF0000", "#FF0000"};

        for (int i = 0; i < upperChartData.size(); i++) {

            List<Integer> points = (List<Integer>) upperChartData.get(i)[2];

            List<Integer> pointTwo = (points.size() > 0) ? ChartHelper.getListOfNumbersBasedOnCurrentWinningNumber(points) : new ArrayList<>();
            Set<Integer> unique = new HashSet<>(points);

            // Plug all recent winning numbers accross all charts into a static map
            if(points.size() > 0)
             ChartHelper.plugNumbersIntoRecentWinningNumberCompanionMap(points);

            List<List<Integer>> dataPoints = new ArrayList<>();
            dataPoints.add((points.size() > 100) ? points.subList(points.size() - 60, points.size()) : points);

           // if(pointTwo.size() > 0)
                //dataPoints.add((pointTwo.size() > 100) ? pointTwo.subList(pointTwo.size() - 30, pointTwo.size()) : pointTwo);

            //LineSpacingHelper.determineMostProbableLineSpacing(points);
           // LineSpacingHelper.num++;

            LineChartWithHover lc = new LineChartWithHover(dataPoints,
                    colors[i],
                    (int) upperChartData.get(i)[3],
                    (int) upperChartData.get(i)[4], unique.toString(),null,374,248);

            Label[] currentLabel = labelData[i];
            currentLabel[0].setText(upperChartData.get(i)[0] + "");
            currentLabel[1].setText(upperChartData.get(i)[1] + "");

            hitAtGamesOut[i].setText(upperChartData.get(i)[6] + "");

            outLastSeen[i].setText(upperChartData.get(i)[7] + "");
            outLastSeenHits[i].setText(upperChartData.get(i)[8] + "");

            panes[i].getChildren().clear();
            panes[i].getChildren().add(lc.getLineChart());
        }
    }

    private void setUpHeaders(int position) {

        gameTitle.setText("");
        gameTitle.setText("Chart Analysis: " + lotteryGame.getGameName());

        analyzedPosition.setText("");
        analyzedPosition.setText("Currently Analyzing Position " + (position + 1));

    }

    public void start() {


        if (buttonHbox.getChildren().size() > 0)
            buttonHbox.getChildren().clear();

        // get size of 2D array
        JFXButton[] buttons = new JFXButton[drawNumbers.length];
        for (int i = 0; i < buttons.length; i++) {
            buttons[i] = new JFXButton("Pos " + (i + 1));
            buttons[i].setStyle("-fx-text-fill: #dac6ac;");
            final int postion = i;
            buttons[i].setOnAction(e -> {

                processDataForChartRendering(postion);
                makeInvisible();
            });
            buttons[i].setOnMouseEntered(e -> {
                buttons[postion].setStyle("-fx-font-size: 10px;" +
                        "-fx-background-color: #dac6ac;" +
                        "-fx-text-fill: #10000C;");
            });
            buttons[i].setOnMouseExited(e -> {
                buttons[postion].setStyle("-fx-text-fill: #dac6ac;");

            });

            buttonHbox.getChildren().add(buttons[i]);
        }

        buttonHbox.setSpacing(10);
        processDataForChartRendering(initialPosition);
    }
}
