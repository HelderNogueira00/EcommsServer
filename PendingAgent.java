public class PendingAgent extends AgentBase {

    public PendingAgent(Agent _base) {

        super(_base);
        addCommand(0, "Authentication");
        addCommand(1, "Disconnection");
    }

    @Override
    public void onPacketReceived(NetworkPacket _pck) {

        int commandID = _pck.readInt();
        switch(getCommandName(commandID)) {

            case "Authentication" -> onAuthentication(mAgent, _pck);
            case "Disconnection" -> onDisconnection(mAgent, _pck);
        }
    }

    private void onAuthentication(Agent _base, NetworkPacket _pck) {

        System.out.println("Authentication Received: ");
    }

    private void onDisconnection(Agent _base, NetworkPacket _pck) {

        System.out.println("Disconnection Received: ");
    }
}