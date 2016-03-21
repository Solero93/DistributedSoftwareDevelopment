package communication;

import controller.ServerCtrl;
import exceptions.ReadGridException;
import utils.ComUtils;
import utils.enums.Message;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Game extends Thread {
    private Socket clientSocket;
    private ServerCtrl ctrl;
    private ComUtils com;

    public Game(Socket sock, String layout, int mode) throws IOException, ReadGridException {
        ctrl = new ServerCtrl();
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
        if (!sendMessage(Message.GRID_RDY.messageCode)) return;
        msg = receiveMessage();
        // TODO Para crear los packets de los mensajes, usa la clase PackageBuilder
        /* EJEMPLO:
            String packet = new PackageBuilder()
                            .setMessage(Message.FIRE)
                            .setPosition("A9")
                            .buildPackage();
            Si se han pasado parámetros que no tocaban para crear el mensaje,
            devuelve un PackageBuildException.
        */
    }

    /**
     * @param msg
     * @return true if we an send the messageCode, false
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
        while (true) {
            try {
                return Message.getMessageFromCode(com.read_string_variable(4));
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return null;
            }

        }
    }

    public void gola(String msg) {

    }
}
