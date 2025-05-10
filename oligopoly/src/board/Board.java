package board;



import player.Player;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.text.Utilities;

import tile.*;
import tile.Tile.Colors;


public class Board {

    private final ImageView imageView;

    private ArrayList<Player> players;       // List of players

    private ArrayList<Tile> tiles;       // List of tiles

    public Board(ImageView imageView) {
        this.imageView = imageView;
        this.tiles = new ArrayList<>();
        this.players = new ArrayList<>();
        initBoard();
    }

    public void addPlayer(Player player) {players.add(player);}

    public void updatePlayers(ArrayList<Player> players) {this.players = players;}

    public void removePlayer(Player player) {players.remove(player);}

    public ArrayList<Player> getPlayers() {return players;}

    public Player getPlayer(int player) {return players.get(player);}

    public ArrayList<Tile> getTiles() {return tiles;}

    public Tile getTile(int tile) {return tiles.get(tile);}

    public void initBoard(){
        tiles.add(new Go("Go", 200));
        tiles.add(new Property("Mediteranian Avenue", 60, new int[]{2,10,30,90,160,250}, 50, Colors.BROWN, 1));
        tiles.add(new CommunityChestTileAdapter());
        tiles.add(new Property("Baltic Avenue", 60, new int[]{4,20,60,180,320,450}, 50, Colors.BROWN, 3));
        tiles.add(new Tax("Income Tax", "Uncle Sam wants the juiciest cut, 10% or $200.", false));
        tiles.add(new Railroad("Reading Railroad", 200));
        tiles.add(new Property("Oriental Avenua", 100, new int[]{6,30,90,270,400,550}, 50, Colors.LIGHT_BLUE, 6));
        tiles.add(new ChanceTileAdapter());
        tiles.add(new Property("Vermont Avenue", 100, new int[]{6,30,90,270,400,550}, 50, Colors.LIGHT_BLUE, 8));
        tiles.add(new Property("Connecticut Avenue", 120, new int[]{8,40,100,300,450,600}, 50, Colors.LIGHT_BLUE, 9));
        tiles.add(new Jail(50, "Jail"));
        tiles.add(new Property("St. Charles Place", 140, new int[]{10,50,150,450,625,750}, 100, Colors.PINK, 11));
        tiles.add(new Utility(150, "Electric Company"));
        tiles.add(new Property("States Avenue", 140, new int[]{10,50,150,450,625,750}, 100, Colors.PINK, 13));
        tiles.add(new Property("Virginia Avenue", 160, new int[]{12,60,180,500,700,900}, 100, Colors.PINK, 14));
        tiles.add(new Railroad("Pennsylvania Railroad", 200));
        tiles.add(new Property("St. James Place", 180, new int[]{14,70,200,550,750,950}, 100, Colors.ORANGE, 16));
        tiles.add(new CommunityChestTileAdapter());
        tiles.add(new Property("Tennessee Avenue", 180, new int[]{14,70,200,550,750,950}, 100, Colors.ORANGE, 18));
        tiles.add(new Property("New York Avenue", 200, new int[]{16,80,220,600,800,1000}, 100, Colors.ORANGE, 19));
        tiles.add(new FreeParking("Free Parking", 20));
        tiles.add(new Property("Kentucky Avenue", 220, new int[]{18,90,250,700,875,1050}, 150, Colors.RED, 21));
        tiles.add(new ChanceTileAdapter());
        tiles.add(new Property("Indiana Avenue", 220, new int[]{18,90,250,700,875,1050}, 150, Colors.RED, 23));
        tiles.add(new Property("Illinois Avenue", 240, new int[]{20,110,330,800,975,1150}, 150, Colors.RED, 24));
        tiles.add(new Railroad("B&O Railroad", 200));
        tiles.add(new Property("Atlantic Avenue", 260, new int[]{22,110,330,800,975,1150}, 150, Colors.YELLOW, 26));
        tiles.add(new Property("Ventnor Avenue", 260, new int[]{22,110,330,800,975,1150}, 150, Colors.YELLOW, 27));
        tiles.add(new Utility(120, "Water Works"));
        tiles.add(new Property("Marvin Gardens", 280, new int[]{24,120,360,850,1025,1200}, 150, Colors.YELLOW, 29));
        tiles.add(new GoToJail("Go To Jail!"));
        tiles.add(new Property("Pacific Avenue", 300, new int[]{26,130,390,900,1100,1275}, 200, Colors.GREEN, 31));
        tiles.add(new Property("North Carolina Avenue", 300, new int[]{26,130,390,900,1100,1275}, 200, Colors.GREEN, 32));
        tiles.add(new CommunityChestTileAdapter());
        tiles.add(new Property("Pennsylvania Ave", 320, new int[]{28,150,450,1000,1200,1400}, 200, Colors.GREEN, 34));
        tiles.add(new Railroad("Short Line", 200));
        tiles.add(new ChanceTileAdapter());
        tiles.add(new Property("Park Place", 350, new int[]{35,175,500,1100,1300,1500}, 200, Colors.DARK_BLUE, 36));
        tiles.add(new Tax("Luxury Tax", "You look too rich, time to pay Uncle Sam.", true));
        tiles.add(new Property("Boardwalk", 400, new int[]{50,200,600,1400,1700,2000}, 200, Colors.DARK_BLUE, 38));
    }

