import java.net.Socket;

/**
 * Created by KrishnChinya on 3/5/17.
 * CLASS TO STORE THE REFERENCE - it store clientname, client is connected to any other client or not
 * client reference socket address and client currently connected to.
 */
public class ClientDetails {
    String clientName;
    boolean connected;
    Socket clientref;
    Socket connectedto;

    public Socket getConnectedto() {
        return connectedto;
    }

    public void setConnectedto(Socket connectedto) {
        this.connectedto = connectedto;
        connected = true;
    }

    public int getClientno() {
        return clientno;
    }

    public void setClientno(int clientno) {
        this.clientno = clientno;
    }

    int clientno;

    //costructor to initialize the class variable

    public ClientDetails(String clientName, boolean connected,int clientno,Socket clientref) {
        this.clientName = clientName;
        this.connected = connected;
        this.clientno = clientno;
        this.clientref = clientref;
    }

    public ClientDetails(String clientName, Socket clientref) {
        this.clientName = clientName;
        this.clientref = clientref;
    }

    //SETTERS AND GETTER METHODS.

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public boolean isConnected() {
        return connected;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public Socket getClientref() {
        return clientref;
    }

    public void setClientref(Socket clientref) {
        this.clientref = clientref;
    }
}
