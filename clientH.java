import java.net.*;
import java.io.*;
import java.util.*;

class clientH {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://google.com");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("no Errors");
        System.out.println("Printing URL: " + url);
        System.out.println("Printing CON: " + con);

        //adding parameters
        // con.setDoOutput(true);
        // DataOutputStream out = new DataOutputStream(con.getOutputStream());
        // out.writeBytes("hl=fr");
        // out.flush();
        // out.close();

        //setting Request Headers
        // con.setRequestProperty("Content-Type", "application/json");
        // String contentType = con.getHeaderField("Content-Type");
        // System.out.println("Printing ContentType: " + contentType);

        //Handling Redirects
        con.setInstanceFollowRedirects(true);

        //Reading Response

        // int status = con.getResponseCode();
        // System.out.println("status: " + status);

        // BufferedReader in = new BufferedReader(
        // new InputStreamReader(con.getInputStream()));
        // String inputLine;
        // StringBuffer content = new StringBuffer();
        // while ((inputLine = in.readLine()) != null) {
        //     content.append(inputLine);
        // }
        // in.close();

        System.out.println(getFullResponse(con));

        //end connection
        con.disconnect();
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
}