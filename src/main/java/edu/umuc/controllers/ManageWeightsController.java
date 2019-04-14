package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class ManageWeightsController extends Controller implements Initializable {
    private static final Logger LOG = LoggerFactory.getLogger(ManageWeightsController.class);
    private static final String DEFAULT_RANK_WEIGHT_YAML ="defaultRankWeight.yaml";
    private static final String SAVED_RANK_WEIGHT_YAML ="savedRankWeight.yaml";

    /**
     * Declaring text field that correspond with the FXML objects
     */
    @FXML
    private TextField winLossWeight;
    
    @FXML
    private TextField oppWinsWeight;
    
    @FXML
    private TextField avgPtsDiffWeight;

    public ManageWeightsController() { }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initializeTextFields();
    }

    private void initializeTextFields() {
        final RankWeight rankWeight = super.loadRankWeight(SAVED_RANK_WEIGHT_YAML);
        setRankWeight(rankWeight);
        winLossWeight.setText(DECIMAL_FORMAT.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(DECIMAL_FORMAT.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(DECIMAL_FORMAT.format(rankWeight.getAvgOppDifference()));
    }

    /**
     * Handles the save weights action
     */
    @FXML
    public void handleSaveWeights() {
        try{
            /**
             * Convert textfield input to float and store new values
             */
            final Float winLoss = Float.parseFloat(winLossWeight.getText());
            final Float oppWins = Float.parseFloat(oppWinsWeight.getText());
            final Float avgOppDifference = Float.parseFloat(avgPtsDiffWeight.getText());
            final RankWeight savedRankWeight = new RankWeight(winLoss, oppWins, avgOppDifference);
            setRankWeight(savedRankWeight);

            /**
             * Write saved weight info to file
             */
            saveWeights(savedRankWeight);

            winLossWeight.setText(DECIMAL_FORMAT.format(savedRankWeight.getWinLoss()));
            oppWinsWeight.setText(DECIMAL_FORMAT.format(savedRankWeight.getOppWins()));
            avgPtsDiffWeight.setText(DECIMAL_FORMAT.format(savedRankWeight.getAvgOppDifference()));

        } catch (NumberFormatException e){
            /**
             * Display alert window to the user if an invalid value is entered
             */
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Invalid Value Detected");
            alert.setContentText("Please only enter numeric values.");

            alert.showAndWait();
        }
    }

    /**
     * Handles the reset weights action
     */
    @FXML
    private void handleResetWeights() {
        final RankWeight rankWeight = super.loadRankWeight(DEFAULT_RANK_WEIGHT_YAML);
        setRankWeight(rankWeight);
        winLossWeight.setText(DECIMAL_FORMAT.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(DECIMAL_FORMAT.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(DECIMAL_FORMAT.format(rankWeight.getAvgOppDifference()));

        /**
         * "Autosave" default values on reset
         */
        saveWeights(rankWeight);
    }

    /**
     * Reusable method to save weights
     */
    private void saveWeights(RankWeight rankWeight){
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        final Yaml yaml = new Yaml(options);

        try {
            FileWriter writer = new FileWriter(configPath + SAVED_RANK_WEIGHT_YAML);
            yaml.dump(rankWeight, writer);
        } catch (IOException ex) {
           LOG.error("Error occurred while saving the rank weights", ex);
        }
    }
}

