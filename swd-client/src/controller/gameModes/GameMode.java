package controller.gameModes;

import utils.Message;

import java.util.ArrayList;

/**
 * Class that represents the -- Object
 */
public abstract class GameMode {
    protected ArrayList<String> cellsHit;
    protected String waitMove;

    public GameMode() {
        this.cellsHit = new ArrayList<>();
        this.waitMove = null;
    }

    public abstract String play();

    public void commitMove(Message message) {
        if (message != Message.ERROR && this.waitMove != null) {
            this.cellsHit.add(this.waitMove);
        }
        this.waitMove = null;
    }
}