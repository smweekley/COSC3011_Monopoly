import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import fxml.PopupManager;
import java.io.File;

import board.Board;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        File fxmlFile = new File("src/fxml/mainMenu.fxml");
        Pane root = FXMLLoader.load(fxmlFile.toURI().toURL());
        primaryStage.setTitle("Oligopoly Main Menu");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        PopupManager.getInstance().initialize(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
