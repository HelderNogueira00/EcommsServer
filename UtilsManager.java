
import java.util.ArrayList;

public class UtilsManager {

    public static byte[] ToByteArray(ArrayList<Byte> _buffer) {

        byte[] buffer = new byte[_buffer.size()];
        for(int n = 0; n < _buffer.size(); n++)
            buffer[n] = _buffer.get(n);

        return buffer;
    }

    
}