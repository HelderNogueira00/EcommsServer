public class CommanderAgent extends AgentBase {

    public final int AgentConnected = 2;
    public final int Connectivity = 3;

    public CommanderAgent(Agent _base) {
        
        super(_base);
        
        NetworkPacket pck = new NetworkPacket(AgentConnected);
        pck.write(EnumsList.AGENT_COMMANDER);
        mAgent.send(pck);
    }

    @Override
    public void onClosed() {

    }
    
    @Override
    public void onPacketReceived(int length, int commandID, NetworkPacket _pck) {

        switch(commandID) {

            case Connectivity -> checkConnectivity(_pck);
        }
    }

    private void checkConnectivity(NetworkPacket _pck) {

        NetworkPacket pck = new NetworkPacket(Connectivity);
        pck.write(0);
        mAgent.send(pck);
    }
    
}