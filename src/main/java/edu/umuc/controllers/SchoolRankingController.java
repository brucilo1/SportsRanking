package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import edu.umuc.models.Sport;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is the controller for the Sports Ranking page
 */
public class SchoolRankingController extends Controller implements Initializable {

    /**
     * Used to format floats into a standard 2 decimal value
     */
    private final static DecimalFormat decimalFormat = new DecimalFormat( "0.00" );
    
    /**
     * File name for the saved ranked weights
     */
    private final static String SAVED_RANK_WEIGHT_YAML ="savedRankWeight.yaml";

    @FXML
    public TableView<SchoolRankingController.FXSchoolRankingTable> tbSchoolRanking;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, Integer> rank;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, String> schoolName;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, Integer> wins;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, Integer> losses;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, String> league;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, Integer> oppWins;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, Float> avgPointDiff;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, Float> totalPoints;

    @FXML
    public TableColumn<SchoolRankingController.FXSchoolRankingTable, String> recordIncorrect;

    @FXML
    public ChoiceBox<String> yearChoice;

    @FXML
    public ChoiceBox<Sport> sportChoice;

    @FXML
    private Label lblWinLoss;
    
    @FXML
    private Label lblOppWins;

    @FXML
    private Label lblAvgPointDiff;

    /**
     * This is the handler for the Rank Calculation button. It saves the selected school and opens the Rank Calculation page
     * @param event 
     */
    @FXML
    private void rankCalc(ActionEvent event){
        if (!tbSchoolRanking.getItems().isEmpty() && tbSchoolRanking.getSelectionModel().getSelectedItem() != null ) {
            final School selected = tbSchoolRanking.getSelectionModel().getSelectedItem().getSchool();
            setSelectedSchool(selected);
            setSelectedLeague(getLeagueForSchool(selected.getSchoolName()));
        }
        loadPage(RANK_CALC_PAGE_FXML);
    }

    /**
     * Initializes the School Ranking page on load
     * @param location
     * @param resources 
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabels();
        initializeChoiceBoxes();
        if (isSchoolsRanked()) {
            populateTable(getSchools());
        }
    }

    /**
     * Loads the drop down boxes with values from the YAML files
     */
    private void initializeChoiceBoxes(){
        final List<String> yearList = new ArrayList<>();
        final int startingYear = getGeneralProperties().getStartingYear();

        for (int year = LocalDate.now().getYear(); year >= startingYear; year--) {
            yearList.add(String.valueOf(year));
        }
        sportChoice.setItems(FXCollections.observableArrayList(getSports()));
        Sport sportSelected = Controller.getSportSelected();
        if (sportSelected == null) {
            sportChoice.getSelectionModel().selectFirst();
        } else {
            int sportSelectedIndex = sportChoice.getItems().indexOf(sportSelected);
            sportChoice.getSelectionModel().select(sportSelectedIndex);
        }

        yearChoice.setItems((FXCollections.observableArrayList(yearList)));
        String yearSelected = Controller.getYearSelected();
        if (yearSelected == null) {
            yearChoice.getSelectionModel().selectFirst();
        } else {
            yearChoice.getSelectionModel().select(yearSelected);
        }
    }

    /**
     * Loads the values of the RankWeight object into the labels for the rank weights
     */
    private void initializeLabels() {
        final RankWeight rankWeight = loadRankWeight(SAVED_RANK_WEIGHT_YAML);

        lblWinLoss.setText(decimalFormat.format(rankWeight.getWinLoss()));
        lblOppWins.setText(decimalFormat.format(rankWeight.getOppWins()));
        lblAvgPointDiff.setText(decimalFormat.format(rankWeight.getAvgOppDifference()));
    }

