package controller.gameModes;

import java.util.Random;

/**
 * Class that represents the -- Object
 */
public class RandomAI extends GameMode {
    public String play() {
        Random r = new Random();
        int letterRandom, numberRandom;
        String letters = "ABCDEFGHIJ", position;
        while (true){
            letterRandom = r.nextInt(10);
            numberRandom = r.nextInt(10);
            position = letters.charAt(letterRandom) + Integer.toString(numberRandom);
            if (!this.cellsHit.contains(position)){
                return position;
            }
        }
    }
}
