package tile;

import player.Player;

public class Utility extends Tile{
    private final int buyCost;
    private Player owner;

    public Utility(int buyCost, String name) {
        this.buyCost = buyCost;
        this.name = name;
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
    
    public void buy(Player player) {
        if (owner == null) {
            return; // already owned, ask for trade?
        }
        if (player.getMoney() < buyCost) {
            return; // ah hell naw you broke
        }
        owner = player;
        player.reduceMoney(buyCost);
    }
}
