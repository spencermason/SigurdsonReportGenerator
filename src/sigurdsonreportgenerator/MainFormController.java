/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.Menu;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Spenc
 */
public class MainFormController implements Initializable {

    private ReportTypeAdapter reportType;
    private SectionAdapter sectionAdapter;
    private SubSectionAdapter subSectionAdapter;
    private EmployeeAdapter employee;
    private Connection conn;

    @FXML
    private MenuBar mainMenu;
    @FXML
    private Menu DatabaseMenu;
    @FXML
    private Menu EditReportMenu;
    @FXML
    private Menu EmployeeMenu;
    @FXML
    private Menu AddReportMenu;
    @FXML
    private ImageView Logo;
    
    public void setModel(EmployeeAdapter emp, ReportTypeAdapter rType, SectionAdapter sAdapter, SubSectionAdapter ssAdapter, Connection cn, boolean admin){
        reportType = rType;
        sectionAdapter = sAdapter;
        subSectionAdapter = ssAdapter;
        employee = emp;
        conn = cn;
        
        EmployeeMenu.setVisible(admin);
        AddReportMenu.setVisible(admin);
        EditReportMenu.setVisible(admin);
        DatabaseMenu.setVisible(admin);
    }
    
    @FXML
    public void Exit(){
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }
        
    @FXML
    public void reset() throws SQLException{
        
        reportType = new ReportTypeAdapter(conn, true);
        sectionAdapter = new SectionAdapter(conn, true);
        subSectionAdapter = new SubSectionAdapter(conn, true);
        employee = new EmployeeAdapter(conn, true);
        
        Employee admin = new Employee("Lara Sigurdson", "lsigurdson", "psychoed", "Administrator", LocalDate.now());
        employee.AddEmployee(admin);
        
        displayAlert("Databases Reset");
    }
    
    @FXML
    public void addEmployee() throws Exception{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddEmployeeForm.fxml"));
        Parent ERROR = (Parent) loader.load();
        AddEmployeeFormController controller = (AddEmployeeFormController) loader.getController();

        controller.setModel(employee);
        
        Scene scene = new Scene(ERROR);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Employee");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    @FXML
    public void editEmployee()throws Exception{
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditEmployeeForm.fxml"));
        Parent ERROR = (Parent) loader.load();
        EditEmployeeFormController controller = (EditEmployeeFormController) loader.getController();

        controller.setModel(employee);
        
        Scene scene = new Scene(ERROR);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit Employee");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    @FXML
    public void addReportType() throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AddReportType.fxml"));
        Parent ERROR = (Parent) loader.load();
        AddReportTypeController controller = (AddReportTypeController) loader.getController();

        controller.setModel(reportType);
        
        Scene scene = new Scene(ERROR);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Add Report Type");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();  
    }
    
    @FXML
    public void editReport() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EditReportForm.fxml"));
        Parent ERROR = (Parent) loader.load();
        EditReportFormController controller = (EditReportFormController) loader.getController();

        controller.setModel(reportType, sectionAdapter, subSectionAdapter);
        
        Scene scene = new Scene(ERROR);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Edit Report");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }
    
    @FXML
    public void generateReport() throws Exception {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("GenerateReport.fxml"));
        Parent ERROR = (Parent) loader.load();
        GenerateReportController controller = (GenerateReportController) loader.getController();

        controller.setModel(reportType, sectionAdapter, subSectionAdapter);
        
        Scene scene = new Scene(ERROR);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Generate Report");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
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
        
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    
    }
}
