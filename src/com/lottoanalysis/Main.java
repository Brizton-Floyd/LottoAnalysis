package com.lottoanalysis;

import com.lottoanalysis.screenavigator.LottoScreenNavigator;
import com.lottoanalysis.controllers.LottoAnalysisHomeController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        // set title for the stage
        primaryStage.setTitle("Lotto Analysis");

        primaryStage.setScene(
                createScene( loadMainPane()

                )
        );

        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     *Creates the main application scene
     */
    private Scene createScene(Pane mainPane){

        Scene scene = new Scene( mainPane );

        // if you have css feel free to add files here
//        scene.getStylesheets().setAll(
//                getClass().getResource("https://fonts.googleapis.com/css?family=Encode+Sans+Semi+Condensed").toExternalForm()
//        );
        return scene;
    }

    /**
     *Loads the main fxml layout sets up the screen navigator
     *Loads the firest lotto screen into the fxml layout
     *@return the loaded pane.
     *@throws IOException if the pane could not be loaded.
     */
    private Pane loadMainPane() throws IOException {

        FXMLLoader loader = new FXMLLoader();

        Pane mainPane = (Pane) loader.load(
                getClass().getResourceAsStream(
                        // Call the class needing to be loaded from the lottoscreen
                        // navigator class
                        LottoScreenNavigator.MAIN
                )
        );

        LottoAnalysisHomeController mainController = loader.getController();

        LottoScreenNavigator.setMainController( mainController );
        LottoScreenNavigator.loadLottoScreen( LottoScreenNavigator.LOTTO_SCREEN_ONE,"",null);

        return mainPane;
    }
    public static void main(String[] args) {
        launch(args);
    }

}
