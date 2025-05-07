/*
package tile;

import board.Chance;
import player.Player;
import java.util.ArrayList;

public class ChanceTileAdapter extends Tile {

    private Chance chanceCard;
    private String name;

    public ChanceTileAdapter() {
        this.name = "Chance";
        this.chanceCard = new Chance();
    }

    public void landOn(Player player) {
        chanceCard.drawCard(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Tile: " + name);
        info.add("Draw a Chance card and follow its instructions.");
        return info;
    }
}
*/