package com.lottoanalysis.ui.companionnumberview;

import com.lottoanalysis.common.LotteryGameConstants;
import com.lottoanalysis.models.drawhistory.AnalyzeMethod;
import com.lottoanalysis.models.drawhistory.DayOfWeek;
import com.lottoanalysis.models.drawhistory.DrawPosition;
import com.lottoanalysis.ui.homeview.base.BaseView;
import com.lottoanalysis.ui.presenters.CompanionNumberPresenter;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CompanionNumberView extends BaseView<CompanionNumberPresenter> {

    private Label positionLabel, analysisLabel, dayOfWeekLabel, gameSpanLabel;

    public CompanionNumberView(){
        super.setPrefWidth(1270);
        super.setPrefHeight(770);
        super.setStyle("-fx-background-color:#515B51;");
        getStylesheets().add("./src/com/lottoanalysis/styles/table_view.css");

        positionLabel = new Label();
        positionLabel.setStyle("-fx-text-fill: black;");
        analysisLabel = new Label();
        analysisLabel.setStyle("-fx-text-fill: black;");
        dayOfWeekLabel= new Label();
        dayOfWeekLabel.setStyle("-fx-text-fill: black;");
        gameSpanLabel = new Label();
        gameSpanLabel.setStyle("-fx-text-fill: black;");
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

    public Label getAnalysisLabel() {
        return analysisLabel;
    }

    public Label getDayOfWeekLabel() {
        return dayOfWeekLabel;
    }

    public Label getGameSpanLabel() {
        return gameSpanLabel;
    }

    private void buildScene(){
        buildSceneMainContainer();
        buildInfoDisplay();
        buildMainPositionGroupRangeDisplay();
        buildCompanionPositionGroupRangeDisplay();
        buildMainPositionInfoDisplay();
        buildCharPanes();
    }

    private void buildCharPanes() {
        HBox hBox = (HBox)lookup("#1");
        hBox.setSpacing(10);

        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setPadding(new Insets(20,0,0,0));
        HBox.setHgrow(vBox,Priority.ALWAYS);

        StackPane mainChartPane = new StackPane();
        mainChartPane.setId("7");
        mainChartPane.setStyle("-fx-background-color: black");
        mainChartPane.setPrefHeight(300);

        StackPane companionChartPane = new StackPane();
        companionChartPane.setStyle("-fx-background-color: black");
        companionChartPane.setId("8");
        companionChartPane.setPrefHeight(300);

        VBox.setVgrow(companionChartPane,Priority.ALWAYS);

        vBox.getChildren().setAll( mainChartPane, companionChartPane );
        hBox.getChildren().add(vBox);
    }

    private void buildMainPositionInfoDisplay() {
        VBox vBox = (VBox)lookup("#2");

        Label label = new Label("Main Position Information Display");
        label.setFont(Font.font(15));
        label.setTextFill(Color.valueOf("#dac6ac"));
        label.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(label);

        StackPane mainPositionPane = new StackPane();
        mainPositionPane.setId("6");
        mainPositionPane.setStyle("-fx-background-color: black");
        mainPositionPane.setPrefHeight(150);
        vBox.getChildren().add( mainPositionPane );
    }

    private void buildCompanionPositionGroupRangeDisplay() {
        VBox vBox = (VBox)lookup("#2");

        Label label = new Label("Companion Position Group Range Hit Table");
        label.setFont(Font.font(15));
        label.setTextFill(Color.valueOf("#dac6ac"));
        label.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(label);

        StackPane mainPositionPane = new StackPane();
        mainPositionPane.setId("5");
        mainPositionPane.setStyle("-fx-background-color: black");
        mainPositionPane.setPrefHeight(175);
        vBox.getChildren().add( mainPositionPane );
    }

    private void buildMainPositionGroupRangeDisplay() {
        VBox vBox = (VBox)lookup("#2");

        Label label = new Label("Main Position Group Range Hit Table");
        label.setFont(Font.font(15));
        label.setTextFill(Color.valueOf("#dac6ac"));
        label.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(label);

        StackPane mainPositionPane = new StackPane();
        mainPositionPane.setId("4");
        mainPositionPane.setStyle("-fx-background-color: black");
        mainPositionPane.setPrefHeight(175);
        vBox.getChildren().add( mainPositionPane );
    }

    private void buildSceneMainContainer() {

        HBox hBox = new HBox();
        hBox.setId("1");
       // hBox.setStyle("-fx-background-color: white");
        AnchorPane.setTopAnchor(hBox,65.0);
        AnchorPane.setLeftAnchor(hBox,5.0);
        AnchorPane.setBottomAnchor(hBox,5.0);
        AnchorPane.setRightAnchor(hBox,5.0);

        getChildren().add(hBox);
    }

    private void buildInfoDisplay() {
        VBox vBox = new VBox();
        vBox.setId("2");
        vBox.setPrefWidth(500);

        Label label = new Label("Information Display");
        label.setTextFill(Color.valueOf("#dac6ac"));
        label.setFont(Font.font(15));
        vBox.getChildren().add(label);

        StackPane infoDisplayStackPane = new StackPane();
        infoDisplayStackPane.setId("3");
        infoDisplayStackPane.setStyle("-fx-background-color: black");
        infoDisplayStackPane.setPrefHeight(100);

        vBox.getChildren().add(infoDisplayStackPane);

        HBox hBox = (HBox)lookup("#1");
        hBox.getChildren().add(vBox);
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
        final Menu analyzeMethodMenu = buildAnalyzeMethodMenu();
        final Menu dayOfWeekMenu = buildDayOfWeekMenu();
        final Menu gameSpanMenu = setUpGameHistoryMenu();

        final Menu analysisMenu = new Menu("", analysisLabel);
        analysisMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 100;");

        final Menu weekDayMenu = new Menu("", dayOfWeekLabel);
        weekDayMenu.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 80;");

        final Menu gameSpan = new Menu("", gameSpanLabel);
        gameSpan.setStyle("-fx-background-color: #0000; -fx-padding: 4 0 0 80;");

        menuBar.getMenus().addAll(drawPositionMenu, analyzeMethodMenu, gameSpanMenu, dayOfWeekMenu, gameSpan, analysisMenu, weekDayMenu);

        AnchorPane.setTopAnchor(menuBar, 0.0);
        AnchorPane.setLeftAnchor(menuBar, 0.0);
        AnchorPane.setRightAnchor(menuBar, 0.0);

        getChildren().add(menuBar);
    }

    private Menu buildPositionMenu() {

        Menu menu = new Menu("Draw Positions");
        final int gameRange = getPresenter().getNumberOfPositionsInGame();
        for (DrawPosition drawPosition : DrawPosition.values()) {

            if ((drawPosition.getIndex() + 1) <= gameRange-1) {

                MenuItem menuItem = new MenuItem(drawPosition.getText());
                menuItem.setOnAction(event -> {

                    DrawPosition position = Arrays.stream(DrawPosition.values())
                            .filter(val -> val.getText().equals(menuItem.getText())).findAny().orElse(null);
                    getPresenter().setDrawPosition( position );
                });
                menu.getItems().add(menuItem);
            }
        }

        return menu;
    }
    private Menu setUpGameHistoryMenu() {

        Menu historyMenu = new Menu("Games Out Span");
        for (int number : Arrays.asList(5, 7, 10, 12, 15, 20, 25, 30)) {
            MenuItem menuItem = new MenuItem(Integer.toString(number));
            menuItem.setOnAction(event -> getPresenter().setGameSpan(Integer.parseInt(menuItem.getText())));
            historyMenu.getItems().add(menuItem);
        }
        return historyMenu;
    }
    private Menu buildAnalyzeMethodMenu() {

        Menu menu = new Menu("Analysis Methods");
        final String gameName = getPresenter().getGameName();

        List<AnalyzeMethod> analyzeMethods = Arrays.stream(AnalyzeMethod.values()).collect(Collectors.toList());
        if (gameName.equals(LotteryGameConstants.PICK4_GAME_NAME_TWO) ||
                gameName.equals(LotteryGameConstants.PICK3_GAME_NAME_TWO)) {
            analyzeMethods.remove(AnalyzeMethod.DELTA_NUMBERS);
        }

        for (AnalyzeMethod analyzeMethod : analyzeMethods) {

            if(AnalyzeMethod.POSITIONAL_SUMS.equals(analyzeMethod) || AnalyzeMethod.DRAW_POSITION.equals(analyzeMethod) ||
                    AnalyzeMethod.DELTA_NUMBERS.equals(analyzeMethod)) {
                final MenuItem menuItem = new MenuItem(analyzeMethod.getTitle());
                menuItem.setOnAction(event -> getPresenter().setAnalysisMethod(analyzeMethod));
                menu.getItems().add(menuItem);
            }
        }

        return menu;
    }

    private Menu buildDayOfWeekMenu() {

        Set<String> days = getPresenter().returnDaysOfWeek();
        Menu menu = new Menu("Day Of Week");
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (days.contains(dayOfWeek.getDay()) || DayOfWeek.ALL.equals(dayOfWeek)) {
                MenuItem menuItem = new MenuItem(dayOfWeek.getFullDayName());
                menuItem.setOnAction(event -> getPresenter().setDayOfWeek(dayOfWeek));
                menu.getItems().add(menuItem);
            }
        }

        return menu;
    }
}
