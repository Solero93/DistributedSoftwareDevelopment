package communication;

import exceptions.ReadGridException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class that represents the ServerCore of the Game
 */
public class ServerCore {
    String layout;
    int port, mode;
    ServerSocket serverSocket;
    ExecutorService threadPool;
    private static final int MAX_THREADS = 10;

    public ServerCore(int port, String layout, int mode) throws IOException {
        this.port = port;
        this.layout = layout;
        this.mode = mode;
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        this.serverSocket = new ServerSocket(port);
    }

    public void serveClients() {
        System.out.println("Server serving at");
        System.out.println("\tAddress " + this.serverSocket.getInetAddress());
        System.out.println("\tPort " + this.serverSocket.getLocalPort());
        System.out.println("\n");
        while (true) {
            Socket sock = null;
            try {
                sock = serverSocket.accept();
                System.out.println("Client with address " + sock.getInetAddress() + " connected to server");
                this.threadPool.execute(new Game(sock, layout, mode));
                System.out.println("Client with address " + sock.getInetAddress() + " served by a thread");
            } catch (IOException e) {
                System.out.println("There has been an error with the client of address" +
                        (sock != null ? sock.getInetAddress() : null));
                this.shutDownAll();
                break;
            } catch (ReadGridException e) {
                System.out.println("There has been an error when trying to create Grid from specified layout");
                this.shutDownAll();
                break;
            }
        }
    }

    private void shutDownAll() {
        this.threadPool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!this.threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                this.threadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!this.threadPool.awaitTermination(5, TimeUnit.SECONDS))
                    System.out.println("Pool did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            this.threadPool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        try {
            this.serverSocket.close();
        } catch (IOException ex) {
            System.out.println("Couldn't close server Socket.");
        }
    }
}