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
            File file;
            if (new File(fileName).isAbsolute()) {
                file = new File(fileName);
            } else {
                File saveDir = new File(SAVE_DIRECTORY);
                if (!saveDir.exists()) {
                    saveDir.mkdirs();
                }
                file = new File(saveDir, fileName);
            }

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
                playerElement.setAttribute("color", String.valueOf(player.getColor()));
                playerElement.setAttribute("number", String.valueOf(player.getNumber()));
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

    public static ArrayList<Player> loadGame(String filePath) {
        try {
            File file = new File(filePath);
            
            ArrayList<Player> players = new ArrayList<>();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
    
            NodeList playerList = doc.getElementsByTagName("Player");
            for (int i = 0; i < playerList.getLength(); i++) {
                Node playerNode = playerList.item(i);
                if (playerNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element playerElement = (Element) playerNode;
    
                    String name = playerElement.getAttribute("name");
                    int position = Integer.parseInt(playerElement.getAttribute("position"));
                    int money = Integer.parseInt(playerElement.getAttribute("money"));
                    boolean inJail = Boolean.parseBoolean(playerElement.getAttribute("jailed"));
                    int timeInJail = Integer.parseInt(playerElement.getAttribute("timeInJail"));
                    String propertiesString = playerElement.getAttribute("properties");
                    String color = playerElement.getAttribute("color");
                    int number = Integer.parseInt(playerElement.getAttribute("number"));

                    Player player = new Player(number, name, color);
                    player.setName(name);
                    player.setPosition(position);
                    player.setMoney(money);
                    player.setNumber(number);
                    if (inJail) {
                        player.goToJail(timeInJail);
                    } else {
                        player.setJailTime(timeInJail);
                    }
                    player.setColor(color);
                    //player.setPropertiesFromString(propertiesString);
                    players.add(player);
                }
            }
            return players;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}