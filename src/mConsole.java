import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class mConsole {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private static List<String> blackList = new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Management Console");
        blackList.add("facebook.com");
        blackList.add("instagram.com");
        blackList.add("twittter.com");
        mConsole mc = new mConsole();
        mc.start(5002);
    }

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        serverSocket.setReuseAddress(true);
        while (true) {
            clientSocket = serverSocket.accept();
            verificationHandler consoleSock = new verificationHandler(clientSocket, blackList);
            new Thread(consoleSock).start();
            //clientSocket.close();
        }
    }

}