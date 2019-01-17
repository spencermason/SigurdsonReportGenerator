/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Spenc
 */
public class SectionAdapter {
    
    
    Connection connection;
    
    
     
    public SectionAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE Section");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                stmt.execute("CREATE TABLE Section ("
                        + "Type VARCHAR(50), "
                        + "Name VARCHAR(50), "
                        + "Position INT)");
            }
        }
    }
    
    public void AddSection(String Type, String Name, int Position) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Name = Name.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();

        
        stmt.executeUpdate("UPDATE Section SET Position = Position + 1 WHERE "
                + "Type = '" + Type + "' AND Position >= " + Position);
        
        stmt.executeUpdate("INSERT INTO Section (Type, Name, Position) VALUES ("
                + "'" + Type + "',"
                + "'" + Name + "',"
                + "" + Position + ")");
                        
        
    }
    
    public ObservableList<String> getSectionList(String Type) throws SQLException{
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Section ORDER BY Position");
        
        final ObservableList<String> data = FXCollections.observableArrayList();
        data.clear();
        
        while (rs.next()){
            if (rs.getString(1).equals(Type)){
                data.add(rs.getString(2));   
            }
            
        }
        return data;
    }
    
    public ObservableList<String> getAllSections() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Section ORDER BY Type, Position");
        
        final ObservableList<String> data = FXCollections.observableArrayList();
        data.clear();
        
        while (rs.next()){
            data.add(rs.getString("Type") + " -> " + rs.getString("Name"));
        }
        
        return data;
    }
    
    
    public int getPosition(String Type, String Section) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Section = Section.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Section WHERE Type = '" + Type + "' AND Name = '" + Section + "'");
        rs.next();
        return rs.getInt("Position");
    }

    
    public void RemoveSection(String Type, String Name, int Position) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Name= Name.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Section WHERE Type = '" + Type + "' AND Name = '" + Name + "'"); 
        stmt.executeUpdate("UPDATE Section SET Position = Position - 1 WHERE "
                + "Type = '" + Type + "' AND Position >= " + Position);
    }
    
    public void RemoveAll(String Type) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM Section WHERE Type = '" + Type + "'"); 
    }
    
    public void EditAllTypeName(String OldType, String NewType) throws SQLException{
        OldType = OldType.replaceAll("'", "''");
        NewType = NewType.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE Section SET Type = '" + NewType + "' WHERE Type = '" + OldType + "'");
    }
    
    public void EditSectionName(String OldName, String NewName) throws SQLException{
        OldName = OldName.replaceAll("'", "''");
        NewName = NewName.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE Section SET Name = '" + NewName + "' WHERE Name = '" + OldName + "'");
    }
    
    public void ViewTable() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM Section");
        
        while (rs.next()){
            System.out.println(rs.getString("Type") + " || " + rs.getString("Name") + " || " + rs.getString("Position"));
        }
    }
}
