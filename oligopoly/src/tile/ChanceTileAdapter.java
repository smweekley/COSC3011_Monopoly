package tile;

import java.util.ArrayList;
import java.util.List;
import board.Chance;
import fxml.PopupManager;
import player.Player;

public class ChanceTileAdapter extends Tile {
    private static final Chance chanceSystem = new Chance();
    
    public ChanceTileAdapter() {
        this.name = "Chance";
    }
    
    public void landOn(Player player) {
        // Draw a card when player lands on this tile
        chanceSystem.drawCard(player, null);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Chance");
        info.add("Draw a Chance card and follow its instructions.");
        return info;
    }
    
    // Static method to initialize the Chance system with player list
    public static void initializeChanceSystem(List<Player> players) {
        Chance.setAllPlayers(players);
    }
}