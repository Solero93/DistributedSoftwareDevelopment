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
        //this.mySocket.setSoTimeout(3 * 1000); TODO LALALA
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
        this.com.write_string_util(pkg);
    }

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
                } catch (NumberFormatException ex){
                    throw new IOException();
                }
                break;
        }
        return msg;
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