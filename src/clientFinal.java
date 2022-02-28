import java.net.*;
import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

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
        String resp;
        StringBuffer sb = new StringBuffer();
        while((resp = in.readLine()) != null){
            sb.append(resp + " \n");
            //System.out.println(resp);
            //TimeUnit.SECONDS.sleep(1);
        }
        stopConnection();
        return sb.toString();
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