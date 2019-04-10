package edu.umuc.controllers;

import edu.umuc.SportsRankingApp;
import edu.umuc.models.GeneralProperties;
import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import edu.umuc.models.Sport;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Controller {
    //Resources
    public static final String SCHOOL_RANKING_FXML = "/SchoolRanking.fxml";
    public static final String LEAGUES_FXML = "/Leagues.fxml";
    public static final String HOME_PAGE_FXML = "/HomePage.fxml";
    public static final String RANK_CALC_PAGE_FXML = "/RankCalculation.fxml";
    private static final String MANAGE_WEIGHTS_FXML = "/ManageWeights.fxml";
    private static final String CONFIG_PROPERTIES = "/config.properties";

    private static GeneralProperties generalProperties = null;
    private final static Float DEFAULT_LEAGUE_WEIGHT = 1F;
    private final static String DEFAULT_LEAGUE_NAME = "NONE";

    //Config files
    private static final String GENERAL_PROPERTIES_YAML = "generalProperties.yaml";
    private final static String LEAGUES_YAML = "leagues.yaml";
    private final static String SCHOOLS_YAML = "schools.yaml";
    private final static String SPORTS_YAML = "sports.yaml";

    //TODO revisit if these need to be static
    private static List<League> leagues = new ArrayList<>();
    private static List<School> schools = new ArrayList<>();
    private static List<Sport> sports = new ArrayList<>();
    private static RankWeight rankWeight;
    private static School selectedSchool;
    private static League selectedLeague;
    private static boolean schoolsRanked = false;

    public static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat( "0.00" );
    
    protected String configPath;
        
    public Controller() {
        try {
            final Properties properties = new Properties();
//            InputStream is = new FileInputStream(new File("C:\\Users\\jhender1\\Documents\\NetBeansProjects\\SportsRanking\\src\\main\\resources" + CONFIG_PROPERTIES));
//            properties.load(is);
            properties.load(this.getClass().getResourceAsStream(CONFIG_PROPERTIES));
            configPath = properties.getProperty("configpath");
            if (configPath == null) {
                System.out.println("No config path found in the properties file.");
                System.exit(-1);
            }
        } catch (IOException e) {
            System.out.println("Unable to read read the config.properties file");
            System.exit(-1);
        }

        if (generalProperties == null) {
            loadGeneralPropertiesData();
            if (generalProperties == null) {
                System.out.println("General Properties not loaded");
                System.exit(-1);
            }
        }
    
        if (leagues.isEmpty()) {
            loadLeaguesData();
            if (leagues.isEmpty()) {
                System.out.println("Leagues not loaded");
                System.exit(-1);
            }
        }
        
        if (schools.isEmpty()) {
            loadSchoolsData();
            if (schools.isEmpty()) {
                System.out.println("Schools not loaded");
                System.exit(-1);
            }
        }
        
        if (sports.isEmpty()) {
            loadSportsData();
            if (sports.isEmpty()) {
                System.out.println("Sports not loaded");
                System.exit(-1);
            }
        }
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
        RankWeight returnRankWeight = new RankWeight();
        // Loads the saved weights from the corresponding YAML file
        final Yaml yaml = new Yaml(new Constructor(RankWeight.class));
        final File file = new File(configPath, yamlName);
        try {
            returnRankWeight = yaml.load(new FileInputStream(file));
            rankWeight = returnRankWeight;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return returnRankWeight;
    }
    
    private void loadGeneralPropertiesData() {
        final File generalPropertiesFile = new File(configPath, GENERAL_PROPERTIES_YAML);
        try (InputStream inputStream = new FileInputStream(generalPropertiesFile)) {
            Yaml yaml = new Yaml();
            generalProperties = yaml.loadAs(inputStream, GeneralProperties.class);
        } catch (Exception ex) {
            System.out.println("Error loading general properties data YAML file. Exception: " + ex.getMessage());
        }
    }
        
    public static GeneralProperties getGeneralProperties() {
        return generalProperties;
    }

    private void loadLeaguesData(){
        File file = new File(configPath, LEAGUES_YAML);
        try (InputStream inputStream = new FileInputStream(file)) {
            final Yaml yaml = new Yaml();
            Iterable<Object> iterableLeagues = yaml.loadAll(inputStream);
            
            iterableLeagues.forEach(iterableLeague -> {
                final Map<String, Object> map = ((HashMap<String, Object>) iterableLeague);
                final League league = new League((String)map.get("leagueId"), (String)map.get("name"), ((Double)map.get("weight")).floatValue());
                final List<HashMap<String, String>> schools = (List)(map.get("schools"));

                league.setSchools(schools.stream().map(school -> (school).get("name")).collect(Collectors.toList()));
                leagues.add(league);
            });

        } catch (Exception e) {
            System.out.println("Error loading Schools Yaml file");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private void loadSchoolsData(){
        File file = new File(configPath, SCHOOLS_YAML);
        try (InputStream inputStream = new FileInputStream(file)) {
            final Yaml yaml = new Yaml();
            Iterable<Object> iterableSchools = yaml.loadAll(inputStream);

            iterableSchools.forEach(iterableSchool -> {
                final Map<String, String> map = ((HashMap<String, String>) iterableSchool);
                final School school = new School(map.get("name"), map.get("path"));
                schools.add(school);
            });

        } catch (Exception e) {
            System.out.println("Error loading Schools Yaml file");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    private void loadSportsData() {
        File file = new File(configPath, SPORTS_YAML);
        try (InputStream inputStream = new FileInputStream(file)) {
            final Yaml yaml = new Yaml();
            Iterable<Object> itrSports = yaml.loadAll(inputStream);
        
            itrSports.forEach(itr -> {
                final Map<String, String> map = ((HashMap<String, String>) itr);
                final Sport sport = new Sport(map.get("name"), map.get("season"), map.get("path"));
                sports.add(sport);
            });
            Collections.sort(sports);
        } catch (Exception e) {
            System.out.println("Error loading Sports Yaml file");
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public static List<League> getLeagues() {
        return leagues;
    }

    public static List<School> getSchools() {
        return schools;
    }
    
    public static List<Sport> getSports() {
        return sports;
    }

    public static Float getLeagueWeightForSchool (String schoolName){
        return leagues.stream()
                .filter(league -> league.containsSchool(schoolName))
                .map(League::getWeight)
                .findAny()
                .orElse(DEFAULT_LEAGUE_WEIGHT);
    }

    public static String getLeagueNameForSchool (String schoolName){
        return leagues.stream()
                .filter(league -> league.containsSchool(schoolName))
                .map(League::getName)
                .findAny()
                .orElse(DEFAULT_LEAGUE_NAME);
    }

    public static RankWeight getRankWeight() {
        return rankWeight;
    }

    public static void setRankWeight(RankWeight rankWeight) {
        Controller.rankWeight = rankWeight;
    }

    public static League getLeagueForSchool (String schoolName){
        for (League singleLeague : leagues) {
            if (singleLeague.getSchools().contains(schoolName)) {
                return singleLeague;
            }
        }
        return null;
    }

    public static void initializeSchools() {
        for (School school : schools) {
            school.initialize();
        }
    }

    public static School getSelectedSchool() {
        return selectedSchool;
    }

    public static void setSelectedSchool(School selectedSchool) {
        Controller.selectedSchool = selectedSchool;
    }

    public static League getSelectedLeague() {
        return selectedLeague;
    }

    public static void setSelectedLeague(League selectedLeague) {
        Controller.selectedLeague = selectedLeague;
    }

    public static boolean isSchoolsRanked() {
        return schoolsRanked;
    }

    public static void setSchoolsRanked(boolean schoolsRanked) {
        Controller.schoolsRanked = schoolsRanked;
    }
}
