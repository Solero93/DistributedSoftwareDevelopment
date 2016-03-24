package communication;

import controller.Controller;
import exceptions.ReadGridException;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Game extends Thread {
    private Controller ctrl;

    public Game(Socket sock, String layout, int mode) throws IOException, ReadGridException {
        this.ctrl = new Controller();
        sock.setSoTimeout(3000); //TODO LALALA
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
        this.ctrl.createCommunication(sock);
    }

    public void run() {
        this.playGame();
        this.ctrl.closeConnections();
    }

    private void playGame() {
        Message firstResponse = new Message();
        while (firstResponse.getCommand() != Command.START) {
            try {
                firstResponse = this.receiveCommand();
            } catch (IOException e) {
                return;
            }
        }
        if (!this.sendCommand(Command.GRID_RDY, null) || !this.throwServerDice()) {
            return;
        }
        while (true) {
            if (this.enemyTurn() || this.myTurn()) {
                return;
            }
        }
    }

    private boolean throwServerDice() {
        while (true){
            try {
                Message msg = this.receiveCommand();
                if (msg.getCommand() == Command.THROW) {
                    switch (this.ctrl.throwServerDice()) {
                        case HUMAN_FIRST:
                            return true;
                        case DRAW:
                            break;
                        case FIRE:
                            if (this.myTurn()) return false;
                            return true;
                        default:
                            //ERROR
                            return false;
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }

    }

    private boolean myTurn() {
        try {
            this.ctrl.play();
        } catch (IOException e) {
            return true;
        }
        Message enemyResponse;
        //while (true) {//TODO Mantener si quieres que pueda enviarte cosas cuando hay error, Si no fuera
            try {
                enemyResponse = this.receiveCommand();
                switch(enemyResponse.getCommand()){
                    case YOU_WIN:
                        //END Game Server Won
                        return true;
                    case HIT:
                    case MISS:
                    case SUNK:
                        this.ctrl.commitMove(enemyResponse);
                        break;
                    case ERROR:
                        //TODO ERROR
                        return true;
                    default:
                        //TODO ERROR?
                        return true;
                }

            }catch (IOException e) {
                return true;
            }
        return false;
        //}
    }

    private boolean enemyTurn() {
        //while (true) {//TODO Mantener si quieres que pueda enviarte cosas cuando hay error, Si no fuera
            Message msg;
            Message myResponse = new Message();
            try {
                msg = this.receiveCommand();
                switch(msg.getCommand()) {
                    case FIRE:
                        myResponse = this.ctrl.hitMyCell(msg.getParams());
                        if( myResponse.getCommand() == Command.YOU_WIN ) return true;
                        break;
                    case ERROR:
                        //TODO ERROR
                        return true;
                    default:
                        //TODO ERROR?
                        return true;
                }
                return false;
            }catch (IOException e) {
                return true;
            }
        //}

    }

    /**
     * @return true if we an send the messageCode, false otherwise
     */
    public boolean sendCommand(Command cmd, String params) {
        try {
            this.ctrl.sendMessage(cmd, params);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Message receiveCommand() throws IOException {
        while (true) {
            try {
                return this.ctrl.waitForEnemy();
            } catch (SocketTimeoutException e) {

            }
        }
    }
}