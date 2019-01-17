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
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import java.sql.Connection;
import java.time.LocalDate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.event.EventHandler;

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
   
   private static final String jdbcDriver = "com.microsoft.sqlserver.jdbc.SQLServerDriver"; //Think I need to use an older driver
   private static final String jdbcURL = "jdbc:sqlserver://localhost:1433;databasename=AdventureWorksDW2014;integratedSecurity=true;";
   
   @FXML
   public TextField UsernameField;
   @FXML
   public TextField PasswordField;
    
   @FXML
   public void tryOk() throws Exception{
       try{
           Ok();
       }
       catch (Exception ex){
           displayAlert("Database error, most likely required to enter password for first time set up");   
       }
   }
   
   
   public void createDatabase() throws SQLException{
       
        reportTypeAdapter = new ReportTypeAdapter(conn, true);
        sectionAdapter = new SectionAdapter(conn, true);
        subSectionAdapter = new SubSectionAdapter(conn, true);
        employeeAdapter = new EmployeeAdapter(conn, true);
        
        Employee admin = new Employee("Lara Sigurdson", "lsigurdson", "psychoed", "Administrator", LocalDate.now());
        employeeAdapter.AddEmployee(admin);
   }
   
    public void Ok() throws Exception{
        
        if (UsernameField.getText().equals("initialize") && PasswordField.getText().equals("password")){
            createDatabase();
            displayAlert("Database initialized please enter administrator username and password to continue");
        }
        else{
            reportTypeAdapter = new ReportTypeAdapter(conn, false);
            sectionAdapter = new SectionAdapter(conn, false);
            subSectionAdapter = new SubSectionAdapter(conn, false);
            employeeAdapter = new EmployeeAdapter(conn, false);
        }


        
        if (employeeAdapter.CheckCredentials(UsernameField.getText(), PasswordField.getText())){
            
            boolean admin;
            Employee emp = employeeAdapter.getEmployee(UsernameField.getText());
            
            if (emp.Type.equals("Administrator")){
                admin = true;
            }
            else{
                admin = false;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainForm.fxml"));
            Parent ERROR = (Parent) loader.load();
            MainFormController controller = (MainFormController) loader.getController();

            controller.setModel(employeeAdapter, reportTypeAdapter,sectionAdapter, subSectionAdapter, conn, admin);

            Scene scene = new Scene(ERROR);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Main Menu");
            stage.initModality(Modality.APPLICATION_MODAL);
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
        setOnEnterEvents();

        
                
//        
//        
//		// Create a variable for the connection string.
//		String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
//			"databaseName=TestDatabase;integratedSecurity=true;";
//
//		// Declare the JDBC objects.
//		Connection conn = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		
//        	try {
//        		// Establish the connection.
//        		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
//            		conn = DriverManager.getConnection(connectionUrl);
//        	}
//        
//		// Handle any errors that may have occurred.
//		catch (Exception e) {
//			e.printStackTrace();
//		}
//
//		finally {
//			if (rs != null) try { rs.close(); } catch(Exception e) {}
//	    		if (stmt != null) try { stmt.close(); } catch(Exception e) {}
//	    		if (conn != null) try { conn.close(); } catch(Exception e) {}
//                }

        try{
           // Class.forName(jdbcDriver).newInstance();
        }
        catch(Exception err){};
        
        
                try {
            // Create a named constant for the URL
            // NOTE: This value is specific for Java DB
            //String DB_URL = "Server=localhost\\SQLEXPRESS;Database=master;Trusted_Connection=True;";
            String DB_URL = "jdbc:derby:SigurdsonReportGenerator;create=true";
            // Create a connection to the database
            conn = DriverManager.getConnection(DB_URL);

        } catch (SQLException ex) {
            displayAlert(ex.getMessage());
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
