package edu.umuc.models;

/**
 * Stores all of the information of a set of rank weights to be used in the calculation of the rank points for a school
 */
public class RankWeight {
    
    /**
     * The rank weight for a schools wins and losses
     */
    private Float winLoss;
 
    /**
     * The rank weight for the opponent wins
     */
    private Float oppWins;
    
    /**
     * The rank weight for average opponent point differences
     */
    private Float avgOppDifference;

    public RankWeight() {
    }

    public RankWeight(Float winLoss, Float oppWins, Float avgOppDifference) {
        this.winLoss = winLoss;
        this.oppWins = oppWins;
        this.avgOppDifference = avgOppDifference;
    }

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
