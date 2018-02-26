package com.lottoanalysis.charts;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.List;

/**
 * Displays a LineChart which displays the value of a plotted Node when you hover over the Node.
 */
@SuppressWarnings("unchecked")
public class LineChartWithHover {

    private LineChart lineChart;
    //private ObservableList<Integer> values;
    private String lineColor;
    private int lower, upper;


    public LineChartWithHover(List<List<Integer>> dataPoints, String lineColor, int lower, int upper, String nums,String title, int width, int height) {

        this.lineColor = lineColor;
        this.lower = lower;
        this.upper = upper;

        lineChart = new LineChart(new NumberAxis(), new NumberAxis());
        if(title != null)
            lineChart.setTitle(title);
        loadDataPointsIntoChart( lineChart, dataPoints);

        lineChart.getStylesheets().add("/com/lottoanalysis/styles/line_chart.css");
        lineChart.setPrefWidth(width);
        lineChart.setPrefHeight(height);
        lineChart.legendVisibleProperty().setValue(false);
        lineChart.setAnimated(true);
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.setAutoRanging(false);
        xAxis.tickLabelFontProperty().set(Font.font(8));
        xAxis.setUpperBound(dataPoints.get(0).size()+1);
        //xAxis.setTickLabelRotation(20);
        xAxis.setStyle("-fx-tick-label-fill: #dac6ac");

        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.tickLabelFontProperty().set(Font.font(8));
        yAxis.setStyle("-fx-tick-label-fill: #dac6ac");
        yAxis.setAutoRanging(false);
        yAxis.setLabel(nums);
        yAxis.setLowerBound(lower-1);
        yAxis.setUpperBound(upper+1);

        yAxis.setTickUnit(4);

    }

    private void loadDataPointsIntoChart(LineChart lineChart, List<List<Integer>> dataPoints) {

        for( int i = 0; i < dataPoints.size(); i++){

            ObservableList<XYChart.Data<Integer,Integer>> dataSet = FXCollections.observableArrayList(plot(dataPoints.get(i)));
            XYChart.Series<Integer,Integer> data = new XYChart.Series("", dataSet );
            lineChart.getData().add( data );
        }
    }

    public LineChart getLineChart() {
        return lineChart;
    }

    /**
     * @return plotted y values for monotonically increasing integer x values, starting from x=1
     */
    private ObservableList<XYChart.Data<Integer, Integer>> plot(List<Integer> y) {

        final ObservableList<XYChart.Data<Integer, Integer>> dataset = FXCollections.observableArrayList();

        int i = 0;
        while (i < y.size()) {
            final XYChart.Data<Integer, Integer> data = new XYChart.Data<>(i + 1, y.get(i));
            //final XYChart.Data<Integer, Integer> datatwo = new XYChart.Data<>(i + 1, 15);

            data.setNode(
                    new HoveredThresholdNode(
                            (i == 0) ? 0 : y.get(i -1),
                            y.get(i)
                    )
            );

            dataset.add(data);
            //dataset.add(datatwo);

            i++;
        }

        return dataset;
    }

    /**
     * a node which displays a value on hover, but is otherwise empty
     */
    class HoveredThresholdNode extends StackPane {
        HoveredThresholdNode(int priorValue, int value) {
            setPrefSize(12, 12);

            final Label label = createDataThresholdLabel(priorValue, value);

            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().setAll(label);
                    setCursor(Cursor.NONE);
                    toFront();
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    getChildren().clear();
                    setCursor(Cursor.CROSSHAIR);
                }
            });
        }

        private Label createDataThresholdLabel(int priorValue, int value) {

            final Label label = new Label(value + "");
            label.getStyleClass().addAll("default-color0", "chart-line-symbol", "chart-series-line");
            label.setStyle("-fx-font-size: 10; -fx-font-weight: bold;");

            if(lineColor != null)
                label.setTextFill(Color.valueOf(lineColor));
            else
                label.setTextFill(Color.valueOf("#FF0000"));

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }
}