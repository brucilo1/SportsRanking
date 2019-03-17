package edu.umuc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;

public class SportsRankingApp extends Application {
    //Static variables
    private static final int DEFAULT_SCENE_WIDTH = 900;
    private static final int DEFAULT_SCENE_HEIGHT = 700;
    private static final String HOME_PAGE_URL = "src/main/java/edu/umuc/fxml/HomePage.fxml";

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
        final Parent homePage = fxmlLoader.load(new FileInputStream(new File(HOME_PAGE_URL)));
        primaryStage.setScene(new Scene(homePage, DEFAULT_SCENE_WIDTH, DEFAULT_SCENE_HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
