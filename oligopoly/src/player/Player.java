package player;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import tile.Property;
import tile.Railroad;
import tile.Tile;
import tile.Utility;

import java.util.ArrayList;
import java.util.Random;

public class Player {
    private Circle tokenc;
    private int number;
    private String color;
    private String name;
    private int money;
    private ArrayList<Tile> properties;
    private int position;
    private boolean inJail;
    private int timeInJail;

    public Player(int number, String name, String color) {
        setTokenc(color);
        this.number = number;
        this.color = color;
        this.name = name;
        this.money = 1500;
        this.position = 0;
        this.inJail = false;
        this.timeInJail = 0;
        this.properties = new ArrayList<>();
    }

    public Circle getTokenc() {
        return tokenc;
    }

    public void setTokenc(String color) {
        //System.out.println("Setting token color to: " + color);
        this.tokenc = new Circle(10, Color.web(color.toLowerCase()));
        this.color = color;
    }

    public int getNumber() {
        return number;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.tokenc = new Circle(10, Color.web(color));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int amount) {
        this.money = amount;
    }

    public ArrayList<Tile> getProperties() {
        return properties;
    }

    public String getPropertiesString() {
        StringBuilder propertyString = new StringBuilder();
        if (properties == null) {
            return "";
        }
        for (int i = 0; i < properties.size(); i++) {
            propertyString.append(properties.get(i).getName());
            if (i < properties.size() - 1) {
                propertyString.append(", ");
            }
        }
        return propertyString.toString();
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int pos) {
        this.position = pos;
    }

    public boolean isJailed() {
        return inJail;
    }

    public int jailTime() {
        return timeInJail;
    }

    public void setJailTime(int time) {
        this.timeInJail = time;
    }

    public void reduceMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't reduce a negative amount of money, use addMoney()");
        }
        this.money -= amount;
    }

    public void addMoney(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't add a negative amount of money, use reduceMoney()");
        }
        this.money += amount;
    }

    public void changeMoney(int amount) {
        this.money += amount;
    }

    public void movePosition(int moves) {
        this.position = (this.position + moves) % 40;
    }

    public void goToJail(int time) {
        this.inJail = true;
        this.timeInJail = time;
        this.position = 10000;
    }

    public void releaseFromJail() {
        this.inJail = false;
        this.timeInJail = 0;
    }

    public int roll(int numDice) {
        if (numDice > 2 || numDice < 1) {
            throw new IllegalArgumentException("Only roll 1 or 2 dice.");
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
        if (check > 2) {
            throw new IllegalArgumentException("Player owns more than 2 utilities, something is broken.");
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
        if (count > 4) {
            throw new IllegalArgumentException("Player owns more than 4 railroads, something is broken.");
        }
        return count;
    }

    public int houseCount() {
        int count = 0;
        for (Tile property : properties) {
            if (property instanceof Property) {
                int houseCount = ((Property) property).getHouseCount();
                if (houseCount < 5) {
                    count += houseCount;
                }
            }
        }
        return count;
    }

    public int hotelCount() {
        int count = 0;
        for (Tile property : properties) {
            if (property instanceof Property) {
                if (((Property) property).getHouseCount() == 5) {
                    count++;
                }
            }
        }
        return count;
    }

    public ObjectProperty<Circle> iconProperty() {
        return new SimpleObjectProperty<>(tokenc);
    }

    public StringProperty nameProperty() {
        return new SimpleStringProperty(name);
    }

    public IntegerProperty moneyProperty() {
        return new SimpleIntegerProperty(money);
    }

    public BooleanProperty hasItemsProperty() {
        return new SimpleBooleanProperty(inJail);
    }
}