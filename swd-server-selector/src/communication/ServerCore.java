package communication;

import exceptions.EndGameException;
import exceptions.ReadGridException;
import utils.Message;
import utils.enums.Command;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Class that represents the ServerCore of the Game
 */
public class ServerCore {
    private String layout;
    private int mode;
    private Selector selector;
    private ServerSocketChannel server;
    private SelectionKey serverKey;
    private CharsetEncoder encoder;
    private CharsetDecoder decoder;
    private ByteBuffer buffer;
    private int numGame;

    public ServerCore(int port, String layout, int mode) throws IOException {
        this.layout = layout;
        this.mode = mode;
        this.selector = Selector.open();
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new java.net.InetSocketAddress(port)); //TODO revisar
        this.server.configureBlocking(false);
        this.serverKey = this.server.register(selector, SelectionKey.OP_ACCEPT);
        Charset charset = Charset.forName("ISO-8859-1");
        this.encoder = charset.newEncoder();
        this.decoder = charset.newDecoder();
        this.buffer = ByteBuffer.allocate(512);
        this.numGame = 0;
    }

    public void serveClients() {
        System.out.println("Server serving at");
        System.out.println("\tAddress " + this.server.socket().getInetAddress());
        System.out.println("\tPort " + this.server.socket().getLocalPort());
        System.out.println();
        while (true) {
            try {
                this.selector.select();
                Set keys = selector.selectedKeys();
                for (Iterator i = keys.iterator(); i.hasNext();) {
                    SelectionKey key = (SelectionKey) i.next();
                    i.remove();

                    if (key == this.serverKey) {
                        if (key.isAcceptable()) {
                            SocketChannel client = this.server.accept();
                            client.configureBlocking(false);
                            SelectionKey clientKey = client.register(this.selector, SelectionKey.OP_READ);
                            key.attach(new Game(this.layout, this.mode, this.numGame++));
                            System.out.println("Client with address "
                                    + client.socket().getInetAddress() + " connected to server");
                        }
                    } else {
                        SocketChannel client = (SocketChannel) key.channel();
                        if (!key.isReadable()) {
                            if (client.read(this.buffer) == -1){
                                key.cancel();
                                client.close();
                            }
                            continue;
                        }

                        this.buffer.flip();
                        String request = this.decoder.decode(this.buffer).toString();
                        this.buffer.clear();
                        System.out.println("Client with address "
                                + client.socket().getInetAddress() + " sent a message to server: "
                                + request);
                        Game clientGame = (Game)key.attachment();
                        ArrayList<Message> clientMessages = this.readRequest(request);
                        try {
                            for (Message clientMsg : clientMessages) {
                                ArrayList<Message> messagesToSend = clientGame.getNextMessages(clientMsg);
                                for (Message serverMsg : messagesToSend) {
                                    String response = serverMsg.buildPackage();
                                    client.write(encoder.encode(CharBuffer.wrap(response)));
                                    if (serverMsg.getCommand() == Command.YOU_WIN) throw new EndGameException();
                                }
                            }
                        } catch (EndGameException ex){
                            clientGame.close();
                            key.cancel();
                            client.close();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("There has been an error while selecting.");
                break;
            } catch (ReadGridException e) {
                System.err.println("There has been an error when trying to create Grid from specified layout");
                break;
            }
        }
    }

    public void shutDownAll() {
        this.buffer.clear();
        try {
            for (SelectionKey key : this.selector.keys()){
                key.channel().close();
                key.cancel();
            }
            this.server.close();
            this.selector.close();
        } catch (IOException e) {
            //TODO do something
        }

    }

    private ArrayList<Message> readRequest(String req) throws StringIndexOutOfBoundsException{
        ArrayList<Message> finalMessages = new ArrayList<>(2);
        for (int i=0; i<req.length() && finalMessages.size() < 2;){
            String commandCode = req.substring(i,i+4);
            Command cmd = Command.getCommandFromCode(commandCode);
            i+=4;
            String params = null;
            switch(cmd){
                case FIRE:
                    i++;
                    params = req.substring(i,i+2);
                    i+=2;
                    break;
                case ERROR:
                    i++;
                    int numChars = Integer.parseInt("" + req.charAt(i) + req.charAt(i+1));
                    params = req.substring(i,i+numChars);
                    i+=numChars;
                    break;
            }
            finalMessages.add(new Message().setCommand(cmd).setParams(params));
        }
        return finalMessages;
    }
}