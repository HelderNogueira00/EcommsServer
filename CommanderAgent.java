public class CommanderAgent extends AgentBase {

    public final int AgentConnected = 2;
    public final int Connectivity = 3;
    public final int OnPlayRequest = 4;
    public final int OnTransferStart = 5;
    public final int OnTransferData = 6;
    public final int OnTransferEnd = 7;
    public final int FileExists = 8;
    public final int FileExistsResult = 9;

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
        }
    }

    private void onTransferData(NetworkPacket _pck) {

        int itemID = _pck.readInt();
        long writtenLength = _pck.readLong();
        DownloadItem currentItem = DownloadsManager.getInstance().getItem(itemID);
        
        if(currentItem != null) {

            if(currentItem.readPos == writtenLength) {

                if()
                NetworkPacket pck = new NetworkPacket(OnTransferData);
                pck.write(itemID);
                pck.write(iyem);
            } else {

                //Resend Packet, MAYBE?
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
        String filePath = BashManager.RunCommand("ecomms_play.sh --play " + "\"" + name + "\"");
        DownloadItem item = DownloadsManager.getInstance().createItem(filePath);

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

            NetworkPacket pck = new NetworkPacket(OnTransferData);
            pck.write(item.id);
            pck.write(buffer.length);
            pck.write(buffer);
            return;
        }

        DownloadsManager.getInstance().removeItem(itemID);
    }
    
}