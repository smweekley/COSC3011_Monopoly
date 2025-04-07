package fxml;

import board.Board;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import player.Player;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import state.GameState;
import state.StateManager;
import javafx.scene.control.cell.PropertyValueFactory;



public class Controller {

    private Board board;

    @FXML private Pane boardPane;
    @FXML private ImageView imageView;
    @FXML private TextField relativeIn;
    @FXML private TextField absoluteIn;
    @FXML private TextField playerIn;
    @FXML private Button saveGame;
    @FXML private Button loadGame;
    @FXML private TableView playersTable;
    @FXML private TableColumn<Player, Circle> iconColumn;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, Integer> moneyColumn;
    @FXML private TableColumn<Player, Boolean> itemsColumn;
    @FXML private Text clickedOnText;


    public void initialize() {

        // Set up scaling
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.fitWidthProperty().bind(boardPane.widthProperty());
        imageView.fitHeightProperty().bind(boardPane.heightProperty());
        boardPane.prefWidthProperty().bind(boardPane.heightProperty());

        //make the board
        this.board = new Board(imageView);

        // Configure cell value factories
        iconColumn.setCellValueFactory(cellData -> cellData.getValue().iconProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        moneyColumn.setCellValueFactory(cellData -> cellData.getValue().moneyProperty().asObject());
        itemsColumn.setCellValueFactory(cellData -> cellData.getValue().hasItemsProperty());

        // Render Circle in the "Token" column // will need image view once we have player token images
        iconColumn.setCellFactory(column -> new TableCell<Player, Circle>() {
            @Override
            protected void updateItem(Circle circle, boolean empty) {
                super.updateItem(circle, empty);
                setGraphic(empty || circle == null ? null : circle);
            }
        });

        // Render "Yes/No" for the boolean "Has Items" column // probably also switch to an image view eventually
        itemsColumn.setCellFactory(column -> new TableCell<Player, Boolean>() {
            @Override
            protected void updateItem(Boolean hasItems, boolean empty) {
                super.updateItem(hasItems, empty);
                if (empty || hasItems == null) {
                    setText(null);
                } else {
                    setText(hasItems ? "Yes" : "No");
                }
            }
        });

        //make the players
        javafx.scene.paint.Color[] colors = {javafx.scene.paint.Color.BLACK,javafx.scene.paint.Color.BLUE,javafx.scene.paint.Color.GREEN};
        int players = 3;    // change to change number of players

        //in the future may be best moved and rewritten when players can select their own icons/import them from file
        //see Board.java line 37
        for (int i = 0; i < players; i++) {
            Player player = new Player(colors[i]);
            boardPane.getChildren().add(player.getTokenc());
            board.addPlayer(player);
            playersTable.getItems().add(player);
            playersTable.setMaxHeight(playersTable.getMaxHeight() + 25);
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
    @FXML
    private void handleImageClick(MouseEvent event) {
        double imageViewWidth = imageView.getBoundsInLocal().getWidth();
        double imageViewHeight = imageView.getBoundsInLocal().getHeight();
        double originalWidth = 1080;
        double originalHeight = 1080;
        double scaleX = originalWidth / imageViewWidth;
        double scaleY = originalHeight / imageViewHeight;
        double scaledX = event.getX() * scaleX;
        double scaledY = event.getY() * scaleY;
        int position = Board.PositionFinder.getClosestPosition(scaledX, scaledY);
        // edit text
        clickedOnText.setText("Selected Tile: " + position);
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

    // Save and load game buttons
    @FXML
    private void saveGame() {
        System.out.println("Saving game...");
        GameState state = new GameState(board);
        StateManager.saveGame(state, "test1.ser"); // Use a file extension
    }
    @FXML
    private void loadGame() {
        System.out.println("Loading game...");
        GameState loadedState = StateManager.loadGame("test1.ser");
        if (loadedState != null) {
            board = loadedState.getBoard();
            // Update the UI or game logic with the loaded board state
            System.out.println("Game loaded successfully.");
        } else {
            System.out.println("Failed to load game.");
        }
    }
}
