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

    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Jail");
        info.add(getName());
        info.add("Players sent here must wait 3 turns or pay bail to get out.");
        StringBuilder propSummary = new StringBuilder();
        if (!playersInJail.isEmpty()) {
            for (Player jailBird : playersInJail) {
                propSummary.append(jailBird.getName() + " is in jail lmao. ");
            }
        } else {
            propSummary.append("The jail is empty.");
        }
        info.add(propSummary.toString());

        propSummary = new StringBuilder();
        if (!playersVisiting.isEmpty()) {
            for (Player jailBird : playersInJail) {
                propSummary.append(jailBird.getName() + " is visiting the jail. ");
            }
        } else {
            propSummary.append("Nobody is visiting jail.");
        }
        info.add(propSummary.toString());

        return info;
    }
    
}