    private void setColorGroup(List<Integer> tileIndexes, List<Property> properties) {
        for (int index : tileIndexes) {
            properties.add((Property) getTile(index)); // Assume getTile returns the correct tile type
        }
        for (Property prop : properties) {
            prop.setColorSet(properties); // Assign the same color set to all properties in the group
        }
    }

    // Set all color groups for properties
    public void setColorGroups() {
        List<Property> browns = new ArrayList<>();
        setColorGroup(Arrays.asList(1, 3), browns);
        
        List<Property> lightBlues = new ArrayList<>();
        setColorGroup(Arrays.asList(6, 8, 9), lightBlues);
        
        List<Property> pinks = new ArrayList<>();
        setColorGroup(Arrays.asList(11, 13, 14), pinks);
        
        List<Property> oranges = new ArrayList<>();
        setColorGroup(Arrays.asList(16, 18, 19), oranges);
        
        List<Property> reds = new ArrayList<>();
        setColorGroup(Arrays.asList(21, 23, 24), reds);
        
        List<Property> yellows = new ArrayList<>();
        setColorGroup(Arrays.asList(26, 27, 29), yellows);
        
        List<Property> greens = new ArrayList<>();
        setColorGroup(Arrays.asList(31, 32, 34), greens);
        
        List<Property> dBlues = new ArrayList<>();
        setColorGroup(Arrays.asList(36, 38), dBlues);
    }

    // Absolute move (move player x to position y where position 0 is go 1 is next etc 40 is "in jail")
    public void movePlayerToPosition(Player player, int position) {
        // Get the current bounds of the image within the ImageView

        Bounds imageBounds = imageView.getBoundsInParent();
        double imageX = imageBounds.getMinX();
        double imageY = imageBounds.getMinY();
        double imageWidth = imageBounds.getWidth();
        double imageHeight = imageBounds.getHeight();
        // Calculate scaling factors
        double originalWidth = 1080; // Original width of the board image
        double originalHeight = 1080; // Original height of the board image
        double scaleX = imageWidth / originalWidth;
        double scaleY = imageHeight / originalHeight;
        // Calculate token position relative to the image
        Circle token = player.getTokenc();
        double tokenX = imageX + getScreenLocation(position)[0] * scaleX;
        double tokenY = imageY + getScreenLocation(position)[1] * scaleY;
        // Set token position
        token.setLayoutX(tokenX);
        token.setLayoutY(tokenY);
        player.setPosition(position);
    }




    // relative move (move player x, y spaces)
    public void relativeMove(Player player, int spaces) {
        int newPosition = (player.getPosition() + spaces) % 40;
        movePlayerToPosition(player, newPosition);
    }

    // helper function that calculates the screen position
    public double[] getScreenLocation(int position) {
        if (position % 10 == 0) {
            return switch (position) {
                case 0 -> new double[]{60, 1010};
                case 10 -> new double[]{25, 70};
                case 20 -> new double[]{1005, 70};
                case 30 -> new double[]{1005, 1010};
                case 40 -> new double[]{70, 80};
                default -> {
                    System.out.println("bad corner position");
                    yield new double[]{500, 500};
                }
            };
        } else return switch ((int) Math.floor(position / 10)) {
            case 0 -> new double[]{100, 905 - 87.5 * (position - 1)};
            case 1 -> new double[]{(((position % 10) - 1) * 87.5) + 185, 120};
            case 2 -> new double[]{970, (((position % 10) - 1) * 87.5) + 210};
            case 3 -> new double[]{880 - (((position % 10) - 1) * 87.5), 990};
            default -> {
                System.out.println("bad edge position");
                yield new double[]{500, 500};
            }
        };
    }

    public class PositionFinder {
        private static final double EDGE_OFFSET = 87.5;
        private static final double CORNER_RADIUS = 50; // Pixel radius for corner detection

        public static int getClosestPosition(double x, double y) {
            // First check corners with proximity radius
            if (isInCorner(x, y, 60, 1010)) return 0;
            if (isInCorner(x, y, 55, 60)) return 10;
            if (isInCorner(x, y, 1005, 70)) return 20;
            if (isInCorner(x, y, 1005, 1010)) return 30;
            // Then check edges
            return getEdgePosition(x, y);
        }

        private static boolean isInCorner(double x, double y, double cornerX, double cornerY) {
            return Math.abs(x - cornerX) < CORNER_RADIUS &&
                    Math.abs(y - cornerY) < CORNER_RADIUS;
        }

        private static int getEdgePosition(double x, double y) {
            // Left edge (positions 1-9)
            if (x < 150) return (int) Math.round((1010 - y - 120) / EDGE_OFFSET) + 1;

            // Top edge (positions 11-19)
            if (y < 150) return (int) Math.round((x - 185) / EDGE_OFFSET) + 11;

            // Right edge (positions 21-29)
            if (x > 900) return (int) Math.round((y - 200) / EDGE_OFFSET) + 21;

            // Bottom edge (positions 31-39)
            if (y > 930) return (int) Math.round((880 - x) / EDGE_OFFSET) + 31;
            // middle
            return 40;
        }
    }
}
