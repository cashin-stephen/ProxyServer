import java.net.*;
import java.util.*;
import java.io.*;

public class mConsole {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private static List<String> blackList = new ArrayList<String>();
    public static void main(String[] args) throws Exception {
        System.out.println("Starting Management Console");
        blackList.add("facebook.com");
        blackList.add("instagram.com");
        blackList.add("twittter.com");
        mConsole mc = new mConsole();
        mc.start(5002);
    }

    public static Boolean checkBlackList(String url) {
        for(int i=0; i<blackList.size(); i++) {
            if(url.equals(blackList.get(i)) || url.contains(blackList.get(i)))
                return false;
        }
        return true;
    }

    public void start(int port) throws Exception {
        serverSocket = new ServerSocket(port);
        clientSocket = serverSocket.accept();
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String url = in.readLine();
        System.out.println("Handling Request for " + url);
    }
}