package controller.gameModes;

import java.util.Random;

/**
 * Class that represents the -- Object
 */
public class RandomAI extends GameMode {
    public String play() {
        Random r = new Random();
        String letters = "ABCDEFGHIJ", position;
        while (true) {
            position = letters.charAt(r.nextInt(10)) + Integer.toString(r.nextInt(10));
            if (!this.cellsHit.contains(position)) {
                return position;
            }
        }
    }
}