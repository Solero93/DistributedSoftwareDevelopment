package controller;

import communication.Communication;

import java.io.IOException;

/**
 * Class that represents the Controller Object
 */
public class ClientCtrl extends Controller {
    private Communication com;

    public void createCommunication(String serverName, int port) throws IOException {
        this.com = new Communication(serverName, port);
    }
}