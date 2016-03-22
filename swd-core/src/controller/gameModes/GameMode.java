package controller.gameModes;

import utils.enums.Command;

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

    public abstract String generateHitPosition();

    public void commitMove(Command command) {
        if (command != Command.ERROR && this.waitMove != null) {
            this.cellsFired.add(this.waitMove);
        }
        this.waitMove = null;
    }
}