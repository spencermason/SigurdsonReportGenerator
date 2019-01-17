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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import java.sql.SQLException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Spenc
 */
public class EditEmployeeFormController implements Initializable {
    
    
    private EmployeeAdapter employees;
    private ObservableList<String> data = FXCollections.observableArrayList();
    private ObservableList<String> TypeData = FXCollections.observableArrayList();
    
    @FXML
    ComboBox EmployeeBox;
    
    @FXML
    ComboBox TypeBox;
    
    @FXML
    Button CancelBtn;
    
    @FXML
    Button OkayBtn;
    
    @FXML
    Button DeleteBtn;
    
    @FXML
    TextField NameField;
    
    @FXML
    TextField UsernameField;
    
    @FXML
    TextField PasswordField;
    
    /**
     * Initializes the controller class.
     */
    
    public void setModel(EmployeeAdapter emp) throws SQLException{
        
        employees = emp;
        
        data.addAll(employees.getEmployeesList());
        
        TypeData.add("Employee");
        TypeData.add("Administrator");
        
    }
    
    @FXML
    public void Exit(){
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void Delete() throws SQLException{
        
        String str = EmployeeBox.getValue().toString();
        int firstSpace = (str.indexOf(" ") >= 0) ? str.indexOf(" ") : str.length()-1;   //get username from combobox value
        String UserName = str.substring(0,firstSpace);
        employees.DeleteEmployee(UserName);
        
        displayAlert(UserName + " has been deleted from database");
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
        
    }
    
    @FXML
    public void EditEmployee() throws SQLException{
        
        Employee employee = new Employee();
        
        employee.setName(NameField.getText());
        employee.setPassword(PasswordField.getText());
        employee.setUsername(UsernameField.getText());
        employee.setType(TypeBox.getValue().toString());
        
        employees.UpdateEmployee(employee);
        
        
        displayAlert("Information updated for " + employee.getUsername());
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
    }
    
    
    @FXML
    public void Update() throws SQLException{
        
        Employee employee;
        
        String str = EmployeeBox.getValue().toString();
        int firstSpace = (str.indexOf(" ") >= 0) ? str.indexOf(" ") : str.length()-1;   //get username from combobox value
        String UserName = str.substring(0,firstSpace);
        
        employee = employees.getEmployee(UserName);
        
        if (employee.getType().equals(TypeData.get(0))){
             TypeBox.setValue(TypeData.get(0));
        }
        else{
            TypeBox.setValue(TypeData.get(1));
        }
        NameField.setText(employee.getName());
        UsernameField.setText(employee.getUsername());
        PasswordField.setText(employee.getPassword());
           
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
                                EditEmployee();
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
                                EditEmployee();
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
                                EditEmployee();
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
        EmployeeBox.setItems(data);
        TypeBox.setItems(TypeData);
    }    
    
}
