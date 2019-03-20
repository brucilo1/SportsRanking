package edu.umuc.models;

import java.util.ArrayList;

public class School {
    private String schoolName = "";
    private String urlPath = "";
    private Float rankPoints = 0f;
    private Integer wins = 0;
    private Integer losses = 0;
    private Float avgPointDifference = 0f;
    ArrayList<String> opponents = new ArrayList<>();
    private Integer opponentsTotalWins = 0;
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

    public ArrayList<String> getOpponents() {
        return opponents;
    }

    public void setOpponents(ArrayList<String> opponents) {
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

    public void addOpponent(String opponent) {
            this.opponents.add(opponent);
    }
    
    public void addOpponentWins(int opponentsTotalWins) {
            this.opponentsTotalWins += opponentsTotalWins;
    }

    public float pointsForWins() {
            return (float)(wins * league.getLeagueWeight());
    }	
    
    public float pointsForLosses() {
            return (float)((losses * (1/league.getLeagueWeight())) * -1);
    }
    
    public float sumOfPoints() {
            return pointsForWins() + pointsForLosses();
    }
    
    public float pointsFromOpponentWins(RankWeight rankWeight) {
            return opponentsTotalWins * rankWeight.getOppWins();
    }
    
    public float pointsFromAveragePointDifferential(RankWeight rankWeight) {
            return (avgPointDifference * rankWeight.getAvgOppDifference());
    }
    
    public float calculateRankPoints(RankWeight rankWeight) {
            rankPoints = (float)(sumOfPoints() + pointsFromOpponentWins(rankWeight) + pointsFromAveragePointDifferential(rankWeight));
            return rankPoints;
    }
}
