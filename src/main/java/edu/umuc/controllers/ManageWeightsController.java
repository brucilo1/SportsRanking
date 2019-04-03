package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

/**
 *
 * @author arielwatkins
 */
public class ManageWeightsController extends Controller implements Initializable {
    
    private static final DecimalFormat df = new DecimalFormat( "0.00" );
    private static final String DEFAULT_RANK_WEIGHT_YAML ="/defaultRankWeight.yaml";
    private static final String SAVED_RANK_WEIGHT_YAML ="/savedRankWeight.yaml";
    
    // Text Fields
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

        winLossWeight.setText(df.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(df.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(df.format(rankWeight.getAvgOppDifference()));
    }

    // Save weights manually entered by user
    @FXML
    public void handleSaveWeights() {
        try{
            // Convert textfield input to float and store new values
            final Float winLoss = Float.parseFloat(winLossWeight.getText());
            final Float oppWins = Float.parseFloat(oppWinsWeight.getText());
            final Float avgOppDifference = Float.parseFloat(avgPtsDiffWeight.getText());
            final RankWeight savedRankWeight = new RankWeight(winLoss, oppWins, avgOppDifference);

            // Write saved weight info to file
            saveWeights(savedRankWeight);

            winLossWeight.setText(df.format(savedRankWeight.getWinLoss()));
            oppWinsWeight.setText(df.format(savedRankWeight.getOppWins()));
            avgPtsDiffWeight.setText(df.format(savedRankWeight.getAvgOppDifference()));

        } catch (NumberFormatException e){

            // Display alter window to user if invalid value is entered
            final Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Invalid Value Detected");
            alert.setContentText("Please only enter numeric values.");

            alert.showAndWait();
        }
    } 
   
    // Reset weight values on manage weights page to original default values
    @FXML
    private void handleResetWeights() {
        final RankWeight rankWeight = super.loadRankWeight(DEFAULT_RANK_WEIGHT_YAML);

        winLossWeight.setText(df.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(df.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(df.format(rankWeight.getAvgOppDifference()));

        // "Autosave" default values on reset
        saveWeights(rankWeight);
    }

    private void saveWeights(RankWeight rankWeight){
        final DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        final Yaml yaml = new Yaml(options);

        try {
            // TODO This won't work when running the JAR.
            // TODO We probably need to store the "savedRankedWeight.yaml" outside the project.
            final FileWriter writer = new FileWriter(URLDecoder.decode(getClass().getResource(SAVED_RANK_WEIGHT_YAML).getFile(), "UTF-8"));
            yaml.dump(rankWeight, writer);
        } catch (IOException e) {
           System.out.println("Error occurred while saving the rank weights" + e.getMessage());
        }
    }
}

