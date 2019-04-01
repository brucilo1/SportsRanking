package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
import edu.umuc.models.RankWeightConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;


/**
 *
 * @author arielwatkins
 */
public class ManageWeightsController extends Controller implements Initializable {
    
    private DecimalFormat df = new DecimalFormat( "0.00" );
    private static String defaultRankWeights ="defaultRankWeight.yaml";
    private static String savedRankWeights ="savedRankWeight.yaml";
    
    // Text Fields
    @FXML
    private TextField winLossWeight;
    
    @FXML
    private TextField oppWinsWeight;
    
    @FXML
    private TextField avgPtsDiffWeight;
    
        public ManageWeightsController() {
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // Loads the saved weights YAML and sets values to associated
        // fields on weights page init
        Yaml yaml = new Yaml(new Constructor(RankWeightConstruct.class));
        InputStream in = null;
        RankWeightConstruct rankWeight;
        try {
            in = new FileInputStream(new File(savedRankWeights));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManageWeightsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        rankWeight = (RankWeightConstruct) yaml.load(in);

        winLossWeight.setText(df.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(df.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(df.format(rankWeight.getAvgOppDifference())); 
    }
    
    // Save weights manually entered by user
    @FXML
    public void handleSaveWeights() throws FileNotFoundException, IOException{ 
        
        // Convert textfield input to float and store new values
        try{
        String winLossValue = winLossWeight.getText();
        Float winLoss = Float.parseFloat(winLossValue);
        String oppWinsValue = oppWinsWeight.getText();
        Float oppWins = Float.parseFloat(oppWinsValue);
        String avgPtsDiffValue = avgPtsDiffWeight.getText();
        Float avgOppDifference = Float.parseFloat(avgPtsDiffValue);
        RankWeight savedRankWeight = new RankWeight(winLoss, oppWins, avgOppDifference);
        
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(savedRankWeight);
        
        // Write saved weight info to file
        FileWriter writer = new FileWriter(savedRankWeights);
        yaml.dump(savedRankWeight, writer);
        
        winLossWeight.setText(df.format(savedRankWeight.getWinLoss()));
        oppWinsWeight.setText(df.format(savedRankWeight.getOppWins()));
        avgPtsDiffWeight.setText(df.format(savedRankWeight.getAvgOppDifference()));
        
        } catch (NumberFormatException e){
            
            // Display alter window to user if invalid value is entered
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Dialog");
            alert.setHeaderText("Invalid Value Detected");
            alert.setContentText("Please only enter numeric values.");

            alert.showAndWait();
        }
    } 
   
    // Reset weight values on manage weights page to original default values
    @FXML
    private void handleResetWeights() throws FileNotFoundException, IOException {
        
        // Loads the default weights YAML file info and sets text boxes
        // to data of each associated value
        Yaml yaml = new Yaml(new Constructor(RankWeightConstruct.class));
        RankWeightConstruct rankWeight;
        InputStream in = new FileInputStream(new File(defaultRankWeights));
        rankWeight = (RankWeightConstruct) yaml.load(in);
        in.close();

        winLossWeight.setText(df.format(rankWeight.getWinLoss()));
        oppWinsWeight.setText(df.format(rankWeight.getOppWins()));
        avgPtsDiffWeight.setText(df.format(rankWeight.getAvgOppDifference()));
        
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        
        // "Autosave" default values on reset
        FileWriter writer = new FileWriter(savedRankWeights);
        yaml.dump(rankWeight, writer);
                
    }
}

