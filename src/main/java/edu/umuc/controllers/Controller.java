package edu.umuc.controllers;

import edu.umuc.SportsRankingApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Controller {
    private static final String SCHOOL_RANKING_URL = "src/main/java/edu/umuc/fxml/SchoolRanking.fxml";
    private static final String LEAGUES_URL = "src/main/java/edu/umuc/fxml/Leagues.fxml";
    private static final String HOME_PAGE_URL = "src/main/java/edu/umuc/fxml/HomePage.fxml";

    @FXML
    private Button btnSchoolsRanking;

    @FXML
    private Button btnLeagues;

    @FXML
    private Button btnHome;

    @FXML
    private void processButtonClickEvents(ActionEvent event) {
        if (event.getSource() == btnSchoolsRanking) {
            loadPage(SCHOOL_RANKING_URL);
        } else if (event.getSource() == btnLeagues){
            loadPage(LEAGUES_URL);
        } else if (event.getSource() == btnHome){
            loadPage(HOME_PAGE_URL);
        }
    }

    private void loadPage(String fxmlUrl) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader();
            final Parent schoolRankingPage = fxmlLoader.load(new FileInputStream(new File(fxmlUrl)));
            final Stage stage = SportsRankingApp.getPrimaryStage();
            stage.setScene(new Scene(schoolRankingPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
