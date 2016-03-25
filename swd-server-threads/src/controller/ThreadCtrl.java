package controller;

import utils.LogCreator;
import utils.Message;
import utils.enums.Actor;
import utils.enums.Command;

import java.io.IOException;
import java.util.Random;

/**
 * Class that represents the -- Object
 */
public class ThreadCtrl extends ClientCtrl {
    private LogCreator logWriter;

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

    public void createLog(String filename) throws IOException{
        this.logWriter = new LogCreator(filename);
    }

    @Override
    public void sendMessage(Command c, String params) throws IOException {
        super.sendMessage(c, params);
        this.logWriter.writeToLog(Actor.SERVER, c, params);
    }

    @Override
    public Message waitForEnemy() throws IOException {
        Message msg = super.waitForEnemy();
        this.logWriter.writeToLog(Actor.CLIENT, msg.getCommand(), msg.getParams());
        return msg;
    }

    @Override
    public void close() {
        super.close();
        try {
            this.logWriter.close();
        } catch (IOException e) {
            // TODO do something
        }
    }
}