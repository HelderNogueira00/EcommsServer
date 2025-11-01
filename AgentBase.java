
public abstract class AgentBase {

    protected int mID;
    protected Agent mAgent;
    
    AgentBase(Agent _base) {

        mAgent = _base;
    }

    protected void close() {

        mID = -1;
        mAgent = null;
        onClosed();
    }

    public void processPacket(NetworkPacket _pck) {

        int length = _pck.readInt();
        int commandID = _pck.readInt();
        onPacketReceived(length, commandID, _pck);
    }

    public abstract void onClosed();
    public abstract void onPacketReceived(int length, int commandID, NetworkPacket _pck);
}