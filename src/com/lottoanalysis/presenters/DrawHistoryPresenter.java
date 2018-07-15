package com.lottoanalysis.presenters;

import com.lottoanalysis.constants.LotteryGameConstants;
import com.lottoanalysis.models.pastresults.AnalyzeMethod;
import com.lottoanalysis.models.pastresults.DrawHistoryAnalyzer;
import com.lottoanalysis.models.pastresults.DrawPositions;
import com.lottoanalysis.models.pastresults.TotalWinningNumberTracker;
import com.lottoanalysis.views.DrawHistoryView;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.util.Arrays;
import java.util.Collection;

public class DrawHistoryPresenter {

    // This is form of aggregation
    private DrawHistoryAnalyzer drawHistoryAnalyzer;
    private DrawHistoryView drawHistoryView;

    public DrawHistoryPresenter(DrawHistoryAnalyzer drawHistoryAnalyzer, DrawHistoryView drawHistoryView) {
        this.drawHistoryAnalyzer = drawHistoryAnalyzer;
        this.drawHistoryView = drawHistoryView;
        attachEvents();

        drawHistoryAnalyzer.analyzeDrawData();
        drawHistoryView.setUpLottoInfoStackPanes();
    }

    private void attachEvents() {

        setUpMenuBarEvents();
    }

    private void setUpMenuBarEvents() {

        Collection<Menu> menus = drawHistoryView.getMenuBar().getMenus();
        for(Menu menu : menus)
        {
            switch (menu.getText())
            {
                case "Draw Position":
                    placeActionsOnDrawPositionOptions( menu.getItems() );
                    break;
                case "Game Span":
                    placeActionOnGameSpanOption( menu.getItems() );
                    break;
                case "Analyze Methods":
                    placeActionsOnAnalyzeMethods( menu.getItems() );
                    break;
            }
        }
    }

    private void placeActionsOnAnalyzeMethods(ObservableList<MenuItem> items) {

        for(MenuItem menuItem : items) {

            menuItem.setOnAction( event -> {

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

        for(MenuItem menuItem: items)
        {
            menuItem.setOnAction( event -> {

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

        for(MenuItem menuItem : menuItems) {

            menuItem.setOnAction( e -> {

                MenuItem menuItem1 = (MenuItem) e.getSource();
                DrawPositions drawPosition = Arrays.stream(DrawPositions.values()).filter( value -> value.getText()
                                                                                .equals(menuItem1.getText()))
                                                                                .findAny().orElse(null);
                drawHistoryAnalyzer.setDrawPositions(drawPosition);
                drawHistoryAnalyzer.analyzeDrawData();

                drawHistoryView.getDrawPositionBeingAnalyzed().textProperty().bind(new SimpleStringProperty("Analyzing Position " +
                (drawHistoryAnalyzer.getDrawPositions().getIndex()+1)));

                drawHistoryView.setUpLottoInfoStackPanes();

            });
        }

        drawHistoryView.getDrawPositionBeingAnalyzed().textProperty().bind(new SimpleStringProperty("Analyzing Position " +
                (drawHistoryAnalyzer.getDrawPositions().getIndex()+1)));
    }


    public DrawHistoryView presentViewForDisplay(){ return drawHistoryView; }

}
