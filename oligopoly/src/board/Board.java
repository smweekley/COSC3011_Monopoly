package board;


import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import player.Player;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import java.util.ArrayList;
import tile.Tile;


public class Board {

    private final ImageView imageView;

    private ArrayList<Player> players;       // List of players

    private ArrayList<Tile> tiles;       // List of tiles

    public Board(ImageView imageView) {
        this.imageView = imageView;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public void addPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void removePlayer(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    //will need updating
    //also Controller.java seems to do some of this already    (line 43-47)
    //however in the future when we have gui for players selecting their icons
    //and theyre iteratively added, i think making a list of those players as theyre added
    //then sending to the new board object via initBoard (referencing Miro) could be useful
    //public void initBoard(ArrayList<Player> initialPlayers) {
    //    for(Player player : initPlayers) { 
    //        addPlayer(player);
    //    }
    /*  commented because some of this is not implemented
        Tile go = new Go();
        Tile chance = new chanceTile(); 
        Tile chest = new communityChestTile(); //reuse same references to these types of tile
        Tile incomeTax = new Tax(50); //no Tax subclass exists, so this is placeholder
        Tile jail = new Jail();
        Tile A1 = new Property("A1" , 80 , 50);
        Tile A2 = new Property("A2" , 120 , 50);
        Tile B1 = new Property("B1" , 150, 50);
        Tile B2 = new Property("B2" , 150, 50);
        Tile B3 = new Property("B3" , 180, 50);
        Tile X1 = new Railroad("X1" , 200);
        tiles[0]  = go;
        tiles[1]  = A1;
        tiles[2]  = chest;
        tiles[3]  = A2;
        tiles[4]  = incomeTax;
        tiles[5]  = X1
        tiles[6]  = B1;
        tiles[7]  = chance;
        tiles[8]  = B2;
        tiles[9]  = B3;
        tiles[10] = jail;
        //etc
    }
    */

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

    // relitve move (move player x, y spaces)
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
            if (y > 900) return (int) Math.round((880 - x) / EDGE_OFFSET) + 31;
            // middle
            return 40;
        }
    }
}
