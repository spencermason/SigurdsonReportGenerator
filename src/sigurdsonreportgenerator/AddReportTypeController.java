/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;


/**
 * FXML Controller class
 *
 * @author Spenc
 */
public class AddReportTypeController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    Button OkBtn;
    
    @FXML
    Button CancelBtn;
    
    @FXML
    TextField TypeField;
    
    private ReportTypeAdapter ReportType;
    
    @FXML
    public void Exit(){
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
    }
    
    public void Okay() throws SQLException{
        
       ReportType.AddType(TypeField.getText());
       displayAlert("Report type '" + TypeField.getText() + "' has been added");
       
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
       
    }
    
    public void setModel(ReportTypeAdapter reportType){
        ReportType = reportType;
    }
    
    private void displayAlert(String msg) {
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("Alert.fxml"));
            Parent ERROR = (Parent) loader.load();
            AlertController controller = (AlertController) loader.getController();

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);
            controller.setAlertText(msg);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException ex1) {

        }
    }
    
    public void setOnEnterEvents(){
            TypeField.setOnKeyPressed(
                    event -> 
                    {
                        if(event.getCode().toString().equals("ENTER")){
                            try{
                                Okay();
                            }
                            catch(Exception ex){}
                        }
                    }
            );
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setOnEnterEvents();
        // TODO
    }    
    
}
