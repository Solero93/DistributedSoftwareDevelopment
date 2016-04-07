package controller.gameModes;

import utils.enums.Command;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that abstracts all the Game modes
 */
public abstract class GameMode {
    protected static final int RANDOM_SEED = 42;
    protected ArrayList<String> cellsFired;
    protected String waitMove;
    protected Random rand;

    /**
     * Abstract constructor of gameModes.
     * Seed 42 because of reasons.
     */
    public GameMode() {
        this.cellsFired = new ArrayList<>();
        this.waitMove = null;
        this.rand = new Random(RANDOM_SEED);
    }

    /**
     * Logic that returns a position to hit
     *
     * @return Position to Hit
     */
    public abstract String generateHitPosition();

    /**
     * Validates a move with the enemy's response.
     * This is to prevent assuming that when a move was sent, it must have been done.
     * If an error happened, the move is undone.
     *
     * @param command : Response Command of enemy
     */
    public void commitMove(Command command) {
        if (command != Command.ERROR && this.waitMove != null) {
            this.cellsFired.add(this.waitMove);
        }
        this.waitMove = null;
    }
}