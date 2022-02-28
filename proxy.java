import java.net.*;
import java.util.*;
import java.io.*;
import javax.net.ssl.HttpsURLConnection;
import java.security.cert.Certificate;


public class proxy {

    private ServerSocket serverSocket;  // Socket for the proxy server
    private Socket clientSocket;        // socket for the Client
    private Socket mcSocket;            // Socket for the Management Console
    private PrintWriter clientOut;      // All Communication to the Client
    private BufferedReader clientIn;    // All Communication from the Client
    private PrintWriter mcOut;          // All Communication to the Management Console
    private BufferedReader mcIn;        // All Communication from the Management Console
    public static void main(String[] args) throws Exception{
        System.out.println("Awaiting Connection");
        proxy server = new proxy();
        server.start(5000);
    }

    public void start(int port) throws Exception{
         while(true) {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            mcSocket = new Socket("127.0.0.1", 5002);
            mcOut = new PrintWriter(mcSocket.getOutputStream(), true);
            mcIn = new BufferedReader(new InputStreamReader(mcSocket.getInputStream()));
            clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String url = clientIn.readLine();
            System.out.println(url);

            //Send request to the Management Console
            mcOut.println(url);

            //determine http or https
            if(url.matches("https.*")) {
                clientOut.println(requestHttps(url));
            }
            else if(url.matches("http.*")) {
                clientOut.println(requestHttp(url));
            }
            else {
                System.out.println("Bad URL");
            }
            stop();
        }
    }

    public void stop() throws Exception {
        mcIn.close();
        mcOut.close();
        clientIn.close();
        clientOut.close();
        clientSocket.close();
        serverSocket.close();
    }

    public static String requestHttps(String urlS) throws Exception {
        System.out.println("HTTPS Case");
        URL url = new URL (urlS);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("Printing URL: " + url);
        System.out.println("Printing CON: " + con);
        return getFullResponse(con);
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
            content.append('\n');
        }
        in.close();
        fullResponseBuilder.append(content);
        return fullResponseBuilder.toString();
    }

    private static String getFullResponse(HttpsURLConnection con) throws Exception{
        StringBuilder fullResponseBuilder = new StringBuilder();

        //getting https Certs
        fullResponseBuilder.append("Response Code : " + con.getResponseCode());
        fullResponseBuilder.append("Cipher Suite : " + con.getCipherSuite());
        fullResponseBuilder.append("\n");
                    
        Certificate[] certs = con.getServerCertificates();
        for(Certificate cert : certs){
            fullResponseBuilder.append("Cert Type : " + cert.getType()+ '\n');
            fullResponseBuilder.append("Cert Hash Code : " + cert.hashCode()+ '\n');
            fullResponseBuilder.append("Cert Public Key Algorithm : " 
                                        + cert.getPublicKey().getAlgorithm()+ '\n');
            fullResponseBuilder.append("Cert Public Key Format : " 
                                        + cert.getPublicKey().getFormat()+ '\n');
            fullResponseBuilder.append('\n');
        }

        //getting Content
        BufferedReader in = new BufferedReader(
        new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
            content.append("\n");
        }
        in.close();
        fullResponseBuilder.append(content);
        return fullResponseBuilder.toString();
    }
        
        

}