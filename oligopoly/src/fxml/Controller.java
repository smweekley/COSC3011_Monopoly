package fxml;

import board.Board;
import player.Player;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.control.TextField;

public class Controller {

    private Board board;

    @FXML
    private Pane boardPane;
    @FXML
    private ImageView imageView;
    @FXML
    private TextField relativeIn;
    @FXML
    private TextField absoluteIn;
    @FXML
    private TextField playerIn;


    public void initialize() {

        // Set up scaling
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(boardPane.widthProperty());
        imageView.fitHeightProperty().bind(boardPane.heightProperty());

        //make the board
        this.board = new Board(imageView);

        //make the players
        javafx.scene.paint.Color[] colors = {javafx.scene.paint.Color.BLACK,javafx.scene.paint.Color.BLUE,javafx.scene.paint.Color.GREEN};
        int players = 3;    // change to change number of players
        //in the future may be best moved and rewritten when players can select their own icons/import them from file
        //see Board.java line 37
        for (int i = 0; i < players; i++) {
            Player player = new Player(colors[i]);
            boardPane.getChildren().add(player.getTokenc());
            board.addPlayer(player);
        }
        for (Player p : board.getPlayers()) {
            board.movePlayerToPosition(p, 0); // Initial position
        }

        //Auto scale
        ChangeListener<Number> resizeListener = (obs, oldVal, newVal)
                -> updateTokenPositions();
        boardPane.widthProperty().addListener(resizeListener);
        boardPane.heightProperty().addListener(resizeListener);
    }

    private void updateTokenPositions() {
        for (Player p : board.getPlayers()) {
            board.movePlayerToPosition(p, p.getCurrentPosition());
        }
    }

    // These are hooked up to the buttons for testing/development
    @FXML
    private void doRelativeMove() {
        int spaces = 0;
        int player = 0;
        try {
            spaces = Integer.parseInt(relativeIn.getText());
            player = Integer.parseInt(playerIn.getText()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("bad relative move");
        }
        board.relativeMove(board.getPlayers().get(player),spaces);
    }
    @FXML
    private void doAbsoluteMove() {
        int spot = 0;
        int player = 0;
        try {
            spot = Integer.parseInt(absoluteIn.getText());
            player = Integer.parseInt(playerIn.getText()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("bad absolute move");
        }
        board.movePlayerToPosition(board.getPlayers().get(player),spot);
    }
}
