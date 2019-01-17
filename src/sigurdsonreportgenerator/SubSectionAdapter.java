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
public class SubSectionAdapter {
    
    Connection connection;
    
    
     
    public SubSectionAdapter(Connection conn, Boolean reset) throws SQLException {
        connection = conn;
        if (reset) {
            Statement stmt = connection.createStatement();
            try {
                // Remove tables if database tables have been created.
            // This will throw an exception if the tables do not exist
            stmt.execute("DROP TABLE SubSection");
                // then do finally
            } catch (SQLException ex) {
                // No need to report an error.
                // The table simply did not exist.
                // do finally to create it
            } finally {
                stmt.execute("CREATE TABLE SubSection ("
                        + "Type VARCHAR(50),"
                        + "Section VARCHAR(50), "
                        + "Name VARCHAR(50), "
                        + "Position INT, "
                        + "Text VARCHAR(8000))");
            }
        }
    }
    
    public void AddSubSection(String Type, String Section, String Name, int Position, String text) throws SQLException{
        
        Statement stmt = connection.createStatement();
        
        Section = Section.replaceAll("'", "''");
        text = text.replaceAll("'", "''");
        Name = Name.replaceAll("'", "''");
        Type = Type.replaceAll("'", "''");
        stmt.executeUpdate("UPDATE SubSection SET Position = Position + 1 WHERE Type = '" + Type + "' AND Section = '" + Section + "' AND Position >= " + Position);
        stmt.executeUpdate("INSERT INTO SubSection (Type, Section, Name, Position, Text) VALUES ("
                + "'" + Type + "',"
                + "'" + Section + "',"
                + "'" + Name + "',"
                + Position + ","
                + "'" + text + "')");
    }
    
    public void AddExistingSubSections(String OldSection, String OldType, String NewType) throws SQLException{
        String OldSection2 = OldSection;
        OldSection = OldSection.replaceAll("'", "''");
        OldType = OldType.replaceAll("'", "''");
        //Add copies of all subsections from oldsection to newsection
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SubSection WHERE Type = '" + OldType + "' AND Section = '" + OldSection + "'");
        while (rs.next()){
            AddSubSection(NewType, OldSection2, rs.getString("Name"), rs.getInt("Position"), rs.getString("Text")); 
        }
    }
    
    public ObservableList<String> getSubSectionList(String Type, String Section) throws SQLException{
        
        Type = Type.replaceAll("'", "''");
        Section = Section.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SubSection WHERE Type = '" + Type + "' AND Section = '" + Section + "' ORDER BY Position");
        
        final ObservableList<String> data = FXCollections.observableArrayList();
        data.clear();
        
        while (rs.next()){
            data.add(rs.getString("Name"));
        }
        return data;
    }
    
    public ObservableList<String> getAllSubSections() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SubSection ORDER BY Type, Section, Position"); 
        
        final ObservableList<String> data = FXCollections.observableArrayList();
        while (rs.next()){
            data.add(rs.getString("Type") + " -> " + rs.getString("Section") + " -> " + rs.getString("Name"));
        }
        return data;
    }
    
    public SubSection getSubSection(String Type, String Section, String Name) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Section = Section.replaceAll("'", "''");
        
        Name = Name.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SubSection WHERE Type = '" + Type + "' AND Section = '" + Section + "' AND Name = '" + Name + "' ORDER BY Position");
        rs.next();
        
        SubSection ss = new SubSection();
        ss.SetName(rs.getString("Name"));
        ss.SetPosition(rs.getInt("Position"));
        ss.SetSection(rs.getString("Section"));
        ss.SetText(rs.getString("Text"));
        ss.SetType(rs.getString("Type"));
        
        return ss;
    }
        
    public void RemoveSubSection(String Type, String Section, String Name, int Position) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Section = Section.replaceAll("'", "''");
        Name = Name.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM SubSection WHERE Type = '" + Type + "' AND Section = '" + Section + "' AND Name = '" + Name + "'"); 
        stmt.executeUpdate("UPDATE SubSection SET Position = Position - 1 WHERE "
                + "Type = '" + Type + "' AND Section = '" + Section + "' AND Position >= " + Position);
    }
    public void RemoveAllInSection(String Type, String Section) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Section = Section.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM SubSection WHERE Type = '" + Type + "' AND Section = '" + Section + "'"); 
    }
    
    public void RemoveAllInType(String Type) throws SQLException{
        Type = Type.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("DELETE FROM SubSection WHERE Type = '" + Type + "'"); 
    }
    
    public void EditSubSection(SubSection OGSS, SubSection SS) throws SQLException{
        OGSS.Name = OGSS.Name.replaceAll("'", "''");
        OGSS.Section = OGSS.Section.replaceAll("'", "''");
        OGSS.Type = OGSS.Type.replaceAll("'", "''");
        
        SS.Type = SS.Type.replaceAll("'", "''");
        SS.Section = SS.Section.replaceAll("'", "''");
        SS.Name = SS.Name.replaceAll("'", "''");
        SS.Text = SS.Text.replaceAll("'", "''");
        
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE SubSection SET "
                + "Type = '" + SS.GetType() + "',"
                + "Section = '" + SS.GetSection() + "',"
                + "Name = '" + SS.GetName() + "',"
                + "Position = " + SS.GetPosition() + ","
                + "Text = '" + SS.GetText() + "' WHERE Type = '" + OGSS.GetType() + "' AND Section = '" + OGSS.GetSection() + "' AND Name = '" + OGSS.GetName() + "'");
    }
    
    public void EditAllSection(String OldSection, String NewSection) throws SQLException{
        OldSection = OldSection.replaceAll("'", "''");
        NewSection = NewSection.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE SubSection SET Section = '" + NewSection + "' WHERE Section = '" + OldSection + "'");
    }
    
    public void EditAllType(String OldType, String NewType) throws SQLException{
        OldType = OldType.replaceAll("'", "''");
        NewType = NewType.replaceAll("'", "''");
        Statement stmt = connection.createStatement();
        stmt.executeUpdate("UPDATE SubSection SET Type = '" + NewType + "' WHERE Type = '" + OldType + "'");
    }
    
    public void ViewTable() throws SQLException{
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM SubSection");
        
        while (rs.next()){
            System.out.println(rs.getString("Type") + " || " + rs.getString("Section") + " || " + rs.getString("Name") + " || " + rs.getString("Position") + " || " + rs.getString("Text"));
        }
    }
}

