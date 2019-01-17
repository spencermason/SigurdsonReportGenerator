/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Spenc
 */
public class ReportTypeAdapter {
    
 Connection connection;
     
    public ReportTypeAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE ReportType");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                stmt.execute("CREATE TABLE ReportType ("
                        + "Name VARCHAR(50))");
            }
        }
    }
    
    public void AddType(String Type) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("INSERT INTO ReportType (Name) VALUES ("
                + "'" + Type + "')");
    }
    
    public ObservableList<String> getTypeList() throws SQLException{
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ReportType");
        
        final ObservableList<String> data = FXCollections.observableArrayList();
        data.clear();
        
        while (rs.next()){
            data.add(rs.getString("Name"));
        }
        return data;
    }
    
    
    public void RemoveType(String Type) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM ReportType WHERE Name = '" + Type + "'"); 
    }
    
    public void EditTypeName(String OldName, String NewName) throws SQLException{
        OldName = OldName.replaceAll("'", "''");
        NewName = NewName.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE ReportType Set Name = '" + NewName + "' WHERE Name = '" + OldName + "'"); 
    }
    
    
    
}
