package communication;

import controller.Controller;
import exceptions.ReadGridException;
import utils.ComUtils;
import utils.Message;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Game extends Thread {
    private Socket clientSocket;
    private Controller ctrl;
    private ComUtils com;

    public Game(Socket sock, String layout, int mode) throws IOException, ReadGridException {
        ctrl = new Controller();
        sock.setSoTimeout(30000);
        com = new ComUtils(sock);
        if (layout == null) {
            this.ctrl.generateGridAutomatic();
        } else {
            this.ctrl.generateGridFromFile(layout);
        }
        this.ctrl.createGameMode(mode);
    }

    public void run() {
        Message msg;
        if (!sendMessage(Message.GRID_RDY.message)) return;
        msg = receiveMessage();

    }

    /**
     * @param msg
     * @return true if we an send the message, false
     */
    public boolean sendMessage(String msg) {
        try {
            com.write_string(msg);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Message receiveMessage() {
        Message msg;
        while (true) {
            try {

                switch (com.read_string_variable(4)) {
                    case "STRT":
                        msg = Message.START;
                        break;
                    case "THRW":
                        msg = Message.START;
                        break;
                    case "FIRE":
                        msg = Message.START;
                        break;
                    case "HIT_":
                        msg = Message.START;
                        break;
                    case "MISS":
                        msg = Message.START;
                        break;
                    case "SUNK":
                        msg = Message.START;
                        break;
                    case "WIN_":
                        msg = Message.START;
                        break;
                    case "ERRO":
                        msg = Message.START;
                        break;
                    case "GRID":
                        msg = Message.START;
                        break;
                    case "FRST":
                        msg = Message.START;
                        break;
                    case "DRAW":
                        msg = Message.START;
                        break;
                    default:
                        msg = Message.UNKNOWN;
                        break;
                }
                return msg;
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return null;
            }

        }
    }

    public void gola(String msg) {

    }
}
