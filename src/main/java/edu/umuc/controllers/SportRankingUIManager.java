package edu.umuc.controllers;

import edu.umuc.models.League;
import edu.umuc.models.RankWeight;
import edu.umuc.models.School;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SportRankingUIManager {
    private List<School> schools;
    private RankWeight rankWeight;

    private static SportRankingUIManager singletonInstance = null;

    private SportRankingUIManager() {
        loadXMLData();
    }

    public static SportRankingUIManager getSingletonInstance() {
        return singletonInstance == null ? new SportRankingUIManager() : singletonInstance;
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

    private void loadXMLData(){
       //TODO Replace all this with the actual call to read from the XML file.
       final List<School> mockedSchools = new ArrayList<>();
       final League mockedLeague = new League("1", "TestLeague", 0.91F);
       for (int i = 0; i < 20; i++){
           final School school = new School("SchoolName" + i, "schoolUrl" + i, mockedLeague );
           school.setWins(i);
           school.setLosses(i);
           school.setAvgPointDifference(i + 0.5F);
           school.setWinLossRecordIncorrect(false);
           school.setUrlPath("Url Path");
           school.setOpponents(new ArrayList<>(Arrays.asList("Opponent1", "Opponent2")));
           school.setOpponentsTotalWins(i);
           mockedSchools.add(new School("SchoolName" + i, "schoolUrl" + i, mockedLeague ));
       }
       setSchools(mockedSchools);
       setRankWeight(new RankWeight(1F, 1F, 0.2F));
    }
}
