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
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

/**
 * Class that represents the Server with Selectors
 */
public class ServerSelector {
    private String layout;
    private int mode;
    private Selector selector;
    private ServerSocketChannel server;
    private SelectionKey serverKey;
    private CharsetEncoder encoder;
    private CharsetDecoder decoder;
    private ByteBuffer buffer;
    private int numGame;

    /**
     * Constructs a Server with a Selector.
     *
     * @param port   : Port it'll be assigned to
     * @param layout : Layout file it'll use
     * @param mode   : Mode it'll play in
     * @throws IOException
     */
    public ServerSelector(int port, String layout, int mode) throws IOException {
        this.layout = layout;
        this.mode = mode;
        this.selector = Selector.open();
        this.server = ServerSocketChannel.open();
        this.server.socket().bind(new java.net.InetSocketAddress(port));
        this.server.configureBlocking(false);
        this.serverKey = this.server.register(selector, SelectionKey.OP_ACCEPT);
        Charset charset = Charset.forName("ISO-8859-1");
        this.encoder = charset.newEncoder();
        this.decoder = charset.newDecoder();
        this.buffer = ByteBuffer.allocate(512);
        this.numGame = 0;
    }

    /**
     * Main logic of the Server with Selectors
     */
    public void serveClients() {
        System.out.println("Server serving at");
        System.out.println("\tAddress " + this.server.socket().getInetAddress());
        System.out.println("\tPort " + this.server.socket().getLocalPort());
        System.out.println();
        while (true) {
            try {
                this.selector.select();
            } catch (IOException e) {
                System.err.println("There has been an error while selecting.");
                continue;
            }
            Set keys = selector.selectedKeys();
            for (Iterator i = keys.iterator(); i.hasNext(); ) {
                SelectionKey key = (SelectionKey) i.next();
                i.remove();

                if (key == this.serverKey) {
                    if (key.isAcceptable()) {
                        SocketChannel client;
                        SelectionKey clientKey = null;
                        try {
                            client = this.server.accept();
                            client.configureBlocking(false);
                            clientKey = client.register(this.selector, SelectionKey.OP_READ);
                            clientKey.attach(new Game(this.layout, this.mode, this.numGame++));
                        } catch (IOException e) {
                            System.err.println("There has been an error while trying to accept client");
                            continue;
                        } catch (ReadGridException e) {
                            System.err.println("Couldn't generate grid for client");
                            closeConnection(clientKey);
                            continue;
                        }
                        System.out.println("Client with address: " +
                                client.socket().getInetAddress() + " connected to server.");
                    }
                } else {
                    SocketChannel client = (SocketChannel) key.channel();
                    if (!key.isReadable()) {
                        continue;
                    }
                    int numBytes;
                    try {
                        numBytes = client.read(this.buffer);
                    } catch (IOException e) {
                        System.err.println("Couldn't read to buffer from client with address: " +
                                client.socket().getInetAddress());
                        continue;
                    }
                    if (numBytes == -1) {
                        this.closeConnection(key);
                        continue;
                    }

                    this.buffer.flip();
                    String request;
                    try {
                        request = this.decoder.decode(this.buffer).toString();
                    } catch (CharacterCodingException e) {
                        System.err.println("Error while receiving message");
                        try {
                            client.write(encoder.encode(CharBuffer.wrap(
                                    new Message()
                                            .setCommand(Command.ERROR)
                                            .setParams("Decoding error")
                                            .buildPackage()
                            )));
                        } catch (IOException ex) {
                            System.err.println("Error while writing response");
                            continue;
                        }
                        continue;
                    }
                    this.buffer.clear();
                    Game clientGame = (Game) key.attachment();
                    try {
                        ArrayList<Message> messagesToSend = clientGame.getNextMessages(request);
                        for (Message serverMsg : messagesToSend) {
                            String response = serverMsg.buildPackage();
                            try {
                                client.write(encoder.encode(CharBuffer.wrap(response)));
                            } catch (IOException e) {
                                System.err.println("Error while writing response.");
                                continue;
                            }
                            if (serverMsg.getCommand() == Command.YOU_WIN) throw new EndGameException();
                        }
                    } catch (EndGameException e) {
                        this.closeConnection(key);
                    }
                }
            }
        }
    }

    /**
     * Shuts down all components of Server
     */
    public void close() {
        this.buffer.clear();
        for (SelectionKey key : this.selector.keys()) {
            closeConnection(key);
        }
        try {
            this.server.close();
            this.selector.close();
        } catch (IOException e) {
            System.err.println("There has been an error while shutting down server.");
        }
    }

    /**
     * Closes connection of a client associated to a key
     *
     * @param key : Client's SelectionKey
     */
    public void closeConnection(SelectionKey key) {
        Game clientGame = (Game) key.attachment();
        clientGame.close();
        key.cancel();
        SocketChannel client = (SocketChannel) key.channel();
        try {
            client.close();
        } catch (IOException e) {
            //Shouldn't do anything
        }
        System.out.println("Client disconnected.");
    }
}