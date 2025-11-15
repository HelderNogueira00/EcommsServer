public class HomeAutomationAgent extends AgentBase {

    private static final int AgentConnected = 2;
        public static final int PowerOnPlug = 2001;
        public static final int PowerOffPlug = 2002;

    public HomeAutomationAgent(Agent _base) {

        super(_base);

        NetworkPacket pck = new NetworkPacket(AgentConnected);
        pck.write(EnumsList.AGENT_ANDROID_HOME);
        mAgent.send(pck);
    }

    @Override
    public void onClosed() {

        mAgent = null;
    }

    @Override
    public void onPacketReceived(int _length, int _commandID, NetworkPacket _pck) {

        switch(_commandID) {

            
        }
    }
}