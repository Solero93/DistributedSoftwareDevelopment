import controller.ThreadCtrl;
import exceptions.ReadGridException;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Flux of the server game
 * The messages are sending by the ctrl
 */
public class Game extends Thread {
    private ThreadCtrl ctrl;

    /**
     * Constructor
     * @param sock
     * @param layout
     * @param mode
     * @param id
     * @throws IOException
     * @throws ReadGridException
     */
    public Game(Socket sock, String layout, int mode, int id) throws IOException, ReadGridException {
        this.ctrl = new ThreadCtrl();
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
        this.ctrl.createCommunication(sock);
        this.ctrl.createLog("ServerThread-" + id + ".log");
    }

    /**
     * Main of the thread
     */
    public void run() {
        this.playGame();
        this.close();
    }

    /**
     * Flux
     */
    private void playGame() {
        Message firstResponse = new Message();
        /**
         * Waiting to the start command
         */
        while (firstResponse.getCommand() != Command.START) {
            try {
                firstResponse = this.receiveCommand();
            } catch (IOException e) {
                return;
            }
        }
        /**
         * Grid generationa and throw the Dice
         */
        if (!this.sendCommand(Command.GRID_RDY, null) || !this.throwServerDice()) {
            return;
        }
        /**
         * Central game flux
         */
        while (true) {
            if (this.enemyTurn() || this.myTurn()) {
                return;
            }
        }
    }

    /**
     * Throw dice flux
     * @return
     */
    private boolean throwServerDice() {
        while (true) {
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
                    }
                }
            } catch (IOException e) {
                return false;
            }
        }
    }

    /**
     * Server turn flux
     * @return
     */
    private boolean myTurn() {
        //while (true) {//TODO Mantener si quieres que pueda enviarte cosas cuando hay error, Si no fuera
        try {
            this.ctrl.play();
            Message enemyResponse = this.receiveCommand();
            this.ctrl.commitMove(enemyResponse);
            switch (enemyResponse.getCommand()) {
                case YOU_WIN:
                    //END Game Server Won
                    return true;
                case HIT:
                    break;
                case MISS:
                    break;
                case SUNK:
                    break;
                case ERROR:
                    return true;
                default://TODO SEND ERRROR  whit While o petar?
                    return true;
            }

        } catch (IOException e) {
            return true;
        }
        return false;
        //}
    }

    /**
     * Enemi turn flux
     * @return
     */
    private boolean enemyTurn() {
        //while (true) {//TODO Mantener si quieres que pueda enviarte cosas cuando hay error, Si no fuera
        try {
            Message msg = this.receiveCommand();
            switch (msg.getCommand()) {
                case FIRE:
                    Message myResponse = this.ctrl.hitMyCell(msg.getParams());
                    if (myResponse.getCommand() == Command.YOU_WIN) return true;
                    break;
                case ERROR:
                    return true;
                default://TODO SEND ERRROR  whit While o petar?
                    return true;
            }
            return false;
        } catch (IOException e) {
            return true;
        }
        //}
    }

    /**
     * @return true if we an send the messageCode, false otherwise
     */
    private boolean sendCommand(Command cmd, String params) {
        try {
            this.ctrl.sendMessage(cmd, params);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private Message receiveCommand() throws IOException {
        while (true) {
            try {
                return this.ctrl.waitForEnemy();
            } catch (SocketTimeoutException e) {
                //Loop while client doesn't answer
            }
        }
    }

    private void close() {
        this.ctrl.close();
    }
}