package edu.umuc.models;

public class League {
    private String leagueName;
    private Float leagueWeight;

    public League(String leagueName, Float leagueWeight) {
        this.leagueName = leagueName;
        this.leagueWeight = leagueWeight;
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

}
