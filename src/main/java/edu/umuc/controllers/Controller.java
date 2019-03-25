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
    private void processButtonClickEvents(ActionEvent event) {
        if (event.getSource() == btnSchoolsRanking) {
            loadPage(SCHOOL_RANKING_FXML);
        } else if (event.getSource() == btnLeagues){
            loadPage(LEAGUES_FXML);
        } else if (event.getSource() == btnHome){
            loadPage(HOME_PAGE_FXML);
        } else if (event.getSource() == rankSchools){
            scrapeData(event);
        } else if (event.getSource() == rankCalc){
            loadPage(RANK_CALC_PAGE_FXML);
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
    
    private void scrapeData(Event event) {
        try {
            final Dialog dialog = new Dialog();
            dialog.setTitle("Ranking Schools");
            dialog.setContentText("This process may take a few minutes, please wait...");
            dialog.show();
            ScrapeData scrapeData = new ScrapeData();
            ArrayList<School> schools = scrapeData.scrapeData("2018", "fall", "football", new RankWeight(0.75f, 0.1f, 0.15f));
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            dialog.close();
            Collections.sort(schools, new Comparator<School>() {
                public int compare(School school1, School school2) {
                    return (int) ((school2.getRankPoints() * 100) - (school1.getRankPoints() * 100));
                }
            });
            
            for (School school : schools) {
                if (school.getWins() != 0 && school.getLosses() != 0) {
                    System.out.println(school.getSchoolName() + ", League: " + school.getLeague().getLeagueName() + ", Rank Points: " + school.getRankPoints() + ", Record incorrect: " + school.isWinLossRecordIncorrect());
                }
            }
        } catch (IOException | ParserConfigurationException | SAXException | XPathExpressionException | InterruptedException | TimeoutException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "Exception thrown during data scraping.", ex);
        }
    }
}
