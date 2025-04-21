package tile;

import player.Player;
import java.util.ArrayList;

public abstract class Tile{
    protected String name;

    public abstract void landOn(Player player); // Needs player argument to know who to affect
    public abstract String getName();
    public abstract void setName(String name);
    public abstract ArrayList<String> getTileInfo();
}
