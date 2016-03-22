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
        sock.setSoTimeout(3000);
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

        Si quieres mandar un mensaje:

        this.ctrl.sendMessage(Command.GRID_RDY, null);

        Si quieres esperar a que te diga algo el cliente

        Message response = this.ctrl.waitEnemyToMove();

        El mensaje tienes dos cosas a coger:
            response.getCommand() -> para coger lo que antes conocías como mensaje
            response.getParams() -> posición / errores (al final son lo mismo pero depende del comando)

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
                Command.getCommandFromCode(com.read_string_variable(4));
            } catch (SocketTimeoutException e) {

            } catch (IOException e) {
                return null;
            }

        }
    }*/
    public void gola(String msg) {

    }
}