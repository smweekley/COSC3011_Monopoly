package state;

import java.io.Serializable;
import java.io.IOException;

import board.Board;

public class GameState {
    private static final long serialVersionUID = 1L;
    private transient Board board;

    public GameState(Board board) {
        this.board = board;
    }

    public Board getBoard() {return board;}

    public void setBoard(Board board) {this.board = board;}

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(board);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        board = (Board) in.readObject();
    }
}