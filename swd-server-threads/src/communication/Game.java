package communication;

import controller.Controller;
import exceptions.ReadGridException;
import utils.ComUtils;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;

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
        this.ctrl.createCommunication(sock);
    }

    public void run() {
        Command msg;
        /*
        if (!sendCommand(Command.GRID_RDY.commandCode)) return;
        msg = receiveCommand();

        Esto se sustituye por:

        Message response = this.ctrl.sendMessage(Command.GRID_RDY, null);

        Si quieres solo esperar a que te diga algo el cliente

        Message answer = this.ctrl.waitEnemyToMove();

        Puedes ver el ejemplo de uso en la clase Game de swd-client:
        */
    }

    /**
     * @param msg
     * @return true if we an send the messageCode, false
     */
    /*

    No hace falta! Va a través de controlador!

    public boolean sendCommand(String msg) {
        try {
            com.write_string(msg);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Command receiveCommand() {
        while (true) {
            try {
                return Command.getCommandFromCode(com.read_string_variable(4));
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return null;
            }

        }
    }*/
    public void gola(String msg) {

    }
}