package edu.umuc.models;

import java.util.ArrayList;

public class School {
    private String schoolName;
    private String urlPath;
    private Float rankPoints = 0f;
    private Integer wins = 0;
    private Integer losses = 0;
    private Float avgPointDifference = 0f;
    private ArrayList<String> opponents = new ArrayList<>();
    private Integer opponentsTotalWins = 0;
    private boolean winLossRecordIncorrect = false;

    public School(String schoolName, String urlPath) {
        this.schoolName = schoolName;
        this.urlPath = urlPath;
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

    public void addOpponent(String opponent) {
            this.opponents.add(opponent);
    }

    public void addOpponentWins(int opponentsTotalWins) {
        this.opponentsTotalWins += opponentsTotalWins;
    }

    public float pointsForWins(Float leagueWeight) {
        return wins * leagueWeight;
    }

    public float pointsForLosses(Float leagueWeight) {
        return (losses * (1/leagueWeight)) * -1;
    }

    public float sumOfPoints(RankWeight rankWeight, Float leagueWeight) {
        return (pointsForWins(leagueWeight) + pointsForLosses(leagueWeight)) * rankWeight.getWinLoss();
    }

    public float pointsFromOpponentWins(RankWeight rankWeight) {
        return opponentsTotalWins * rankWeight.getOppWins();
    }

    public float pointsFromAveragePointDifferential(RankWeight rankWeight) {
        return (avgPointDifference * rankWeight.getAvgOppDifference());
    }

    public float calculateRankPoints(RankWeight rankWeight, Float leagueWeight) {
        rankPoints = sumOfPoints(rankWeight, leagueWeight) + pointsFromOpponentWins(rankWeight) + pointsFromAveragePointDifferential(rankWeight);
        return rankPoints;
    }

    public boolean isWinLossRecordIncorrect() {
        return winLossRecordIncorrect;
    }

    public void setWinLossRecordIncorrect(boolean winLossRecordIncorrect) {
        this.winLossRecordIncorrect = winLossRecordIncorrect;
    }
}
