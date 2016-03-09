package controller.gameModes;

import utils.Message;

import java.util.ArrayList;

/**
 * Class that represents the -- Object
 */
public abstract class GameMode {
    protected ArrayList<String> cellsFired;
    protected String waitMove;

    public GameMode() {
        this.cellsFired = new ArrayList<>();
        this.waitMove = null;
    }

    public abstract String play();

    public void commitMove(Message message) {
        if (message != Message.ERROR && this.waitMove != null) {
            this.cellsFired.add(this.waitMove);
        }
        this.waitMove = null;
    }
}