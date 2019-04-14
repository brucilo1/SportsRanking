package edu.umuc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static edu.umuc.controllers.Controller.HOME_PAGE_FXML;

public class SportsRankingApp extends Application {
    /**
     * Static variables associated with the SCENE
     */
    private static final int DEFAULT_SCENE_WIDTH = 900;
    private static final int DEFAULT_SCENE_HEIGHT = 700;

    /**
     * The primary stage refers to the active window. In our design, we always want
     * to close the previous windows and open a new one. If this design were to
     * change, the developer would need to create a new stage.
     */
    private static Stage primaryStage;

    private void setPrimaryStage(Stage stage) {
        SportsRankingApp.primaryStage = stage;
    }

    static public Stage getPrimaryStage() {
        return SportsRankingApp.primaryStage;
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        setPrimaryStage(primaryStage);

        final FXMLLoader fxmlLoader = new FXMLLoader();
        final Parent homePage = fxmlLoader.load(getClass().getResourceAsStream(HOME_PAGE_FXML));
        primaryStage.setScene(new Scene(homePage, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT));
        primaryStage.show();
    }

    /**
     * Main class of the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}
