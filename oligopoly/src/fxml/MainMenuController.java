package fxml;

import board.Board;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import player.Player;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import state.StateManager;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;

public class MainMenuController {

    @FXML private VBox mainMenu;
    @FXML private Button loadGameButton;

    @FXML
    private void newGame() {
        try {
            File fxmlFile = new File("src/fxml/newGameMenu.fxml");
            Pane root = FXMLLoader.load(fxmlFile.toURI().toURL());
            Stage stage = (Stage) mainMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Oligopoly - Player Selection");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));

        File defaultDirectory = new File("saves");
        if (defaultDirectory.exists()) {
            fileChooser.setInitialDirectory(defaultDirectory);
        } else {
            // Fallback to home dir if saves doesn't exist
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        File selectedFile = fileChooser.showOpenDialog(loadGameButton.getScene().getWindow());
        if (selectedFile != null) {
            ArrayList<Player> loadedPlayers = StateManager.loadGame(selectedFile.getAbsolutePath());

            try {
                System.out.println("Starting a new game...");
    
                File fxmlFile = new File("src/fxml/board.fxml");
                FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
                Pane root = loader.load();
    
                Controller boardController = loader.getController();
                if (boardController == null) {
                    throw new IllegalStateException("Controller is not set. Check FXML file for fx:controller attribute.");
                }
                boardController.initializePlayers(new ArrayList<>(loadedPlayers));
    
                Stage stage = (Stage) mainMenu.getScene().getWindow();
                stage.setScene(new Scene(root, 1280, 720));
                stage.setTitle("Oligopoly - Game Board");
    
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                System.err.println(e.getMessage());
            }
        } else {
            System.out.println("File selection cancelled.");
        }
    }

    @FXML
    private void exit() {
        System.out.println("Exiting...");
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

}