
import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class SSLServer {

    private final int MAX_AGENTS;
    private final int SERVER_PORT;
    private final String SERVER_IP;

    private boolean isRunning;
    private SSLContext mContext;
    private SSLServerSocket mSocket;

    private HashMap<Integer, Agent> mAgents;
    private static SSLServer INSTANCE = null;

    public SSLServer(String serverIP, int serverPort, int maxAgents) {

        INSTANCE = this;
        this.SERVER_IP = serverIP;
        this.MAX_AGENTS = maxAgents;
        this.SERVER_PORT = serverPort;

        loadVars();
        loadSSL();
    }

    public void init() {

        try {

            InetAddress addr = InetAddress.getByName(SERVER_IP);
            SSLServerSocketFactory ssf = mContext.getServerSocketFactory();
            mSocket = (SSLServerSocket)ssf.createServerSocket(SERVER_PORT, 50, addr);

            isRunning = true;
            mSocket.setNeedClientAuth(false);
            System.out.println("SSL Server Has Been Initialized: " + SERVER_IP + ":" + SERVER_PORT);

            while(isRunning)
                accept();
        }
        catch(Exception _e) { System.out.println("Server Init Exception: " + _e.getMessage()); }
    }

    private void accept() {

        try {

            SSLSocket clientSocket = (SSLSocket)mSocket.accept();
            for(int n = 0; n < MAX_AGENTS; n++) {

                if(mAgents.get(n).isAvailable()) {

                    mAgents.get(n).init(clientSocket);
                    return;
                }
            }

            clientSocket.close();
            System.out.println("No AVailable SPace For More AGents!");
        }
        catch(Exception _e) { System.out.println("Accepting Client Error: " + _e.getMessage()); }
    }

    private void loadVars() {

        mAgents = new HashMap<>();

        for(int n = 0; n < MAX_AGENTS; n++)
            mAgents.put(n, new Agent(n));
    }

    private void loadSSL() {

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

    public static SSLServer getInstance() { return INSTANCE; }
}