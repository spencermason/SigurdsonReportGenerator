/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sigurdsonreportgenerator;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import java.sql.SQLException;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TreeView;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.cell.CheckBoxTreeCell;
import javafx.fxml.FXML;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;
import java.io.File;
import javax.swing.JFileChooser;
import java.awt.AWTException;
import javafx.scene.layout.AnchorPane;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.docx4j.openpackaging.exceptions.Docx4JException;
import org.docx4j.openpackaging.parts.WordprocessingML.AlternativeFormatInputPart;
import org.docx4j.openpackaging.parts.PartName;
import org.docx4j.openpackaging.contenttype.ContentType;
import org.docx4j.relationships.Relationship;
import org.docx4j.wml.CTAltChunk;
import org.docx4j.jaxb.Context;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Component;

/**
 * FXML Controller class
 *
 * @author Spenc
 */
public class GenerateReportController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    private ReportTypeAdapter reportType;
    private SectionAdapter sectionAdapter;
    private SubSectionAdapter subSectionAdapter;
    private ObservableList<String> TypeData = FXCollections.observableArrayList();
    private ObservableList<String> SectionData = FXCollections.observableArrayList();
    private ObservableList<String> SubSectionData = FXCollections.observableArrayList();
    private ObservableList<String> GenderData = FXCollections.observableArrayList();
    
    @FXML
    Button OkBtn, CancelBtn;
    @FXML
    ComboBox TypeBox;
    @FXML
    TreeView TreeView;
    @FXML
    HTMLEditor HtmlEditor;
    @FXML
    AnchorPane AnchorPane;
    @FXML
    ComboBox GenderBox;
    @FXML
    TextField NameField;
    
    
    @FXML
    public void setTreeItems() throws SQLException{
        if (TypeBox.getValue() != null){
            TreeView.setRoot(null);
            SectionData.clear();
            SectionData.addAll(sectionAdapter.getSectionList(TypeBox.getValue().toString()));
            //Add sections to tree view
            CheckBoxTreeItem<String> root = new CheckBoxTreeItem<String>(TypeBox.getValue().toString());
            int i = 0;
            int j;
            while (i < SectionData.size()){
                root.getChildren().add(new CheckBoxTreeItem<String>(SectionData.get(i)));

                j = 0;
                SubSectionData.clear();
                SubSectionData.addAll(subSectionAdapter.getSubSectionList(TypeBox.getValue().toString(), SectionData.get(i)));
                while (j < SubSectionData.size()){
                    CheckBoxTreeItem<String> section = (CheckBoxTreeItem<String>) root.getChildren().get(i);
                    section.getChildren().add(new CheckBoxTreeItem<String>(SubSectionData.get(j)));
                    CheckBoxTreeItem<String> subSection = (CheckBoxTreeItem<String>) section.getChildren().get(j);
                    subSection.setSelected(false);
                    subSection.selectedProperty().addListener((obs,oldVal, newVal) ->{
                        try{
                            fillText();
                        }
                        catch(SQLException ex){}
                        });
                    j++;
                }
                CheckBoxTreeItem<String> section = (CheckBoxTreeItem<String>) root.getChildren().get(i);
                section.setSelected(false);
                section.setExpanded(true);
                section.selectedProperty().addListener((obs,oldVal, newVal) ->{
                    try{
                        fillText();
                    }
                    catch (SQLException ex){}
                    });
                i++;
            }
            root.setExpanded(true);
            TreeView.setCellFactory(CheckBoxTreeCell.<String>forTreeView());

            TreeView.setRoot(root);
            TreeView.setShowRoot(false);
            fillText();
        }
    }
    

    public void fillTextOG() throws SQLException{
        ObservableList<CheckBoxTreeItem<String>> SelectedSectionData = FXCollections.observableArrayList();
        ObservableList<CheckBoxTreeItem<String>> SelectedSubSectionData = FXCollections.observableArrayList();
        String prevText = " ";
        
        SelectedSectionData.addAll(TreeView.getRoot().getChildren());
        
        String HtmlText = "<!DOCTYPE html> <html><style>BODY {text-align: justify}</style><body>";
        
        for (int i = 0; i < SelectedSectionData.size(); i++){
            if (SelectedSectionData.get(i).isIndeterminate() || SelectedSectionData.get(i).isSelected()){
                if (prevText.startsWith("*")){
                    HtmlText = HtmlText + "</ol><i><b><p><font face = \"Times New Roman\" size = \"3\">" + SelectedSectionData.get(i).getValue() + " </p></br></br></b></i><ol>";
                }
                else{
                    HtmlText = HtmlText + "<i><b><p><font face = \"Times New Roman\" size = \"3\">" + SelectedSectionData.get(i).getValue() + " </p></br></br></b></i>";
                }
                for (int k = 0; k < SelectedSectionData.get(i).getChildren().size(); k++){
                    CheckBoxTreeItem<String> subSection;
                    subSection = (CheckBoxTreeItem<String>) SelectedSectionData.get(i).getChildren().get(k);
                    if (subSection.isSelected()){
                        //Add the text to text area
                        SubSection SS = subSectionAdapter.getSubSection(TypeBox.getValue().toString(), SelectedSectionData.get(i).getValue(), subSection.getValue());
                        //Change the apostophee's and quotations
                        String s = SS.Text;
                        s = s.replaceAll("’", "'");
                        s = s.replaceAll("‘", "'");
                        s = s.replaceAll("“", "\"");
                        s = s.replaceAll("”", "\"");
                        s = s.replaceAll("\n\n", "</p><br/><p>");
                        s = s.replaceAll("\n", "</p><p>");
                        
                        //Add in name and gender
                        if (!NameField.getText().equals("")){
                            s = s.replaceAll("XX", NameField.getText());
                        }
                        if (GenderBox.getValue() != null){
                            if (GenderBox.getValue().toString().equals("Male")){
                                s = s.replaceAll("HIM/HER", "him");
                                s = s.replaceAll("HIS/HER", "his");
                                s = s.replaceAll("HE/SHE", "he");
                                s = s.replaceAll("HIMSELF/HERSELF", "himself");
                            }
                            if (GenderBox.getValue().toString().equals("Female")){
                                s = s.replaceAll("HIM/HER", "her");
                                s = s.replaceAll("HIS/HER", "her");
                                s = s.replaceAll("HE/SHE", "she");
                                s = s.replaceAll("HIMSELF/HERSELF", "herself");
                            }
                        }
                        
                        if (s.startsWith("*")){
                            if (!prevText.startsWith("*")){
                                HtmlText = HtmlText + "<ol>";
                            }

                            String[] strArray = s.substring(1).split("\\*");
                            for (int j = 0; j < strArray.length; j++){
                                if (j == 1){
                                    HtmlText = HtmlText + "<ul type = \"square\"><li><p>" + strArray[j] + "</p><br/></li>";
                                }
                                else if (strArray.length >1){
                                    HtmlText = HtmlText + "<li><p>" + strArray[j] + "</p><br/></li>";
                                }
                                else{
                                    HtmlText = HtmlText + "<li><p>" + strArray[j] + "</p><br/></li>";
                                }
                            }
                            if (strArray.length > 1){
                                HtmlText = HtmlText + "</ul>";
                            }
                        }
                        else{
                            if (prevText.startsWith("*")){
                                HtmlText = HtmlText + "</ol><p>" + s + "</p><br/><br/>";
                            }
                            else{
                                HtmlText = HtmlText + "<p>" + s + "</p><br/><br/>";
                            }
                            
                        }
                        prevText = s;
                    }
                }
                HtmlText = HtmlText + "</font>";
                
            }
        }
        
        HtmlText = HtmlText + "</body></html>";
        
        HtmlEditor.setHtmlText(HtmlText);
        
    }
    
        public void fillText() throws SQLException{
        ObservableList<CheckBoxTreeItem<String>> SelectedSectionData = FXCollections.observableArrayList();
        ObservableList<CheckBoxTreeItem<String>> SelectedSubSectionData = FXCollections.observableArrayList();
        String prevText = " ";
        
        SelectedSectionData.addAll(TreeView.getRoot().getChildren());
        
        String HtmlText = "<!DOCTYPE html> <html><style>BODY {text-align: justify}</style><body>";
        
        for (int i = 0; i < SelectedSectionData.size(); i++){
            if (SelectedSectionData.get(i).isIndeterminate() || SelectedSectionData.get(i).isSelected()){
                if (prevText.startsWith("*")){
                    HtmlText = HtmlText + "</ol><i><b><p><font face = \"Times New Roman\" size = \"3\">" + SelectedSectionData.get(i).getValue() + " </p></br></br></b></i><ol>";
                }
                else{
                    HtmlText = HtmlText + "<i><b><p><font face = \"Times New Roman\" size = \"3\">" + SelectedSectionData.get(i).getValue() + " </p></br></br></b></i>";
                }
                for (int k = 0; k < SelectedSectionData.get(i).getChildren().size(); k++){
                    CheckBoxTreeItem<String> subSection;
                    subSection = (CheckBoxTreeItem<String>) SelectedSectionData.get(i).getChildren().get(k);
                    if (subSection.isSelected()){
                        //Add the text to text area
                        SubSection SS = subSectionAdapter.getSubSection(TypeBox.getValue().toString(), SelectedSectionData.get(i).getValue(), subSection.getValue());
                        //Change the apostophee's and quotations
                        String s = SS.Text;
                        s = s.replaceAll("’", "'");
                        s = s.replaceAll("‘", "'");
                        s = s.replaceAll("“", "\"");
                        s = s.replaceAll("”", "\"");
                        s = s.replaceAll("\n\n", "</p><br/><p>");
                        s = s.replaceAll("\n", "</p><p>");
                        
                        //Add in name and gender
                        if (!NameField.getText().equals("")){
                            s = s.replaceAll("XX", NameField.getText());
                        }
                        if (GenderBox.getValue() != null){
                            if (GenderBox.getValue().toString().equals("Male")){
                                s = s.replaceAll("HIM/HER", "him");
                                s = s.replaceAll("HIS/HER", "his");
                                s = s.replaceAll("HE/SHE", "he");
                                s = s.replaceAll("HIMSELF/HERSELF", "himself");
                            }
                            if (GenderBox.getValue().toString().equals("Female")){
                                s = s.replaceAll("HIM/HER", "her");
                                s = s.replaceAll("HIS/HER", "her");
                                s = s.replaceAll("HE/SHE", "she");
                                s = s.replaceAll("HIMSELF/HERSELF", "herself");
                            }
                        }
                        
                        String[] strArray = s.split("\\*");
                        for (int j = 0; j < strArray.length; j++){
                            if (j == 0){
                                HtmlText = HtmlText + "<p>" + strArray[j] + "</p>";
                            }
                            else if (j == 1){
                                HtmlText = HtmlText + "<ol><li><p>" + strArray[j] + "</p><br/></li>";
                            }
                            else if (strArray.length >1){
                                HtmlText = HtmlText + "<li><p>" + strArray[j] + "</p><br/></li>";
                            }
                        }
                        HtmlText = HtmlText + "</ol>";
                    }
                }
                HtmlText = HtmlText + "</font>";
                
            }
        }
        
        HtmlText = HtmlText + "</body></html>";
        
        HtmlEditor.setHtmlText(HtmlText);
        
    }
 
    @FXML
    public void Okay() throws AWTException, Exception{

        
        //Open file chooser window to get file path
        JFileChooser jfc = new JFileChooser();
        jfc.setPreferredSize(new Dimension(1200, 800));
        jfc.setVisible(true);
        jfc.showDialog(null,"Save");
        File filename = jfc.getSelectedFile();


        if (filename != null){
            System.out.println(HtmlEditor.getHtmlText());
            String text =  "<html><body><p><font color=\"red\">To fix spacing: Press ctrl + A to select all text, then under home go to paragraph and click the icon in the bottom corner. Set spacing before and after to 0. </font> </p>";
            text = text + HtmlEditor.getHtmlText();
            xhtmlToDocx(text, filename);
        }
        
        //Open the file
        Process p = Runtime.getRuntime()
                        .exec("rundll32 url.dll,FileProtocolHandler " + filename + ".docx");
        p.waitFor();
    }
    
    public static void xhtmlToDocx(String xhtml, File fileName) throws InvalidFormatException, Docx4JException
        {
            //.. HTML Code
            String html = xhtml;

            WordprocessingMLPackage wordMLPackage = WordprocessingMLPackage.createPackage();
            AlternativeFormatInputPart afiPart = new AlternativeFormatInputPart(new PartName("/hw.html"));
            afiPart.setBinaryData(html.getBytes());
            afiPart.setContentType(new ContentType("text/html"));
            Relationship altChunkRel = wordMLPackage.getMainDocumentPart().addTargetPart(afiPart);

            // .. the bit in document body
            CTAltChunk ac = Context.getWmlObjectFactory().createCTAltChunk();
            ac.setId(altChunkRel.getId() );
            wordMLPackage.getMainDocumentPart().addObject(ac);

            // .. content type
            wordMLPackage.getContentTypeManager().addDefaultContentType("html", "text/html");
            wordMLPackage.save(new java.io.File(fileName.toString() + ".docx"));
        }
    
    @FXML
    public void Exit(){
        Stage stage = (Stage) CancelBtn.getScene().getWindow();
        stage.close();
    }
    
    public void setModel(ReportTypeAdapter rType, SectionAdapter sAdapter, SubSectionAdapter ssAdapter) throws SQLException{
        reportType = rType;
        sectionAdapter = sAdapter;
        subSectionAdapter = ssAdapter;
        
        TypeData.addAll(reportType.getTypeList());
        GenderData.add("Male");
        GenderData.add("Female");
    }
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb){
        
        // TODO
        TypeBox.setVisible(true);
        TypeBox.setItems(TypeData);
        GenderBox.setItems(GenderData);
    }    
    
    
}


