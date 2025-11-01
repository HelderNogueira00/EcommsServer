public class PendingAgent extends AgentBase {

    public final int Auth = 0;
    public final int Disconnect = 1;

    public PendingAgent(Agent _base) {

        super(_base);
        authenticate();
    }

    @Override
    public void onPacketReceived(int length, int commandID, NetworkPacket _pck) {

        switch(commandID) {

            case Auth -> onAuthentication(mAgent, _pck);
            case Disconnect -> onDisconnection(mAgent, _pck);
        }
    }

    @Override
    public void onClosed() {

        mAgent = null;
    }

    private void onAuthentication(Agent _base, NetworkPacket _pck) {

        try {

            NetworkPacket pck = new NetworkPacket(Auth);
            pck.write(1);

            String clientToken = UtilsManager.ToAES256HashString(_pck.readString());
            String[] tokensList = new String(UtilsManager.ReadFile(EnvironmentVars.AuthListFile)).split("\\n");
            
            for(String s : tokensList) {

                String token = s.split(":")[0];
                int type = Integer.parseInt(s.split(":")[1]);

                if(token.equals(clientToken)) {

                    pck.write(0);
                    mAgent.send(pck);
                    mAgent.promote(type);
                    return;
                }
            }

            pck.write(1);
            mAgent.send(pck);
            mAgent.promote(-1);
        }
        catch(Exception _e) { System.out.println("Agent Auth Error: " + _e.getMessage()); }
    }

    private void onDisconnection(Agent _base, NetworkPacket _pck) {

        System.out.println("Disconnection Received: ");
    }

    private void authenticate() {

        NetworkPacket pck = new NetworkPacket(Auth);
        pck.write(0);
        mAgent.send(pck);
    }
}