package src.tile;

import player.Player;

public abstract class Tile{
    protected String name;

    public abstract void landOn(Player player); // Needs player argument to know who to affect
    public abstract String getName();
    public abstract void setName(String name);
}
