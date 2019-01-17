/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
public class AddEmployeeFormController implements Initializable {
    
    Employee employee = new Employee();
    private EmployeeAdapter employees;
    final ObservableList<String> data = FXCollections.observableArrayList();
    
    @FXML
    ComboBox TypeBox;
    
    @FXML
    Button CancelBtn;
    
    @FXML
    Button OkayBtn;
    
    @FXML
    TextField NameField;
    
    @FXML
    TextField UsernameField;
    
    @FXML
    TextField PasswordField;
    @FXML
    private Button OkayBtn1;
    @FXML
    private ComboBox<?> TypeBox1;
    
    /**
     * Initializes the controller class.
     */
    
    public void setModel(EmployeeAdapter emp){
        employees = emp;
        
        data.add("Administrator");
        data.add("Employee");
        
    }
    
    @FXML
    public void Exit(){
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void Okay() throws SQLException{
        
        if (NameField.getText().trim().equals("") || PasswordField.getText().trim().equals("") || UsernameField.getText().trim().equals("") || TypeBox.getValue() == null){
            displayAlert("Please fill out all of the fields");
        }
        else{
            employee.setName(NameField.getText());
            employee.setPassword(PasswordField.getText());
            employee.setUsername(UsernameField.getText());
            employee.setType(TypeBox.getValue().toString());

            employees.AddEmployee(employee);

            Stage stage = (Stage) CancelBtn.getScene().getWindow();
            stage.close();
        }
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
            NameField.setOnKeyPressed(
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
            UsernameField.setOnKeyPressed(
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
            PasswordField.setOnKeyPressed(
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
        // TODO
        setOnEnterEvents();
        TypeBox.setItems(data);
    }    
    
}
