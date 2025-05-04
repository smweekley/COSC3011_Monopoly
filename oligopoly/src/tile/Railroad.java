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
        mortgageValue = (int) (buyCost / 2);
        isMortgaged = false;
        owner = null;
        isOwned = false;
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

    public boolean isOwned() {return isOwned;}

    public boolean isMortgaged() {return false;}

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
        try {               // try catch to skip for testing
            int numOwnedRails = owner.railroadCount();
            return (RENT * (int)Math.pow(2, numOwnedRails - 1));
        }catch (Exception e) {
            return 55;
        }

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

    public boolean getMortgaged(){
        return isMortgaged;
    }

    public void mortgage() {// Rough outline without logic checks.
        owner.addMoney((int) (buyCost / 2));
        isMortgaged = true;
        return;
    }

    /*
    [0] = Tile Type
    [1] = Tile Name
    [2] = Is property owned
    [3] = Rent
    [4] = Is property mortgaged
    [5] = Cost to unmortgage
    [6] = If owner owns other properties of same type, and their names if true.
    */
    public Player getOwner() { return owner;}

     public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<String>();
        int railRoadsOwned = 0;
        int unmortgageCost = (int) (mortgageValue * 1.10);
        
 
        info.add("Railroad");
        info.add(getName());

        if (isOwned()){
            info.add(getOwner().getName());
        } else {
            info.add("Bank");
        }

        info.add(Integer.toString(buyCost));

        if (getMortgaged()){
            info.add("This tile is mortgaged.");
        } else {
            info.add("This tile is not mortgaged.");
        }

        info.add("$" + unmortgageCost);

        StringBuilder propSummary = new StringBuilder();
        if (isOwned()) {
            ArrayList<Tile> properties = owner.getProperties();
            for (Tile prop : properties) {
                if (prop instanceof Railroad) {
                    Railroad rrTileinfo = (Railroad) prop;
                    if (rrTileinfo.getOwner() == this.getOwner() && rrTileinfo != this) {
                        railRoadsOwned++;
                        propSummary.append(prop.getName()).append(". ");
                    }
                }
            }
        
            if (railRoadsOwned > 0) {
                info.add("Player also owns these Railroads: " + propSummary.toString());
            } else {
                info.add("Player does not own any other Railroads.");
            }
        } else {
            info.add("Tile is unowned. No Railroad ownership information available.");
        }

        return info;
    }
}
