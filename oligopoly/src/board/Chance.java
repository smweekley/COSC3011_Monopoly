/*
package board;

import java.util.Random;
import java.util.Set;
import java.util.Deque;
import java.util.HashSet;
import java.util.ArrayDeque;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import player.Player;

the calls used for player in chance are .changePosition(int),
.getHouse() to get the number of houses that player controls,
getHotel() to get the number of hotels the player controls,
changeMoney(int) will send a negative or positive number to be added to the players money,
getPosition() to get the players current position,
jailFreeCard() to give the player a get out of jail free card
toJail() to send them to jail
the only work that needs done is checking the correct position number to the right property position
and get the pictures called when the player lands on the space

public class Chance {
    int[] cards = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
    int cardDrawn = 0;
    Random cardGenerator = new Random();
    Deque<Integer> chestDeck = new ArrayDeque<>();
    public CommunityChest() {
        if (chestDeck.isempty()){ //the shuffle function if the deck is empty
            Set<Integer> drawnCards = new HashSet<>();
            while(drawnCards.size() < 15) {
                int nextCard = cardGenerator.nextInt(15) + 1;
                if(!drawnCards.contains(nextCard)) {
                    drawnCards.add(nextCard);
                    chestDeck.addFirst(nextCard);
                }
            }
        }
    }

    public void drawCard(Player current) {
        if (!chestDeck.isEmpty()) {
            cardDrawn = chestDeck.pop();
            callCardEffect(cardDrawn, current);
        } else {
            communityChest();
        }
    }

    private void callCardEffect(int card, Player current) {
        switch (card) {
            case 1:
                advanceToGo(current);
                break;
            case 2:
                advanceStCharles(current);
                break;
            case 3:
                repairs(current);
                break;
            case 4:
                advanceBoardWalk(current);
                break;
            case 5:
                advanceRailroad(current);
                break;
            case 6:
                advanceIllinois(current);
                break;
            case 7:
                advanceUtility(current);
                break;
            case 8:
                jailFree(current);
                break;
            case 9:
                toJail(current);
                break;
            case 10:
                advanceReading(current);
                break;
            case 11:
                buildingLoan(current);
                break;
            case 12:
                bankDividend(current);
                break;
            case 13:
                goBack(current);
                break;
            case 14:
                electedChairman(current);
                break;
            case 15:
                poorTax(current);
                break;
        }
    }

    private void advanceToGo(Player current) {
        current.changePosition(0);
        image(primaryStage, "File:src/board/chance/advanceToGo.jpg");
    }

    private void advanceStCharles(Player current) {
        current.changePosition(10);
        image(primaryStage, "File:src/board/chance/advanceStCharles.jpg");
    }

    private void repairs(Player current) {
        int total = 0;
        total -= (current.getHouse() * 25);
        total -= (current.getHotel() * 100);
        current.changeMoney(total);
        image(primaryStage, "File:src/board/chance/repairs.jpg");
    }

    private void advanceBoardWalk(Player current) {
        current.changePosition(37);
        image(primaryStage, "File:src/board/chance/advanceBoardWalk.jpg");
    }

    private void advanceRailroad(Player current) {
        if (current.getPosition() < 5 || current.getPosition() > 36)
            current.changePosition(5);
        else if (current.getPosition() < 15)
            current.changePosition(15);
        else if (current.getPosition() < 25)
            current.changePosition(25);
        else if (current.getPosition() < 35)
            current.changePosition(35);
        image(primaryStage, "File:src/board/chance/advanceRailroad.jpg");
    }

    private void advanceIllinois(Player current) {
        current.changePosition(17);
        image(primaryStage, "File:src/board/chance/advanceIllinois.jpg");
    }

    private void advanceUtility(Player current) {
        if (current.getPosition() < 18 || current.getPosition() < 24)
            current.changePosition(18);
        else if (current.getPosition() < 23)
            current.changePosition(23);
        image(primaryStage, "File:src/board/chance/advanceUtility.jpg");
    }

    private void jailFree(Player current) {
        current.jailFreeCard(true);
        image(primaryStage, "File:src/board/chance/jailFree.jpg");
    }

    private void toJail(Player current) {
        current.toJail();
        image(primaryStage, "File:src/board/chance/toJail.jpg");
    }

    private void advanceReading(Player current) {
        current.changePosition(15);
        image(primaryStage, "File:src/board/chance/advanceReading.jpg");
    }

    private void buildingLoan(Player current) {
        current.reduceMoney(150);
        image(primaryStage, "File:src/board/chance/buildingLoan.jpg");
    }

    private void bankDividend(Player current) {
        current.addMoney(50);
        image(primaryStage, "File:src/board/chance/bankDividend.jpg");
    }

    private void goBack(Player current) {
        int position;
        position = current.getPosition() - 3;
        current.changePosition(position);
        image(primaryStage, "File:src/board/chance/goBack.jpg");
    }

    private void electedChairman(Player current) {
        for (int i = 0; i < board.players.length(); i++) {
            current[i].changeMoney(50);
        }
        image(primaryStage, "File:src/board/chance/electedChairman.jpg");
    }

    private void poorTax(Player current) {
        current.changeMoney(-15);
        image(primaryStage, "File:src/board/chance/poorTax.jpg");
    }

    private image (stage primaryStage, String imagePath) {

        Image cardPic = new Image(imagePath);
        ImageView showImage = new ImageView(cardPic);

        Button closeButton = new Button("ok");
        closeButton.setOnAction(event -> primaryStage.close());

        StackPane layout = new StackPane();
        layout.getChrildren().addAll(imageView, closeButton);

        StackPane.setAlignment(closeButton, javafx.geometry.pos.BOTTOM_CENTER);

        Scene windowSize = new Scene(layout, 400, 300);

        primaryStage.setTitle("Chance Card Drawn");
        primaryStage.setScene(windowSize);

        primaryStage.show();
    }
}

*/
