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
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.Parent;

public class MainMenuController {

    @FXML private VBox mainMenu;

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
        System.out.println("Loading game...");
        // Implement load game logic here
    }

    @FXML
    private void exit() {
        System.out.println("Exiting...");
        Stage stage = (Stage) mainMenu.getScene().getWindow();
        stage.close();
    }

}