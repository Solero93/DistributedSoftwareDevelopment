package communication;

import utils.ComUtils;
import utils.Message;
import utils.enums.Actor;
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

    /**
     * Constructs the Communication in case of Client
     *
     * @param serverName : Name of the server
     * @param port       : Port to connect to
     * @throws IOException
     */
    public Communication(String serverName, int port) throws IOException {
        this.mySocket = new Socket(InetAddress.getByName(serverName), port);
        this.mySocket.setSoTimeout(Actor.CLIENT.timeOut * 1000);
        this.com = new ComUtils(this.mySocket);
    }

    /**
     * Constructs the Communication in case of Threads
     *
     * @param sock : Socket of Client
     * @throws IOException
     */
    public Communication(Socket sock) throws IOException {
        this.mySocket = sock;
        this.mySocket.setSoTimeout(Actor.SERVER.timeOut * 1000);
        this.com = new ComUtils(sock);
    }

    /**
     * Send a Command and its params
     *
     * @param c      : Command to send
     * @param params : Command's params
     * @throws IOException
     */
    public void sendMessage(Command c, String params) throws IOException {
        String pkg = new Message()
                .setCommand(c)
                .setParams(params)
                .buildPackage();
        this.com.write_string_util(pkg);
    }

    /**
     * Blocking read operation that returns when it received a message
     *
     * @return Read Message
     * @throws IOException : In case TimeOut happens or other IO Error
     */
    public Message waitForMessage() throws IOException {
        Message msg = new Message();
        char[] tmp;
        Command cmd = Command.getCommandFromCode(this.com.read_string_util(4));
        msg.setCommand(cmd);
        switch (cmd) {
            case FIRE:
                tmp = this.com.read_string_util(3).toCharArray();
                msg.setParams("" + Character.toUpperCase(tmp[1]) + tmp[2]);
                break;
            case ERROR:
                tmp = this.com.read_string_util(3).toCharArray();
                try {
                    msg.setParams(this.com.read_string_util(Integer.parseInt("" + tmp[1] + tmp[2])));
                } catch (NumberFormatException ex) {
                    throw new IOException();
                }
                break;
        }
        return msg;
    }

    /**
     * Closes all connections.
     */
    public void close() {
        try {
            this.com.close();
            this.mySocket.close();
        } catch (IOException e) {
            //No need to treat
        }
    }
}