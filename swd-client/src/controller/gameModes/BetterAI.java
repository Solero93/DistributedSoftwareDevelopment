package controller.gameModes;

import utils.Message;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class that represents the BetterAI
 */
public class BetterAI extends GameMode {
    private enum Direction {
        UP, DOWN, LEFT, RIGHT;

        public int x;
        public int y;

        static {
            UP.x = 0;
            UP.y = +1;
            DOWN.x = 0;
            DOWN.y = -1;
            LEFT.x = -1;
            LEFT.y = 0;
            RIGHT.x = 0;
            RIGHT.y = +1;
        }
    }

    private String firstHit;
    private Message lastMessage;
    private Direction currHitDir;

    public BetterAI() {
        super();
        this.firstHit = null;
        this.currHitDir = null;
    }

    public String play() {
        String position;
        // TODO make a more intelligent random algorithm
        if (this.firstHit == null) { // If there's no recent HIT => random
            while (true) {
                Random r = new Random();
                position = "ABCDEFGHIJ".charAt(r.nextInt(10)) + Integer.toString(r.nextInt(10));
                if (!this.cellsFired.contains(position)) {
                    this.waitMove = position;
                    return position;
                }
            }
        }
        // If there's recent HIT => Apply AI
        if (lastMessage == Message.HIT) {
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

    public void commitMove(Message message) {
        switch (message) {
            case HIT:
                if (this.firstHit == null) {
                    this.firstHit = this.waitMove;
                } else {
                    this.lastMessage = message;
                }
                break;
            case MISS:
                if (this.firstHit != null) {
                    this.lastMessage = message;
                }
                break;
            case SUNK:
                this.firstHit = null;
                this.currHitDir = null;
                this.lastMessage = null;
                break;
        }
        super.commitMove(message);
    }
}
