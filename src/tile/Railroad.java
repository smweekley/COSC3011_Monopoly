package tile;

import player.Player;

public class Railroad extends Tile{
    private final int buyCost;
    private final int[] rent;
    private final int mortgageValue;
    private Player owner;

    public Railroad(int buyCost, int[] rent, int mortgageValue) {
        this.buyCost = buyCost;
        this.rent = rent.clone();
        this.mortgageValue = mortgageValue;
        owner = null;
    }

    public Railroad() {
        buyCost = 200;
        rent = new int[]{25,50,100,200};
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
        else if (player != owner) {
            int numOwnedRails = owner.railroadCount();
            player.reduceMoney(rent[numOwnedRails]);
        }
        return;
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

    public void mortgage() {
        return; // idk how mortaging works so i'll finish it later 
    }

}
