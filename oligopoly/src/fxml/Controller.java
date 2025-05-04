package fxml;

import board.Board;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import player.Player;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import state.StateManager;
import javafx.stage.FileChooser;
import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import tile.Property;
import tile.Railroad;
import tile.Tile;
import tile.Utility;

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

    @FXML private ImageView clickImg;
    @FXML private Button mortgage;
    @FXML private Button upgrade;
    @FXML private Text clickedAddress;
    @FXML private Text clickedSet;
    @FXML private Text clickedOwner;
    @FXML private Text clickedOwnersOthers;
    @FXML private Text clickedRent;
    @FXML private Text clickedUpgrade;
    @FXML private Text clickedUpgradeRent;
    @FXML private Text clickedMortgaged;
    @FXML private Text clickedMortgageCost;
    @FXML private Text clickedMortgageValue;

    @FXML private ImageView landedImg;
    @FXML private Button landedInteract;
    @FXML private Button landedUpgradeBtn;
    @FXML private Text landedAddress;
    @FXML private Text landedSet;
    @FXML private Text landedOwner;
    @FXML private Text landedOwnersOthers;
    @FXML private Text landedRent;
    @FXML private Text landedUpgrade;
    @FXML private Text landedUpgradeRent;
    @FXML private Text landedMortgaged;
    @FXML private Text landedMortgageCost;
    @FXML private Text landedMortgageValue;

    private int displayedTile;


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
                -> updateIconPositions();
        boardPane.widthProperty().addListener(resizeListener);
        boardPane.heightProperty().addListener(resizeListener);
        clickDisplayTile(40);
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

    private void updateIconPositions() {
        for (Player p : board.getPlayers()) {
            board.movePlayerToPosition(p, p.getPosition());
            int position = board.getPlayers().getFirst().getPosition();
            updateLandedInfo(position);
        }
        for (Tile p : board.getTiles()){
            displayHouses(p);
        }
    }

    public void displayHouses(Tile tile){
        if(tile instanceof Property ){
            int location = ((Property) tile).getIndex();
            int houses = ((Property) tile).getHouseCount();
            Label label;
            if(houses > 0) {
                if(((Property) tile).getHouseLabel() == null){
                    label = new Label(String.valueOf(houses));
                    label.setFont(new Font(30));
                    boardPane.getChildren().add(label);
                    ((Property) tile).setHouseLabel(label);
                    moveHouseNum(label,location);
                }else{
                    label = ((Property) tile).getHouseLabel();
                    moveHouseNum(label,location);
                }
            }
        }
    }

    public void moveHouseNum(Label label, int location){
        double[] scaleFactor = scaleFactor(imageView);

        double imageX = imageView.getBoundsInParent().getMinX();
        double imageY = imageView.getBoundsInParent().getMinY();

        double tokenX = imageX + board.getScreenLocation(location)[0] / scaleFactor[0];
        double tokenY = imageY + board.getScreenLocation(location)[1] / scaleFactor[1];

        if(location < 10){
            tokenX = tokenX - (80 / scaleFactor[0]);
            tokenY = tokenY - (50 / scaleFactor[1]);
        }else if (location < 20){
            tokenX = tokenX - (15 / scaleFactor[0]);
            tokenY = tokenY - (110 / scaleFactor[1]);
        }else if (location < 30){
            tokenX = tokenX + (60 / scaleFactor[0]);
            tokenY = tokenY - (50 / scaleFactor[1]);
        }else{
            tokenX = tokenX - (10 / scaleFactor[0]);
            tokenY = tokenY + (20 / scaleFactor[1]);
        }

        label.setLayoutX(tokenX);
        label.setLayoutY(tokenY);
    }

    public static double[] scaleFactor(ImageView imageView){
        double imageViewWidth = imageView.getBoundsInLocal().getWidth();
        double imageViewHeight = imageView.getBoundsInLocal().getHeight();
        double originalWidth = 1080;
        double originalHeight = 1080;
        double scaleX = originalWidth / imageViewWidth;
        double scaleY = originalHeight / imageViewHeight;
        return new double[]{scaleX, scaleY};
    }



    @FXML
    private void handleImageClick(MouseEvent event) {
        // handle click on board
        double[] scaleFactor = scaleFactor(imageView);
        double scaledX = event.getX() * scaleFactor[0];
        double scaledY = event.getY() * scaleFactor[1];
        int position = Board.PositionFinder.getClosestPosition(scaledX, scaledY);
        //update display
        clickDisplayTile(position);
        displayedTile = position;
    }

    @FXML
    private void handleLandedInteract() {
        Player player = board.getPlayers().getFirst();
        Tile tile = board.getTile(player.getPosition());

        // handle landed tile interaction button  to-do:enable when working
        String function = landedInteract.getText();
        switch (function) {
            case "Mortgage" -> {
                if (tile instanceof Property) {
                    //((Property) tile).mortgage(player)
                } else if (tile instanceof Railroad) {
                    //((Railroad) tile).mortgage(player)
                } else if (tile instanceof Utility) {
                    //((Utility) tile).mortgage(player)
                }
            }
            case "Unmortgage" -> {
                if (tile instanceof Property) {
                    //((Property) tile).unmortgage(player)
                } else if (tile instanceof Railroad) {
                    //((Railroad) tile).unmortgage(player)
                } else if (tile instanceof Utility) {
                    //((Utility) tile).unmortgage(player)
                }
            }
            case "Pay Rent" -> {
                //((Property) tile).payRent(player);
            }
            case "Buy" -> {
                //((Property) tile).buyProperty(player);
            }
            case "Draw" -> {
                if (/*tile instanceof Chest*/ true) {
                    //((Chest) tile).drawCard(player)
                } else {
                    //((Chance) tile).drawCard(player)
                }
            }
            case null, default -> PopupManager.showPopup("Invald \" landedInteract \" ");
        }
    }

    @FXML
    private void handleLandedUpgrade() {
        Player player = board.getPlayers().getFirst();
        Tile tile = board.getTile(player.getPosition());
        //tile.buyHouse(player);     //to-do:enable when working
    }

    @FXML
    private void handleMortgage() {
        Player player = board.getPlayers().getFirst();
        Tile tile = board.getTile(displayedTile);
        if(Objects.equals(mortgage.getText(), "Mortgage")) {
            //tile.mortgage(player);     //to-do:enable when working
        }else{
            //tile.unMortgage(player);    //to-do:enable when working
        }
    }

    @FXML
    private void handleUpgrade() {
        Player player = board.getPlayers().getFirst();
        Tile tile = board.getTile(displayedTile);
        //tile.buyHouse(player);     //to-do:enable when working
    }

    private void clickDisplayTile(int position){
        Tile tile = board.getTile(position);
        ArrayList<String> input = tile.getTileInfo();
        //update display
        if(input.size() >= 5) {
            if (Objects.equals(input.get(4), "This property is mortgaged.")) {
                mortgage.setText("Unmortgage");
            } else {
                mortgage.setText("Mortgage");
            }
        }

        if(Objects.equals(input.getFirst(), "Property")){

            Tile.Colors[] colors =  Tile.Colors.values();
            for (int i = 0; i < colors.length; i++){
                if (Objects.equals(input.get(5), colors[i].toString())){
                    Image image = new Image("fxml/propImg/" + i + ".png");
                    clickImg.setImage(image);
                }
            }
            clickedAddress.setText("Name: " + input.get(1));
            clickedSet.setText("Set: " + input.get(1).charAt(0));
            clickedOwner.setText("Owner: " + input.get(2));
            clickedOwnersOthers.setText(input.get(8));                        //need
            clickedRent.setText("Current Rent: $" + input.get(3));
            if(((Property) tile).isOwned()){
                clickedUpgrade.setText("Upgrade Cost: $" + input.get(4));
            }else{
                clickedUpgrade.setText("Buy Cost: $" + input.get(4));
            }
            clickedUpgradeRent.setText("Rent After Upgrade: $"+ input.get(10));
            clickedMortgaged.setText(input.get(6));
            clickedMortgageCost.setText("Costs $" + input.get(9) + " to Unmortgage.");
            clickedMortgageValue.setText("Worth $"+ input.get(7) +" When Mortgaged.");

            if(Objects.equals(input.get(6), "This property is mortgaged.")){
                mortgage.setText("Unmortgage");
            }else{
                mortgage.setText("Mortgage");
            }
            if(((Property) tile).isOwned()){
                Player player = board.getPlayer(0);
                if (((Property) tile).getOwner() == player) {
                    mortgage.setVisible(true);
                    upgrade.setVisible(true);
                } else{
                    mortgage.setVisible(false);
                    upgrade.setVisible(false);
                }
            }




        }else if(Objects.equals(input.getFirst(), "Railroad")){

            Image image = new Image("/fxml/propImg/" + 11 + ".png");
            clickImg.setImage(image);
            clickedAddress.setText("Name: " + input.get(1));
            clickedSet.setText("Set: Railroad");
            clickedOwner.setText("Owner: " + input.get(2));
            clickedOwnersOthers.setText(input.get(6));                        //not imp yet
            clickedRent.setText("Cost: $" + input.get(3));
            clickedUpgrade.setText("Rent Scales with Ownership");
            clickedUpgradeRent.setText("1RR: $50, 2RR's: $100,\n3RR's: $150, 4RR's: $200");
            clickedMortgaged.setText(input.get(4));
            clickedMortgageCost.setText("Costs " + input.get(5) + " to Unmortgage.");
            clickedMortgageValue.setText("Worth $"+ input.get(7) +" When Mortgaged.");

            if(((Railroad) tile).isOwned()){
                Player player = board.getPlayer(0);
                if (((Railroad) tile).getOwner() == player) {
                    mortgage.setVisible(true);
                    upgrade.setVisible(true);
                } else{
                    mortgage.setVisible(false);
                    upgrade.setVisible(false);
                }
            }

        }else if(Objects.equals(input.getFirst(), "Utility")){

            Image image;
            if(Objects.equals(input.get(1), "U1")){
                image = new Image("/fxml/propImg/" + 10 + ".png");
            }else{
                image = new Image("/fxml/propImg/" + 14 + ".png");
            }
            clickImg.setImage(image);
            clickedAddress.setText("Name: " + input.get(1));
            clickedSet.setText("Set: Utility");
            clickedOwner.setText("Owner: " + input.get(2) + "\n Cost: $" + input.get(3));
            clickedOwnersOthers.setText(input.get(6));                        //not imp yet
            clickedRent.setText("Rent is Based on Roll: ");
            clickedUpgrade.setText("With One Utility 4* the Dice Roll");
            clickedUpgradeRent.setText("With Both Utility 10* the Dice Roll");
            clickedMortgaged.setText(input.get(4));
            clickedMortgageCost.setText("Costs " + input.get(5) + " to Unmortgage.");
            clickedMortgageValue.setText("Worth $"+ input.get(7) +" When Mortgaged.");

            if(((Utility) tile).isOwned()){
                Player player = board.getPlayer(0);
                if (((Utility) tile).getOwner() == player) {
                    mortgage.setVisible(true);
                    upgrade.setVisible(true);
                } else{
                    mortgage.setVisible(false);
                    upgrade.setVisible(false);
                }
            }

        }else if(Objects.equals(input.getFirst(), "Go To Jail")){

            Image image = new Image("/fxml/propImg/" + 15 + ".png");
            clickImg.setImage(image);
            clickedAddress.setText("Name: " + input.get(1));
            clickedSet.setText(input.get(2));
            clickedOwner.setText(input.get(3));
            clickedOwnersOthers.setText(input.get(4));
            clickedRent.setText("");
            clickedUpgrade.setText("");
            clickedUpgradeRent.setText("");
            clickedMortgaged.setText("");
            clickedMortgageCost.setText("");
            clickedMortgageValue.setText("");

            mortgage.setVisible(false);
            upgrade.setVisible(false);

        }else if(Objects.equals(input.getFirst(), "Go")){

            Image image = new Image("/fxml/propImg/" + 8 + ".png");
            clickImg.setImage(image);
            clickedAddress.setText("Name: " + input.get(1));
            clickedSet.setText(input.get(2));
            clickedOwner.setText(input.get(3));
            clickedOwnersOthers.setText("");
            clickedRent.setText("");
            clickedUpgrade.setText("");
            clickedUpgradeRent.setText("");
            clickedMortgaged.setText("");
            clickedMortgageCost.setText("");
            clickedMortgageValue.setText("");

            mortgage.setVisible(false);
            upgrade.setVisible(false);
        }
        else if(Objects.equals(input.getFirst(), "Jail")){

            Image image = new Image("/fxml/propImg/" + 16 + ".png");
            clickImg.setImage(image);
            clickedAddress.setText("Name: " + input.get(1));
            clickedSet.setText(input.get(2));
            clickedOwner.setText(input.get(3));
            clickedOwnersOthers.setText(input.get(4));
            clickedRent.setText(input.get(5));
            clickedUpgrade.setText(input.get(6));
            clickedUpgradeRent.setText("");
            clickedMortgaged.setText("");
            clickedMortgageCost.setText("");
            clickedMortgageValue.setText("");

            mortgage.setVisible(false);
            upgrade.setVisible(false);

        }else     //to-do: update with tax, chest, chance, free parking once working
        {

            Image image = new Image("/fxml/propImg/" + 17 + ".png");
            clickImg.setImage(image);
            clickedAddress.setText("Default Tile");
            clickedSet.setText("Shouldn't be Shown");
            clickedOwner.setText("Or Buy House");
            clickedOwnersOthers.setText("");
            clickedRent.setText("");
            clickedUpgrade.setText("");
            clickedUpgradeRent.setText("");
            clickedMortgaged.setText("");
            clickedMortgageCost.setText("");
            clickedMortgageValue.setText("");

            mortgage.setVisible(false);
            upgrade.setVisible(false);
        }
        if(position == 40){
            Image image = new Image("/fxml/propImg/" + 17 + ".png");
            clickImg.setImage(image);
            clickedAddress.setText("Click a Tile to get Information");
            clickedSet.setText("Or Mortgage and Unmortgage");
            clickedOwner.setText("Or Buy House if you own it");
            clickedOwnersOthers.setText("");
            clickedRent.setText("");
            clickedUpgrade.setText("");
            clickedUpgradeRent.setText("");
            clickedMortgaged.setText("");
            clickedMortgageCost.setText("");
            clickedMortgageValue.setText("");

            mortgage.setVisible(false);
            upgrade.setVisible(false);
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
        updateLandedInfo(board.getPlayer(0).getPosition());
    }

    // it might make sense to move some/all of roll() and endTurn() to board

    @FXML
    private void roll() {
        int state = 0;      // type of roll normal, dubs etc
        Player currentPlayer = board.getPlayers().getFirst();
        if (currentPlayer.getRolls() > 0){
            Random random = new Random();
            int die1 = random.nextInt(6 - 1 + 1) + 1;
            int die2 = random.nextInt(6 - 1 + 1) + 1;
            int total = die1 + die2;
            if(!currentPlayer.isJailed() && currentPlayer.getPosition() + total >= 40){
                PopupManager.showPopup("Passed Go Collect $200");
                currentPlayer.addMoney(200);
            }

            if (die1 == die2){
                if(currentPlayer.isJailed()){
                    currentPlayer.releaseFromJail();
                    PopupManager.showPopup("You rolled doubles get out of jail." );
                }
                state = 1;
                if (currentPlayer.getDubs() == 2){
                    currentPlayer.setDubs(0);
                    PopupManager.showPopup("You rolled 3 doubles in a row go to jail." );
                    return;
                }
                currentPlayer.setDubs(currentPlayer.getDubs()+1);
            }
            updateRoll(die1, die2, state);
            board.relativeMove(board.getPlayers().getFirst(),total);
            currentPlayer.setRolls(currentPlayer.getRolls()-(1-state));
        }else{
            updateRoll(0, 0, 2);
        }
        int position = board.getPlayers().getFirst().getPosition();
        Tile tile = board.getTile(position);
        updateLandedInfo(position);
        tile.landOn(currentPlayer);
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
        if(currentPlayer.jailTime() == 1){
            currentPlayer.releaseFromJail();
        }else {
            currentPlayer.setJailTime(currentPlayer.jailTime() - 1);
        }
        updateLandedInfo(currentPlayer.getPosition());
    }

    @FXML
    private void updateRoll(int die1, int die2,int state) {
        // update roll display
        // to-do2: add images
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

    private void updateLandedInfo(int position ){
        landedInteract.setVisible(false);
        landedUpgradeBtn.setVisible(false);

        ArrayList<String> input = board.getTile(position).getTileInfo();
        Tile tile = board.getTile(position);
        Player player = board.getPlayers().getFirst();

        // update position display
        if(Objects.equals(input.getFirst(), "Property")){
            Property property = (Property) tile;
            Tile.Colors[] colors =  Tile.Colors.values();
            for (int i = 0; i < colors.length; i++){
                if (Objects.equals(input.get(5), colors[i].toString())){
                    Image image = new Image("/fxml/propImg/" + i + ".png");
                    landedImg.setImage(image);
                }
            }
            landedAddress.setText("Name: " + input.get(1));
            landedSet.setText("Set: " + input.get(1).charAt(0));
            landedOwner.setText("Owner: " + input.get(2));
            landedOwnersOthers.setText(input.get(8));                        //not imp yet
            landedRent.setText("Current Rent: $" + input.get(3));
            if(property.isOwned()){
                landedUpgrade.setText("Upgrade Cost: $" + input.get(4));
            }else{
                landedUpgrade.setText("Buy Cost: $" + input.get(4));
            }
            landedUpgradeRent.setText("Rent After Upgrade: $"+ input.get(10));
            landedMortgaged.setText(input.get(6));
            landedMortgageCost.setText("Costs $" + input.get(9) + " to Unmortgage.");
            landedMortgageValue.setText("Worth $"+ input.get(7) +" When Mortgaged.");

            // update and show buttons
            if(property.getOwner() == player){
                if(property.isMortgaged()){
                    landedInteract.setText("Unmortgage");
                }else{
                    landedInteract.setText("Mortgage");
                }
                landedUpgradeBtn.setText("Upgrade");
                landedInteract.setVisible(true);
                landedUpgradeBtn.setVisible(true);
            }else if (property.isOwned()){
                landedInteract.setText("Pay Rent");
                landedInteract.setVisible(true);
            }else{
                landedInteract.setText("Buy");
                landedInteract.setVisible(true);
            }

        }else if(Objects.equals(input.getFirst(), "Railroad")){
            Railroad railroad = ((Railroad)tile);
            Image image = new Image("/fxml/propImg/" + 11 + ".png");
            landedImg.setImage(image);
            landedAddress.setText("Name: " + input.get(1));
            landedSet.setText("Set: Railroad");
            landedOwner.setText("Owner: " + input.get(2));
            landedOwnersOthers.setText(input.get(6));                        //not imp yet
            landedRent.setText("Cost: $" + input.get(3));
            landedUpgrade.setText("Rent Scales with Ownership");
            landedUpgradeRent.setText("1RR: $50, 2RR's: $100,\n3RR's: $150, 4RR's: $200");                         //need
            landedMortgaged.setText(input.get(4));
            landedMortgageCost.setText("Costs $" + input.get(5) + " to Unmortgage.");
            landedMortgageValue.setText("Worth $"+ input.get(7) +" When Mortgaged.");

            if(railroad.getOwner() == player){
                if(railroad.isMortgaged()){
                    landedInteract.setText("Unmortgage");
                    landedInteract.setVisible(true);
                }else{
                    landedInteract.setText("Mortgage");
                    landedInteract.setVisible(true);
                }
            }else if (railroad.isOwned()){
                landedInteract.setText("Pay Rent");
                landedInteract.setVisible(true);
            }else{
                landedInteract.setText("Buy");
                landedInteract.setVisible(true);
            }
        }else if(Objects.equals(input.getFirst(), "Utility")){
            Utility utility = ((Utility)tile);
            Image image;
            if(Objects.equals(input.get(1), "U1")){
                image = new Image("/fxml/propImg/" + 10 + ".png");
            }else{
                image = new Image("/fxml/propImg/" + 14 + ".png");
            }
            landedImg.setImage(image);
            landedAddress.setText("Name: " + input.get(1));
            landedSet.setText("Set: Utility");
            landedOwner.setText("Owner: " + input.get(2) + "\n Cost: $" + input.get(3));
            landedOwnersOthers.setText(input.get(6));
            landedRent.setText("Rent is Based on Roll: " + input.get(3));               // may not work
            landedUpgrade.setText("With One Utility 4* the Dice Roll");
            landedUpgradeRent.setText("With Both Utility 10* the Dice Roll");
            landedMortgaged.setText(input.get(4));
            landedMortgageCost.setText("Costs $" + input.get(5) + " to Unmortgage.");
            landedMortgageValue.setText("Worth $"+ input.get(7) +" When Mortgaged.");

            // update and show buttons
            if(utility.getOwner() == player){
                if(utility.isMortgaged()){
                    landedInteract.setText("Unmortgage");
                    landedInteract.setVisible(true);
                }else{
                    landedInteract.setText("Mortgage");
                    landedInteract.setVisible(true);
                }
            }else if (utility.isOwned()){
                landedInteract.setText("Pay Rent");
                landedInteract.setVisible(true);
            }else{
                landedInteract.setText("Buy");
                landedInteract.setVisible(true);
            }

        }else if(Objects.equals(input.getFirst(), "Go To Jail")){
            Image image = new Image("/fxml/propImg/" + 15 + ".png");
            landedImg.setImage(image);
            landedAddress.setText("Name: " + input.get(1));
            landedSet.setText(input.get(2));
            landedOwner.setText(input.get(3));
            landedOwnersOthers.setText(input.get(4));
            landedRent.setText("");
            landedUpgrade.setText("");
            landedUpgradeRent.setText("");
            landedMortgaged.setText("");
            landedMortgageCost.setText("");
            landedMortgageValue.setText("");

            // update and show buttons
            landedInteract.setText("Go To Jail");
            landedInteract.setVisible(true);

        }else if(Objects.equals(input.getFirst(), "Go")){
            Image image = new Image("/fxml/propImg/" + 8 + ".png");
            landedImg.setImage(image);
            landedAddress.setText("Name: " + input.get(1));
            landedSet.setText(input.get(2));
            landedOwner.setText(input.get(3));
            landedOwnersOthers.setText("");
            landedRent.setText("");
            landedUpgrade.setText("");
            landedUpgradeRent.setText("");
            landedMortgaged.setText("");
            landedMortgageCost.setText("");
            landedMortgageValue.setText("");

        }else if(Objects.equals(input.getFirst(), "Jail")){
            Image image = new Image("/fxml/propImg/" + 16 + ".png");
            landedImg.setImage(image);
            landedAddress.setText("Name: " + input.get(1));
            landedSet.setText(input.get(2));
            landedOwner.setText(input.get(3));
            landedOwnersOthers.setText(input.get(4));
            landedRent.setText(input.get(5));
            landedUpgrade.setText(input.get(6));
            landedUpgradeRent.setText("You Will Be Released in ");
            landedMortgaged.setText(player.jailTime() + "Turns");
            landedMortgageCost.setText("");
            landedMortgageValue.setText("lol you in jail");

        }else  //to-do: update with tax, chest, chance, free parking once working
        {
            Image image = new Image("/fxml/propImg/" + 17 + ".png");
            landedImg.setImage(image);
            landedAddress.setText("Land on a Tile to");
            landedSet.setText("Get Information or Interact");
            landedOwner.setText("");
            landedOwnersOthers.setText("");
            landedRent.setText("");
            landedUpgrade.setText("");
            landedUpgradeRent.setText("");
            landedMortgaged.setText("");
            landedMortgageCost.setText("");
            landedMortgageValue.setText("");
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
            updateIconPositions();
        } else {
            System.out.println("File selection cancelled.");
        }
    }
}
