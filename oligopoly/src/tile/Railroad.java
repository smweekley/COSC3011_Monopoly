package tile;

import java.util.ArrayList;

import player.Player;

public class Railroad extends Tile{
    private final int buyCost;
    private final static int RENT = 25; // Monopoly rule: rent is 25 * 2^(number of railroads owned - 1)
    private int mortgageValue;
    private boolean isMortgaged;
    private boolean isOwned;
    private Player owner;

    public Railroad(String name, int buyCost) {
        this.name = name;
        this.buyCost = buyCost;
        this.mortgageValue = (int) (buyCost / 2);
        this.isMortgaged = false;
        this.owner = null;
        this.isOwned = false;
    }

    public Railroad() {
        buyCost = 200;
        mortgageValue = 100;
        owner = null;
    }

    public String getName() {
        return name;
    }

    public void setName(String newName) {
        name = newName;
    }

    public void landOn(Player player) {
        if (owner == null) {
            // ask player if they want to buy
        }
        else if (player != owner && !isMortgaged) {
            player.reduceMoney(getRent());
            owner.addMoney(getRent());
        }
        return;
    }

    public int getRent() {
        int numOwnedRails = owner.railroadCount();
        return (RENT * (int)Math.pow(2, numOwnedRails - 1));
    }

    public void buy(Player player) {
        if (owner == null) {
            return; // already owned, ask for trade?
        }
        if (player.getMoney() < buyCost) {
            return; // ah hell naw you broke
        }
        owner = player;
        player.reduceMoney(buyCost);
        player.addProperty(this);
    }

    public void mortgage() {    // Rough outline without logic checks.
        owner.addMoney((int) (buyCost / 2));
        this.isMortgaged = true;
        return;
    }

    public Player getOwner() { return owner;}

     public ArrayList<String> getTileInfo() {
        ArrayList<String> temp = new ArrayList<String>();
        int railRoadsOwned = 0;
        int unmortgageCost = (int) (mortgageValue * 1.10);

        StringBuilder propSummary = new StringBuilder();
        if (this.isOwned) {
            ArrayList<Tile> properties = owner.getProperties();
            for (Tile prop : properties) {
                if (prop instanceof Railroad) {
                    Railroad rrTileTemp = (Railroad) prop;
                    if (rrTileTemp.getOwner() == this.getOwner() && rrTileTemp != this) {
                        railRoadsOwned++;
                        propSummary.append(prop.getName() + ". ");
                    }
                }
            }
        }
        temp.add("Tile Type: Railroad");
        temp.add("Tile Name: " + this.getName());

        if (this.isOwned){
            temp.add("Property Owner: " + this.owner.getName());
        } else {
            temp.add("Property Owner: Not Owned");
        }

        temp.add("Rent: " + Integer.toString(this.getRent()));
        temp.add("Mortgaged: " + Boolean.toString(this.isMortgaged));
        temp.add("Cost to Lift Mortgage: $" + unmortgageCost);
        if (this.isOwned && railRoadsOwned > 0) {
            temp.add("Property Owner Also Owns: " + propSummary);
        }

        return temp;
    }

}
