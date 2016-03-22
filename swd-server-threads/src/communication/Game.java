package communication;

import controller.Controller;
import exceptions.ReadGridException;
import utils.ComUtils;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Game extends Thread {
    private Socket clientSocket;
    private Controller ctrl;

    public Game(Socket sock, String layout, int mode) throws IOException, ReadGridException {
        ctrl = new Controller();
        sock.setSoTimeout(3000);
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
        this.ctrl.createCommunication(sock);
    }

    public void run() {

        Message msg;
        msg = null;
        boolean loop;
        if (!this.sendCommand(Command.GRID_RDY, null)) {
            this.ctrl.closeConnections();
            return;
        }
        //throw---------------------
        while (msg == null) {
            msg = receiveCommand();
            if (msg == null) {
                this.ctrl.closeConnections();
                return;
            } else if (msg.getCommand() == Command.THROW) {
                try {
                    msg = this.ctrl.throwServerDice();
                } catch (IOException e) {
                    this.ctrl.closeConnections();
                    return;
                }
                switch (msg.getCommand()) {
                    case FIRE:

                        break;
                    case HIT:
                        break;
                    case THROW:
                        msg = null;//
                        break;
                    default:
                        //TODO SEND ERROR
                        msg = null;
                        break;

                }
            } else {
                //TODO SEND ERROR
                msg = null;
            }
        }

        while (true) {
            Message enemyResponse= new Message();
            try {
                this.ctrl.play();
            } catch (IOException e) {
                this.ctrl.closeConnections();
                return;
            }
            loop=true;
            while (loop) {
                try {
                    enemyResponse = this.ctrl.waitForEnemy();
                    loop=false;
                    if (enemyResponse.getCommand() == Command.YOU_WIN) {
                        this.ctrl.closeConnections();
                        return;
                    }//TODO TRATAR EL MENSAJE CUANDO ES HIT MISS SUNK O UN ERROR
                } catch (SocketTimeoutException e) {

                } catch (IOException e) {
                    this.ctrl.closeConnections();
                    return;
                }

            }
            loop=true;
            while(loop) {
                Message myResponse = new Message();
                try {
                    myResponse = this.ctrl.waitForEnemy();
                    //TODO TRATAR flujo
                } catch (SocketTimeoutException e) {

                } catch (IOException e) {
                    this.ctrl.closeConnections();
                    return;
                }
            }
        }


    }

    /**
     * @return true if we an send the messageCode, false
     */


    public boolean sendCommand(Command cmd, String params) {
        try {
            this.ctrl.sendMessage(Command.GRID_RDY, null);
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

    public void gola(String msg) {

    }
}