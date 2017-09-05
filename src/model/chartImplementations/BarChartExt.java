package model.chartImplementations;

import javafx.scene.Node;
import javafx.scene.chart.Axis;
import javafx.scene.chart.BarChart;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;

import java.util.HashMap;
import java.util.Map;


/**
 * Custom barchart with text on top of bars
 *
 * @param <X>
 * @param <Y>
 */
public class BarChartExt<X, Y> extends BarChart<X, Y> {

    /**
     * Registry for text nodes of the bars
     */
    Map<Node, Node> nodeMap = new HashMap<>();

    public BarChartExt(Axis xAxis, Axis yAxis) {
        super(xAxis, yAxis);
    }

    /**
     * Add text for bars
     */
    @Override
    protected void seriesAdded(Series<X, Y> series, int seriesIndex) {

        super.seriesAdded(series, seriesIndex);

        for (int j = 0; j < series.getData().size(); j++) {

            Data<X, Y> item = series.getData().get(j);

            Text text = new Text(item.getYValue().toString());
            text.setStyle("-fx-font-size: 10pt;");

            TextFlow textFlow = new TextFlow(text);
            textFlow.setTextAlignment(TextAlignment.CENTER);

            nodeMap.put(item.getNode(), textFlow);
            this.getPlotChildren().add(textFlow);


        }

    }

    /**
     * Remove text of bars
     */
    @Override
    protected void seriesRemoved(final Series<X, Y> series) {

        for (Node bar : nodeMap.keySet()) {

            Node text = nodeMap.get(bar);
            getPlotChildren().remove(text);

        }

        nodeMap.clear();

        super.seriesRemoved(series);
    }

    /**
     * Adjust text of bars, position them on top
     */
    @Override
    protected void layoutPlotChildren() {

        super.layoutPlotChildren();

        for (Node bar : nodeMap.keySet()) {


            TextFlow textFlow = (TextFlow) nodeMap.get(bar);

            if (bar.getBoundsInParent().getHeight() > 30) {
                ((Text) textFlow.getChildren().get(0)).setFill(Color.WHITE);
                textFlow.resize(bar.getBoundsInParent().getWidth(), 200);
                textFlow.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() + 10);
            } else {
                ((Text) textFlow.getChildren().get(0)).setFill(Color.GRAY);
                textFlow.resize(bar.getBoundsInParent().getWidth(), 200);
                textFlow.relocate(bar.getBoundsInParent().getMinX(), bar.getBoundsInParent().getMinY() - 20);
            }


        }

    }
}