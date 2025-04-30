package tile;

import java.util.ArrayList;
import player.Player;

public class GoToJail extends Tile{


    public void landOn(Player player) {
        player.goToJail(3);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Go To Jail");
        info.add(getName());
        info.add("Landing here sends you to Jail.");
        info.add("Do not pass GO.");
        info.add("Do not collect $200.");
        return info;
    }
    
}
