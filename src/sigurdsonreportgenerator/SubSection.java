/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

/**
 *
 * @author Spenc
 */
public class SubSection {
    
    String Type;
    String Section;
    String Name;
    int Position;
    String Text;
    
    public void SetSection(String section){
        Section = section;
    }
    
    public void SetName(String name){
        Name = name;
    }
    
    public void SetPosition(int position){
        Position = position;
    }
    
    public void SetText(String text){
        Text = text;
    }
    public void SetType (String type){
        Type = type;
    }
    
    
    
    public String GetSection(){
        return Section;
    }
    
    public String GetName(){
        return Name;
    }
    
    public int GetPosition(){
        return Position;
    }
    
    public String GetText(){
        return Text;
    }
    public String GetType(){
        return Type;
    }
}
