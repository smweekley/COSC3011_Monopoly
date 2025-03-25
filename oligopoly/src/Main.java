import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;

import board.Board;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        File fxmlFile = new File("src/fxml/board.fxml");
        Pane root = FXMLLoader.load(fxmlFile.toURI().toURL());
        primaryStage.setTitle("Oligopoly");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
