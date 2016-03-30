package controller;

import communication.Communication;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;

/**
 * Class that represents the Controller of Client
 * The difference from Controller is that it has the Communication features now.
 */
public class ClientCtrl extends Controller {
    protected Communication com;

    /**
     * @see Communication#Communication(String, int)
     */
    public void createCommunication(String serverName, int port) throws IOException {
        this.com = new Communication(serverName, port);
    }

    /**
     * @see Communication#Communication(Socket)
     */
    public void createCommunication(Socket sock) throws IOException {
        this.com = new Communication(sock);
    }

    /**
     * Closes all components of Controller
     */
    public void close() {
        this.com.close();
    }

    public void sendMessage(Command c, String params) throws IOException {
        this.com.sendMessage(c, params);
    }

    /**
     * @see Communication#waitForMessage()
     */
    public Message waitForEnemy() throws IOException {
        Message response = this.com.waitForMessage();
        return response;
    }

    /**
     * @see Controller#play()
     */
    @Override
    public Message play() throws IOException {
        Message msg = super.play();
        this.sendMessage(msg.getCommand(), msg.getParams());
        return msg;
    }

    /**
     * @see Controller#hitMyCell(String)
     */
    @Override
    public Message hitMyCell(String position) throws IOException {
        Message msg = super.hitMyCell(position);
        this.sendMessage(msg.getCommand(), msg.getParams());
        return msg;
    }
}
