package tile;

public class Property extends Tile{
    private String name;
    private String propertyOwner;
    private final int[] rent = {50, 200, 600, 1400, 1700, 2000};
    boolean setOwned;

    // Get and set property name
    public void setName(String tileName) { name = tileName; }
    public String getName() { return name; }

    // Get and set owner name
    public void setOwner(String player) { propertyOwner = player; }
    public String getOwner() { return propertyOwner; }

    public void landOn(){
        // Do stuff
    }
}