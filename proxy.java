import java.net.*;

import javax.print.attribute.standard.RequestingUserName;

import java.io.*;


public class proxy {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    public static void main(String[] args) throws Exception{
        System.out.println("Awaiting Connection");
        proxy server = new proxy();
        server.start(5000);
    }

    public void start(int port) throws Exception{
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String url = in.readLine();
        System.out.println(url);
        out.println("ACK");

        //determine http or https
        if(url.matches("https.*")) {
            requestHttps();
        }
        else if(url.matches("http.*")) {
            requestHttp();
        }
        else {
            System.out.println("Bad URL");
        }
    }

    public static void requestHttps() {
        System.out.println("HTTPS Case");
    }

    public static void requestHttp() {
        System.out.println("HTTP Case");
    }

    public void stop() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

}