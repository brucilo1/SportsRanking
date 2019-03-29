package edu.umuc.controllers;

import edu.umuc.SportsRankingApp;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.xml.sax.SAXException;

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
    
    // Text Fields
    @FXML
    private TextField winLossWeight;
    
    @FXML
    private TextField oppWinsWeight;
    
    @FXML
    private TextField avgPtsDiffWeight;


    @FXML
    private void processButtonClickEvents(ActionEvent event) {
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
    
    // Save weights manually entered by user
    @FXML
    public void handleSaveWeights(){ 
        
        // Convert textfield input to float and store new values
        try{
        String winLossValue = winLossWeight.getText();
        Float winLoss = Float.parseFloat(winLossValue);
        String oppWinsValue = oppWinsWeight.getText();
        Float oppWins = Float.parseFloat(oppWinsValue);
        String avgPtsDiffValue = avgPtsDiffWeight.getText();
        Float avgPtsDiff = Float.parseFloat(avgPtsDiffValue);
        
        RankWeight rankWeight = new RankWeight(winLoss, oppWins, avgPtsDiff);
        winLossWeight.setText(rankWeight.getWinLoss().toString());
        oppWinsWeight.setText(rankWeight.getOppWins().toString());
        avgPtsDiffWeight.setText(rankWeight.getAvgOppDifference().toString());
        } catch (NumberFormatException e){
            
            // Display alter window to user if invalid value is entered
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Invalid Value Detected");
            alert.setContentText("Please only enter numeric values.");

            alert.showAndWait();
            
            e.printStackTrace();
        }
    } 
   
    // Reset weight values on manage weights page to original default values
    @FXML
    private void handleResetWeights() {
        RankWeight rankWeight = new RankWeight();
        winLossWeight.setText(rankWeight.getWinLoss().toString());
        oppWinsWeight.setText(rankWeight.getOppWins().toString());
        avgPtsDiffWeight.setText(rankWeight.getAvgOppDifference().toString());
    }
}
