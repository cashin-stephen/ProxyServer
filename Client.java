import java.io.*;
import java.net.*;

public class Client {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    public void startConnection(String ip, int port) throws Exception {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public String sendMessage(String msg) throws Exception {
        //out.println(msg);
        String resp = msg;
        return resp;
    }

    public void stopConnection() throws Exception{
        in.close();
        out.close();
        clientSocket.close();
    }

    public static void main(String[] args) throws Exception{
        System.out.println("Client Starting up");
        Client client = new Client();
        client.startConnection("127.0.0.1", 6666);
        System.out.println("Connection made");
        client.sendMessage("hello server");
        client.stopConnection();
    }
}