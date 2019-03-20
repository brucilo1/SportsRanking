package edu.umuc.models;

public class League {
    private String leagueId;
    private String leagueName;
    private Float leagueWeight;

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

}
