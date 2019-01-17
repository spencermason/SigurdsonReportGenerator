/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.time.LocalDate;
import java.util.Date;
/**
 *
 * @author Spenc
 */
public class Employee {
    
    String Type;
    String Username;
    String Password;
    LocalDate LastChange;
    String Name;
    
    public Employee (){
        Type = "";
        Username = "";
        Password = "";
        LastChange = null;
        Name = "";
    }
    
    public Employee (String name, String username, String password, String type, LocalDate lastChange){
        Name = name;
        Username = username;
        Password = password;
        Type = type;
        LastChange = lastChange;
    }
    
    
    void setType(String type){
        Type = type;
    }
    void setUsername(String username){
        Username = username;
    }
    void setPassword(String password){
        Password = password;
    }
    void setLastChange(LocalDate lastChange){
        LastChange = lastChange;
    }
    void setName(String name){
        Name = name;
    }
    
    String getType(){
        return Type;
    }
    String getUsername(){
        return Username;
    }
    String getPassword(){
        return Password;
    }
   LocalDate getLastChange(){
        return LastChange;
    }
    String getName(){
        return Name;
    }
}
 