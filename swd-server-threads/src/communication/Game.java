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
        //sock.setSoTimeout(3000); TODO LALALA
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
            firstResponse = this.receiveCommand();
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
        try {
            Message enemyMsg = this.ctrl.waitForEnemy();
            if (enemyMsg.getCommand() == Command.THROW) {
                this.ctrl.throwServerDice();
                return true;
            }
            // TODO treat errors
        } catch (SocketTimeoutException ex) {
        } catch (IOException e) {
        }
        return false;
    }

    private boolean myTurn() {
        try {
            this.ctrl.play();
        } catch (IOException e) {
            return true;
        }
        Message enemyResponse;
        boolean loop = true;
        while (loop) {
            try {
                enemyResponse = this.ctrl.waitForEnemy();
                if (enemyResponse.getCommand() == Command.YOU_WIN) {
                    //END Game Server Won
                    return true;
                } else {
                    this.ctrl.commitMove(enemyResponse);
                }
                loop = false;
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return true;
            }

        }
        return false;
    }

    private boolean enemyTurn() {
        boolean loop = true;
        while (loop) {
            Message msg;
            Message myResponse = new Message();
            try {
                msg = this.ctrl.waitForEnemy();
                if (msg.getCommand() == Command.ERROR) {
                    //TODO TRACTAR ERROR
                } else {
                    myResponse = this.ctrl.hitMyCell(msg.getParams());
                }
                loop = false;
                if (myResponse.getCommand() == Command.YOU_WIN) return true;

            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return true;
            }
        }
        return false;
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

    public Message receiveCommand() {
        while (true) {
            try {
                return this.ctrl.waitForEnemy();
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return null;
            }
        }
    }
}