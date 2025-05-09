package tile;

import java.util.ArrayList;
import java.util.List;
import board.CommunityChest;
import player.Player;

public class CommunityChestTileAdapter extends Tile {
    private static final CommunityChest communityChestSystem = new CommunityChest();
    
    public CommunityChestTileAdapter() {
        this.name = "Community Chest";
    }
    
    public void landOn(Player player) {
        // Draw a card when player lands on this tile
        communityChestSystem.drawCard(player, null);
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Community Chest");
        info.add("Draw a Community Chest card and follow its instructions.");
        return info;
    }
    
    // Static method to initialize the CommunityChest system with player list
    public static void initializeCommunityChestSystem(List<Player> players) {
        CommunityChest.setAllPlayers(players);
    }
}