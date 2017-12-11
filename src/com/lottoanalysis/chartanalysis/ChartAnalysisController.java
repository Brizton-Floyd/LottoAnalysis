package com.lottoanalysis.chartanalysis;

import com.jfoenix.controls.JFXButton;
import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.lottoinfoandgames.LotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickFourLotteryGame;
import com.lottoanalysis.lottoinfoandgames.lottogames.PickThreeLotteryGame;
import com.lottoanalysis.screenloader.MainController;
import com.lottoanalysis.utilities.ChartHelper;
import com.lottoanalysis.utilities.LineRetracementAnalyzer;
import com.lottoanalysis.utilities.LottoBetSlipDefinitions;
import com.lottoanalysis.utilities.NumberAnalyzer;
import com.sun.javafx.font.freetype.HBGlyphLayout;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.net.URL;
import java.util.*;

public class ChartAnalysisController  {

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
    private Label gameTitle, analyzedPosition,hotHits,hotOut,warmHits,warmOut,coldHits,coldOut, warmHitsAtGamesOut,
            hotHitsAtGamesOut,coldHitsAtGamesOut, hotOutLastSeen, warmOutLastSeen, coldOutLastSeen,hotOutLastSeenHits,
            warmOutLastSeenHits,coldOutLastSeenHits;

    @FXML
    private AnchorPane chartOneAnchorPane, chartTwoAnchorPane, chartThreeAnchorPane, areaChartAnchorPane;

    @FXML
    private HBox buttonHbox;

    @FXML
    private AreaChart areaChart;

    @FXML
    public void intialize(){


    }

    private void processDataForChartRendering(int position){

        // set up headers
        setUpHeaders( position );

        int[] positionArray = drawNumbers[position];

        // set up top level charts
        List<Object[]> upperChartData = ChartHelper.setUpTopLevelCharts( positionArray );
        setUpTopCharts( upperChartData );

        // set up main area chart
        setUpAreaChart(positionArray);

    }

    @SuppressWarnings("unchecked")
    private void setUpAreaChart(int[] positionArray) {

        areaChart.getData().clear();
        CategoryAxis xAxis = (CategoryAxis) areaChart.getXAxis();
        xAxis.setStyle("-fx-tick-label-fill: #dac6ac");

        NumberAxis yAxis = (NumberAxis) areaChart.getYAxis();
        yAxis.setStyle("-fx-tick-label-fill: #dac6ac");

        XYChart.Series currentWinningNums = new XYChart.Series();
        currentWinningNums.setName("Current Winning Numbers");
        int count = 1;

        for(int i = positionArray.length - 80; i < positionArray.length; i++){

            currentWinningNums.getData().add( new XYChart.Data<>(count++ + "", positionArray[i]));
        }

        XYChart.Series proceedingDigit = new XYChart.Series();
        proceedingDigit.setName("Proceeding winning num");
        count = 1;
        int[] data = ChartHelper.generateListOfAllWinningNumbersAfterCurrentWinningNumber( positionArray);
        int num = (data.length >= 100) ? data.length - 80 : 0;

        for(int i = num; i < data.length; i++){
            proceedingDigit.getData().add( new XYChart.Data<>(count++ + "",data[i]));
        }
        areaChart.getData().addAll(currentWinningNums, proceedingDigit);
    }

    @SuppressWarnings("unchecked")
    private void setUpTopCharts(List<Object[]> upperChartData) {

        AnchorPane[] panes = {chartOneAnchorPane, chartTwoAnchorPane, chartThreeAnchorPane};
        Label[][] labelData = {{hotHits,hotOut},{warmHits,warmOut},{coldHits,coldOut}};
        Label[] hitAtGamesOut = {hotHitsAtGamesOut,warmHitsAtGamesOut,coldHitsAtGamesOut};
        Label[] outLastSeen = { hotOutLastSeen, warmOutLastSeen,coldOutLastSeen};
        Label[] outLastSeenHits = {hotOutLastSeenHits, warmOutLastSeenHits, coldOutLastSeenHits};

        String[] colors = {"#FF0000","#FF0000","#FF0000"};

        for (int i = 0; i < upperChartData.size(); i++){

            List<Integer> points = (List<Integer>)upperChartData.get(i)[2];
            Set<Integer> unique = new HashSet<>( points );

            LineChartWithHover lc = new LineChartWithHover(FXCollections.observableArrayList(points.subList(points.size() - 60,
                                                            points.size())),
                                                            colors[i],
                                                            (int)upperChartData.get(i)[3],
                                                            (int)upperChartData.get(i)[4],unique.toString());
            Label[] currentLabel = labelData[i];
            currentLabel[0].setText( upperChartData.get(i)[0]+"");
            currentLabel[1].setText( upperChartData.get(i)[1]+"");

            hitAtGamesOut[i].setText( upperChartData.get(i)[6]+"");

            outLastSeen[i].setText( upperChartData.get(i)[7]+"");
            outLastSeenHits[i].setText(upperChartData.get(i)[8]+"");

            panes[i].getChildren().clear();
            panes[i].getChildren().add( lc.getLineChart() );dd
        }
    }

    private void setUpHeaders(int position) {

        gameTitle.setText("");
        gameTitle.setText( "Chart Analysis: " + lotteryGame.getGameName() );

       analyzedPosition.setText("");
       analyzedPosition.setText("Currently Analyzing Position " + (position + 1));

    }

    public void start(){


        if(buttonHbox.getChildren().size() > 0)
            buttonHbox.getChildren().clear();

        // get size of 2D array
        JFXButton[] buttons = new JFXButton[drawNumbers.length];
        for(int i = 0; i < buttons.length; i++){
            buttons[i] = new JFXButton("Pos " + (i+1));
            buttons[i].setStyle("-fx-text-fill: #dac6ac;");
            final int postion = i;
            buttons[i].setOnAction( e -> {

                processDataForChartRendering( postion );

            });
            buttons[i].setOnMouseEntered(e -> {
                buttons[postion].setStyle("-fx-font-size: 10px;" +
                        "-fx-background-color: #dac6ac;" +
                        "-fx-text-fill: #10000C;");
            });
            buttons[i].setOnMouseExited(e -> {
                buttons[postion].setStyle("-fx-text-fill: #dac6ac;");

            });

            buttonHbox.getChildren().add( buttons[i] );
        }

        buttonHbox.setSpacing(10);
        processDataForChartRendering(initialPosition);
    }
}
