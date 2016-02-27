package communication;

import controller.Client;

import java.util.ArrayList;

/**
 * Class that represents the -- Object
 */
public class Communication {
    private ArrayList<Client> clients;

    public Communication(){
        this.clients = new ArrayList<>();
        // Putting in 2 clients to try out client project
        this.clients.set(0,new Client(0));
        this.clients.set(1,new Client(1));
    }

    public int hitCell(String position, int clientId){
        return this.clients.get(clientId).hitMyCell(position);
    }
}
