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
    Socket mySocket;

    public Communication(String serverName, int port) throws IOException {
        this.mySocket = new Socket(InetAddress.getByName(serverName), port);
        this.com = new ComUtils(this.mySocket);
    }

    public Communication(Socket sock) throws IOException {
        this.mySocket = sock;
        this.com = new ComUtils(sock);
    }

    public void sendMessage(Command c, String params) throws IOException {
        String pkg = new Message()
                .setCommand(c)
                .setParams(params)
                .buildPackage();
        this.com.write_string(pkg);
    }

    public Message waitForMessage() throws IOException {
        return new Message(this.com.read_string());
    }

    public void close() {
        try {
            this.com.close();
            this.mySocket.close();
        } catch (IOException e) {
            //TODO
        }
    }
}