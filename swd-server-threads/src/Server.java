import java.io.IOException;

/**
 * Main class with the Server
 */
public class Server {
    public static void main(String args[]) {
        // Parsing command line arguments
        String helpMessage = "Us: java Server -p <port> [-f layout] [-i 1|2]";
        for (String s : args) {
            if (s.equals("-h")) {
                System.out.println(helpMessage);
                return;
            }
        }
        int port = -1;
        String layout = null;
        int mode = 1; // Default value of mode

        for (int i = 0; i < args.length; i += 2) {
            try {
                switch (args[i]) {
                    case "-p":
                        port = Integer.parseInt(args[i + 1]);
                        break;
                    case "-f":
                        layout = args[i + 1];
                        break;
                    case "-i":
                        mode = Integer.parseInt(args[i + 1]);
                        break;
                    default:
                        System.err.println("Error in  command format\n" + helpMessage);
                        return;
                }
            } catch (NumberFormatException ex) {
                System.err.println("Error parsing numerical arguments\n" + helpMessage);
                return;
            }
        }
        if (port == -1) {
            System.err.println("Missing parametres\n" + helpMessage);
            return;
        }
        if (mode != 1 && mode != 2) {
            System.err.println("Invalid mode\n" + helpMessage);
            return;
        }

        ServerThread serverThread;
        try {
            serverThread = new ServerThread(port, layout, mode);
        } catch (IOException e) {
            System.err.println("There has been an error trying to create the server.");
            return;
        }
        serverThread.serveClients();
        serverThread.close();
        System.out.println("Server ended. Thank you for playing battleships!");
    }
}