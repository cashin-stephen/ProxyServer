import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class clientHandler implements Runnable {

    private Thread t;
    private String threadName;
    private final Socket clientSocket;
    private Socket mcSocket;            // Socket for the Management Console
    private PrintWriter clientOut;      // All Communication to the Client
    private BufferedReader clientIn;    // All Communication from the Client
    private PrintWriter mcOut;          // All Communication to the Management Console
    private BufferedReader mcIn;        // All Communication from the Management Console
    private HashMap<String, String> cache = new HashMap<String,String>();

    clientHandler(Socket socket, HashMap<String, String> oldCache) {
        this.clientSocket = socket;
        this.cache = oldCache;
    }

    public HashMap<String, String> start () {
        System.out.println("Starting " +  threadName );
        if (t == null) {
           t = new Thread (this, threadName);
           t.start ();
        }
        return cache;
     }

    public void run() {
        try {
            mcSocket = new Socket("127.0.0.1", 5002);
            mcOut = new PrintWriter(mcSocket.getOutputStream(), true);
            mcIn = new BufferedReader(new InputStreamReader(mcSocket.getInputStream()));
            clientOut = new PrintWriter(clientSocket.getOutputStream(), true);
            clientIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String url = clientIn.readLine();
            System.out.println(url);

            //Send request to the Management Console
            mcOut.println(url);
            Boolean valid = Boolean.parseBoolean(mcIn.readLine());
            //System.out.println(valid);

            //determine validity against blackList
            if(valid == false) {
                System.out.println("Selected URL Blocked");
                clientOut.println("Selected URL blocked");
            }
            //check cache, invalid & bad urls will never be cached
            else if(cache.containsKey(url)) {
                System.out.println("cache match");
                clientOut.println(cache.get(url));
            }

            //determine http or https or Bad URL
            else if(url.matches("https.*")) {
                String req = requestHttps(url);
                cache.put(url, req);
                clientOut.println(req);
            }
            else if(url.matches("http.*")) {
                String req = requestHttp(url);
                cache.put(url, req);
                clientOut.println(req);
            }
            else {
                System.out.println("Bad URL");
            }
            clientSocket.close();
        } catch(Exception e) {System.out.println("error occurred" + "/n" + e);}
        
    }

    public static String requestHttps(String urlS) throws Exception {
        System.out.println("HTTPS Case");
        URL url = new URL (urlS);
        HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("Printing URL: " + url);
        System.out.println("Printing CON: " + con);
        con.setInstanceFollowRedirects(true);
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