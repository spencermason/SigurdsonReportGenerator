/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 *
 * @author Spenc
 */
public class EmployeeAdapter {
    
    Connection connection;
    
    
     
    public EmployeeAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE Employees");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                stmt.execute("CREATE TABLE Employees ("
                        + "Username VARCHAR(20), "
                        + "Password VARCHAR(20), "
                        + "Type VARCHAR(13), "
                        + "Name VARCHAR(30), "
                        + "LastChange DATE "
                        + ")");
            }
        }
    }
    
    public void AddEmployee(Employee employee) throws SQLException{
        Statement stmt = connection.createStatement();
        employee.Name = employee.Name.replaceAll("'", "''");
        employee.Password = employee.Password.replaceAll("'", "''");
        employee.Type = employee.Type.replaceAll("'", "''");
        employee.Username = employee.Username.replaceAll("'", "''");
        
        
        stmt.executeUpdate("INSERT INTO Employees (Username, Password, Type, Name, LastChange) VALUES ("
                + "'" + employee.getUsername() + "',"
                + "'" + employee.getPassword() + "',"
                + "'" + employee.getType() + "',"
                + "'" + employee.getName() + "',"
                + "'" + Date.valueOf(LocalDate.now()) + "')");
                
    }
    
    public void DeleteEmployee(String Username) throws SQLException{
        
        Username = Username.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Employees WHERE Username = '" + Username + "'"); 
    }
       

    public void UpdateEmployee(Employee employee) throws SQLException{        
        employee.Name = employee.Name.replaceAll("'", "''");
        employee.Password = employee.Password.replaceAll("'", "''");
        employee.Type = employee.Type.replaceAll("'", "''");
        employee.Username = employee.Username.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE Employees SET "
                + "Password = '" + employee.getPassword() + "',"
                + "Type = '" + employee.getType() + "',"
                + "Name = '" + employee.getName() + "' WHERE Username = '" + employee.getUsername() + "'");
    }
    public ObservableList<String> getEmployeesList() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Employees");
        
        final ObservableList<String> data = FXCollections.observableArrayList();
        data.clear();
        
        while (rs.next()){
            data.add(rs.getString(1) + " - " + rs.getString(4));
        }
        return data;
    }
    
    
    public boolean CheckCredentials(String Username, String Password) throws SQLException{
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Employees");
        
        while (rs.next()){
            if (rs.getString(1).equals(Username) && rs.getString(2).equals(Password)){
                
                return true;
            }
        }
        
        return false;
    }
    
    public Employee getEmployee(String Username) throws SQLException{
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Employees");
        
        Employee employee = new Employee();
        
        while (rs.next()){
            if (rs.getString(1).equals(Username)){
                employee.setUsername(rs.getString(1));
                employee.setPassword(rs.getString(2));
                employee.setType(rs.getString(3));
                employee.setName(rs.getString(4));
            }
        }
        return employee;
    }
    
    public void PrintTable() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Employees");
        
        while (rs.next()){
            System.out.println(rs.getString(1) + "    " + rs.getString(2) + "    " + rs.getString(3) + "    " + rs.getString(4) + "    " + rs.getDate(5));
        }
        
    }
}
