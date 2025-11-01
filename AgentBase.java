
import java.util.ArrayList;
import java.util.HashMap;

public abstract class AgentBase {

    protected int mID;
    protected Agent mAgent;
    private HashMap<Integer, String> commandsList;
    
    AgentBase(Agent _base) {

        mAgent = _base;
        commandsList = new HashMap<>();
    }

    public void addCommand(int _id, String _name) {

        commandsList.put(_id, _name);
    }

    public String getCommandName(int id) {

        return commandsList.get(id);
    } 

    public abstract void onPacketReceived(NetworkPacket _pck);
}