package tile;

import java.util.ArrayList;

import player.Player;

public class Utility extends Tile{
    private final int buyCost;
    private Player owner;
    private boolean isOwned;
    private int mortgageValue;
    private boolean isMortgaged;

    public Utility(int buyCost, String name) {
        this.buyCost = buyCost;
        mortgageValue = (int) (buyCost / 2);
        this.name = name;
        isMortgaged = false;
        owner = null;
    }

    public int getCost() {return buyCost;}

    public Player getOwner() {return owner;}

    public void setOwner(Player newOwner) {owner = newOwner;}

    public void setName(String newName) { name = newName; }

    public String getName() { return name; }

    public boolean isOwned() { return isOwned; }

    public boolean isMortgaged() { return isMortgaged; }

    public void landOn(Player player) {
        if (owner != null && owner != player) { // Only pay rent if it's owned and not the player landing on it
            boolean ownsBoth = player.ownsBothUtilities();

            int roll = player.roll(2); // Roll 2 dice for utilities in Monopoly
            int rent = ownsBoth ? 10 * roll : 4 * roll;

            player.reduceMoney(rent);
            owner.addMoney(rent); // Give rent to the owner
        }
    }

    /*
    [0] = Tile Type
    [1] = Tile Name
    [2] = Tile Ownership
    [3] = Rent
    [4] = Is property mortgaged
    [5] = Cost to unmortgage
    [6] = If owner owns other properties of same type, and their names if true.
    */
    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<String>();
        int utilitiesOwned = 0;
        int unmortgageCost = (int) (mortgageValue * 1.10);

        info.add("Utility");
        info.add(getName());

        if (this.isOwned()) {
            info.add(getOwner().getName());
        } else {
            info.add("Bank");
        }

        info.add(String.valueOf(buyCost));

        if (isMortgaged()) {
            info.add("This tile is mortgaged.");
        } else {
            info.add("This tile is not mortgaged.");
        }
        
        info.add("$" + unmortgageCost);

        StringBuilder propSummary = new StringBuilder();
        if (isOwned()) {
            ArrayList<Tile> properties = owner.getProperties();
            for (Tile prop : properties) {
                if (prop instanceof Utility) {
                    Utility utilityTileinfo = (Utility) prop;
                    if (utilityTileinfo.getOwner() == this.getOwner() && utilityTileinfo != this) {
                        utilitiesOwned++;
                        propSummary.append(prop.getName() + ". ");
                    }
                }
            }
            if (utilitiesOwned > 0) {
                info.add("Property owner also owns: " + propSummary);
            } else {
                info.add("Property owner does not own any other utilities.");
            }
        } else {
            info.add("Tile is unowned.");
        }

        info.add(String.valueOf((buyCost / 2)));

        return info;
    }
}
