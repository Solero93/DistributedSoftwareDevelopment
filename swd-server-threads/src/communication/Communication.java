package communication;

import exceptions.ReadGridException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents the Communication of the Game
 */
public class Communication {
    String layout;
    int port, mode;
    ServerSocket serverSocket;
    ExecutorService threadPool;
    private static final int MAX_THREADS = 10;

    public Communication(int port, String layout, int mode) throws IOException {
        this.port = port;
        this.layout = layout;
        this.mode = mode;
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        this.serverSocket = new ServerSocket(port);
    }

    public void serveClients() {
        while (true) {
            try {
                Socket sock = serverSocket.accept();
                this.threadPool.submit(new Game(sock, layout, mode));
            } catch (IOException e) {
                System.out.println("There has been an error with the client socket.");
                this.threadPool.shutdown();
                try {
                    this.serverSocket.close();
                } catch (IOException ex) {
                    System.out.println("Couldn't close server Socket.");
                }
                break;
            } catch (ReadGridException e) {
                System.out.println("There has been an error when trying to create Grid from layout");
                this.threadPool.shutdown();
                try {
                    this.serverSocket.close();
                } catch (IOException ex) {
                    System.out.println("Couldn't close server Socket.");
                }
                break;
            }
        }
    }
}