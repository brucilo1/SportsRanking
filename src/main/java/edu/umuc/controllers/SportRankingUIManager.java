package edu.umuc.controllers;

import edu.umuc.models.*;

public class SportRankingUIManager {

    private RankWeight rankWeight;

    private static SportRankingUIManager singletonInstance = null;

    private SportRankingUIManager() {
    }

    //Attempting to use the Singleton pattern; however, moving between pages makes the instance null
    public static SportRankingUIManager getSingletonInstance() {
        return singletonInstance == null ? new SportRankingUIManager() : singletonInstance;
    }

    public RankWeight getRankWeight() {
        return rankWeight;
    }

    public void setRankWeight(RankWeight rankWeight) {
        this.rankWeight = rankWeight;
    }
}
