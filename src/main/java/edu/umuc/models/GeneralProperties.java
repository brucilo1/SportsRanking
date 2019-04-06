package edu.umuc.models;

/**
 * Model to hold a variety of general properties.
 */
public class GeneralProperties {

    private int startingYear;
    private String yamlDirectory;

    public int getStartingYear() {
        return startingYear;
    }

    public void setStartingYear(int startingYear) {
        this.startingYear = startingYear;
    }

    public String getYamlDirectory() {
        return yamlDirectory;
    }

    public void setYamlDirectory(String yamlDirectory) {
        this.yamlDirectory = yamlDirectory;
    }

    @Override
    public String toString() {
        return "GeneralProperties{" + "startingYear=" + startingYear + ", yamlDirectory=" + yamlDirectory + '}';
    }
    
}
