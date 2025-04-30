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
import java.util.Random;
import fxml.PopupManager;

public class Controller {

    private Board board;

    @FXML private Pane boardPane;
    @FXML private ImageView imageView;
    @FXML private TextField relativeIn;
    @FXML private TextField absoluteIn;
    @FXML private TextField playerIn;
    @FXML private Button saveGameButton;
    @FXML private Button loadGameButton;
    @FXML private TableView playersTable;
    @FXML private TableColumn<Player, Circle> iconColumn;
    @FXML private TableColumn<Player, String> nameColumn;
    @FXML private TableColumn<Player, Integer> moneyColumn;
    @FXML private TableColumn<Player, Boolean> itemsColumn;
    @FXML private Text clickedOnText;
	
	@FXML private Button roll;
    @FXML private Button endTurn;
    @FXML private Text rolledText;

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
        boardPane.getChildren().retainAll(imageView);
        playersTable.setFixedCellSize(25);

        ArrayList<Player> currentPlayers = new ArrayList<>(board.getPlayers());
        for (Player player : currentPlayers) {
            board.removePlayer(player);
        }

        playersTable.getItems().clear();

        for (Player player : players) {
            board.addPlayer(player);
            boardPane.getChildren().add(player.getTokenc());
            playersTable.getItems().add(player);
            board.movePlayerToPosition(player, player.getPosition());
        }

        int headerHeight = 50;
        playersTable.setMaxHeight(playersTable.getItems().size() * playersTable.getFixedCellSize() + headerHeight);
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

    // it might make sense to move some/all of roll() and endTurn() to board

    @FXML
    private void roll() {
        // checks if current player can roll dice if so rolls 2 random numbers
        // moves them there and updates the text and makes a popup
        //  to-do: call land $200 for go on maybe move to board
        int state = 0;      // type of roll normal, dubs etc
        Player currentPlayer = board.getPlayers().getFirst();
        if (currentPlayer.getRolls() > 0){
            Random random = new Random();
            int die1 = random.nextInt(6 - 1 + 1) + 1;
            int die2 = random.nextInt(6 - 1 + 1) + 1;
            if (die1 == die2){
                // get out of jail state 4 ?
                state = 1;
                if (currentPlayer.getDubs() == 2){
                    // 3rd time rolling dubs
                    currentPlayer.setDubs(0);
                    PopupManager.showPopup("You rolled 3 doubles in a row go to jail." );
                    return;
                }
                currentPlayer.setDubs(currentPlayer.getDubs()+1);
            }
            updateRoll(die1, die2, state);
            board.relativeMove(board.getPlayers().getFirst(),die1+die2);
            currentPlayer.setRolls(currentPlayer.getRolls()-(1-state));
            PopupManager.showPopup("You landed on " + currentPlayer.getPosition());
        }else{
            updateRoll(0, 0, 2);
        }
    }

    @FXML
    private void endTurn() {
        // passes play to next player
        //  to-do: countdown jail sentence maybe move to board
        ArrayList<Player> list = board.getPlayers();
        Player currentPlayer = list.removeFirst();
        currentPlayer.setRolls(currentPlayer.getRolls()+1);
        list.add(currentPlayer);
        currentPlayer.setDubs(0);
        // countdown jail sentence
    }

    @FXML
    private void updateRoll(int die1, int die2,int state) {
        int roll = die1 + die2;
        switch (state){
            case 0:// normal
                rolledText.setText("You Rolled a " + die1 + " and a " + die2 + ", " +
                        roll + " total.");
                return;
            case 1:// doubles
                rolledText.setText("You Rolled a " + die1 + " and a " + die2 + ", " +
                        roll + " total. " + "\nThat's doubles so you can roll again.");
                return;
            case 2:// no more
                rolledText.setText("Can't roll no more rolls left. End turn for more.");
        }
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File defaultDirectory = new File("saves");
        if (defaultDirectory.exists()) {
            fileChooser.setInitialDirectory(defaultDirectory);
        } else {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        File selectedFile = fileChooser.showSaveDialog(saveGameButton.getScene().getWindow());
        if (selectedFile != null) {
            String chosenPath = selectedFile.getAbsolutePath();
            if (!chosenPath.toLowerCase().endsWith(".xml")) {
                chosenPath += ".xml";
            }
            System.out.println("Saving game to: " + chosenPath);
            StateManager.saveGame(board, chosenPath);
        } else {
            System.out.println("Save cancelled.");
        }
    }

    @FXML
    private void loadGame() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Game File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML Files", "*.xml"));

        File defaultDirectory = new File("saves");
        if (defaultDirectory.exists()) {
            fileChooser.setInitialDirectory(defaultDirectory);
        } else {
            // Fallback to home dir if saves doesn't exist
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        }

        File selectedFile = fileChooser.showOpenDialog(loadGameButton.getScene().getWindow());
        if (selectedFile != null) {
            System.out.println("Loading game from: " + selectedFile.getAbsolutePath());
            ArrayList<Player> players = StateManager.loadGame(selectedFile.getAbsolutePath());
            this.board = new Board(imageView);
            initializePlayers(players);
            updateTokenPositions();
        } else {
            System.out.println("File selection cancelled.");
        }
    }
}
