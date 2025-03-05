import java.util.ArrayList;

public class Player{
    private String name;
    private int money;
    private ArrayList<Property> properties;
    private int position;
    private String token;
    private boolean inJail;
    private int timeInJail;

    public Player(String name, int money, String token) {
        this.name = name;
        this.money = money;
        this.token = token;
        this.position = 0;
        this.inJail = false;
        this.timeInJail = 0;
        this.properties = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public int getMoney() {
        return money;
    }

    public String getProperties() {
        StringBuilder propertyString = new StringBuilder();
    
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

    public String getToken() {
        return token;
    }

    public boolean isJailed() {
        return inJail;
    }

    public int jailTime() {
        return timeInJail;
    }

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

}
