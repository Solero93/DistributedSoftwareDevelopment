package communication;

import controller.Controller;
import controller.SelectorCtrl;
import exceptions.EndGameException;
import exceptions.ReadGridException;
import utils.Message;

import utils.enums.Actor;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Random;

public class Game {
    private SelectorCtrl ctrl;
    int id;

    public Game(String layout, int mode, int id) throws IOException, ReadGridException {
        this.ctrl = new SelectorCtrl();
        this.id=id;
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
        this.ctrl.createLog("ServerGame-"+id+".log");
    }

    public ArrayList<Message> getNextMessages(Message message) throws EndGameException {
        ArrayList<Message> msgToSend=new ArrayList<Message>();
        try {
            this.ctrl.writeToLog(Actor.CLIENT, message.getCommand(), message.getParams());

            msgToSend = this.getMessages(message);
            for (Message temp : msgToSend) {
                this.ctrl.writeToLog(Actor.SERVER, temp.getCommand(), temp.getParams());
            }
        } catch (IOException e) {

        }
        return msgToSend;
    }
    public ArrayList<Message> getMessages(Message message) throws EndGameException {
        ArrayList<Message> msgToSend= new ArrayList<Message>();
        try {
            switch (message.getCommand()) {
                case YOU_WIN:
                case ERROR:
                    this.ctrl.close();
                    throw new EndGameException();
                case MISS:
                case HIT:
                case SUNK:
                    this.ctrl.commitMove(message);
                    return msgToSend;

                case FIRE:
                    msgToSend.add(this.ctrl.hitMyCell(message.getParams()));
                    if(msgToSend.get(0).getCommand() == Command.YOU_WIN ) {
                        this.ctrl.close();
                        return msgToSend;
                    }
                    msgToSend.add(this.ctrl.play());
                    return msgToSend;
                case THROW:
                    switch (this.throwServerDice()){
                        case DRAW:
                            msgToSend.add(new Message().setCommand(Command.DRAW));
                            return msgToSend;
                        case HUMAN_FIRST:
                            msgToSend.add(new Message().setCommand(Command.HUMAN_FIRST));
                            return msgToSend;
                        case FIRE:
                            msgToSend.add(this.ctrl.hitMyCell(message.getParams()));
                            if(msgToSend.get(0).getCommand() == Command.YOU_WIN ) {
                                this.ctrl.close();
                                return msgToSend;
                            }
                            return msgToSend;
                    }
                    return msgToSend;
                case START:
                    msgToSend.add(new Message().setCommand(Command.GRID_RDY));
                    return msgToSend;
                //TODO Flujos
            }
        } catch (IOException e) {

        }
        return msgToSend;
    }

    public Command throwServerDice() throws IOException {
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