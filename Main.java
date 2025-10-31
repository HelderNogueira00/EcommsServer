public class Main {

    public static void main(String[] _args) {

        SSLServer server = new SSLServer("10.8.0.1", 4520);
        server.init();
    }
}