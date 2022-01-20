/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.lang.ClassNotFoundException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import javafx.scene.Node;

/**
 * FXML Controller class
 *
 * @author Spenc
 */

public class LoginController implements Initializable {
   private EmployeeAdapter employeeAdapter;
   private ReportTypeAdapter reportTypeAdapter;
   private SectionAdapter sectionAdapter;
   private SubSectionAdapter subSectionAdapter;
   private Employee employee;
   private Connection conn;
   
   
   @FXML
   public TextField UsernameField;
   @FXML
   public TextField PasswordField;
    
   @FXML
   public void tryOk() throws Exception{
       try{
           Ok();
       }
       catch (SQLException ex){
           displayAlert(ex.getMessage());
       }
       catch (IOException ex){
           displayAlert(ex.getMessage());
       }
   }
   
   
   public void createDatabase() throws SQLException{
       
        reportTypeAdapter = new ReportTypeAdapter(conn, true);
        sectionAdapter = new SectionAdapter(conn, true);
        subSectionAdapter = new SubSectionAdapter(conn, true);
        employeeAdapter = new EmployeeAdapter(conn, true);
        
        Employee admin = new Employee("default", "user", "123", "Administrator", LocalDate.now());
        employeeAdapter.AddEmployee(admin);
   }
   
    public void Ok() throws SQLException, IOException{
        
        
        
        reportTypeAdapter = new ReportTypeAdapter(conn, false);
        sectionAdapter = new SectionAdapter(conn, false);
        subSectionAdapter = new SubSectionAdapter(conn, false);
        employeeAdapter = new EmployeeAdapter(conn, false);


        
        if (employeeAdapter.CheckCredentials(UsernameField.getText(), PasswordField.getText())){
            
            Employee emp = employeeAdapter.getEmployee(UsernameField.getText());
            
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
            Parent mainMenu = (Parent) loader.load();
            MainFormController controller = (MainFormController) loader.getController();

            controller.setModel(employeeAdapter, reportTypeAdapter,sectionAdapter, subSectionAdapter, conn, emp.Type);

            Stage stage = (Stage) UsernameField.getScene().getWindow();
            stage.close();
            
            stage = new Stage();
            Scene scene = new Scene(mainMenu);
            stage.setScene(scene);
            stage.setTitle("Main Menu");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    try{

                        
                        Stage stage = (Stage) e.getSource();
                        stage.close();
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                        Parent login = (Parent) loader.load();
                        stage = new Stage();
                        Scene scene = new Scene(login);
                        stage.setScene(scene);
                        stage.setTitle("Login");
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.show();}
                    
                    catch(IOException ex){
                        displayAlert(ex.getMessage());
                    }
                }
            });
                    
            stage.showAndWait();
        }
        else{
            displayAlert("Username or password is incorrect");    
        }
    }
    
    public void Exit(){
        Stage stage = (Stage) UsernameField.getScene().getWindow();
        stage.close();
    }
    
    public void setOnEnterEvents(){
            UsernameField.setOnKeyPressed(
                    event -> 
                    {
                        if(event.getCode().toString().equals("ENTER")){
                            try{
                                tryOk();
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
                                tryOk();
                            }
                            catch(Exception ex){}
                        }
                    }
            );
    }
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        LocalDate expiry = LocalDate.parse("2022-02-01");
        if (LocalDate.now().isAfter(expiry)){
            displayAlert("Trial has expired");
            System.exit(0);
        }
        setOnEnterEvents();

        try{
        }
        catch(Exception err){};
        
        
                try {
            conn = DriverManager.getConnection("jdbc:derby://localhost:1527/local","username","password");
            

        } catch (SQLException ex) {
            displayAlert(ex.getMessage());
        }
                
        DatabaseMetaData dbmd;
       try {
           dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, "EMPLOYEES", null);
            if (!rs.next()){
                createDatabase();
                displayAlert("local database initialized, log in with username:user password:123");
        }
       } catch (SQLException ex) {
           Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
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
    
}
