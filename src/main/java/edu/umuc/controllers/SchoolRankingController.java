package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import edu.umuc.models.Sport;
import javafx.beans.property.SimpleFloatProperty;
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

public class SchoolRankingController extends Controller implements Initializable {

    private final static DecimalFormat decimalFormat = new DecimalFormat( "0.00" );
    private final static String SAVED_RANK_WEIGHT_YAML ="savedRankWeight.yaml";
    
    @FXML
    private TableView<SchoolRankingController.FXSchoolRankingTable> tbSchoolRanking;

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
    public ChoiceBox<String> yearChoice;

    @FXML
    public ChoiceBox<Sport> sportChoice;

    @FXML
    private Label lblWinLoss;
    
    @FXML
    private Label lblOppWins;

    @FXML
    private Label lblAvgPointDiff;
    
    @FXML
    private void rankCalc(ActionEvent event){
        if (!tbSchoolRanking.getItems().isEmpty()) {
            if (tbSchoolRanking.getSelectionModel().getSelectedItem() != null) {
                School selected = tbSchoolRanking.getSelectionModel().getSelectedItem().getSchool();
                Controller.setSelectedSchool(selected);
                Controller.setSelectedLeague(getLeagueForSchool(selected.getSchoolName()));
            }
        }
        loadPage(RANK_CALC_PAGE_FXML);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeLabels();
        initializeChoiceBoxes();
    }

    private void initializeChoiceBoxes(){
        final List<String> yearList = new ArrayList<>();
        final int startingYear = Controller.getGeneralProperties().getStartingYear();

        for (int year = LocalDate.now().getYear(); year >= startingYear; year--) {
            yearList.add(String.valueOf(year));
        }
        yearChoice.setItems((FXCollections.observableArrayList(yearList)));
        yearChoice.getSelectionModel().selectFirst();

        sportChoice.setItems(FXCollections.observableArrayList(Controller.getSports()));
        sportChoice.getSelectionModel().selectFirst();
    }

    private void initializeLabels() {
        final RankWeight rankWeight = loadRankWeight(SAVED_RANK_WEIGHT_YAML);

        lblWinLoss.setText(decimalFormat.format(rankWeight.getWinLoss()));
        lblOppWins.setText(decimalFormat.format(rankWeight.getOppWins()));
        lblAvgPointDiff.setText(decimalFormat.format(rankWeight.getAvgOppDifference()));
    }

    @FXML
    private void processRankSchoolsEvent(ActionEvent event) {
        scrapeData();
    }

    private void scrapeData() {
        rank.setCellValueFactory(new PropertyValueFactory<>("rank"));
        schoolName.setCellValueFactory(new PropertyValueFactory<>("schoolName"));
        wins.setCellValueFactory(new PropertyValueFactory<>("wins"));
        losses.setCellValueFactory(new PropertyValueFactory<>("losses"));
        league.setCellValueFactory(new PropertyValueFactory<>("league"));
        oppWins.setCellValueFactory(new PropertyValueFactory<>("oppWins"));
        avgPointDiff.setCellValueFactory(new PropertyValueFactory<>("avgPointDiff"));
        totalPoints.setCellValueFactory(new PropertyValueFactory<>("totalPoints"));

        //List that populates the UI Table
        final ObservableList<SchoolRankingController.FXSchoolRankingTable> rankedSchools = FXCollections.observableArrayList();

        initializeSchools();
        final String yearSelectedString = yearChoice.getValue();
        final String sportSelectedString = sportChoice.getValue().toString();
        final RankWeight rankWeight = new RankWeight(Float.parseFloat(lblWinLoss.getText()),
                Float.parseFloat(lblOppWins.getText()),
                Float.parseFloat(lblAvgPointDiff.getText()));

        final Sport sportSelected = getSports().stream()
                .filter(sportItem -> sportSelectedString.equals(sportItem.getName()))
                .findFirst()
                .orElse(null);

        if (sportSelected == null) {
            return;
        }

        try {
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Ranking Schools");
            alert.setContentText("Please wait, this process may take a few minutes...");
            alert.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
            alert.show();

            //Scrapes data
            final ScrapeData scrapeData = new ScrapeData();
            final List<School> schools = scrapeData.scrapeData(yearSelectedString, sportSelected.getSeason(), sportSelected.getPath(), rankWeight);

            alert.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            alert.close();

            //Ranking and sorting
            schools.sort((school1, school2) -> (int) ((school2.getRankPoints() * 100) - (school1.getRankPoints() * 100)));

            //Output schools with wrong information
            schools.stream()
                    .filter(school -> !(school.getWins() == 0 && school.getLosses() == 0))
                    .map(school -> school.getSchoolName()
                            + ", League: " + Controller.getLeagueNameForSchool(school.getSchoolName())
                            + ", Rank Points: " + school.getRankPoints()
                            + ", Record incorrect: " + school.isWinLossRecordIncorrect())
                    .forEach(System.out::println);

            //Populating the Ranked School List
            schools.stream()
                    .filter(school -> !(school.getWins() == 0 && school.getLosses() == 0))
                    .forEach(school -> rankedSchools.add(new FXSchoolRankingTable(rankedSchools.size() + 1, Controller.getLeagueNameForSchool(school.getSchoolName()), school)));
            tbSchoolRanking.setItems(rankedSchools);

        } catch (InterruptedException | TimeoutException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, "Exception thrown during data scraping.", ex);
        }
    }

    public static void initializeSchools() {
        for (School school : getSchools()) {
            school.initialize();
        }
    }

    /**
     * In order to load information on the UI, we need to create a class with the JavaFX
     * bean properties ex: SimpleStringProperty and SimpleFloatProperty.
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
	         
        public School getSchool() {
            return school;
        }
    }
}