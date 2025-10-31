
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
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

            SSLServerSocketFactory ssf = mContext.getServerSocketFactory();
            mSocket = (SSLServerSocket)ssf.createServerSocket(SERVER_PORT);

            mSocket.setNeedClientAuth(true);
            System.out.println("SSL Server Has Been Initialized: " + SERVER_IP + ":" + SERVER_PORT);

            while(true) {
                
            }
        }
        catch(Exception _e) { System.out.println("Server Init Exception: " + _e.getMessage()); }
    }

    private void load() {

        try {

            KeyStore ts = KeyStore.getInstance("PKCS12");
            KeyStore ks = KeyStore.getInstance("PKCS12");

            ks.load(new FileInputStream(EnvironmentVars.KeystorePath), EnvironmentVars.KeystorePassword);
            ts.load(new FileInputStream(EnvironmentVars.TruststorePath), EnvironmentVars.TruststorePassword);
        
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");

            tmf.init(ts);
            kmf.init(ks, EnvironmentVars.KeystorePassword);

            mContext = SSLContext.getInstance("TLS");
            mContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
        }
        catch(Exception _e) { System.out.println("Server Load Exception: " + _e.getMessage()); }
    }
}