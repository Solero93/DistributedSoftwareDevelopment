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
    String bufferMessages;

    public Game(String layout, int mode, int id) throws IOException, ReadGridException {
        this.ctrl = new SelectorCtrl();
        this.id=id;
        bufferMessages="";
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
        this.ctrl.createLog("ServerGame-"+id+".log");
    }
    public void close(){
        this.ctrl.close();
    }

    public ArrayList<Message> getNextMessages(String request) throws EndGameException {
        ArrayList<Message> msgToSend=new ArrayList<Message>();
        ArrayList<Message> msgRecived=new ArrayList<Message>();
        bufferMessages= bufferMessages + request;
        msgRecived=readMessages(msgRecived);
        for (Message message : msgRecived) {
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
    public ArrayList<Message> readMessages(ArrayList<Message> msgRecived)  {
        Message msg;
        char[] tmp;
        int num;
        while(true) {
            msg = new Message();
            if (bufferMessages.length() < 4) return msgRecived;
            Command cmd = Command.getCommandFromCode(bufferMessages.substring(0,4));
            msg.setCommand(cmd);
            switch (cmd) {
                case FIRE:
                    if (bufferMessages.length() < 7) return msgRecived;
                    tmp = bufferMessages.substring(4,7).toCharArray();
                    msg.setParams("" + Character.toUpperCase(tmp[1]) + tmp[2]);
                    msgRecived.add(msg);
                    bufferMessages=bufferMessages.substring(7);
                    break;
                case ERROR:
                    if (bufferMessages.length() < 7) return msgRecived;
                    tmp = bufferMessages.substring(4,7).toCharArray();
                    num=Integer.parseInt("" + tmp[1] + tmp[2]);
                    if (bufferMessages.length() < (7+num)) return msgRecived;
                    msg.setParams(bufferMessages.substring(7,7+num));
                    msgRecived.add(msg);
                    bufferMessages=bufferMessages.substring(7+num);
                    break;
                default:
                    msgRecived.add(msg);
                    bufferMessages=bufferMessages.substring(4);
                    break;
            }
        }

    }
    public ArrayList<Message> getMessages(Message message, ArrayList<Message> msgToSend) throws EndGameException {
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
                            msgToSend.add(this.ctrl.play());
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