package player;

import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
//-------------------------------
import java.util.ArrayList;
import java.util.Random;
import tile.Property;
import tile.Railroad;
import tile.Tile;
import tile.Utility;


public class Player {
    private final Circle tokenc;
    private int currentPosition;

    public Player(Color color) {
        tokenc = new Circle(10, color);
    }


    public Circle getTokenc() { return tokenc; }
    public int getCurrentPosition() { return currentPosition; }
    public void setCurrentPosition(int position) { this.currentPosition = position; }

    //----------------------------------------------------------------------------------------


    private String name;
    private int money;
    private ArrayList<Tile> properties;
    //private ArrayList<Card> cards;
    private int position;
    private String token;
    private boolean inJail;
    private int timeInJail;

    public Player(Circle tokenc, String name, int money, String token) {
        this.tokenc = tokenc;
        this.name = name;
        this.money = money;
        this.token = token;
        position = 0;
        inJail = false;
        timeInJail = 0;
        properties = new ArrayList<>();
    }

    public String getName() {return name;}

    public int getMoney() {return money;}

    public ArrayList<Tile> getProperties() {return properties;}

    public String getPropertiesString() {
        StringBuilder propertyString = new StringBuilder();

        for (int i = 0; i < properties.size(); i++) {
            propertyString.append(properties.get(i).getName());
            if (i < properties.size() - 1) {
                propertyString.append(", ");
            }
        }

        return propertyString.toString();
    }

    public int getPosition() {return position;}

    public String getToken() {return token;}

    public boolean isJailed() {return inJail;}

    public int jailTime() {return timeInJail;}

    public void reduceMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't remove a negative amount of money, use addMoney()");
        }
        money -= amount;
    }

    public void addMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't add a negative amount of money, use reduceMoney()");
        }
        money += amount;
    }

    public void movePosition(int moves) {
        position = (position + moves)%20; // Change to amount of tiles
    }

    public void goToJail(int time) {
        inJail = true;
        timeInJail = time;
        position = 10000; // !!! change for jail position !!!
    }

    public void releaseFromJail() {
        inJail = false;
        timeInJail = 0;
    }

    public int roll(int numDice) {
        if (numDice > 2 || numDice < 1) {
            throw new IllegalArgumentException("Only row 1 or 2 dice.");
        }
        Random random = new Random();

        int roll1 = random.nextInt(6) + 1;
        int roll2 = random.nextInt(6) + 1;

        return (numDice == 1) ? roll1 : (roll1 + roll2);
    }

    public boolean ownsBothUtilities() {
        int check = 0;
        for (Tile property : properties) {
            if (property instanceof Utility) {
                check += 1;
            }
        }
        return (check == 2);
    }

    public int railroadCount() {
        int count = 0;
        for (Tile property : properties) {
            if (property instanceof Railroad) {
                count += 1;
            }
        }
        return count;
    }





}
