
package tile;
import player.Player;
import java.util.ArrayList;

import fxml.PopupManager;

public class Tax extends Tile {
    private String description;
    private boolean luxuryTax;
    public Tax (String name, String desc, boolean luxTax) {
        this.name = name;
        this.description = desc;
        this.luxuryTax = luxTax;
    }

    public void setName(String name) { this.name = name; }

    public String getName() { return this.name; }

    private void setDescription(String desc) { this.description = desc; }

    public String getDescription() { return description; }

    private void setLuxuryTax(boolean luxTax) { luxuryTax = luxTax; }


public void landOn(Player player) {
    if (luxuryTax) {
        player.reduceMoney(75);
        PopupManager.showPopup("You paid $75 in luxury tax.");
    } else {
        int incomeTax = Math.min(200, (int)(player.getMoney() * 0.10));
        player.reduceMoney(incomeTax);
        PopupManager.showPopup("You paid $" + incomeTax + " in income tax.");
    }
}


    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<>();
        info.add("Tax");
        info.add(getName());
        info.add(getDescription());
        return info;
    }
}
