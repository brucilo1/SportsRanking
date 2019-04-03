package edu.umuc.controllers;

import edu.umuc.SportsRankingApp;
import edu.umuc.models.RankWeight;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.IOException;

public class Controller {
    public static final String SCHOOL_RANKING_FXML = "/SchoolRanking.fxml";
    public static final String LEAGUES_FXML = "/Leagues.fxml";
    public static final String HOME_PAGE_FXML = "/HomePage.fxml";
    public static final String RANK_CALC_PAGE_FXML = "/RankCalculation.fxml";
    private static final String MANAGE_WEIGHTS_FXML = "/ManageWeights.fxml";
    
    public Controller() {
    }

    @FXML
    private Button btnSchoolsRanking;

    @FXML
    private Button btnLeagues;

    @FXML
    private Button btnHome;

    @FXML
    private Button rankSchools;

    @FXML
    private Button rankCalc;
    
    @FXML
    private Button btnManageWeights;
    
    @FXML
    private Button btnResetWeights;
    
    @FXML
    private Button btnSaveWeights;


    @FXML
    private void processButtonClickEvents(ActionEvent event){
        if (event.getSource() == btnSchoolsRanking) {
            loadPage(SCHOOL_RANKING_FXML);
        } else if (event.getSource() == btnLeagues){
            loadPage(LEAGUES_FXML);
        } else if (event.getSource() == btnHome){
            loadPage(HOME_PAGE_FXML);
        } else if (event.getSource() == rankCalc){
            loadPage(RANK_CALC_PAGE_FXML);
        } else if (event.getSource() == btnManageWeights){
            loadPage(MANAGE_WEIGHTS_FXML);            
        }
    }

    protected void loadPage(String fxmlUrl) {
        try {
            final FXMLLoader fxmlLoader = new FXMLLoader();
            final Parent schoolRankingPage = fxmlLoader.load(getClass().getResourceAsStream(fxmlUrl));
            final Stage stage = SportsRankingApp.getPrimaryStage();
            stage.setScene(new Scene(schoolRankingPage));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Added method for re-usability purposes in child classes
    protected RankWeight loadRankWeight(String yamlName) {
        // Loads the saved weights from the corresponding YAML file
        final Yaml yaml = new Yaml(new Constructor(RankWeight.class));
        return yaml.load(getClass().getResourceAsStream(yamlName));
    }
}
