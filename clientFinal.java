import java.net.*;
import java.util.*;
import java.io.*;

public class clientFinal {
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public static void main(String[] args) throws Exception {
        System.out.println("Please Enter the Url");
        Scanner input = new Scanner(System.in);
        String url = input.next();
        input.close();
        clientFinal client = new clientFinal();
        System.out.println(client.sendURL(url));
    }

    public String sendURL(String url) throws Exception {
        startConnection("127.0.0.1", 5000);
        out.println(url);
        String resp = in.readLine();
        stopConnection();
        return resp;
    }


    public void startConnection(String ip, int port) throws Exception {
        clientSocket = new Socket(ip, port);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }

    public void stopConnection() throws Exception{
        in.close();
        out.close();
        clientSocket.close();
    }


}