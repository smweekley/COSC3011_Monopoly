// Encompasses purchasable properties that can be built upon with houses/hotels.

package tile;

// import javafx stuff
import player.Player;
import java.util.ArrayList;
import java.util.List;

import fxml.PopupManager;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.util.Collections;

public class Property extends Tile{

    private Colors propertyColor;
    private Player propertyOwner;
    private int[] rent; // ex: {50, 200, 600, 1400, 1700, 2000} {0 houses, 1 house, 2, 3, 4, 1 hotel}
    private int purchasePrice;
    private int houseCount;
    private int hotelCount;
    private int houseCost;  // $50 for brown and light blue, $100 for pink and orange, $150 for red and yellow, $200 for green and dark blue
    private int index;
    private boolean propertyOwned;
    private ArrayList<Property> colorSet; // Properties of same color
    private boolean isMortgaged;
    private Label houseLabel;


    // Constructor
    public Property(String name, int purchasePrice, int[] rent, int houseCost, Colors propertyColor, int index) {
        this.setName(name);
        this.purchasePrice = purchasePrice;
        this.rent = rent;
        this.houseCost = houseCost;
        this.propertyColor = propertyColor;
        this.houseCount = 0;
        this.propertyOwner = null;
        this.propertyOwned = false;
        this.isMortgaged = false;
        this.index = index;
        this.houseLabel = null;
    }

    // Match properties based on color. Call this AFTER all properties have been constructed
    // example: List<Property> oranges = Arrays.asList(prop1, prop2, prop3)
    //          for ( Property prop : oranges) { prop.setColorSet(oranges); }
    public void setColorSet(List<Property> colorSet) {
        this.colorSet = new ArrayList<Property>(colorSet);
        this.colorSet.remove(this); // Remove self-reference
    }

    // Constructor from save data (assuming we just read from lines of plaintext detailing each tile/player's info)
    public Property(String name, int purchasePrice, int[] rent, int houseCost, Player propertyOwner, int houseCount, boolean isMortgaged) {
        setName(name);
        this.purchasePrice = purchasePrice;
        this.rent = rent;
        this.propertyOwner = propertyOwner;
        this.houseCount = houseCount;
        this.houseCost = houseCost;
        this.isMortgaged = isMortgaged;

        if (propertyOwner != null) this.propertyOwned = false;
        else this.propertyOwned = true;
    }

    // Getters and Setters
    public void setName(String tileName) { this.name = tileName; }

    public String getName() { return name; }

    public int getIndex() {
        return index;
    }

    public Player getOwner() { return propertyOwner; }

    public int getPurchasePrice() { return purchasePrice; }

    public int getHouseCount() { return houseCount; }

    public void setHouseCount(int count){this.houseCount = count;}

    public void setHouseLabel(Label label){this.houseLabel = label;}

    public Label getHouseLabel(){return houseLabel;}

    public boolean isOwned() { return propertyOwned; }

    public boolean isMortgaged() { return isMortgaged; }

    // Return an unalterable copy of colorSet for exterior checks
    public List<Property> getColorSet() { return Collections.unmodifiableList(colorSet); }

    public int getRent() {
        int index = hotelCount > 0 ? 5 : houseCount;
        return rent[index];
    }

    public int getNextRent() {
        int index = hotelCount > 0 ? 5 : houseCount;
        return rent[index+1];
    }

    public void setOwner(Player player) {
        propertyOwner = player;
        propertyOwned = true;
    }
    
    // Buy Property
    public void buyProperty(Player player) {  //to-do:standardize
        if (propertyOwned == false && player.getMoney() >= purchasePrice) {
            player.reduceMoney(purchasePrice);
            setOwner(player);
            player.addProperty(this);
            PopupManager.showPopup(player.getName() + " bought " + getName() + " for $" + purchasePrice);
        }
        else if (propertyOwned == true) {
            PopupManager.showPopup("This property is already owned.");
        }
        else if (propertyOwned == false && player.getMoney() < purchasePrice) {
            PopupManager.showPopup("You cannot afford to buy this property.");
        }
    }

    // Sell Property (Just goes back to bank)
    public void sellProperty(Player player) {
        if (propertyOwned == true && propertyOwner == player) {
            int refundAmount = (purchasePrice + (houseCost * houseCount)) / 2;  // Sells for half the value of property and houses/hotels
            player.addMoney(refundAmount);
            propertyOwner.removeProperty(this);
            propertyOwner = null;
            propertyOwned = false;
            PopupManager.showPopup(player.getName() + " sold " + getName() + " back to the bank for $" + refundAmount);
        }
    }

    // Pay Rent
    public void payRent(Player player) {
        if (!isMortgaged()) {
            int rentAmount = getRent();
            player.reduceMoney(rentAmount);
            propertyOwner.addMoney(rentAmount);
            PopupManager.showPopup(player.getName() + " paid $" + rentAmount + " in rent to " + propertyOwner.getName());
        }
        else {
            PopupManager.showPopup("Property is mortgaged, no rent is due!");
        }
        return; // Property is mortgaged, no rent can be collected.
    }

