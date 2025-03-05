package tile;

import player.Player;
import java.util.ArrayList;

public class Jail extends Tile{
    private final int fine;
    private ArrayList<Player> playersInJail;
    private ArrayList<Player> playersVisiting;

    public Jail(int fine, String name) {
        this.fine = fine;
        this.name = name;
        playersInJail = new ArrayList<>();
        playersVisiting = new ArrayList<>();
    }

    public void landOn(Player player) {
        playersVisiting.add(player);
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }
}
