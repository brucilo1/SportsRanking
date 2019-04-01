package edu.umuc.controllers;

import edu.umuc.models.RankWeight;
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
    private static String yamlLocation="defaultRankWeight.yaml";
    
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
        
        // TO DO: Replace hardcoded values with values read from file
        winLossWeight.setText(df.format(0.75));
        oppWinsWeight.setText(df.format(.1));
        avgPtsDiffWeight.setText(df.format(.15));    
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
        System.out.println(output);
        
        // Write saved weight info to file
        FileWriter writer = new FileWriter("savedRankWeight.yaml");
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
        
        //TO DO: load rank weight values from YAML file and reset weights to default
        
        //Code from James
	// Reads defaultRankedWeights.yaml file to the defaultRankWeight object
//	Yaml defaultRankWeightYaml = new Yaml(new Constructor(RankWeight.class));
//	File defaultRankWeightYamlFile = new File("defaultRankWeight.yaml");
//	FileInputStream defaultRankWeightYamlInputStream = new FileInputStream(defaultRankWeightYamlFile);
//	RankWeight defaultRankWeight = defaultRankWeightYaml.load(defaultRankWeightYamlInputStream);
//	System.out.println(defaultRankWeight.toString());

        // This will currently only load the strings of each value, minus the float values
        Yaml yaml = new Yaml(new Constructor(Collection.class));
        InputStream in = null;
        Collection<RankWeight> rankWeights;
        in = new FileInputStream(new File(yamlLocation));
        rankWeights = (Collection<RankWeight>) yaml.load(in);
        in.close();
        System.out.println(rankWeights);
        
                
    }
}
