package com.lottoanalysis.ui.gameselection;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;

import java.util.List;

public class GameSelectionViewImpl extends AnchorPane implements GameSelectionView {

    private GameSelectionViewListener gameSelectionViewListener;
    private MenuBar menuBar = new MenuBar();

    public GameSelectionViewImpl(){
        setPrefWidth(400);
        setPrefHeight(400);
        setStyle("-fx-background-color: #515b51; -fx-border-radius: 0.5em;");
        getStylesheets().add("./src/com/lottoanalysis/styles/menu_bar.css");
    }

    @Override
    public void initializeListener(GameSelectionViewListener gameSelectionViewListener) {
        this.gameSelectionViewListener = gameSelectionViewListener;
    }

    @Override
    public void initializeView() {

        buildMenuBar();
    }

    private void buildMenuBar() {
        gameSelectionViewListener.injectMenuItemValues();
        buildUpdaterMenu();
    }

    private void buildUpdaterMenu() {

        Menu menu = new Menu("Updater");
        MenuItem item = new MenuItem("Perform Game Updates");
        item.setOnAction( e -> {

        });

        menu.getItems().add(item);

        menuBar.getMenus().add( menu );
    }

    @Override
    public void setMenuBarItems(List<String> values) {

        Menu gameMenu = new Menu("Game Selection");

        for(String val : values){
            MenuItem item = new MenuItem(val);
            item.setOnAction(event -> {
                gameSelectionViewListener.notifyMainViewOfValueChange( item.getText() );
            });
            gameMenu.getItems().add(item);
        }

        menuBar.getMenus().add(gameMenu);

        AnchorPane.setTopAnchor(menuBar,0.0);
        AnchorPane.setRightAnchor(menuBar,0.0);
        AnchorPane.setLeftAnchor(menuBar,0.0);

        getChildren().add(menuBar);
    }

    @Override
    public AnchorPane view() {
        return this;
    }
}
