package com.davidm.PDFAnnotationParser;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainApp extends Application {

    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage stage) throws Exception {


        String fxmlFile = "/fxml/hello.fxml";
        FXMLLoader loader = new FXMLLoader();
        Parent rootNode = (Parent) loader.load(getClass().getResourceAsStream(fxmlFile));
        AnnotationParseController controller = loader.getController();
        controller.setStage(stage);

        Scene scene = new Scene(rootNode, 700, 400);
        scene.getStylesheets().add("/styles/styles.css");

        stage.setTitle("PDF Parser");
        stage.setScene(scene);
        stage.show();
    }
}
