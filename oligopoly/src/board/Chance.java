package board;

import java.util.Random;
import java.util.Set;
import java.util.Deque;
import java.util.HashSet;
import java.util.ArrayDeque;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;

import javafx.scene.image.Image;
import fxml.PopupManager;
import player.Player;

public class Chance {
    private final Random cardGenerator = new Random();
    private final Deque<Integer> chestDeck = new ArrayDeque<>();
    private int cardDrawn = 0;
    private static List<Player> allPlayers; // Reference to all players in the game

    // Static method to set the player list (call this during game setup)
    public static void setAllPlayers(List<Player> players) {
        allPlayers = new ArrayList<>(players);
    }

    // Card action callback interface
    public interface CardActionCallback {
        void onCardActionComplete(Player player);
    }

    public Chance() {
        shuffleDeck();
    }

    private void shuffleDeck() {
        chestDeck.clear();
        Set<Integer> drawnCards = new HashSet<>();
        while (drawnCards.size() < 15) {
            int nextCard = cardGenerator.nextInt(15) + 1;
            if (drawnCards.add(nextCard)) {
                chestDeck.addFirst(nextCard);
            }
        }
    }

    public void drawCard(Player current, CardActionCallback callback) {
        if (chestDeck.isEmpty()) {
            shuffleDeck();
        }
        cardDrawn = chestDeck.pop();
        
        // First show the card image
        String imagePath = getCardImagePath(cardDrawn);
        Image cardImage = new Image("File:" + imagePath);
        
        PopupManager.showPopup(cardImage);
        
        // Apply the card effect
        applyCardEffect(cardDrawn, current);
        
        // Notify that action is complete
        if (callback != null) {
            callback.onCardActionComplete(current);
        }
    }
    
    public int getLastCardDrawn() {
        return cardDrawn;
    }
    
    private String getCardImagePath(int cardId) {
        return "src/board/chance/" + getCardImageName(cardId);
    }
    
    private String getCardImageName(int cardId) {
        return switch (cardId) {
            case 1 -> "advanceToGo.jpg";
            case 2 -> "advanceStCharles.jpg";
            case 3 -> "repairs.jpg";
            case 4 -> "advanceBoardWalk.jpg";
            case 5 -> "advanceRailroad.jpg";
            case 6 -> "advanceIllinois.jpg";
            case 7 -> "advanceUtility.jpg";
            case 8 -> "jailFree.jpg";
            case 9 -> "toJail.jpg";
            case 10 -> "advanceReading.jpg";
            case 11 -> "buildingLoan.jpg";
            case 12 -> "bankDividend.jpg";
            case 13 -> "goBack.jpg";
            case 14 -> "electedChairman.jpg";
            case 15 -> "poorTax.jpg";
            default -> "default.jpg";
        };
    }

    private void applyCardEffect(int card, Player current) {
        switch (card) {
            case 1 -> advanceToGo(current);
            case 2 -> advanceStCharles(current);
            case 3 -> repairs(current);
            case 4 -> advanceBoardWalk(current);
            case 5 -> advanceRailroad(current);
            case 6 -> advanceIllinois(current);
            case 7 -> advanceUtility(current);
            case 8 -> jailFree(current);
            case 9 -> toJail(current);
            case 10 -> advanceReading(current);
            case 11 -> buildingLoan(current);
            case 12 -> bankDividend(current);
            case 13 -> goBack(current);
            case 14 -> electedChairman(current);
            case 15 -> poorTax(current);
        }
    }

    // Card effect methods
    private void advanceToGo(Player current) {
        current.setPosition(0);
    }

    private void advanceStCharles(Player current) {
        current.setPosition(10);
    }

    private void repairs(Player current) {
        int total = -25 * current.houseCount() - 100 * current.hotelCount();
        current.changeMoney(total);
    }

    private void advanceBoardWalk(Player current) {
        current.setPosition(37);
    }

    private void advanceRailroad(Player current) {
        int pos = current.getPosition();
        if (pos < 5 || pos > 35) current.setPosition(5);
        else if (pos < 15) current.setPosition(15);
        else if (pos < 25) current.setPosition(25);
        else current.setPosition(35);
    }

    private void advanceIllinois(Player current) {
        current.setPosition(17);
    }

    private void advanceUtility(Player current) {
        int pos = current.getPosition();
        if (pos < 12 || pos >= 28) current.setPosition(12);
        else current.setPosition(28);
    }

    private void jailFree(Player current) {
        current.giveJailCard();
    }

    private void toJail(Player current) {
        current.goToJail(3);
    }

    private void advanceReading(Player current) {
        current.setPosition(15);
    }

    private void buildingLoan(Player current) {
        current.changeMoney(-150);
    }

    private void bankDividend(Player current) {
        current.changeMoney(50);
    }

    private void goBack(Player current) {
        current.setPosition(current.getPosition() - 3);
    }

    private void electedChairman(Player current) {
        // Now uses the allPlayers reference to affect all players
        if (allPlayers != null) {
            for (Player p : allPlayers) {
                if (p != current) {
                    current.changeMoney(-50);
                    p.changeMoney(50);
                }
            }
        }
    }

    private void poorTax(Player current) {
        current.changeMoney(-15);
    }
}