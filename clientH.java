import java.net.*;

class clientH {

    public static void main(String[] args) throws Exception {
        URL url = new URL("http://google.com");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        System.out.println("no Errors");
        System.out.println("Printing URL: " + url);
        System.out.println("Printing CON: " + con);
    }
}