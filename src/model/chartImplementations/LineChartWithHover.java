package model.chartImplementations;

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

/**
 * Displays a LineChart which displays the value of a plotted Node when you hover over the Node.
 */
public class LineChartWithHover {

    private LineChart lineChart;
    private ObservableList<Integer> values;

    @SuppressWarnings("unchecked")
    public LineChartWithHover(String title, ObservableList<Integer> values) {

        this.values = values;

        lineChart = new LineChart(
                new NumberAxis(), new NumberAxis(),
                FXCollections.observableArrayList(
                        new XYChart.Series("", FXCollections.observableArrayList(plot(this.values)))
                )
        );

        lineChart.getStylesheets().add("/styles/line_chart.css");
        lineChart.setPrefWidth(630);
        lineChart.setPrefHeight(323);
        lineChart.legendVisibleProperty().setValue(false);
        lineChart.setTitle(title);
        lineChart.setAnimated(true);
        NumberAxis xAxis = (NumberAxis) lineChart.getXAxis();
        xAxis.tickLabelFontProperty().set(Font.font(8));

        //xAxis.setTickLabelRotation(20);
        xAxis.setStyle("-fx-tick-label-fill: #dac6ac");
        xAxis.setLabel("Drawing Ranges");

        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.tickLabelFontProperty().set(Font.font(8));
        yAxis.setStyle("-fx-tick-label-fill: #dac6ac");
        yAxis.setAutoRanging(true);
        yAxis.setLabel("Digit");

    }

    @SuppressWarnings("unchecked")
    public void removePointsFromList(int begin, int end){
        getLineChart().getData().clear();
        values.remove(begin,end);
        XYChart.Series data = new XYChart.Series("",FXCollections.observableArrayList(plot(values)));

        getLineChart().getData().add(data);
    }
    public LineChart getLineChart() {
        return lineChart;
    }

    /**
     * @return plotted y values for monotonically increasing integer x values, starting from x=1
     */
    private ObservableList<XYChart.Data<Integer, Integer>> plot(ObservableList<Integer> y) {

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

            label.setTextFill(Color.FIREBRICK);

            label.setMinSize(Label.USE_PREF_SIZE, Label.USE_PREF_SIZE);
            return label;
        }
    }
}