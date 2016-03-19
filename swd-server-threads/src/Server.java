import communication.Communication;

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
                        System.out.println("Error in  command format\n" + helpMessage);
                        return;
                }
            } catch (NumberFormatException ex) {
                System.out.println("Error parsing numerical arguments\n" + helpMessage);
                return;
            }
        }
        if (port == -1) {
            System.out.println("Missing parametres\n" + helpMessage);
            return;
        }
        if (mode != 1 && mode != 2) {
            System.out.println("Invalid mode\n" + helpMessage);
            return;
        }

        try {
            Communication communication = new Communication(port, layout, mode);
            communication.serveClients();
        } catch (IOException e) {
            System.out.println("There has been an error trying to create the server.");
        }
        System.out.println("Server ended.");
    }
}