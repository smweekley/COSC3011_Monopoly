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
        this.mortgageValue = (int) (buyCost / 2);
        this.name = name;
        this.isMortgaged = false;
        owner = null;
    }

    public int getCost() {return buyCost;}

    public Player getOwner() {return owner;}

    public void setOwner(Player newOwner) {owner = newOwner;}

    public void landOn(Player player) {
        if (owner != null && owner != player) { // Only pay rent if it's owned and not the player landing on it
            boolean ownsBoth = player.ownsBothUtilities();

            int roll = player.roll(2); // Roll 2 dice for utilities in Monopoly
            int rent = ownsBoth ? 10 * roll : 4 * roll;

            player.reduceMoney(rent);
            owner.addMoney(rent); // Give rent to the owner
        }
    }

    public void setName(String newName) {
        name = newName;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getTileInfo() {
        ArrayList<String> temp = new ArrayList<String>();
        int utilitiesOwned = 0;
        int unmortgageCost = (int) (mortgageValue * 1.10);

        StringBuilder propSummary = new StringBuilder();
        if (this.isOwned) {
            ArrayList<Tile> properties = owner.getProperties();
            for (Tile prop : properties) {
                if (prop instanceof Utility) {
                    Utility utilityTileTemp = (Utility) prop;
                    if (utilityTileTemp.getOwner() == this.getOwner() && utilityTileTemp != this) {
                        utilitiesOwned++;
                        propSummary.append(prop.getName() + ". ");
                    }
                }
            }
        }
        temp.add("Tile Type: Utility");
        temp.add("Tile Name: " + this.getName());

        if (this.isOwned) {
            temp.add("Property Owner: " + this.owner.getName());
        } else {
            temp.add("Property Owner: Not Owned");
        }

        temp.add("Rent: Depends on dice roll.");
        temp.add("Mortgaged: " + Boolean.toString(this.isMortgaged));
        temp.add("Cost to Lift Mortgage: $" + unmortgageCost);
        if (this.isOwned && utilitiesOwned > 0) {
            temp.add("Property Owner Also Owns: " + propSummary);
        }

        return temp;
    }
}
