package com.lottoanalysis.presenters;

import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.models.pastresults.*;
import com.lottoanalysis.views.DrawHistoryView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.util.Arrays;
import java.util.Collection;

public class DrawHistoryPresenter {

    // This is form of aggregation
    private DrawHistoryAnalyzer drawHistoryAnalyzer;
    private DrawHistoryView drawHistoryView;

    public DrawHistoryPresenter(DrawHistoryAnalyzer drawHistoryAnalyzer, DrawHistoryView drawHistoryView) {
        this.drawHistoryAnalyzer = drawHistoryAnalyzer;
        this.drawHistoryView = drawHistoryView;

        drawHistoryAnalyzer.analyzeDrawData();
        drawHistoryView.setUpLottoInfoStackPanes();

        attachEvents();

    }

    private void attachEvents() {

        setUpMenuBarEvents();
        setUpRadioButtonEvents();
    }

    private void setUpRadioButtonEvents() {

        ToggleGroup toggleGroup = new ToggleGroup();
        drawHistoryAnalyzer.setDayOfWeekPopulationNeeded(false);

        HBox hBox = drawHistoryView.getDayOfWeekRadioButtons();

        for(Node node : hBox.getChildren())
        {
            RadioButton button = (RadioButton)node;
            button.setToggleGroup(toggleGroup);
        }

        toggleGroup.selectedToggleProperty().addListener(((observable, oldValue, newValue) -> {

            for(DayOfWeek dayOfWeek : DayOfWeek.values()){

                if(dayOfWeek.getDay().equals( newValue.getUserData().toString())){

                    drawHistoryAnalyzer.setDayOfWeek( dayOfWeek );

                    drawHistoryAnalyzer.analyzeDrawData();
                    drawHistoryView.setUpLottoInfoStackPanes();
                }
            }
        }));

    }

    private void setUpMenuBarEvents() {

        Collection<Menu> menus = drawHistoryView.getMenuBar().getMenus();
        for (Menu menu : menus) {
            switch (menu.getText()) {
                case "Draw Position":
                    placeActionsOnDrawPositionOptions(menu.getItems());
                    break;
                case "Game Span":
                    placeActionOnGameSpanOption(menu.getItems());
                    break;
                case "Analyze Methods":
                    placeActionsOnAnalyzeMethods(menu.getItems());
                    break;
            }
        }
    }

    private void placeActionsOnAnalyzeMethods(ObservableList<MenuItem> items) {

        for (MenuItem menuItem : items) {

            menuItem.setOnAction(event -> {

                AnalyzeMethod analyzeMethod = Arrays.stream(AnalyzeMethod.values()).filter(method -> method.getTitle().
                        equals(menuItem.getText())).
                        findAny().orElse(null);

                drawHistoryAnalyzer.setAnalyzeMethod(analyzeMethod);
                drawHistoryAnalyzer.analyzeDrawData();

                drawHistoryView.setUpLottoInfoStackPanes();
            });
        }

    }

    private void placeActionOnGameSpanOption(ObservableList<MenuItem> items) {

        for (MenuItem menuItem : items) {
            menuItem.setOnAction(event -> {

                MenuItem spanMenuItem = (MenuItem) event.getSource();

                drawHistoryAnalyzer.setGameSpan(Integer.parseInt(spanMenuItem.getText()));
                drawHistoryAnalyzer.analyzeDrawData();
                drawHistoryView.getHitAvgInSpanLabel().textProperty().bind(new SimpleStringProperty(
                        "The average winning numbers in a game span of " + drawHistoryAnalyzer.getGameSpan() +
                                " is " + TotalWinningNumberTracker.getAverage() + ""));
                drawHistoryView.setUpLottoInfoStackPanes();

            });
        }

        drawHistoryView.getHitAvgInSpanLabel().textProperty().bind(new SimpleStringProperty(
                "The average winning numbers in a game span of " + drawHistoryAnalyzer.getGameSpan() +
                        " is " + TotalWinningNumberTracker.getAverage() + ""));
    }

    private void placeActionsOnDrawPositionOptions(Collection<MenuItem> menuItems) {

        for (MenuItem menuItem : menuItems) {

            menuItem.setOnAction(e -> {

                MenuItem menuItem1 = (MenuItem) e.getSource();
                DrawPositions drawPosition = Arrays.stream(DrawPositions.values()).filter(value -> value.getText()
                        .equals(menuItem1.getText()))
                        .findAny().orElse(null);
                drawHistoryAnalyzer.setDrawPositions(drawPosition);
                drawHistoryAnalyzer.analyzeDrawData();

                drawHistoryView.getDrawPositionBeingAnalyzed().textProperty().bind(new SimpleStringProperty("Analyzing Position " +
                        (drawHistoryAnalyzer.getDrawPositions().getIndex() + 1)));

                drawHistoryView.setUpLottoInfoStackPanes();

            });
        }

        drawHistoryView.getDrawPositionBeingAnalyzed().textProperty().bind(new SimpleStringProperty("Analyzing Position " +
                (drawHistoryAnalyzer.getDrawPositions().getIndex() + 1)));
    }


    public DrawHistoryView presentViewForDisplay() {
        return drawHistoryView;
    }

}
