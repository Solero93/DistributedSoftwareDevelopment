package controller;

import utils.Message;
import utils.enums.Command;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

/**
 * Class that represents the -- Object
 */
public class ServerCtrl extends Controller {
    BufferedWriter logFileWriter;
    private static final String SERVER = "S: ", CLIENT = "C: ";

    public ServerCtrl(String filename) throws IOException {
        super();
        this.logFileWriter = new BufferedWriter(
                new FileWriter(filename));
    }

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

    @Override
    public void sendMessage(Command c, String params) throws IOException {
        super.sendMessage(c, params);
        if (params == null){
            this.logFileWriter.write(SERVER + c.commandCode + "\n");
        } else {
            this.logFileWriter.write(SERVER + c.commandCode + " " + params + "\n");
        }
    }

    @Override
    public Message waitForEnemy() throws IOException {
        Message msg = super.waitForEnemy();
        if (msg.getParams() == null){
            this.logFileWriter.write(CLIENT + msg.getCommand().commandCode + "\n");
        } else {
            this.logFileWriter.write(CLIENT + msg.getCommand().commandCode + " " + msg.getParams() + "\n");
        }
        return msg;
    }

    @Override
    public void close() {
        super.close();
        try {
            this.logFileWriter.flush(); // Must write last contents into file
            this.logFileWriter.close();
        } catch (IOException e) {
            // TODO do something
        }
    }
}
