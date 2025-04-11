/*

package board

import java.util.Random;

// the class takes the current player draws a card then acts on that card with one of 3 calls to player changeMoney(int), changePosition(int), inJail(bool)
// the deck will auto shuffle itself when its empty and will send a positive or negative int to changeMoney to change the money the player has
// changePosition will be sent 0 if the player is sent to go (does not add the 200$ as I assume that the check for go will do that)
// inJail is sent true if the player is sent to jail
// The call to use community chest is drawCard(player)
// final note I wasnt sure what folder to put this in so I just put it in the main area.

public class CommunityChest implements Board {
  
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

  public void drawCard(player current) {
    if(!chestDeck.isEmpty()) {
      cardDrawn = chestDeck.pop();
      callCardEffect(cardDrawn, current);
    }
    else {
      communityChest();
    }
  }

private void callCardEffect(int card,player current) {
    switch (card) {
      case 1; 
        assessed(current);
        break;
      case 2;
        advanceToGo(current);
        break;
      case 3:
        eachPlayerCollect(current);
        break;
      case 4:
        xMasFund(current);
        break;
      case 5:
        taxRefund(current);
        break;
      case 6:
        beautyContest(current);
        break;
      case 7:
        schoolTax(current);
        break;
      case 8:
        stockSale(current);
        break;
      case 9:
        bankError(current);
        break;
      case 10:
        doctor(current);
        break;
      case 11:
        lifeInsurance(current);
        break;
      case 12:
        hospitalBill(current);
        break;
      case 13:
        marriage(current);
        break;
      case 14:
        jail(current);
        break;
      case 15:
        inherit(current);
        break;
    }
  }

private void assest(player current) {
  int moneyDeducted = 0;
  moneyDeducted -= (current.houseCount() * 40);
  moneyDeducted -= (current.hotelCount() * 115);
  current.changeMoney(moneyDeducted);
  image(primaryStage, ""File:src/board/communityChest/assest.jpg");
}

private void advanceToGo(player current) {
  current.changePosition(0);
  image(primaryStage, ""File:src/board/communityChest/advanceToGo.jpg");
}

private void eachPlayerCollect(player current) {
  int collected = 0;
  for (int i = 0; i < numberPlayers; i++) {
    collected += 50;
    numberPlayers[i].changeMoney(-50);
  }
  current.changeMoney(collected);
  image(primaryStage, ""File:src/board/communityChest/eachPlayerCollect.jpg");
}

private void xMasFund(player current) {
  current.changeMoney(100);
  image(primaryStage, ""File:src/board/communityChest/xMasFund.jpg");
}

private void taxRefund(player current) {
  current.changeMoney(20);
  image(primaryStage, ""File:src/board/communityChest/taxRefund.jpg");
}

private void schoolTax(player current) {
  current.changeMoney(-150);
  image(primaryStage, ""File:src/board/communityChest/schoolTax.jpg");
}

private void stockSale(player current) {
  current.changeMoney(45);
  image(primaryStage, ""File:src/board/communityChest/stockSale.jpg");
}

private void bankError(player current) {
  current.changeMoney(200);
  image(primaryStage, ""File:src/board/communityChest/bankError.jpg");
}

private void doctor(player current) {
  current.changeMoney(-50);
  image(primaryStage, ""File:src/board/communityChest/doctor.jpg");
}

private void lifeInsurace(player current) {
  current.changeMoney(100);
  image(primaryStage, ""File:src/board/communityChest/lifeInsurance.jpg");
}

private void hospitalBill(player current) {
  current.changeMoney(-100);
  image(primaryStage, ""File:src/board/communityChest/hospitalBill.jpg");
}

private void marrage(player current) {
  current.changeMoney(25);
  image(primaryStage, ""File:src/board/communityChest/marrage.jpg");
}

private void jail(player current) {
  current.inJail(true);
  image(primaryStage, ""File:src/board/communityChest/jail.jpg");
}

private void inherit(player current) {
  current.changeMoney(100);
  image(primaryStage, ""File:src/board/communityChest/inherit.jpg");
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
*/
