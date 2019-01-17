/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
/**
 * FXML Controller class
 *
 * @author Spenc
 */
public class AlertController implements Initializable {

    @FXML
    public Label Label;
    
    public void setAlertText(String text) {
        // set text from another class
        Label.setText(text);
    } 
        
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
