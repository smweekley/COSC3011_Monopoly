// Encompasses purchasable properties that can be built upon with houses/hotels.

package tile;

// import javafx stuff
import player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class Property extends Tile{
    private Player propertyOwner;
    private final int[] rent = {50, 200, 600, 1400, 1700, 2000}; // {0 houses, 1 house, 2, 3, 4, 1 hotel}
    private final int purchasePrice;
    private int houseCount;
    private int hotelCount;
    private int houseCost;  // $50 for brown and light blue, $100 for pink and orange, $150 for red and yellow, $200 for green and dark blue
    private boolean propertyOwned;
    private ArrayList<Property> colorSet; // Properties of same color

    // Constructor
    public Property(String name, int purchasePrice, int houseCost) {
        setName(name);
        this.purchasePrice = purchasePrice;
        this.propertyOwner = null;
        this.houseCount = 0;
        this.houseCost = houseCost;
        this.propertyOwned = false;
    }

    // Match properties based on color. Call this AFTER all properties have been constructed
    // example: List<Property> oranges = Arrays.asList(prop1, prop2, prop3)
    //          for ( Property prop : oranges) { prop.setColorSet(oranges); }
    public void setColorSet(ArrayList<Property> colorSet) {
        this.colorSet = new ArrayList<Property>(colorSet);
        this.colorSet.remove(this); // Remove self-reference
    }

    // Constructor from save data (assuming we just read from lines of plaintext detailing each tile/player's info)
    public Property(String name, int purchasePrice, int houseCost, Player propertyOwner, int houseCount) {
        setName(name);
        this.purchasePrice = purchasePrice;
        this.propertyOwner = propertyOwner;
        this.houseCount = houseCount;
        this.houseCost = houseCost;

        if (propertyOwner != null) this.propertyOwned = false;
        else this.propertyOwned = true;
    }

    // Getters and Setters
    public void setName(String tileName) { this.name = tileName; }

    public String getName() { return name; }

    public Player getOwner() { return propertyOwner; }

    public int getPurchasePrice() { return purchasePrice; }

    public int getRent() { return rent[houseCount + hotelCount]; }

    public int getHouseCount() { return houseCount; }

    public boolean isOwned() { return propertyOwned; }

    public void setOwner(Player player) {
        propertyOwner = player;
        propertyOwned = true;
    }

    // Return an unalterable copy of colorSet for exterior checks
    public List<Property> getColorSet() { return Collections.unmodifiableList(colorSet); }
    
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

        if (propertyOwner != player) {  // Player doesn't own property
            System.out.println("You don't own this property.");
            return false;
        }

        if (!ownsFullSet(player)) { // Player doesn't own every property in colorSet
            System.out.println("You must own all properties of this color set to build houses.");
            return false;
        }

        if (hotelCount >= 1) {  // Player already has a hotel
            System.out.println("This property already has a hotel. You can't build any more.");
            return false;
        }

        if (player.getMoney() < houseCost) {    // Player is too poor, lmao stay mad wagey
            System.out.println("You don't have enough money to buy a house.");
            return false;
        }

        // Ensure even distribution of houses among colorSet
        int minHouse = (this.houseCount + this.hotelCount), maxHouse = (this.houseCount + this.hotelCount);
        for ( Property prop : colorSet) {
            int buildings = ( prop.houseCount + prop.hotelCount );
            if ( buildings < minHouse ) minHouse = buildings;
            if ( buildings > maxHouse ) maxHouse = buildings;
        }
        if ( (this.houseCount + this.hotelCount) > minHouse) {
            System.out.println("This property must have the least number of houses/hotels to build on it.");
            return false;
        }


        // Build house/hotel
        player.reduceMoney(this.houseCost);
        if (this.houseCount == 4) {
            this.hotelCount++;
        }
        else {
            this.houseCount++;
        }

        if (this.hotelCount >= 1) {
            System.out.println("A hotel has been built on " + this.name + ".");
        } else {
            System.out.println("A house has been built on " + this.name + ". Total houses: " + this.houseCount);
        }
        return true;
    }

    // Check if player owns all properties in colorSet
    private boolean ownsFullSet(Player player) {
        for (Property property : colorSet) {
            if (property.getOwner() != player) {
                return false;
            }
        }
        return true;
    }

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