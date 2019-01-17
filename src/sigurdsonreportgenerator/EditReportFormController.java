/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.fxml.FXML;
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
public class EditReportFormController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button DeleteBtn;
    @FXML
    Button CancelBtn;
    @FXML
    Button OkBtn;
    @FXML
    ComboBox TypeBox;
    @FXML
    ComboBox SectionBox;
    @FXML
    ComboBox SubSectionBox;
    @FXML
    TextField SectionField;
    @FXML
    ComboBox InsertAt;
    @FXML
    ComboBox InsertAt2;
    @FXML
    TextField SubSectionField;
    @FXML
    TextArea TextArea;
    @FXML
    CheckBox TypeCheckBox;
    @FXML
    CheckBox SectionCheckBox;
    @FXML
    CheckBox SubSectionCheckBox;
    @FXML
    ComboBox ExistingSectionBox;
    @FXML
    ComboBox ExistingSubSectionBox;
    @FXML
    TextField TypeField;
    
    private ReportTypeAdapter ReportType;
    private SectionAdapter sectionAdapter;
    private SubSectionAdapter subSectionAdapter;
    
    private ObservableList<String> TypeData = FXCollections.observableArrayList();
    private ObservableList<String> SectionData = FXCollections.observableArrayList();
    private ObservableList<String> SubSectionData = FXCollections.observableArrayList();
    private ObservableList<String> ExistingSectionData = FXCollections.observableArrayList();
    private ObservableList<String> ExistingSubSectionData = FXCollections.observableArrayList();
    private ObservableList<String> InsertAtData = FXCollections.observableArrayList();
    private ObservableList<String> InsertAt2Data = FXCollections.observableArrayList();
    
    public void setModel(ReportTypeAdapter rType, SectionAdapter sAdapter, SubSectionAdapter ssAdapter) throws SQLException{
        
        ReportType = rType;
        sectionAdapter = sAdapter;
        subSectionAdapter = ssAdapter;
        
        TypeData.addAll(ReportType.getTypeList());
    }
    
    @FXML
    public void Exit(){
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void TypeChange() throws SQLException {
        
        SectionBox.setValue(null);
        SubSectionBox.setValue(null);
        updateControls();
        
        if (TypeBox.isVisible() && TypeBox.getValue() != null){
            SectionData.clear();
            SectionData.addAll(sectionAdapter.getSectionList(TypeBox.getValue().toString()));
            SectionData.add("Add Existing Section");
            SectionData.add("Add New Section");
            SectionBox.setItems(SectionData); 

            InsertAtData.clear();
            for(int i = 0; i < SectionData.size(); i++){
                InsertAtData.add( (i+1) + ". " + SectionData.get(i));
            }
            InsertAtData.remove(InsertAtData.size()-1);
            InsertAtData.remove(InsertAtData.size()-1);
            InsertAtData.add((InsertAtData.size() + 1) + ". (last section)");
            InsertAt.setItems(InsertAtData);
            
            ExistingSectionData.clear();
            ExistingSectionData = sectionAdapter.getAllSections();
            ExistingSectionBox.setItems(ExistingSectionData);
        }

    }
    
    @FXML
    public void SectionChange() throws SQLException {
        SubSectionBox.setValue(null);
        updateControls();
        if (SubSectionBox.isVisible()){
            SubSectionData.clear();
            SubSectionData.addAll(subSectionAdapter.getSubSectionList(TypeBox.getValue().toString(), SectionBox.getValue().toString()));
            SubSectionData.add("Add Existing Sub-Section");
            SubSectionData.add("Add New Sub-Section");
            SubSectionBox.setItems(SubSectionData);

            InsertAt2Data.clear();
            for(int i = 0; i < SubSectionData.size(); i++){
                InsertAt2Data.add( (i+1) + ". " + SubSectionData.get(i));
            }
            InsertAt2Data.remove(InsertAt2Data.size()-1);
            InsertAt2Data.remove(InsertAt2Data.size()-1);
            InsertAt2Data.add((InsertAt2Data.size() + 1) + ". (last section)");
            InsertAt2.setItems(InsertAt2Data);
            
            ExistingSubSectionData.clear();
            ExistingSubSectionData = subSectionAdapter.getAllSubSections();
            ExistingSubSectionBox.setItems(ExistingSubSectionData);
        }
    }
    
    @FXML
    public void ExistingSubSectionChange() throws SQLException{
        updateControls();
        if (ExistingSubSectionBox.isVisible() && ExistingSubSectionBox.getValue() != null){
            String str = ExistingSubSectionBox.getValue().toString();
            String subStr[] = str.split(" -> ");
            SubSection SS = subSectionAdapter.getSubSection(subStr[0], subStr[1], subStr[2]);
            TextArea.setText(SS.GetText());
        }
    }
    
    @FXML
    public void SubSectionChange() throws SQLException{
        
        updateControls();
    }
    
    
    @FXML
    public void Okay() throws SQLException{
        try{
            
            if (TypeField.isVisible() && TypeField.getText().equals("")){
                throw new NullPointerException();
            }
            
            if (SectionField.isVisible() && SectionField.getText().equals("")){
                throw new NullPointerException();
            }
            if (SubSectionField.isVisible() && SubSectionField.getText().equals("")){
                throw new NullPointerException();
            }
            
            if (SectionBox.isVisible() && SectionBox.getValue().toString().equals("Add New Section")){
                if (InsertAt.isVisible()){
                    String strArr[] = InsertAt.getValue().toString().split("\\.");
                    int position = Integer.parseInt(strArr[0]);
                    
                    sectionAdapter.AddSection(TypeBox.getValue().toString(), SectionField.getText(), position);
                    displayAlert("Section '" + SectionField.getText() + "' has been added");
                }
                else{
                    sectionAdapter.AddSection(TypeBox.getValue().toString(), SectionField.getText(), 1);
                    displayAlert("Section '" + SectionField.getText() + "' has been added");
                }
            }
            if (SectionBox.isVisible() && SectionBox.getValue().toString().equals("Add Existing Section")){
                String str = ExistingSectionBox.getValue().toString();
                String subStr[] = str.split(" -> ");
                if (InsertAt.isVisible()){
                    String strArr[] = InsertAt.getValue().toString().split("\\.");
                    int position = Integer.parseInt(strArr[0]);
                    
                    sectionAdapter.AddSection(TypeBox.getValue().toString(), subStr[1], position);
                }
                else{
                    sectionAdapter.AddSection(TypeBox.getValue().toString(), subStr[1], 1);
                }
                subSectionAdapter.AddExistingSubSections(subStr[1], subStr[0], TypeBox.getValue().toString());
                displayAlert("Section '" + subStr[1] + "' has been added");
            }
            if (SubSectionBox.isVisible()){
                if (SubSectionBox.getValue().toString().equals("Add New Sub-Section")){
                    if (InsertAt2.isVisible()){
                        String strArr[] = InsertAt2.getValue().toString().split("\\.");
                        int position = Integer.parseInt(strArr[0]);
                        
                        subSectionAdapter.AddSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SubSectionField.getText(), position, TextArea.getText());
                        displayAlert("Sub-Section '" + SubSectionField.getText() + "' has been added");
                    }
                    else{
                        subSectionAdapter.AddSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SubSectionField.getText(), 1, TextArea.getText());
                        displayAlert("Sub-Section '" + SubSectionField.getText() + "' has been added");
                    }
                }
                else if (SubSectionBox.getValue().toString().equals("Add Existing Sub-Section") && ExistingSubSectionBox.getValue() != null){
                    String str = ExistingSubSectionBox.getValue().toString();
                    String subStr[] = str.split(" -> ");
                    SubSection SS = subSectionAdapter.getSubSection(subStr[0], subStr[1], subStr[2]);
                    if (InsertAt2.isVisible()){
                        String strArr[] = InsertAt2.getValue().toString().split("\\.");
                        int position = Integer.parseInt(strArr[0]);
                        
                        subSectionAdapter.AddSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SS.Name, position, TextArea.getText());
                    }
                    else{
                        subSectionAdapter.AddSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SS.Name, 1, TextArea.getText());
                    }
                    displayAlert("Sub-Section '" + SS.Name + "' has been added");
                }

                else{
                    SubSection SS = subSectionAdapter.getSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SubSectionBox.getValue().toString());
                    SubSection newSS = new SubSection();
                    newSS.Name = SS.Name;
                    newSS.Position = SS.Position;
                    newSS.Section = SS.Section;
                    newSS.Type = SS.Type;
                    newSS.Text = TextArea.getText();
                    subSectionAdapter.EditSubSection(SS, newSS);
                    displayAlert("Sub-Section '" + SS.Name + "' has been edited");
                }

            }
            //Edit names if CheckBoxes are visible and selected
            if (TypeCheckBox.isVisible() && TypeCheckBox.isSelected()){
                ReportType.EditTypeName(TypeBox.getValue().toString(), TypeField.getText());
                sectionAdapter.EditAllTypeName(TypeBox.getValue().toString(), TypeField.getText());
                subSectionAdapter.EditAllType(TypeBox.getValue().toString(), TypeField.getText());
                displayAlert("Report type '" + TypeBox.getValue().toString() + "' has been renamed to '" + TypeField.getText() + "'");
            }
            if (SectionCheckBox.isVisible() && SectionCheckBox.isSelected()){
                sectionAdapter.EditSectionName(SectionBox.getValue().toString(), SectionField.getText());
                subSectionAdapter.EditAllSection(SectionBox.getValue().toString(), SectionField.getText());
                displayAlert("Section '" + SectionBox.getValue().toString() + "' has been renamed to '" + SectionField.getText() + "'");
            }
            if (SubSectionCheckBox.isVisible() && SubSectionCheckBox.isSelected()){
                SubSection OGSS;
                SubSection SS = new SubSection();
                OGSS = subSectionAdapter.getSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SubSectionBox.getValue().toString());
                SS.Position = OGSS.Position;
                SS.Type = OGSS.Type;
                SS.Section = OGSS.Section;
                SS.Text = OGSS.Text;
                SS.Name = SubSectionField.getText();
                subSectionAdapter.EditSubSection(OGSS, SS);
            }

            TypeData.clear();
            TypeData.addAll(ReportType.getTypeList());
            TypeBox.setItems(TypeData);

            TypeBox.setValue(null);
            SectionBox.setValue(null);
            SubSectionBox.setValue(null);
        }catch(NullPointerException|SQLException ex1){
            displayAlert("Please fill in all of the fields");
        }
    }
    
    @FXML
    public void delete() throws SQLException{

        if (SubSectionBox.isVisible() && SubSectionBox.getValue() != null){
            SubSection SS = subSectionAdapter.getSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SubSectionBox.getValue().toString());
            subSectionAdapter.RemoveSubSection(SS.Type, SS.Section, SS.Name, SS.Position);
        }
        else if (SectionBox.isVisible() && SectionBox.getValue() != null){
            sectionAdapter.RemoveSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), sectionAdapter.getPosition(TypeBox.getValue().toString(), SectionBox.getValue().toString()));
            subSectionAdapter.RemoveAllInSection(TypeBox.getValue().toString(), SectionBox.getValue().toString());
        }
        else if (TypeBox.isVisible() && TypeBox.getValue() != null){
            ReportType.RemoveType(TypeBox.getValue().toString());
            sectionAdapter.RemoveAll(TypeBox.getValue().toString());
            subSectionAdapter.RemoveAllInType(TypeBox.getValue().toString());
        }
        
        TypeData.clear();
        TypeData.addAll(ReportType.getTypeList());
        TypeBox.setItems(TypeData);
        
        TypeBox.setValue(null);
        SectionBox.setValue(null);
        SubSectionBox.setValue(null);
    }
    
    public void updateControls() throws SQLException{
        
//        System.out.println("////////////////////////////////////////////////////////////////////////////////");
//        sectionAdapter.ViewTable();
//        subSectionAdapter.ViewTable();
        
        boolean typeVal = false;
        boolean sectionVal = false;
        boolean subSectionVal = false;
        
        if (TypeCheckBox.isVisible()){
            typeVal = TypeCheckBox.isSelected();
        }
        if (SectionCheckBox.isVisible()){
            sectionVal = SectionCheckBox.isSelected();
        }
        if (SubSectionCheckBox.isVisible()){
            subSectionVal = SubSectionCheckBox.isSelected();
        }
        
        hideControls();
        TypeCheckBox.setSelected(typeVal);
        SectionCheckBox.setSelected(sectionVal);
        SubSectionCheckBox.setSelected(subSectionVal);
        
        TypeBox.setVisible(true);
        
        
        if (TypeBox.isVisible() && TypeBox.getValue() != null){
            TypeCheckBox.setVisible(true);
            SectionCheckBox.setVisible(false);
            SubSectionCheckBox.setVisible(false);
            if (TypeCheckBox.isSelected()){
                TypeField.setVisible(true);
            }
            
            else{
                DeleteBtn.setVisible(true);
                SectionBox.setVisible(true);
            }
        }
        
        if (SectionBox.isVisible() && SectionBox.getValue() != null){
            if (SectionBox.getValue().toString().equals("Add New Section") || SectionBox.getValue().toString().equals("Add Existing Section")){
                TypeCheckBox.setVisible(false);
                DeleteBtn.setVisible(false);
                if (sectionAdapter.getSectionList(TypeBox.getValue().toString()).size() > 0){
                    InsertAt.setVisible(true);
                }
                if (SectionBox.getValue().toString().equals("Add Existing Section")){
                    ExistingSectionBox.setVisible(true);
                }
                if (SectionBox.getValue().toString().equals("Add New Section")){
                    SectionField.setVisible(true);
                }
            }
            else{
                SectionCheckBox.setVisible(true);
                TypeCheckBox.setVisible(false);
                SubSectionBox.setVisible(true);
                DeleteBtn.setVisible(true);
            }
            if (SectionCheckBox.isSelected()){
                SectionField.setVisible(true);
                DeleteBtn.setVisible(false);
                SubSectionBox.setVisible(false);
            }
        }
        
        if (SubSectionBox.isVisible() && SubSectionBox.getValue() != null){
            TextArea.setDisable(false);
            if (SubSectionBox.getValue().toString().equals("Add New Sub-Section") || SubSectionBox.getValue().toString().equals("Add Existing Sub-Section")){
                TypeCheckBox.setVisible(false);
                DeleteBtn.setVisible(false);
                SectionCheckBox.setVisible(false);
               if (subSectionAdapter.getSubSectionList(TypeBox.getValue().toString(), SectionBox.getValue().toString()).size() > 0){
                   InsertAt2.setVisible(true);
               }
               if (SubSectionBox.getValue().toString().equals("Add Existing Sub-Section")){
                   ExistingSubSectionBox.setVisible(true);
               }
               if (SubSectionBox.getValue().toString().equals("Add New Sub-Section")){
                   SubSectionField.setVisible(true);
               }
            }
            else{
                SubSection SS = subSectionAdapter.getSubSection(TypeBox.getValue().toString(), SectionBox.getValue().toString(), SubSectionBox.getValue().toString());
                TextArea.setText(SS.GetText());
                SubSectionCheckBox.setVisible(true);
                SectionCheckBox.setVisible(false);
                TypeCheckBox.setVisible(false);
                DeleteBtn.setVisible(true);
            }
            if (SubSectionCheckBox.isSelected()){
                SubSectionField.setVisible(true);
                DeleteBtn.setVisible(false);
            }
        }
    }
    

    
    public void hideControls(){
        DeleteBtn.setVisible(false);
        TypeBox.setVisible(false);
        SectionBox.setVisible(false);
        SubSectionBox.setVisible(false);
        SectionField.setVisible(false);
        InsertAt.setVisible(false);
        SubSectionField.setVisible(false);
        InsertAt2.setVisible(false);
        TextArea.setDisable(true);
        TypeCheckBox.setVisible(false);
        SectionCheckBox.setVisible(false);
        SubSectionCheckBox.setVisible(false);
        ExistingSectionBox.setVisible(false);
        ExistingSubSectionBox.setVisible(false);
        TypeField.setVisible(false);
        SubSectionField.setText("");
        SectionField.setText("");
        TypeField.setText("");
        TextArea.setText("");
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
            TypeField.setOnKeyPressed(
                    event -> 
                    {
                        if(event.getCode().toString().equals("ENTER")){
                            try{
                                Okay();
                            }
                            catch(Exception ex){}
                        }
                    }
            );
            SectionField.setOnKeyPressed(
                    event -> 
                    {
                        if(event.getCode().toString().equals("ENTER")){
                            try{
                                Okay();
                            }
                            catch(Exception ex){}
                        }
                    }
            );
            SubSectionField.setOnKeyPressed(
                    event -> 
                    {
                        if(event.getCode().toString().equals("ENTER")){
                            try{
                                Okay();
                            }
                            catch(Exception ex){}
                        }
                    }
            );
    }
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setOnEnterEvents();
        hideControls();
        TypeBox.setVisible(true);
        TypeBox.setItems(TypeData);
        
    }    
    
}
