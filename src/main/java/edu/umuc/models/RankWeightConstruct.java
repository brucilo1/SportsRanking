package edu.umuc.models;

public class RankWeightConstruct {
    private Float winLoss;
    private Float oppWins;
    private Float avgOppDifference;


    public Float getWinLoss() {
        return winLoss;
    }

    public void setWinLoss(Float winLoss) {
        this.winLoss = winLoss;
    }

    public Float getOppWins() {
        return oppWins;
    }

    public void setOppWins(Float oppWins) {
        this.oppWins = oppWins;
    }

    public Float getAvgOppDifference() {
        return avgOppDifference;
    }

    public void setAvgOppDifference(Float avgOppDifference) {
        this.avgOppDifference = avgOppDifference;
    }
}
