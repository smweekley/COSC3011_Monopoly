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
import state.StateManager;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;

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

    @FXML
    public void initialize() {
        //System.out.println("Initializing board...");
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

        //Auto scale
        ChangeListener<Number> resizeListener = (obs, oldVal, newVal)
                -> updateTokenPositions();
        boardPane.widthProperty().addListener(resizeListener);
        boardPane.heightProperty().addListener(resizeListener);
    }

    public void initializePlayers(ArrayList<Player> players) {
        //System.out.println("Initializing players...");
        for (Player player : players) {
            boardPane.getChildren().add(player.getTokenc());
            board.addPlayer(player);
            playersTable.getItems().add(player);
            playersTable.setMaxHeight(playersTable.getMaxHeight() + 25);
        }
        for (Player player : board.getPlayers()) {
            board.movePlayerToPosition(player, 0); // Initial position
            System.out.println(player.getPosition());
        }
        updateTokenPositions();
    }

    private void updateTokenPositions() {
        for (Player p : board.getPlayers()) {
            board.movePlayerToPosition(p, p.getPosition());
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
        StateManager.saveGame(this.board, "test1.csv");
    }

    @FXML
    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Serialized Files", "*.ser"));

        File defaultDirectory = new File("saves");
        if (defaultDirectory.exists()) {
            fileChooser.setInitialDirectory(defaultDirectory);
        } else {
            // Fallback to home dir if saves doesn't exist
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        File selectedFile = fileChooser.showOpenDialog(loadGame.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Loading game from: " + selectedFile.getAbsolutePath());
            Board newBoard = StateManager.loadGame(selectedFile.getAbsolutePath());
            this.board = newBoard;
            updateTokenPositions();
        } else {
            System.out.println("File selection cancelled.");
        }
    }
}
