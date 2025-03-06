package board;


import player.Player;
import javafx.geometry.Bounds;
import javafx.scene.shape.Circle;
import javafx.scene.image.ImageView;
import java.util.ArrayList;


public class Board {

    private final ImageView imageView;

    private final ArrayList<Player> players;       // List of players

    public Board(ImageView imageView) {
        this.imageView = imageView;
        this.players = new ArrayList<>();
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public ArrayList<Player> getPlayers() {
        return players;
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
        player.setCurrentPosition(position);
    }
    // relitve move (move player x, y spaces)
    public void relativeMove(Player player, int spaces){
        int newPosition = (player.getCurrentPosition() + spaces) % 40;
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
        }else return switch ((int) Math.floor(position / 10)) {
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
}