    // Build house/hotel (should be able to be done by player at any point during their turn regardless of position
    // will have to adjust for that as well as allowing player to buy multiple houses per turn given that they have the money)
    // to-do: add popups
    public boolean buyHouse(Player player) {

        if (propertyOwner != player) {  // Player doesn't own property
            PopupManager.showPopup("You don't own this property.");
            return false;
        }

        if (!ownsFullSet(player)) { // Player doesn't own every property in colorSet
            PopupManager.showPopup("You must own all properties of this color set to build houses.");
            return false;
        }

        if (hotelCount >= 1) {  // Player already has a hotel
            PopupManager.showPopup("This property already has a hotel. You can'te build any more.");
            return false;
        }

        if (player.getMoney() < houseCost) {    // Player is too poor, lmao stay mad wagey
            PopupManager.showPopup("You don't have enough money to buy a house.");
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
            PopupManager.showPopup("This property must have the least number of houses/hotels to build on it.");
            return false;
        }


        // Build house/hotel
        player.reduceMoney(this.houseCost);
        if (getHouseCount() == 4) {
            hotelCount++;
        }
        else {
            houseCount++;
        }

        if (getHouseCount() >= 1) {
            PopupManager.showPopup("A hotel has been build on " + getName() + ".");
        } else {
            PopupManager.showPopup("A house has been built on " + getName() + ". Total houses: " + getHouseCount());
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


    public void setMortgaged(boolean mortgaged) {
        this.isMortgaged = mortgaged;
    }

    // Land On Logic
    public void landOn(Player player) {
        boolean lmao = false;
        if (!isOwned() && !lmao) {
            showPurchasePopup(player);
            lmao = true;
        } else if (propertyOwner != player && !lmao) {
            payRent(player);
            lmao = true;
        } else {
            PopupManager.showPopup(player.getName() + " landed on their own property.");
            // Optional: Offer to build houses here or defer to player turn menu
        }
    }

    //////////////// Purchase tile test method ////////////////
    private void showPurchasePopup(Player player) {
    Stage popup = new Stage();
    popup.initModality(Modality.APPLICATION_MODAL);
    popup.setTitle("Buy Property");

    Label label = new Label(player.getName() + ", would you like to buy " + name + " for $" + purchasePrice + "?");

    Button buyButton = new Button("Buy");
    Button passButton = new Button("Pass");

    buyButton.setOnAction(e -> {
        buyProperty(player);
    });

    passButton.setOnAction(e -> {
        popup.close();
    });

    VBox layout = new VBox(10, label, buyButton, passButton);
    layout.setStyle("-fx-padding: 20;");
    popup.setScene(new Scene(layout, 300, 150));
    popup.showAndWait();
}

    

    /*
    Returns ArrayList of strings
    [0] = Tile Type
    [1] = Tile Name
    [2] = Tile Ownership
    [3] = Rent
    [4] = House Cost
    [5] = Property Color
    [6] = Is property mortgaged
    [7] = Cost to unmortgage
    [8] = If property owner owns other properties in color set, and their details.
    */
    public ArrayList<String> getTileInfo() {
        ArrayList<String> info = new ArrayList<String>();
        int propertiesOwnedInSet = 0;
        int unmortgageCost = (int) ((purchasePrice / 2) * 1.10);
        int mortgageCost = (purchasePrice / 2);

        info.add("Property");
        info.add(getName());

        if (isOwned()){
            info.add(propertyOwner.getName());
        } else {
            info.add("Not Owned");
        }

        info.add(Integer.toString(getRent()));

        if (isOwned()){
            info.add(Integer.toString(houseCost));
        } else {
            info.add(Integer.toString(purchasePrice));
        }

        info.add(propertyColor.toString());
        if (isMortgaged()) {
            info.add("This property is mortgaged.");
        } else{
            info.add("This property is not mortgaged.");
        }
        
        info.add(String.valueOf(unmortgageCost));

        StringBuilder propSummary = new StringBuilder();
        if (isOwned()) {
            for (Property prop : colorSet) {
                if (prop.getOwner() == this.getOwner()) {
                    propertiesOwnedInSet++;
                    propSummary.append(prop.getName() + " with " + prop.houseCount);
                    if(prop.houseCount == 5) {
                        propSummary.append("1 hotel.");
                    } else {
                        propSummary.append(prop.houseCount).append(prop.houseCount == 1 ? " house. " : " houses.");
                    }
                }
            }
            if (propertiesOwnedInSet > 0) {
                info.add("Property Owner Also Owns: " + propSummary);
            }
            else {
                info.add("Property Owner Also Owns: None of the other properties in the set.");
            }
        }
        info.add("Not Owned: no info");
        info.add(String.valueOf(mortgageCost));
        info.add(String.valueOf(getNextRent()));
        return info;
    }
}