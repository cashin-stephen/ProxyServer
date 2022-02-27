import java.net.*;
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
        //while (true) {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String greeting = in.readLine();
            System.out.println(greeting);
            out.println("ACK");
                // if ("hello server".equals(greeting)) {
                //     System.out.println("hello client");
                // }
                // else {
                //     out.println("unrecognised greeting");
                // }
        //}
    }

    public void stop() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

}