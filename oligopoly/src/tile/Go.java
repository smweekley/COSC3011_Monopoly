package tile;

import player.Player;

public class Go extends Tile{
    private final int collectMoney;

    public Go(int collectMoney) {
        this.collectMoney = collectMoney;
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
}
