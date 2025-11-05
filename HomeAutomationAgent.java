public class HomeAutomationAgent extends AgentBase {

    private static final int OnPowerOn = 100;
    private static final int OnPowerOff = 101;

    public HomeAutomationAgent(Agent _base) {

        super(_base);
    }

    @Override
    public void onClosed() {

        mAgent = null;
    }

    @Override
    public void onPacketReceived(int _length, int _commandID, NetworkPacket _pck) {

        switch(_commandID) {

            case OnPowerOn -> onPowerOn(_pck);
            case OnPowerOff -> onPowerOff(_pck);
        }
    }

    private void onPowerOn(NetworkPacket _pck) {

        
    }

    private void onPowerOff(NetworkPacket _pck) {


    }
}