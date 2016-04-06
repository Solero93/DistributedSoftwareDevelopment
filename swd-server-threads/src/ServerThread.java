import exceptions.ReadGridException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class that represents Thread Server
 */
public class ServerThread {
    private String layout;
    private int mode;
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    private static final int MAX_THREADS = 10;
    private int numGames;

    /**
     * Constructs the Thread Server
     *
     * @param port   : Port to assign server to
     * @param layout : Layout it'll use
     * @param mode   : Mode it'll play in
     * @throws IOException
     */
    public ServerThread(int port, String layout, int mode) throws IOException {
        this.layout = layout;
        this.mode = mode;
        this.threadPool = Executors.newFixedThreadPool(MAX_THREADS);
        this.serverSocket = new ServerSocket(port);
        this.numGames = 0;
    }

    /**
     * Main logic of the Threading Server
     */
    public void serveClients() {
        System.out.println("Server serving at");
        System.out.println("\tAddress " + this.serverSocket.getInetAddress());
        System.out.println("\tPort " + this.serverSocket.getLocalPort());
        System.out.println("");
        while (true) {
            Socket sock = null;
            try {
                sock = serverSocket.accept();
                System.out.println("Client with address " + sock.getInetAddress() + " connected to server");
                this.threadPool.execute(new Game(sock, layout, mode, this.numGames++));
                System.out.println("Client with address " + sock.getInetAddress() + " served by a thread");
                System.out.println("");
            } catch (IOException e) {
                System.err.println("There has been an error with the client with address: " +
                        (sock != null ? sock.getInetAddress() : null));
                break;
            } catch (ReadGridException e) {
                System.err.println("There has been an error when trying to create Grid from specified layout" +
                        "of client with address: " + (sock != null ? sock.getInetAddress() : null));
                break;
            }
        }
    }

    /**
     * Shuts down all components of Server
     */
    public void close() {
        this.threadPool.shutdown();
        try {
            // Wait a while for existing tasks to terminate
            if (!this.threadPool.awaitTermination(5, TimeUnit.SECONDS)) {
                this.threadPool.shutdownNow(); // Cancel currently executing tasks
                // Wait a while for tasks to respond to being cancelled
                if (!this.threadPool.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("Pool did not terminate");
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
            System.err.println("Couldn't close server Socket.");
        }
    }
}