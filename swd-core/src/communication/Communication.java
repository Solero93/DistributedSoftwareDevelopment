package communication;

import utils.ComUtils;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Class that abstracts the Communication
 */

public class Communication {
    ComUtils com;

    public Communication(String serverName, int port) throws IOException {
        this.com = new ComUtils(new Socket(InetAddress.getByName(serverName), port));
    }

    public Communication(Socket sock) throws IOException {
        this.com = new ComUtils(sock);
    }

    public Message sendMessage(Command c, String params) throws IOException {
        String pkg = new Message()
                .setCommand(c)
                .setParams(params)
                .buildPackage();
        this.com.write_string(pkg);
        Message response = waitForMessage();
        return response;
    }

    public Message waitForMessage() throws IOException {
        return new Message(this.com.read_string());
    }
}