package communication;

import utils.enums.Message;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class that abstracts the Communication
 */

public class Communication {
    Socket clientSocket;

    public Communication(String serverName, int port) throws IOException {
        clientSocket = new Socket(InetAddress.getByName(serverName), port);
    }

    public Message sendHit(String position) {
        return null;
    }
}
