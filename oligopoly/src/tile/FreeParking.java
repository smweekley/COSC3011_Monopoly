package tile;

import javafx.scene.control.Label;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import player.Player;

import java.util.ArrayList;

public class FreeParking extends Tile {

    private int index;

    public FreeParking(String name, int index) {
        setName(name);
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public void landOn(Player player) {
        showFreeParkingPopup(player);
    }

    private void showFreeParkingPopup(Player player) {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.setTitle("Free Parking");

        Label label = new Label(player.getName() + " landed on Free Parking!.");

        Button okButton = new Button("OK");
        okButton.setOnAction(e -> popup.close());

        VBox layout = new VBox(10, label, okButton);
        layout.setStyle("-fx-padding: 20;");
        popup.setScene(new Scene(layout, 300, 100));
        popup.showAndWait();
    }

    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Free Parking");
        info.add(name);
        info.add("No owner");
        info.add("No rent");
        info.add("N/A");
        info.add("N/A");
        info.add("No mortgage info");
        info.add("N/A");
        info.add("Nothing to own here");
        info.add("N/A");
        info.add("N/A");
        return info;
    }
}