import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class verificationHandler implements Runnable{

    Thread t;
    Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;
    private List<String> blackList = new ArrayList<String>();

    verificationHandler(Socket socket, List<String> blackList) {
        this.clientSocket = socket;
        this.blackList = blackList; 
    }

    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String url = in.readLine();
            out.println(checkBlackList(url));
            System.out.println("Handling Request for " + url);
            clientSocket.close();
        } catch(Exception e) {System.out.println("error occurred" + "/n" + e);}
    }

    public Boolean checkBlackList(String url) {
        for (String element : blackList) {
            if(url.contains(element))
                return false;
        }
        return true;
    }
}