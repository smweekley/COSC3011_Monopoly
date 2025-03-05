package tile;

import player.Player;
import java.util.ArrayList;

public class Jail extends Tile{
    private final int fine;
    private final String name;
    private ArrayList<Player> playersInJail;
    private ArrayList<Player> playersVisiting;

    public Jail(int fine, String name) {
        this.fine = fine;
        this.name = name;
        this.playersInJail = new ArrayList<>();
        this.playersVisiting = new ArrayList<>();
    }

    public void landOn(Player player) {
        playersVisiting.add(player);
    }

    public String getName() {
        return "Jail Tile";
    }

    public void setName() {
        return; // Not needed, we want a final name so it doesn't get changed
    }
}
