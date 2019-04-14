package edu.umuc.models;

import java.util.List;

public class League {
    private String leagueId;
    private String name;
    private float weight;
    private List<String> schools;

    public League() {
        
    }
    
    public League(String leagueId, String leagueName, float leagueWeight) {
        this.leagueId = leagueId;
        this.name = leagueName;
        this.weight = leagueWeight;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId) {
        this.leagueId = leagueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String leagueName) {
        this.name = leagueName;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float leagueWeight) {
        this.weight = leagueWeight;
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

    @Override
    public String toString() {
        return "League{" + "leagueId=" + leagueId + ", name=" + name + ", weight=" + weight + ", schools=" + schools + '}';
    }
    
    
}
