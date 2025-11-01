import java.io.InputStream;
import java.io.OutputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import javax.net.ssl.SSLSocket;

public class Agent {

    private final int mID;
    private SSLSocket mSocket;
    private InputStream mInput;
    private OutputStream mOutput;
    private volatile boolean running = false;
    private AgentBase mType;

    public Agent(int id, SSLSocket socket) {

        mID = id;

        try {

            mSocket = socket;
            mInput = mSocket.getInputStream();
            mOutput = mSocket.getOutputStream();

            running = true;
            mType = new PendingAgent(this);   
            System.out.println("Agent Connected, Waiting For Authentication: " + mInput);
            receive();
        }
        catch(Exception _e) { System.out.println("Agent Connection Error: " + _e.getMessage()); disconnect(_e.getMessage()); }
    }

    public void receive() {

        try {

            if (mInput == null)
                return;

            byte[] lengthBuffer = new byte[4];
            mInput.read(lengthBuffer);

            int length = ByteBuffer.allocate(4).put(lengthBuffer).getInt();
            mType.onPacketReceived(new NetworkPacket(length));
            System.out.println("Packet Length: " + length);

            receive();

        }
        catch(Exception _e) { System.out.println("Agent Receiving Error: " + _e.getMessage()); disconnect(_e.getMessage()); }
    }

    public void send(NetworkPacket _pck) {

        System.out.println("running");
        try {
            if(mOutput != null) {

                byte[] buffer = _pck.prepare();
                System.out.println("sending buffer: " + buffer);
                mOutput.write(buffer);
                mOutput.flush();
            }
        }
        catch(Exception _e) { System.out.println("Agent Sending Error: " + _e.getMessage()); }
    }

    public void disconnect(String _err) {

        try {

            if(mSocket != null) {

                mInput.close();
                mOutput.close();
                mSocket.close();
            }           

            mInput = null;
            mOutput = null;
            mSocket = null;
        }
        catch(Exception _e) { System.out.println("Agent Disconnected: " + _err); }
    } 
}