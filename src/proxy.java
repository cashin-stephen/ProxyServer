import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class proxy {

    private ServerSocket serverSocket;  // Socket for the proxy server
    private Socket clientSocket;        // socket for the Client
    private HashMap<String, String> cache = new HashMap<String,String>();
 
    public static void main(String[] args) throws Exception{
        System.out.println("Awaiting Connection");

        //Start the server on port 5000
        proxy server = new proxy();
        server.startServer(5000);
    }

    public void startServer(int port) throws Exception{
        
        //set up server socket
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);

        //Allow Management Console to accept connection with server and start thread for the request
        while(true) {
            clientSocket = serverSocket.accept();
            clientHandler clientSock = new clientHandler(clientSocket, cache);
            new Thread(clientSock).start();
        }
    }
}