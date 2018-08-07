package com.lottoanalysis.ui.gameselection;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class GameSelectionViewImpl extends AnchorPane implements GameSelectionView {

    private GameSelectionViewListener gameSelectionViewListener;

    private MenuBar menuBar = new MenuBar();
    private Label messageLabel = new Label("Updating....");
    private ProgressBar progressBar = new ProgressBar();

    public GameSelectionViewImpl(){
        setPrefWidth(400);
        setPrefHeight(400);
        setStyle("-fx-background-color: #515b51; -fx-border-radius: 0.5em;");
        getStylesheets().add("./src/com/lottoanalysis/styles/menu_bar.css");

        messageLabel.setVisible(false);
        progressBar.setVisible(false);
    }

    @Override
    public void initializeListener(GameSelectionViewListener gameSelectionViewListener) {
        this.gameSelectionViewListener = gameSelectionViewListener;
    }

    @Override
    public void initializeView() {

        buildMenuBar();
        buildProgressIndicators();
    }

    @Override
    public void bindToMessage(ReadOnlyStringProperty readOnlyStringProperty) {
        messageLabel.textProperty().bind( readOnlyStringProperty );
    }

    @Override
    public void bindToProgressAndMessage(ReadOnlyDoubleProperty progress, ReadOnlyStringProperty message) {

        messageLabel.setVisible(true);
        progressBar.setVisible(true);

        progressBar.progressProperty().bind(progress);
        messageLabel.textProperty().bind(message);
    }

    private void buildProgressIndicators() {

        VBox progressBox = new VBox();

        progressBar.setPrefWidth(400);

        progressBox.getChildren().addAll(messageLabel,progressBar);

        AnchorPane.setTopAnchor(progressBox, 30.0);
        AnchorPane.setLeftAnchor(progressBox, 5.0);
        AnchorPane.setRightAnchor(progressBox, 0.0);

        getChildren().add(progressBox);
    }

    private void buildMenuBar() {
        gameSelectionViewListener.injectMenuItemValues();
        buildUpdaterMenu();
    }

    private void buildUpdaterMenu() {

        Menu menu = new Menu("Updater");
        MenuItem item = new MenuItem("Perform Game Updates");
        item.setOnAction( e -> {
            gameSelectionViewListener.executeGameUpdates();
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
                gameSelectionViewListener.notifyMainViewOfValueChange( item.getText(), false );
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
