package tile;

import player.Player;

public class Property extends Tile{
    private String name; // Don't need this, name is defined in Tile so just use those methods :) -Chase
    private Player propertyOwner;
    private final int[] rent = {50, 200, 600, 1400, 1700, 2000};
    boolean setOwned;

    // Get and set property name
    public void setName(String tileName) { name = tileName; }
    public String getName() { return name; }

    // Get and set owner name
    public void setOwner(Player player) { propertyOwner = player; }
    public Player getOwner() { return propertyOwner; }

    public void landOn(Player player){
        return; // I put this here because the error was bothering me :p -Chase
        // Do stuff
    }
}