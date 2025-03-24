// Encompasses purchasable properties that can be built upon with houses/hotels.

package src.tile;

// import javafx stuff
import player.Player;
import java.util.List;

public class Property extends Tile{
    private Player propertyOwner;
    private final int[] rent = {50, 200, 600, 1400, 1700, 2000}; // {0 houses, 1 house, 2, 3, 4, 1 hotel}
    private final int purchasePrice;
    private int houseCount; // 0-4 are houses, 5 is a hotel
    private int houseCost;  // $50 for brown and light blue, $100 for pink and orange, $150 for red and yellow, $200 for green and dark blue
    private boolean propertyOwned;
    private List<Property> colorSet; // Properties of same color

    // Constructor
    public Property(String name, int purchasePrice, int houseCost) {
        setName(name);
        this.purchasePrice = purchasePrice;
        this.propertyOwner = null;
        this.houseCount = 0;
        this.houseCost = houseCost;
        this.propertyOwned = false;
        //  Handle colorSet here?
    }

    // Constructor from save data (assuming we just read from lines of plaintext detailing each tile/player's info)
    public Property(String name, int purchasePrice, Player propertyOwner, int houseCount, int houseCost) {
        setName(name);
        this.purchasePrice = purchasePrice;
        this.propertyOwner = propertyOwner;
        this.houseCount = houseCount;
        this.houseCost = houseCost;
        if (propertyOwner != null) {
            this.propertyOwned = false;
        } else { this.propertyOwned = true; }
        // Handle colorSet here?
    }

    // Getters and Setters
    public void setName(String tileName) { this.name = tileName; }

    public String getName() { return name; }

    public Player getOwner() { return propertyOwner; }

    public int getPurchasePrice() { return purchasePrice; }

    public int getRent() { return rent[houseCount]; }

    public boolean isOwned() { return propertyOwned; }

    public void setColorSet(List<Property> colorSet) { this.colorSet = colorSet; }  // Match properties based on color

    public void setOwner(Player player) {
        propertyOwner = player;
        propertyOwned = true;
    }
    
    // Buy Property
    public boolean buyProperty(Player player) {
        if (propertyOwned == false && player.getMoney() >= purchasePrice) {
            player.reduceMoney(purchasePrice);
            setOwner(player);
            System.out.println(player.getName() + " bought " + getName() + " for $" + purchasePrice);    // Replace with GUI stuff
            return true;
        }
        return false;
        }

    // Sell Property (Just goes back to bank)
    public void sellProperty(Player player) {
        if (propertyOwned == true && propertyOwner == player) {
            int refundAmount = (purchasePrice + (houseCost * houseCount)) / 2;  // Sells for half the value of property and houses/hotels
            player.addMoney(refundAmount);
            propertyOwner = null;
            propertyOwned = false;
            System.out.println(player.getName() + " sold " + getName() + " back to the bank for $" + refundAmount);  // Replace with GUI stuff
        }
    }

    // Pay Rent
    public void payRent(Player player) {
        if (propertyOwned == true && propertyOwner != player) {
            int rentAmount = getRent();
            player.reduceMoney(rentAmount);
            propertyOwner.addMoney(rentAmount);
            System.out.println(player.getName() + " paid $" + rentAmount + " in rent to " + propertyOwner.getName());   // Replace with GUI stuff
        }
    }

    // Build house/hotel (should be able to be done by player at any point during their turn regardless of position
    // will have to adjust for that as well as allowing player to buy multiple houses per turn given that they have the money)
    public boolean buyHouse(Player player) {
        if (propertyOwner != player) {
            System.out.println("You don't own this property.");
            return false;
        }
        if (!ownsFullSet(player)) {
            System.out.println("You must own all properties of this color set to build houses.");
            return false;
        }
        if (houseCount >= 5) {
            System.out.println("This property already has a hotel. You can't build any more.");
            return false;
        }
        if (player.getMoney() < houseCost) {
            System.out.println("You don't have enough money to buy a house.");
            return false;
        }
        /* Implement even distribution of houses. Very annoying. Will come back to this
        if ( houses not evenly distributed) {
            System.out.println("Houses must be built evenly across all properties in the color set.");
            return false;
        }
        */

        player.reduceMoney(houseCost);
        houseCount++;

        if (houseCount == 5) {
            System.out.println("A hotel has been built on " + name + ".");
        } else {
            System.out.println("A house has been built on " + name + ". Total houses: " + houseCount);
        }
        return true;
    }

    // Check if player owns all properties in colorSet
    private boolean ownsFullSet(Player player) {
        int ownedProp = 0;
        for (Property property : colorSet) {
            if (property.getOwner() == player) {
                ownedProp++;
            }
        }
        if (ownedProp == colorSet.size()) {
            return true;
        }
        return false;
    }

    // Check if houses are evenly distributed across colorSet
    //
    //

    // Land On Logic
    public void landOn(Player player) {
        if (!isOwned()) {
            System.out.println(player.getName() + " can buy " + name + " for $" + purchasePrice);
            // GUI stuff to prompt player for actions
        } else if (propertyOwner != player) {
            payRent(player);
        } else {
            System.out.println(player.getName() + " landed on their own property.");

        }
    }
}