package communication;

import controller.Controller;
import exceptions.ReadGridException;
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

        msg = new Message();
        while(msg.getCommand()!= Command.START){
            try {
                msg = this.ctrl.waitForEnemy();
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                this.ctrl.closeConnections();
                return;
            }
        }
        Command cmd;
        boolean loop;
        if (!this.sendCommand(Command.GRID_RDY, null)) {
            this.ctrl.closeConnections();
            return;
        }
        //throw---------------------
        loop = true;
        while (loop) {
            msg = receiveCommand();
            if (msg == null) {
                this.ctrl.closeConnections();
                return;
            } else if (msg.getCommand() == Command.THROW) {
                try {
                    cmd = this.ctrl.throwServerDice();
                } catch (IOException e) {
                    this.ctrl.closeConnections();
                    return;
                }
                switch (cmd) {
                    case FIRE:

                        break;
                    case HUMAN_FIRST:
                        if(this.enemyTurn()){
                            this.ctrl.closeConnections();
                            return;
                        }
                        break;
                    case DRAW:
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
            if(this.myTurn()){
                this.ctrl.closeConnections();
                return;
            }

            if(this.enemyTurn()){
                this.ctrl.closeConnections();
                return;
            }

        }


    }

    public boolean myTurn(){
        Message enemyResponse;
        try {
            this.ctrl.play();
        } catch (IOException e) {
            return true;
        }
        boolean loop = true;
        while (loop) {
            try {
                enemyResponse = this.ctrl.waitForEnemy();
                loop = false;
                if (enemyResponse.getCommand() == Command.YOU_WIN) {
                    //END Game Server Won
                    return true;
                }else{
                    this.ctrl.commitMove(enemyResponse);
                }
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return true;
            }

        }
        return true;
    }

    public boolean enemyTurn(){
        boolean loop = true;
        while (loop) {
            Message msg;
            Message myResponse = new Message();
            try {
                msg = this.ctrl.waitForEnemy();

                if (msg.getCommand() == Command.ERROR) {
                    //TODO TRACTAR ERROR
                }else{
                    myResponse= this.ctrl.hitMyCell(msg.getParams());
                }
                loop = false;
                if (myResponse.getCommand()== Command.YOU_WIN) return true;

            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return true if we an send the messageCode, false
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

    public void gola(String msg) {

    }
}