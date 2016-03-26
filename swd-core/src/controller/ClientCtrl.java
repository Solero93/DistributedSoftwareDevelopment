package controller;

import communication.Communication;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.net.Socket;

/**
 * Class that represents the -- Object
 */
public class ClientCtrl extends Controller {

    protected Communication com;

    public void createCommunication(String serverName, int port) throws IOException {
        this.com = new Communication(serverName, port);
    }

    public void createCommunication(Socket sock) throws IOException {
        this.com = new Communication(sock);
    }

    public void close() {
        this.com.close();
    }

    public void sendMessage(Command c, String params) throws IOException {
        this.com.sendMessage(c, params);
    }

    public Message waitForEnemy() throws IOException {
        Message response = this.com.waitForMessage();
        return response;
    }

    @Override
    public Message play() throws IOException {
        Message msg = super.play();
        this.sendMessage(msg.getCommand(), msg.getParams());
        return msg;
    }

    @Override
    public Message hitMyCell(String position) throws IOException {
        Message msg = super.hitMyCell(position);
        this.sendMessage(msg.getCommand(), null);
        return msg;
    }
}
