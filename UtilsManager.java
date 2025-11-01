
import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HexFormat;

public class UtilsManager {

    public static byte[] ToByteArray(ArrayList<Byte> _buffer) {

        byte[] buffer = new byte[_buffer.size()];
        for(int n = 0; n < _buffer.size(); n++)
            buffer[n] = _buffer.get(n);

        return buffer;
    }

    public static byte[] ToAES256Hash(String _input) {

        byte[] buffer = null;
        try {

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            buffer = digest.digest(_input.getBytes());
        }
        catch(Exception _e) { System.out.println("Hashing Error: " + _e.getMessage()); }
        return buffer;
    }
    
    public static String ToAES256HashString(String _input) {

        byte[] buffer = ToAES256Hash(_input);
        return HexFormat.of().formatHex(buffer);
    }

    public static byte[] ReadFile(String _path) {

        byte[] buffer = null;
        
        try {

            File f = new File(_path);
            if(f.exists()) {

                buffer = new byte[(int)f.length()];
                FileInputStream fin = new FileInputStream(f);
                
                for(int pos = 0; pos < f.length(); pos++)
                    buffer[pos] = (byte)fin.read();
                
                fin.close();
            }
        }
        catch(Exception _e) { System.out.println(); }
        return buffer;
    }
    
}