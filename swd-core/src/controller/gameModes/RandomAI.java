package controller.gameModes;

import java.util.Random;

/**
 * Class that represents the RandomAI
 */
public class RandomAI extends GameMode {
    public String generateHitPosition() {
        String letters = "ABCDEFGHIJ", position;
        while (true) {
            position = letters.charAt(this.rand.nextInt(10)) + Integer.toString(this.rand.nextInt(10));
            if (!this.cellsFired.contains(position)) {
                return position;
            }
        }
    }
}