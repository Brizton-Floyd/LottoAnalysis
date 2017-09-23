package controller.logicalControllers;

import com.jfoenix.controls.JFXButton;
import controller.MainController;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import model.LotteryGame;
import sun.font.FontFamily;
import org.jsoup.Jsoup;
import javafx.scene.paint.*;
import javax.swing.text.html.HTML;
import java.awt.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ChartAnalysisController implements Initializable{

    private MainController mainController;
    private LotteryGame game;
    private int[][] numbers;

    @FXML
    private AnchorPane chartAnalysisMainPane;

    @FXML
    private RadioButton last20;

    @FXML
    private LineChart lineChart;

    @FXML
    private TextFlow aboveText;
    @FXML
    private TextFlow belowText;

    @FXML
    private HBox buttonContainerBox;

    private void createDynamicButtons() {

        for(int i = 0; i < numbers.length; i++){
            JFXButton button = new JFXButton("Pos " + (i + 1));
            final int val = i;
            button.setFont(Font.font(10));
            button.setFocusTraversable(false);
            HBox.setMargin(button,new Insets(15,0,0,0));
            button.setOnAction(e -> loadChart(numbers[val],20,(val + 1)));
            // button.setStyle("-fx-font-family: 'Encode Sans Semi Condensed', sans-serif;");
            button.textFillProperty().setValue(Paint.valueOf("#dac6ac"));
            button.setOnMouseEntered(e -> {button.setStyle("-fx-font-size: 10px;" +
                    "-fx-background-color: #dac6ac;" +
                    "-fx-text-fill: #10000C;");});
            button.setOnMouseExited(e -> {
                button.setStyle("-fx-text-fill: #dac6ac;");

            });
            buttonContainerBox.getChildren().add(button);
        }
    }
    public void init(MainController mainController) {

        this.mainController = mainController;
        lineChart.getStylesheets().add("/styles/line_chart.css");

        CategoryAxis xAxis = (CategoryAxis) lineChart.getXAxis();
        xAxis.tickLabelFontProperty().set(Font.font(8));
        //xAxis.setTickLabelRotation(20);
        xAxis.setStyle("-fx-tick-label-fill: #dac6ac");
        xAxis.setLabel("Drawing Ranges");

        NumberAxis yAxis = (NumberAxis) lineChart.getYAxis();
        yAxis.tickLabelFontProperty().set(Font.font(8));
        yAxis.setStyle("-fx-tick-label-fill: #dac6ac");
        yAxis.setAutoRanging(true);
        yAxis.setLabel("Digit");

        TextFlow[] d = {aboveText, belowText};

        for(int i = 0; i < d.length; i++){

            Text t = new Text("When the above average hit counter is ");
            t.setFill(Color.valueOf("#dac6ac"));

            Text tt = new Text(" 8 ");
            tt.setFill(Color.BLACK);

            Text ttt = new Text(" games out. The next draw number was greater than the position average ");
            ttt.setFill(Color.valueOf("#dac6ac"));

            Text tttt = new Text(" 755 ");
            tttt.setFill(Color.BLACK);

            Text ttttt = new Text(" times. Its been ");
            ttttt.setFill(Color.valueOf("#dac6ac"));

            Text tttttt = new Text(" 0 ");
            tttttt.setFill(Color.BLACK);

            Text ttttttt = new Text(" draws since the current games out appeared in the next drawing. ");
            ttttttt.setFill(Color.valueOf("#dac6ac"));

            d[i].getChildren().addAll(t,tt, ttt, tttt,ttttt,tttttt,ttttttt);

        }




//        aboveText.getChildren().addAll();
//        belowText.getChildren().addAll(t);
        //aboveText.setFill(Paint.valueOf("#dac6ac"));

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ToggleGroup toggleGroup = last20.getToggleGroup();
        ObservableList<Toggle> toggles = toggleGroup.getToggles();

        for(Toggle t : toggles){

            if(t instanceof RadioButton){
                RadioButton button = (RadioButton)t;
                switch (button.getText()){
                    case"Last 20 Draws":
                        button.setOnAction( e -> {});
                        break;
                    case"Last 40 Draws":
                        button.setOnAction( e -> {});
                        break;
                    case"Last 60 Draws":
                        button.setOnAction( e -> {});
                        break;
                    case"Last 80 Draws":
                        button.setOnAction( e -> {});
                        break;
                    case"Last 100 Draws":
                        button.setOnAction( e -> {});
                        break;

                }
            }
        }
    }

    private void loadChart(int[] number, int i, int pos) {
        lineChart.getData().clear();

        lineChart.setTitle(game.getGameName() + " Pos " + pos + " Analysis");
        System.out.println(game.getGameName() + "Pos " + pos + " Analysis");
    }

    public void setNumbers(int[][] numbers) {
        this.numbers = numbers;
        createDynamicButtons();
    }

    public void showPane(){
        chartAnalysisMainPane.setVisible(true);
    }

    public void hidePane(){
        chartAnalysisMainPane.setVisible(false);
    }

    public void clearButtonBox() {
        if( buttonContainerBox != null && buttonContainerBox.getChildren().size() > 1) {

            buttonContainerBox.getChildren().remove(1,buttonContainerBox.getChildren().size());

        }

    }

    public void setGame(LotteryGame game) {
        this.game = game;
        loadChart(numbers[0],20, 1);
    }
}
