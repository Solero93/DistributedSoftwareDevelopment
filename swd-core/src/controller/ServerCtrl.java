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
        switch(c){
            case FIRE:
                this.logFileWriter.write(SERVER + c.commandCode + " " + params + "\n");
                break;
            case MISS:
                this.logFileWriter.write(CLIENT + c.commandCode + "\n");
                break;
            case HIT:
                this.logFileWriter.write(CLIENT + c.commandCode + "\n");
                break;
            case SUNK:
                this.logFileWriter.write(CLIENT + c.commandCode + "\n");
                break;
            case YOU_WIN:
                this.logFileWriter.write(CLIENT + c.commandCode + "\n");
                break;
            case ERROR:
                this.logFileWriter.write(CLIENT + c.commandCode + " " + params + "\n");
                break;
            case GRID_RDY:
                this.logFileWriter.write(SERVER + c.commandCode + "\n");
                break;
            case HUMAN_FIRST:
                break;
            case DRAW:
                break;
            case UNKNOWN:
                break; // TODO maybe?
        }
    }

    @Override
    public Message waitForEnemy() throws IOException {
        Message msg = super.waitForEnemy();
        Command c = msg.getCommand();
        String params = msg.getParams();
        switch(c){
            case START:
                this.logFileWriter.write(CLIENT + c.commandCode + "\n");
                break;
            case THROW:
                this.logFileWriter.write(CLIENT + c.commandCode + "\n");
                break;
            case FIRE:
                this.logFileWriter.write(CLIENT + c.commandCode + " " + params + "\n");
                break;
            case MISS:
                this.logFileWriter.write(SERVER + c.commandCode + "\n");
                break;
            case HIT:
                this.logFileWriter.write(SERVER + c.commandCode + "\n");
                break;
            case SUNK:
                this.logFileWriter.write(SERVER + c.commandCode + "\n");
                break;
            case YOU_WIN:
                this.logFileWriter.write(SERVER + c.commandCode + "\n");
                break;
            case ERROR:
                this.logFileWriter.write(SERVER + c.commandCode + " " + params + "\n");
                break;
            case UNKNOWN:
                break; // TODO maybe?
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
