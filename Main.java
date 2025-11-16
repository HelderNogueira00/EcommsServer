public class Main {

    public static void main(String[] _args) {

        SSLServer server = new SSLServer("10.8.0.1", 4520, 10);
        server.init();

        //String clientToken = UtilsManager.ToAES256HashString("4d564gy51uz31pç5@135a13e1645HD61g5re4#CB9w2g1f3&");
        //System.out.println(clientToken);
    }
}