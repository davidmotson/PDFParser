package com.davidm.PDFAnnotationParser;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;



import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;



import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.interactive.annotation.PDAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import com.sun.javafx.stage.StageHelper;

public class AnnotationParseController {   
    @FXML 
    private TextField inputFileField;
    @FXML 
    private TextField outputFolderField;
    @FXML
    private TextArea outputInfo;
    
    private StringBuilder outputInfoBuffer = new StringBuilder();
    
    private Stage stage;
    
    private Optional<List<File>> inputFiles = Optional.empty();
    
    private Optional<File> outputDirectory = Optional.empty();
        
    public void inputClick(){
    	FileChooser chooser = new FileChooser();
    	chooser.setTitle("Choose PDF Files");
    	chooser.getExtensionFilters().add(new ExtensionFilter("PDF", "*.pdf"));
    	inputFiles = Optional.ofNullable(chooser.showOpenMultipleDialog(stage));
    	inputFileField.setText(inputFiles.filter(x -> !x.isEmpty()).map(String::valueOf).orElse("No file(s) Selected"));
    }
    
    public void outputClick(){
    	DirectoryChooser chooser = new DirectoryChooser();
    	chooser.setTitle("Choose Output Folder");
    	outputDirectory = Optional.ofNullable(chooser.showDialog(stage));
    	outputFolderField.setText(outputDirectory.map(String::valueOf).orElse("No Folder Selected"));
    }
    
    public void process(){
    	outputInfoBuffer = new StringBuilder();
    	if(!inputFiles.filter(x -> !x.isEmpty()).isPresent()){
    		outputInfoBuffer.append("Error: No Input Files!!\n");
    	}
    	if(!outputDirectory.isPresent()){
    		outputInfoBuffer.append("Error: No Output Folder!!\n");
    	}
    	if(outputDirectory.isPresent() && inputFiles.isPresent()){
    		parsePDFs(inputFiles.get(), outputDirectory.get(), outputInfoBuffer);
    	}
        outputInfo.setText(outputInfoBuffer.toString());
    }
    
    public void parsePDFs(List<File> input, File outputDir, StringBuilder errorLog){
    	input.forEach(file -> {
    		try {
				errorLog.append("Processing: " + file.getAbsolutePath() + "\n");
				PrintStream outputStream = new PrintStream(new File(outputDir, file.getName().replace(".pdf", ".txt")));
				parsePDF(file).forEach(outputStream::println);
				outputStream.close();
			} catch (Exception e) {
				errorLog.append("Error processing: " + e.getMessage() + "\n");
			}
    	});
    	errorLog.append("All Done");
    }
    
    public List<String> parsePDF(File input) throws IOException{
    	List<String> output = new ArrayList<String>();
    	for (PDPage page : ((List<PDPage>) PDDocument.load(input).getDocumentCatalog().getAllPages())) {
			for (PDAnnotation annotation : page.getAnnotations()) {
				if (annotation == null) {
					continue;
				}
				String contents = annotation.getContents();
				if (contents != null) {
					output.add(contents);
				}
			}
		}
    	return output;
    }
    
    public void setStage(Stage in){
    	stage = in;
    }

}
