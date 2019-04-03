package edu.umuc.models;

public class Sport implements Comparable<Sport>{

    private String name;
    private String season;
    private String path;

    public Sport(String name, String season, String path) {
        this.name = name;
        this.season = season;
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(Sport other) {
        return name.compareTo(other.getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
