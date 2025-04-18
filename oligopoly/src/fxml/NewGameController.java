package fxml;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;

import board.Board;
import player.Player;
import state.StateManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class NewGameController {

    @FXML private VBox newGameMenu;
    @FXML private Slider playerSlider;
    @FXML private TableView<Player> playerTable;
    @FXML private TableColumn<Player, String> playerNumberColumn;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, String> colorColumn;
    @FXML private TextField nameInput;
    @FXML private ComboBox<String> colorComboBox;

    private ObservableList<Player> playerData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        playerNumberColumn.setCellValueFactory(new PropertyValueFactory<>("number"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<>("color"));

        playerSlider.valueProperty().addListener((obs, oldVal, newVal) -> updatePlayerTable(newVal.intValue()));

        colorComboBox.setItems(FXCollections.observableArrayList(
            "Red", "Blue", "Green", "Yellow", "Black", "Orange", "Purple", "Pink"
        ));
        colorComboBox.setValue("Red"); // Default color

        updatePlayerTable((int) playerSlider.getValue());

        playerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nameInput.setText(newSelection.getName());
                colorComboBox.setValue(newSelection.getColor());
            }
        });
    }

    private void updatePlayerTable(int numberOfPlayers) {
        playerData.clear();
        for (int i = 1; i <= numberOfPlayers; i++) {
            playerData.add(new Player(i, "Player " + i, "Red")); // Default color
        }
        playerTable.setItems(playerData);
    }

    @FXML
    private void updatePlayer() {
        Player selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
        if (selectedPlayer != null) {
            selectedPlayer.setName(nameInput.getText());
            selectedPlayer.setColor(colorComboBox.getValue());
            playerTable.refresh();
        }
    }

    @FXML
    private void startGame() {
        try {
            System.out.println("Starting a new game...");

            File fxmlFile = new File("src/fxml/board.fxml");
            FXMLLoader loader = new FXMLLoader(fxmlFile.toURI().toURL());
            Pane root = loader.load();

            Controller boardController = loader.getController();
            if (boardController == null) {
                throw new IllegalStateException("Controller is not set. Check FXML file for fx:controller attribute.");
            }
            boardController.initializePlayers(new ArrayList<>(playerData));

            Stage stage = (Stage) newGameMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Oligopoly - Game Board");

        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalStateException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    private void back() {
        try {
            // Load the main menu
            File fxmlFile = new File("src/fxml/mainMenu.fxml");
            Parent root = FXMLLoader.load(fxmlFile.toURI().toURL());
            Stage stage = (Stage) newGameMenu.getScene().getWindow();
            stage.setScene(new Scene(root, 1280, 720));
            stage.setTitle("Oligopoly - Main Menu");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}