package edu.umuc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static edu.umuc.controllers.Controller.HOME_PAGE_FXML;

public class SportsRankingApp extends Application {
    //Static variables
    private static final int DEFAULT_SCENE_WIDTH = 900;
    private static final int DEFAULT_SCENE_HEIGHT = 700;

    //Declaring Primary Stage here for re-usability in Controllers
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

    public static void main(String[] args) {
        launch(args);
    }
}
