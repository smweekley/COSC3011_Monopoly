package fxml;

import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.StageStyle;


// PopupManager.showPopup("str") to make a popup with "str" can add more later
// also its a singleton

public class PopupManager {
    private static PopupManager instance;

    private static volatile Stage primaryStageHolder;

    public static synchronized void initialize(Stage primaryStage) {
        primaryStageHolder = primaryStage;
    }

    private PopupManager() {}

    public static synchronized PopupManager getInstance() {
        if (instance == null) {
            instance = new PopupManager();
        }
        return instance;
    }

    public static void showPopup(String message) {

        final Stage primaryStage = primaryStageHolder;

        if (primaryStage == null) {
            System.err.println("PopupUtil not initialized! Call initialize() first.");
            return;
        }

        if (Platform.isFxApplicationThread()) {
            createPopup(primaryStage, message);
        } else {
            Platform.runLater(() -> createPopup(primaryStage, message));
        }
    }

    private static void createPopup(Stage ownerStage, String message) {
        Stage popupStage = new Stage();
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.initOwner(ownerStage);
        popupStage.initStyle(StageStyle.UTILITY);
        popupStage.setTitle("Message");

        Label label = new Label(message);
        Button okButton = new Button("OK");
        okButton.setOnAction(e -> popupStage.close());

        VBox root = new VBox(10, label, okButton);
        root.setStyle("-fx-padding: 20; -fx-alignment: center;");

        Scene scene = new Scene(root);
        popupStage.setScene(scene);
        popupStage.sizeToScene();
        popupStage.show();
    }

}