package state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import state.GameState;

public class StateManager {

    private static final String SAVE_DIRECTORY = "saves";

    public static void saveGame(GameState gameState, String fileName) {
        // check if saves dir exists
        File saveDir = new File(SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File saveFile = new File(saveDir, fileName);

        try (FileOutputStream fileOut = new FileOutputStream(saveFile);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
            out.writeObject(gameState);
            System.out.println("Game saved to " + saveFile.getAbsolutePath());
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public static GameState loadGame(String fileName) {
        File saveFile = new File(SAVE_DIRECTORY, fileName);

        GameState gameState = null;
        try (FileInputStream fileIn = new FileInputStream(saveFile);
            ObjectInputStream in = new ObjectInputStream(fileIn)) {
            gameState = (GameState) in.readObject();
            System.out.println("Game loaded from " + saveFile.getAbsolutePath());
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            c.printStackTrace();
        }
        return gameState;
    }
}