package communication;

import controller.SelectorCtrl;
import exceptions.EndGameException;
import exceptions.ReadGridException;
import utils.Message;
import utils.enums.Actor;
import utils.enums.Command;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Game flux frome the selector server
 */
public class Game {
    private SelectorCtrl ctrl;
    private String bufferMessages;

    /**
     * Constructor
     * @param layout
     * @param mode
     * @param id
     * @throws IOException
     * @throws ReadGridException
     */
    public Game(String layout, int mode, int id) throws IOException, ReadGridException {
        this.ctrl = new SelectorCtrl();
        bufferMessages = "";
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
        this.ctrl.createLog("ServerGame-" + id + ".log");
    }

    /**
     * Close the log
     */
    public void close() {
        this.ctrl.close();
    }

    /**
     * Basic flux called from the selector
     * Looks if there are a message at the buffer and answer it.
     * @param request
     * @return Returns an array of messages to send
     * @throws EndGameException
     */
    public ArrayList<Message> getNextMessages(String request) throws EndGameException {
        ArrayList<Message> msgToSend = new ArrayList<>();
        ArrayList<Message> msgReceived = new ArrayList<>();
        this.bufferMessages = this.bufferMessages + request;
        //Looks
        msgReceived = readMessages(msgReceived);

        for (Message message : msgReceived) {
            try {
                this.ctrl.writeToLog(Actor.CLIENT, message.getCommand(), message.getParams());

                msgToSend = this.getMessages(message, msgToSend);
                for (Message temp : msgToSend) {
                    this.ctrl.writeToLog(Actor.SERVER, temp.getCommand(), temp.getParams());
                }
            } catch (IOException e) {

            }
        }
        return msgToSend;
    }

    /**
     * Looks if there are a complete  command at the buffer
     * Create the Message from the command
     * @param msgReceived
     * @return
     */
    private ArrayList<Message> readMessages(ArrayList<Message> msgReceived) {
        Message msg;
        char[] tmp;
        int num;
        while (true) {
            msg = new Message();
            if (this.bufferMessages.length() < 4) return msgReceived;
            Command cmd = Command.getCommandFromCode(this.bufferMessages.substring(0, 4));
            msg.setCommand(cmd);
            switch (cmd) {
                case FIRE:
                    if (this.bufferMessages.length() < 7) return msgReceived;
                    tmp = this.bufferMessages.substring(4, 7).toCharArray();
                    msg.setParams("" + Character.toUpperCase(tmp[1]) + tmp[2]);
                    msgReceived.add(msg);
                    this.bufferMessages = this.bufferMessages.substring(7);
                    break;
                case ERROR:
                    if (this.bufferMessages.length() < 7) return msgReceived;
                    tmp = this.bufferMessages.substring(4, 7).toCharArray();
                    num = Integer.parseInt("" + tmp[1] + tmp[2]);
                    if (this.bufferMessages.length() < (7 + num)) return msgReceived;
                    msg.setParams(this.bufferMessages.substring(7, 7 + num));
                    msgReceived.add(msg);
                    this.bufferMessages = this.bufferMessages.substring(7 + num);
                    break;
                default:
                    msgReceived.add(msg);
                    this.bufferMessages = this.bufferMessages.substring(4);
                    break;
            }
        }

    }

    /**
     * Answers of the messages put
     * @param message
     * @param msgToSend
     * @return
     * @throws EndGameException
     */
    private ArrayList<Message> getMessages(Message message, ArrayList<Message> msgToSend) throws EndGameException {
        try {
            switch (message.getCommand()) {
                case YOU_WIN:
                case ERROR:
                    //We close wen one wins or an error has sent
                    this.ctrl.close();
                    throw new EndGameException();
                case MISS:
                case HIT:
                case SUNK:
                    // 
                    this.ctrl.commitMove(message);
                    return msgToSend;
                case FIRE:
                    msgToSend.add(this.ctrl.hitMyCell(message.getParams()));
                    if (msgToSend.get(0).getCommand() == Command.YOU_WIN) {
                        this.ctrl.close();
                        return msgToSend;
                    }
                    msgToSend.add(this.ctrl.play());
                    return msgToSend;
                case THROW:
                    switch (this.throwServerDice()) {
                        case DRAW:
                            msgToSend.add(new Message().setCommand(Command.DRAW));
                            return msgToSend;
                        case HUMAN_FIRST:
                            msgToSend.add(new Message().setCommand(Command.HUMAN_FIRST));
                            return msgToSend;
                        case FIRE:
                            msgToSend.add(this.ctrl.play());
                            return msgToSend;
                    }
                    return msgToSend;
                case START:
                    msgToSend.add(new Message().setCommand(Command.GRID_RDY));
                    return msgToSend;
            }
        } catch (IOException e) {

        }
        return msgToSend;
    }

    /**
     * Throw dice method
     *
     */
    private Command throwServerDice() throws IOException {
        int dice1, dice2;
        Random rand = new Random();

        dice1 = rand.nextInt(6) + 1;
        dice2 = rand.nextInt(6) + 1;
        if (dice1 > dice2) {
            return Command.HUMAN_FIRST;

        } else if (dice1 == dice2) {
            return Command.DRAW;

        } else {
            return Command.FIRE;
        }
    }
}