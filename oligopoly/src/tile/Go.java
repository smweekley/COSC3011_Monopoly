package tile;

import java.util.ArrayList;

import player.Player;

public class Go extends Tile{

    private final int collectMoney;

    public Go(String name, int collectMoney) {
        this.collectMoney = collectMoney;
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    // we need to detect if a player goes over it instead of just landing on it and call this method
    public void landOn(Player player) {
        player.addMoney(collectMoney);
    }

    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Go");
        info.add(getName());
        info.add("Collect $200 when passing or");
        info.add("landing on this tile.");
        return info;
    }
    
}
