import java.net.*;
import javax.print.attribute.standard.RequestingUserName;
import java.util.*;
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
        //out.println("ACK");

        //determine http or https
        if(url.matches("https.*")) {
            out.println(requestHttps(url));
        }
        else if(url.matches("http.*")) {
            out.println(requestHttp(url));
        }
        else {
            System.out.println("Bad URL");
        }
    }

    public static String requestHttps(String url) throws Exception {
        System.out.println("HTTPS Case");
        return "s";
    }

    public static String requestHttp(String urlS) throws Exception {
        URL url = new URL (urlS);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("Printing URL: " + url);
        System.out.println("Printing CON: " + con);

        //Handling Redirects
        con.setInstanceFollowRedirects(true);
        return getFullResponse(con);
    }

    public static String getFullResponse(HttpURLConnection con) throws IOException {
        StringBuilder fullResponseBuilder = new StringBuilder();

        //Reading Response Status Info 
        fullResponseBuilder.append(con.getResponseCode())
            .append(" ")
            .append(con.getResponseMessage())
            .append("\n");

        //Reading Headers

        con.getHeaderFields().entrySet().stream()
            .filter(entry -> entry.getKey() != null)
            .forEach(entry -> {
                fullResponseBuilder.append(entry.getKey()).append(": ");
                List headerValues = entry.getValue();
                Iterator it = headerValues.iterator();
                if (it.hasNext()) {
                    fullResponseBuilder.append(it.next());
                    while (it.hasNext()) {
                        fullResponseBuilder.append(", ").append(it.next());
                    }
                }
                fullResponseBuilder.append("\n");
            });

        //getting response Content

        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        fullResponseBuilder.append(content);
        return fullResponseBuilder.toString();
    }

    public void stop() throws Exception {
        in.close();
        out.close();
        clientSocket.close();
        serverSocket.close();
    }

}