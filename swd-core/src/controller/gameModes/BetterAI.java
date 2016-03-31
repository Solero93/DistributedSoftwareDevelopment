package controller.gameModes;

import utils.enums.Command;

/**
 * Class that represents the BetterAI
 *
 * @see GameMode
 */
public class BetterAI extends GameMode {
    private enum Direction {
        UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

        public int x;
        public int y;

        Direction(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private String firstHit;
    private Command lastCommand;
    private Direction currHitDir;

    public BetterAI() {
        super();
        this.firstHit = null;
        this.currHitDir = null;
    }

    /**
     * Uses a better AI to generate a position to Hit.
     * Basically it uses random logic until it Hits a ship, then it explores until its SUNK.
     *
     * @return Position to Hit
     */
    @Override
    public String generateHitPosition() {
        String position;
        // TODO make a more intelligent random algorithm
        if (this.firstHit == null) { // If there's no recent HIT => random
            while (true) {
                position = "ABCDEFGHIJ".charAt(this.rand.nextInt(10)) + Integer.toString(this.rand.nextInt(10));
                if (!this.cellsFired.contains(position)) {
                    this.waitMove = position;
                    return position;
                }
            }
        }
        // If there's recent HIT => Apply AI
        if (lastCommand == Command.HIT) {
            // If the last move was a HIT
            // Keep on hitting on the same direction
            String lastHitPosition = this.cellsFired.get(this.cellsFired.size() - 1);
            char posX = lastHitPosition.charAt(0), posY = lastHitPosition.charAt(1);
            position = ((char) (posX + currHitDir.x)) + "" + ((char) (posY + currHitDir.y));
            if (!this.cellsFired.contains(position) && this.isValidPosition(position)) {
                this.waitMove = position;
                return position;
            }
        }
        // If the last move was a MISS or we are in the first hit
        // Go back to the first hit and choose another direction
        char posX = this.firstHit.charAt(0), posY = this.firstHit.charAt(1);
        Direction[] allDirections = {Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT};
        for (Direction dir : allDirections) {
            position = ((char) (posX + dir.x)) + "" + ((char) (posY + dir.y));
            // If position to hit is valid, advance over there
            if (!this.cellsFired.contains(position) && this.isValidPosition(position)) {
                this.currHitDir = dir;
                this.waitMove = position;
                return position;
            }
        }
        return null; // Shouldn't arrive here TODO fix that it arrives here
    }

    private boolean isValidPosition(String position) {
        return ("ABCDEFGHIJ".indexOf(position.charAt(0)) >= 0
                && position.charAt(1) >= '0' && position.charAt(1) <= '9'
        );
    }

    /**
     * Validates a move with the enemy's response.
     * This is to prevent assuming that when a move was sent, it must have been done.
     * If an error happened, the move is undone.
     *
     * @param command : Response Command of enemy
     */
    @Override
    public void commitMove(Command command) {
        switch (command) {
            case HIT:
                if (this.firstHit == null) {
                    this.firstHit = this.waitMove;
                } else {
                    this.lastCommand = command;
                }
                break;
            case MISS:
                if (this.firstHit != null) {
                    this.lastCommand = command;
                }
                break;
            case SUNK:
                this.firstHit = null;
                this.currHitDir = null;
                this.lastCommand = null;
                break;
        }
        super.commitMove(command);
    }
}
