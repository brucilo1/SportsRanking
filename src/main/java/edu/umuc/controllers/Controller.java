package edu.umuc.controllers;

import edu.umuc.SportsRankingApp;
import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
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
    private static final String SCHOOL_RANKING_URL = "src/main/java/edu/umuc/fxml/SchoolRanking.fxml";
    private static final String LEAGUES_URL = "src/main/java/edu/umuc/fxml/Leagues.fxml";
    private static final String HOME_PAGE_URL = "src/main/java/edu/umuc/fxml/HomePage.fxml";
    private static final String RANK_CALC_PAGE_URL = "src/main/java/edu/umuc/fxml/RankCalculation.fxml";

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
            loadPage(SCHOOL_RANKING_URL);
        } else if (event.getSource() == btnLeagues){
            loadPage(LEAGUES_URL);
        } else if (event.getSource() == btnHome){
            loadPage(HOME_PAGE_URL);
        } else if (event.getSource() == rankSchools){
            scrapeData(event);
        } else if (event.getSource() == rankCalc){
            loadPage(RANK_CALC_PAGE_URL);
        }
    }

    protected void loadPage(String fxmlUrl) {
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
        } catch (IOException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
