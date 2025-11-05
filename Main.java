public class Main {

    public static void main(String[] _args) {

        //SSLServer server = new SSLServer("10.8.0.1", 4520, 10);
        //server.init();

        String clientToken = UtilsManager.ToAES256HashString("a1g65sd4g6s45fz45fz54f5z4f5sad4f'asas&SH48D61S53LZ0EGsfhdf04·nkjldsd");
        System.out.println(clientToken);
    }
}