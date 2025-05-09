package board;

import java.util.*;
import javafx.scene.image.Image;
import fxml.PopupManager;
import player.Player;

public class CommunityChest {
    private final Random cardGenerator = new Random();
    private final Deque<Integer> chestDeck = new ArrayDeque<>();
    private int cardDrawn = 0;
    private static List<Player> allPlayers; // Static reference to all players

    // Interface for callbacks
    public interface CardActionCallback {
        void onCardActionComplete(Player player);
    }

    // Static method to set all players (call during game initialization)
    public static void setAllPlayers(List<Player> players) {
        allPlayers = new ArrayList<>(players);
    }

    public CommunityChest() {
        shuffleDeck();
    }

    private void shuffleDeck() {
        chestDeck.clear();
        Set<Integer> drawn = new HashSet<>();
        while (drawn.size() < 15) {
            int next = cardGenerator.nextInt(15) + 1;
            if (drawn.add(next)) {
                chestDeck.addFirst(next);
            }
        }
    }

    public void drawCard(Player current, CardActionCallback callback) {
        if (chestDeck.isEmpty()) {
            shuffleDeck();
        }
        cardDrawn = chestDeck.pop();
        
        // Show card image first
        String imagePath = getCardImagePath(cardDrawn);
        Image cardImage = new Image("File:" + imagePath);
        
        PopupManager.showPopup(cardImage);
        
        // Apply card effect
        applyCardEffect(cardDrawn, current);
        
        // Callback when complete
        if (callback != null) {
            callback.onCardActionComplete(current);
        }
    }
    
    public int getLastCardDrawn() {
        return cardDrawn;
    }
    
    private String getCardImagePath(int cardId) {
        return "src/board/communityChest/" + getCardImageName(cardId);
    }
    
    private String getCardImageName(int cardId) {
        return switch (cardId) {
            case 1 -> "assessed.jpg";
            case 2 -> "advanceToGo.jpg";
            case 3 -> "eachPlayerCollect.jpg";
            case 4 -> "xMasFund.jpg";
            case 5 -> "taxRefund.jpg";
            case 6 -> "beautyContest.jpg";
            case 7 -> "schoolTax.jpg";
            case 8 -> "stockSale.jpg";
            case 9 -> "bankError.jpg";
            case 10 -> "doctor.jpg";
            case 11 -> "lifeInsurance.jpg";
            case 12 -> "hospitalBill.jpg";
            case 13 -> "marriage.jpg";
            case 14 -> "jail.jpg";
            case 15 -> "inherit.jpg";
            default -> "default.jpg";
        };
    }

    private void applyCardEffect(int card, Player current) {
        switch (card) {
            case 1 -> assessed(current);
            case 2 -> advanceToGo(current);
            case 3 -> eachPlayerCollect(current);
            case 4 -> xMasFund(current);
            case 5 -> taxRefund(current);
            case 6 -> beautyContest(current);
            case 7 -> schoolTax(current);
            case 8 -> stockSale(current);
            case 9 -> bankError(current);
            case 10 -> doctor(current);
            case 11 -> lifeInsurance(current);
            case 12 -> hospitalBill(current);
            case 13 -> marriage(current);
            case 14 -> jail(current);
            case 15 -> inherit(current);
        }
    }

    // Card effect methods - no UI handling, just player state changes
    private void assessed(Player current) {
        int amount = -(current.houseCount() * 40 + current.hotelCount() * 115);
        current.changeMoney(amount);
    }

    private void advanceToGo(Player current) {
        current.setPosition(0);
    }

    private void eachPlayerCollect(Player current) {
        if (allPlayers != null) {
            int collected = 0;
            for (Player p : allPlayers) {
                if (p != current) {
                    p.changeMoney(-50);
                    collected += 50;
                }
            }
            current.changeMoney(collected);
        }
    }

    private void xMasFund(Player current) {
        current.changeMoney(100);
    }

    private void taxRefund(Player current) {
        current.changeMoney(20);
    }

    private void beautyContest(Player current) {
        current.changeMoney(10);
    }

    private void schoolTax(Player current) {
        current.changeMoney(-150);
    }

    private void stockSale(Player current) {
        current.changeMoney(45);
    }

    private void bankError(Player current) {
        current.changeMoney(200);
    }

    private void doctor(Player current) {
        current.changeMoney(-50);
    }

    private void lifeInsurance(Player current) {
        current.changeMoney(100);
    }

    private void hospitalBill(Player current) {
        current.changeMoney(-100);
    }

    private void marriage(Player current) {
        current.changeMoney(25);
    }

    private void jail(Player current) {
        current.goToJail(3);
    }

    private void inherit(Player current) {
        current.changeMoney(100);
    }
}