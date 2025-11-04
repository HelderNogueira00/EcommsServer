
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class BashManager {

    public static String RequestAudio(String _device, String _song) {

        switch(_device) {

            case "local": return RunCommand("ecomms_play.sh --id " + "\"" + _song + "\"");
        }

        return "";
    }

    public static String RunCommand(String _command) {

        String r = "";
        ProcessBuilder pb = new ProcessBuilder();
        pb.command("bash", "-c", _command);

        try {

            Process p = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
        
            String line = "";
            while((line = reader.readLine()) != null)
                r += line;

            int exitCode = p.waitFor();
        }
        catch(Exception _e) { System.out.println("Bash Error: " + _e.getMessage()); }
        return r;
    }
}