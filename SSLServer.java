
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class SSLServer {

    private final int SERVER_PORT;
    private final String SERVER_IP;

    private SSLContext mContext;
    private SSLServerSocket mSocket;

    public SSLServer(String serverIP, int serverPort) {

        this.SERVER_IP = serverIP;
        this.SERVER_PORT = serverPort;

        load();
    }

    public void init() {

        try {

            InetAddress addr = InetAddress.getByName(SERVER_IP);
            SSLServerSocketFactory ssf = mContext.getServerSocketFactory();
            mSocket = (SSLServerSocket)ssf.createServerSocket(SERVER_PORT, 50, addr);

            mSocket.setNeedClientAuth(true);
            System.out.println("SSL Server Has Been Initialized: " + SERVER_IP + ":" + SERVER_PORT);

            while(true) {
                
                SSLSocket clientSocket = (SSLSocket)mSocket.accept();
                System.out.println("Client Connected");
            }
        }
        catch(Exception _e) { System.out.println("Server Init Exception: " + _e.getMessage()); }
    }

    private void load() {

        try {

            String textData = Files.readString(new File(EnvironmentVars.KeystorePasswordFile).toPath());
            char[] ksPassword = textData.replaceAll("\r", "").replaceAll("\n", "").toCharArray();

            KeyStore ts = KeyStore.getInstance("PKCS12");
            KeyStore ks = KeyStore.getInstance("PKCS12");

            ks.load(new FileInputStream(EnvironmentVars.KeystorePath), ksPassword);
            ts.load(new FileInputStream(EnvironmentVars.TruststorePath), ksPassword);
        
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            tmf.init(ts);
            kmf.init(ks, ksPassword);

            mContext = SSLContext.getInstance("TLS");
            mContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        catch(Exception _e) { System.out.println("Server Load Exception: " + _e.getMessage()); }
    }
}