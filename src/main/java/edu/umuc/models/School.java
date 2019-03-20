package edu.umuc.models;

import java.util.Set;

public class School {
    private String schoolName;
    private String urlPath;
    private Float rankPoints;
    private Integer wins;
    private Integer losses;
    private Float avgPointDifference;
    private Set<String> opponents;
    private Integer opponentsTotalWins;
    private League league;

    public School(String schoolName, String urlPath, League league) {
        this.schoolName = schoolName;
        this.urlPath = urlPath;
        this.league = league;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public String getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(String urlPath) {
        this.urlPath = urlPath;
    }

    public Float getRankPoints() {
        return rankPoints;
    }

    public void setRankPoints(Float rankPoints) {
        this.rankPoints = rankPoints;
    }

    public Integer getWins() {
        return wins;
    }

    public void setWins(Integer wins) {
        this.wins = wins;
    }

    public Integer getLosses() {
        return losses;
    }

    public void setLosses(Integer losses) {
        this.losses = losses;
    }

    public Float getAvgPointDifference() {
        return avgPointDifference;
    }

    public void setAvgPointDifference(Float avgPointDifference) {
        this.avgPointDifference = avgPointDifference;
    }

    public Set<String> getOpponents() {
        return opponents;
    }

    public void setOpponents(Set<String> opponents) {
        this.opponents = opponents;
    }

    public Integer getOpponentsTotalWins() {
        return opponentsTotalWins;
    }

    public void setOpponentsTotalWins(Integer opponentsTotalWins) {
        this.opponentsTotalWins = opponentsTotalWins;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}
