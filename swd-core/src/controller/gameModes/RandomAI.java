package controller.gameModes;

import java.util.Random;

/**
 * Class that represents the RandomAI
 */
public class RandomAI extends GameMode {
    public String generateHitPosition() {
        String letters = "ABCDEFGHIJ", position;
        while (true) {
            Random r = new Random();
            position = letters.charAt(r.nextInt(10)) + Integer.toString(r.nextInt(10));
            if (!this.cellsFired.contains(position)) {
                return position;
            }
        }
    }
}