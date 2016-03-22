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
        msg=null;
        if (!this.sendCommand(Command.GRID_RDY, null)){
            this.ctrl.closeConnections();
            return;
        }
        while (msg == null) {
            msg = receiveCommand();
            if(msg==null){
                this.ctrl.closeConnections();
                return;
            }else if(msg.getCommand() == Command.THROW){
                try {
                    msg= this.ctrl.throwServerDice();
                } catch (IOException e) {
                    this.ctrl.closeConnections();
                    return;
                }
                switch (msg.getCommand()){
                    case FIRE:
                        break;
                    case HIT:
                        break;
                    case THROW:
                        msg = null;//
                        break;
                    default:
                        //TODO ERROR
                        msg = null;
                        break;

                }
            }else{
                //TODO ENVIO DE ERROR
                msg=null;
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