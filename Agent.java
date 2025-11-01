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
    private AgentBase mType;
    private Thread agentThread;

    public Agent(int id) {

        mID = id;
    }

    public void init(SSLSocket socket) {


        try {

            mSocket = socket;
            mInput = mSocket.getInputStream();
            mOutput = mSocket.getOutputStream();

            mType = new PendingAgent(this);   
            agentThread = new Thread(new Runnable() {

                @Override
                public void run() { while(true) receive(); } 
            });

            agentThread.start();
        }
        catch(Exception _e) { System.out.println("Agent Connection Error: " + _e.getMessage()); disconnect(_e.getMessage()); }
    }

    public void receive() {

       try {

             if(mInput == null)
                return;

            ArrayList<Byte> receivedBuffer = new ArrayList<>();
            while(receivedBuffer.size() < 4) {

                byte val = (byte)mInput.read();
                if(val == -1)
                    disconnect("Null byte received!");
                
                    receivedBuffer.add(val);
            }
            
            int pckLength = ByteBuffer.allocate(4).put(UtilsManager.ToByteArray(receivedBuffer)).getInt(0);
            while(receivedBuffer.size() < pckLength + 4) {

                byte val = (byte)mInput.read();
                if(val == -1)
                    disconnect("Null byte received!");
                
                receivedBuffer.add(val);
            }

            mType.processPacket(new NetworkPacket(receivedBuffer));
        }
        catch(Exception _e) { System.out.println("Client Receiving Error: " + _e.getMessage()); }
    }

    public void promote(int _type) {

        mType.close();
        switch(_type) {

            default: disconnect("Invalid Agent!"); break;
            
            case AgentTypes.Commander: mType = new CommanderAgent(this); break;
        }
    }

    public void send(NetworkPacket _pck) {

        System.out.println("running");
        try {
            if(mOutput != null) {

                byte[] buffer = _pck.prepare();
                mOutput.write(buffer);
                mOutput.flush();
            }
        }
        catch(Exception _e) { System.out.println("Agent Sending Error: " + _e.getMessage()); }
    }

    public void disconnect(String _err) {

        try {

            if(mSocket != null)
                mSocket.close();
            
            if(mInput != null)
                mInput.close();

            if(mOutput != null)
                mOutput.close();

            if(mType != null)
                mType.close();
            
            mInput = null;
            mOutput = null;
            mSocket = null;
            mType = null;
        }
        catch(Exception _e) { System.out.println("Agent Disconnected: " + _err); }
    } 

    public boolean isConnected() { return mSocket.isConnected() && mInput != null && mOutput != null; }

    public boolean isAvailable() {

        return mSocket == null && mOutput == null && mInput == null && mType == null;
    }
}