package controller;

import utils.enums.Command;

import java.io.IOException;
import java.util.Random;

/**
 * Class that represents the -- Object
 */
public class ServerCtrl extends Controller {
    public Command throwServerDice() throws IOException {
        int dice1, dice2;
        Random rand = new Random();

        dice1 = rand.nextInt(6) + 1;
        dice2 = rand.nextInt(6) + 1;
        if (dice1 > dice2) {
            this.sendMessage(Command.HUMAN_FIRST, null);
            return Command.HUMAN_FIRST;

        } else if (dice1 == dice2) {
            this.sendMessage(Command.DRAW, null);
            return Command.DRAW;

        } else {
            return Command.FIRE;
        }

    }
}
