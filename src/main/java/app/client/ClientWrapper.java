package app.client;

import java.util.List;

/**
 * Created by Admin on 09.11.2016.
 */
public class ClientWrapper {
    private List<Client> clients;

    public List<Client> getClients() {
        return clients;
    }

    public ClientWrapper(List<Client> clients) {
        this.clients = clients;
    }

    public ClientWrapper() {

    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
