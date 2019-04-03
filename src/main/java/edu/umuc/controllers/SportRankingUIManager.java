package edu.umuc.controllers;

import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

public class SportRankingUIManager {

    private final static Float DEFAULT_LEAGUE_WEIGHT = 1F;
    private final static String DEFAULT_LEAGUE_NAME = "NONE";
    private final static String LEAGUES_YAML = "/leagues.yaml";
    private final static String SCHOOLS_YAML = "/schools.yaml";

    private List<School> schools;
    private List<League> leagues;
    //TODO we need to create a Sport class to load the corresponding YAML file
    //private List<Sport> sports;
    private RankWeight rankWeight;

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

        //TODO Load sports here
        //loadSportsData();
    }

    private void loadLeagueData(){
        final Yaml leagueYaml = new Yaml();
        final InputStream resourceAsStream = getClass().getResourceAsStream(LEAGUES_YAML);
        final Iterable<Object> iterableLeagues = leagueYaml.loadAll(resourceAsStream);
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
        final Yaml leagueYaml = new Yaml();
        final InputStream resourceAsStream = getClass().getResourceAsStream(SCHOOLS_YAML);
        final Iterable<Object> iterableSchools = leagueYaml.loadAll(resourceAsStream);
        final ArrayList<School> schools = new ArrayList<>();

        iterableSchools.forEach(iterableSchool -> {
            final Map<String, String> map = ((HashMap<String, String>) iterableSchool);
            final School school = new School(map.get("name"), map.get("path"));
            schools.add(school);
        });

        this.setSchools(schools);
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
}
