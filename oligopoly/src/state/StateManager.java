package state;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import board.Board;
import player.Player;
import tile.Tile;

public class StateManager {

    private static final String SAVE_DIRECTORY = "saves";

    public static void saveGame(Board board, String fileName) {
        try {
            File saveDir = new File(SAVE_DIRECTORY);
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }

            // Create the full file path
            File file = new File(saveDir, fileName);

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            // Make root Board
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("Board");
            doc.appendChild(rootElement);

            // Add players
            for (Player player : board.getPlayers()) {
                Element playerElement = doc.createElement("Player");
                playerElement.setAttribute("name", player.getName());
                playerElement.setAttribute("position", String.valueOf(player.getPosition()));
                playerElement.setAttribute("money", String.valueOf(player.getMoney()));
                playerElement.setAttribute("jailed", String.valueOf(player.isJailed()));
                playerElement.setAttribute("timeInJail", String.valueOf(player.jailTime()));
                playerElement.setAttribute("properties", String.valueOf(player.getPropertiesString()));
                // also need to track cards somehow with plaintext
                rootElement.appendChild(playerElement);
            }

            // Write the content to file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);

            System.out.println("Board state saved to " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Board loadGame(String filePath) {
        return null;
        /*
        Board board = new Board();
        try {
            File file = new File(filePath);

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();

            NodeList playerList = doc.getElementsByTagName("Player");
            for (int i = 0; i < playerList.getLength(); i++) {
                Node playerNode = playerList.item(i);
                if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element playerElement = (Element) playerNode;

                    // Extract player attributes
                    String name = playerElement.getAttribute("name");
                    int position = Integer.parseInt(playerElement.getAttribute("position"));
                    int money = Integer.parseInt(playerElement.getAttribute("money"));
                    boolean inJail = Boolean.parseBoolean(playerElement.getAttribute("jailed"));
                    int timeInJail = Integer.parseInt(playerElement.getAttribute("timeInJail"));

                    // Create a new Player object
                    Player player = new Player();
                    player.moveToPosition(position);
                    if (inJail) {
                        player.goToJail(timeInJail);
                    } else {
                        player.setJailTime(timeInJail);
                    }
                    player.setName(name);
                    player.setMoney(money);

                    // Add the player to the board
                    board.addPlayer(player);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return board;
    */
    }
}