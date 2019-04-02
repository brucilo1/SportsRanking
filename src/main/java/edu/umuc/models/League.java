package edu.umuc.models;

import java.util.List;

public class League {
    private String leagueId;
    private String leagueName;
    private Float leagueWeight;
    private List<String> schools;

    public League(String leagueId, String leagueName, Float leagueWeight) {
        this.leagueId = leagueId;
        this.leagueName = leagueName;
        this.leagueWeight = leagueWeight;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public Float getLeagueWeight() {
        return leagueWeight;
    }

    public void setLeagueWeight(Float leagueWeight) {
        this.leagueWeight = leagueWeight;
    }

    public List<String> getSchools() {
        return schools;
    }

    public void setSchools(List<String> schools) {
        this.schools = schools;
    }

    public boolean containsSchool(String schoolName) {
        return schools.stream().anyMatch(school -> school.equals(schoolName));
    }
}