    /**
     * Handles the Rank Schools button event and begins the scrape data process
     * @param event 
     */
    @FXML
    private void processRankSchoolsEvent(ActionEvent event) {
        /**
         * Clears all record data from the schools before running the scrape process
         */
        initializeSchools();

        setSportSelected(sportChoice.getValue());
        setYearSelected(yearChoice.getValue());

        final RankWeight rankWeight = new RankWeight(Float.parseFloat(lblWinLoss.getText()),
                Float.parseFloat(lblOppWins.getText()),
                Float.parseFloat(lblAvgPointDiff.getText()));

        final Sport sportSelected = getSports().stream()
                .filter(sportItem -> getSportSelected().getName().equals(sportItem.getName()))
                .findFirst()
                .orElse(null);

        if (sportSelected == null) {
            return;
        }

        try {
            
            /**
             * Present an alert to the user indicating the process may take some time.
             * This should be updated to a progress bar when the threading is refactored to allow for this.
             */
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Ranking Schools");
            alert.setContentText("Please wait, this process may take a few minutes...");
            alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
            alert.show();

            /** 
             * Scrapes data from Washington Post based on the UI selections
             */
            final ScrapeData scrapeData = new ScrapeData();
            final List<School> schools = scrapeData.scrapeData(getYearSelected(), sportSelected.getSeason(), sportSelected.getPath(), rankWeight);

            alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            alert.close();

            /**
             * Flag used to determine if schools have been ranked
             */
            setSchoolsRanked(true);

            /**
             * Populates the table on the School Ranking Page
             */
            populateTable(schools);

        } catch (InterruptedException | TimeoutException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "Exception thrown during data scraping.", ex);
        }
    }

    private void populateTable(List<School> schools) {
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        schoolName.setCellValueFactory(new PropertyValueFactory<>("schoolName"));
        wins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        losses.setCellValueFactory(new PropertyValueFactory<>("losses"));
        league.setCellValueFactory(new PropertyValueFactory<>("league"));
        oppWins.setCellValueFactory(new PropertyValueFactory<>("oppWins"));
        avgPointDiff.setCellValueFactory(new PropertyValueFactory<>("avgPointDiff"));
        totalPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));
        recordIncorrect.setCellValueFactory(new PropertyValueFactory<>("recordIncorrect"));

        /**
         * List that populates the UI Table
         */
        final ObservableList<SchoolRankingController.FXSchoolRankingTable> rankedSchools = FXCollections.observableArrayList();

        /**
         * Populating the Ranked School List
         */
        schools.stream()
                .filter(school -> !(school.getWins() == 0 && school.getLosses() == 0))
                .forEach(school -> rankedSchools.add(
                        new SchoolRankingController.FXSchoolRankingTable(rankedSchools.size() + 1, getLeagueNameForSchool(school.getSchoolName()), school)));

        tbSchoolRanking.setItems(rankedSchools);
    }

    /**
     * Remove all sport information from the schools before running the scrape process
     */
    public static void initializeSchools() {
        for (School school : getSchools()) {
            school.initialize();
        }
    }

    /**
     * In order to load information on the UI, we need to create a class with the JavaFX
     * bean properties ex: SimpleStringProperty and SimpleIntegerProperty.
     *
     * Created a local class FXSchoolRankingTable for this purpose
     */
    public class FXSchoolRankingTable {
        private final SimpleIntegerProperty rank;
        private final SimpleStringProperty schoolName;
        private final SimpleIntegerProperty wins;
        private final SimpleIntegerProperty losses;
        private final SimpleStringProperty league;
        private final SimpleIntegerProperty oppWins;
        private final SimpleStringProperty avgPointDiff;
        private final SimpleStringProperty totalPoints;
        private final SimpleStringProperty recordIncorrect;
        private final School school;
        
        private FXSchoolRankingTable(Integer rank, String league, School school) {

            this.rank = new SimpleIntegerProperty(rank);
            this.schoolName = new SimpleStringProperty(school.getSchoolName());
            this.wins = new SimpleIntegerProperty(school.getWins());
            this.losses = new SimpleIntegerProperty(school.getLosses());
            this.league = new SimpleStringProperty(league);
            this.oppWins = new SimpleIntegerProperty(school.getOpponentsTotalWins());
            this.avgPointDiff = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getAvgPointDifference()));
            this.totalPoints = new SimpleStringProperty(DECIMAL_FORMAT.format(school.getRankPoints()));
            this.recordIncorrect = new SimpleStringProperty(school.isWinLossRecordIncorrect() ? "YES" : "");
            this.school = school;
        }

        public int getRank() {
            return rank.get();
        }

        public SimpleIntegerProperty rankProperty() {
            return rank;
        }

        public String getSchoolName() {
            return schoolName.get();
        }

        public SimpleStringProperty schoolNameProperty() {
            return schoolName;
        }

        public int getWins() {
            return wins.get();
        }

        public SimpleIntegerProperty winsProperty() {
            return wins;
        }

        public int getLosses() {
            return losses.get();
        }

        public SimpleIntegerProperty lossesProperty() {
            return losses;
        }

        public String getLeague() {
            return league.get();
        }

        public SimpleStringProperty leagueProperty() {
            return league;
        }

        public int getOppWins() {
            return oppWins.get();
        }

        public SimpleIntegerProperty oppWinsProperty() {
            return oppWins;
        }

        public String getAvgPointDiff() {
            return avgPointDiff.get();
        }

        public SimpleStringProperty avgPointDiffProperty() {
            return avgPointDiff;
        }

        public String getTotalPoints() {
            return totalPoints.get();
        }

        public SimpleStringProperty totalPointsProperty() {
            return totalPoints;
        }
	         
        public String getRecordIncorrect() {
            return recordIncorrect.get();
        }

        public SimpleStringProperty recordIncorrectProperty() {
            return recordIncorrect;
        }
        
        public School getSchool() {
            return school;
        }        
    }
}