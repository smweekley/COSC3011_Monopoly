package tile;

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
}
