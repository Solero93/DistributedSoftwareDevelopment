package controller.gameModes;

import utils.enums.Command;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that represents the -- Object
 */
public abstract class GameMode {
    protected ArrayList<String> cellsFired;
    protected String waitMove;
    protected Random rand;
    protected static final int RANDOM_SEED = 42;

    public GameMode() {
        this.cellsFired = new ArrayList<>();
        this.waitMove = null;
        this.rand = new Random(RANDOM_SEED);
    }

    public abstract String generateHitPosition();

    public void commitMove(Command command) {
        if (command != Command.ERROR && this.waitMove != null) {
            this.cellsFired.add(this.waitMove);
        }
        this.waitMove = null;
    }
}