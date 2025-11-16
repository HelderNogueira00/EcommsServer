public class CommanderAgent extends AgentBase {

    public final int AgentConnected = 2;
    public final int Connectivity = 3;
    public final int OnPlayRequest = 4;
    public final int OnTransferStart = 5;
    public final int OnTransferData = 6;
    public final int OnTransferEnd = 7;
    public final int FileExists = 8;
    public final int FileExistsResult = 9;
    public static final int PowerOnPlug = 2001;
    public static final int PowerOffPlug = 2002;

    private int waitingCommand = -1;

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
            case OnPlayRequest -> onPlayLocalReceived(_pck);
            case OnTransferStart -> onTransferStart(_pck);
            case OnTransferData -> onTransferData(_pck);
            case OnTransferEnd -> onTransferEnd(_pck);
            case PowerOnPlug -> onPowerOn(_pck);
            case PowerOffPlug -> onPowerOff(_pck);
        }
    }

    private void onPowerOn(NetworkPacket _pck) {

        String username = _pck.readString();
        Agent agent = SSLServer.getInstance().getAgentByUsername(username);

        if(agent == null) {

            System.out.println("No AGent Found With THis Username: " + username);
            return;
        }

        System.out.println("Sending Poweron plug");
        NetworkPacket pck = new NetworkPacket(PowerOnPlug);
        pck.write(0);
        agent.send(pck);
    }

    private void onPowerOff(NetworkPacket _pck) {

        String username = _pck.readString();
        Agent agent = SSLServer.getInstance().getAgentByUsername(username);

        if(agent == null) {

            System.out.println("No AGent Found With THis Username: " + username);
            return;
        }

        System.out.println("Sending Poweroff plug: " + agent.getRemoteIP());
        NetworkPacket pck = new NetworkPacket(PowerOffPlug);
        pck.write(0);
        agent.send(pck);
    }

    private void onTransferEnd(NetworkPacket _pck) {

        System.out.println("transfer ended");
        int itemID = _pck.readInt();
        long writtenLength = _pck.readLong();
        DownloadItem item = DownloadsManager.getInstance().getItem(itemID);

        if(item.length == writtenLength) {

            System.out.println("File TRansfered Successfully!");
        }
        else
            System.out.println("File TRansfer Error.");
    }

    private void onTransferData(NetworkPacket _pck) {

        int itemID = _pck.readInt();
        long writtenLength = _pck.readLong();
        DownloadItem currentItem = DownloadsManager.getInstance().getItem(itemID);

        if(currentItem != null) {
        System.out.println("transfer in progress");

            if(currentItem.readPos == writtenLength) {

                if(currentItem.readPos < currentItem.length) {

                    byte[] buffer = currentItem.readBlock();
                    NetworkPacket pck = new NetworkPacket(OnTransferData);
                    pck.write(itemID);
                    pck.write(buffer.length);
                    pck.write(buffer);
                    mAgent.send(pck);
                }
                else {

                    NetworkPacket pck = new NetworkPacket(OnTransferEnd);
                    pck.write(currentItem.id);
                    pck.write(currentItem.readPos);
                    mAgent.send(pck);
                }
            } else {

                System.out.println("Packet Not Received Correctly!");
            }
        }
    }

    private void checkConnectivity(NetworkPacket _pck) {

        NetworkPacket pck = new NetworkPacket(Connectivity);
        pck.write(0);
        mAgent.send(pck);
    }

    private void onPlayLocalReceived(NetworkPacket _pck) {

        int type = _pck.readInt();
        String name = _pck.readString();
        String filePath = BashManager.RunCommand(EnvironmentVars.EcommsBashAPI + "ecomms_play.sh --play " + "\"" + name + "\"");
        DownloadItem item = DownloadsManager.getInstance().createItem(filePath);
        System.out.println("Path: " + item.path);

        if(item != null) {

            NetworkPacket pck = new NetworkPacket(OnTransferStart);
            pck.write(item.id);
            pck.write(item.path);
            pck.write(item.length);
            pck.write(item.blockSize);
            mAgent.send(pck);
        }
        else {

            //send not is full!
        }
    }

    private void onTransferStart(NetworkPacket _pck) {

        int itemID = _pck.readInt();
        boolean ok = _pck.readBool();

        if(ok) {

            DownloadItem item = DownloadsManager.getInstance().getItem(itemID);
            byte[] buffer = item.readBlock();

            System.out.println("Starting TRansfer: ");
            NetworkPacket pck = new NetworkPacket(OnTransferData);
            pck.write(item.id);
            pck.write(buffer.length);
            pck.write(buffer);
            return;
        }

        DownloadsManager.getInstance().removeItem(itemID);
    }
    
}