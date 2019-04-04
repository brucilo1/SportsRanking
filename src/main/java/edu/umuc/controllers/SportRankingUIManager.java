package edu.umuc.controllers;

import edu.umuc.models.*;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SportRankingUIManager {

    private final static Float DEFAULT_LEAGUE_WEIGHT = 1F;
    private final static String DEFAULT_LEAGUE_NAME = "NONE";
    private final static String LEAGUES_YAML = "/leagues.yaml";
    private final static String SCHOOLS_YAML = "/schools.yaml";
    private final static String SPORTS_YAML = "/sports.yaml";
    private final static String GENERAL_PROPERTIES_YAML = "/general-properties.yaml";

    private List<School> schools;
    private List<League> leagues;
    private List<Sport> sports;
    private RankWeight rankWeight;
    private GeneralProperties generalProperties;

    private static SportRankingUIManager singletonInstance = null;

    private SportRankingUIManager() {
        loadYamlData();
    }

    //Attempting to use the Singleton pattern; however, moving between pages makes the instance null
    public static SportRankingUIManager getSingletonInstance() {
        return singletonInstance == null ? new SportRankingUIManager() : singletonInstance;
    }

    private void loadYamlData(){
        loadLeagueData();
        loadSchoolsData();
        loadSportsData();
        loadGeneralPropertiesData();
    }

    private void loadLeagueData(){
        final Iterable<Object> iterableLeagues = loadAllForYaml(LEAGUES_YAML);
        final ArrayList<League> leagues = new ArrayList<>();

        iterableLeagues.forEach(iterableLeague -> {
            final Map<String, Object> map = ((HashMap<String, Object>) iterableLeague);
            final League league = new League((String)map.get("leagueId"), (String)map.get("name"), ((Double)map.get("weight")).floatValue());
            final List<HashMap<String, String>> schools = (List)(map.get("schools"));

            league.setSchools(schools.stream().map(school -> (school).get("name")).collect(Collectors.toList()));
            leagues.add(league);
        });

        this.setLeagues(leagues);
    }

    private void loadSchoolsData(){
        final Iterable<Object> iterableSchools = loadAllForYaml(SCHOOLS_YAML);
        final ArrayList<School> schools = new ArrayList<>();

        iterableSchools.forEach(iterableSchool -> {
            final Map<String, String> map = ((HashMap<String, String>) iterableSchool);
            final School school = new School(map.get("name"), map.get("path"));
            schools.add(school);
        });

        this.setSchools(schools);
    }

    private void loadSportsData() {
        final Iterable<Object> itrSports = loadAllForYaml(SPORTS_YAML);
        final List <Sport> sports = new ArrayList<>();

        itrSports.forEach(itr -> {
            final Map<String, String> map = ((HashMap<String, String>) itr);
            final Sport sport = new Sport(map.get("name"), map.get("season"), map.get("path"));
            sports.add(sport);
        });
        Collections.sort(sports);
        this.setSports(sports);
    }

    private void loadGeneralPropertiesData() {
        try (InputStream inputStream = getClass().getResourceAsStream(GENERAL_PROPERTIES_YAML)) {
            Yaml yaml = new Yaml();
            generalProperties = yaml.loadAs(inputStream, GeneralProperties.class);
        } catch (Exception ex) {
            System.out.println("Error loading general properties data YAML file. Exception: " + ex.getMessage());
        }
    }

    private Iterable<Object> loadAllForYaml(String fileName) {
        final InputStream inputStream = getClass().getResourceAsStream(fileName);
        final Yaml yaml = new Yaml();
        return yaml.loadAll(inputStream);
    }

    public Float getLeagueWeightForSchool (String schoolName){
        return leagues.stream()
                .filter(league -> league.containsSchool(schoolName))
                .map(League::getLeagueWeight)
                .findAny()
                .orElse(DEFAULT_LEAGUE_WEIGHT);
    }

    public String getLeagueNameForSchool (String schoolName){
        return leagues.stream()
                .filter(league -> league.containsSchool(schoolName))
                .map(League::getLeagueName)
                .findAny()
                .orElse(DEFAULT_LEAGUE_NAME);
    }

    public List<School> getSchools() {
        return schools;
    }

    public void setSchools(List<School> schools) {
        this.schools = schools;
    }

    public RankWeight getRankWeight() {
        return rankWeight;
    }

    public void setRankWeight(RankWeight rankWeight) {
        this.rankWeight = rankWeight;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    public List<Sport> getSports() {
        return sports;
    }

    public void setSports(List<Sport> sports) {
        this.sports = sports;
    }

    public GeneralProperties getGeneralProperties() {
        return generalProperties;
    }

    public void setGeneralProperties(GeneralProperties generalProperties) {
        this.generalProperties = generalProperties;
    }
}
