
import java.io.File;
import java.io.FileInputStream;

 public class DownloadItem {

    public int id;
    public long length;
    public int readPos;
    public String path;
    public int blockSize;

    private FileInputStream fin;

    public DownloadItem(int _id) {

        this.id = _id;
    }

    public void assign(String _path) {

        try {

            File f = new File(_path);
            fin = new FileInputStream(f);

            length = f.length();
            readPos = 0;
            blockSize = 512000;
        }
        catch(Exception _e) { _e.printStackTrace(); }
    }

    public byte[] readBlock() {

        byte[] buffer = null;
        try {

            if(fin != null) {

                long bytesToRead = length - readPos;
                int bufferLength = (int)((bytesToRead - blockSize) < 0 ? bytesToRead : blockSize); 

                buffer = new byte[bufferLength];
                for(int n = 0; n < buffer.length; n++)
                    buffer[readPos + n] = (byte)fin.read();

                readPos += bufferLength;
            }
        }
        catch(Exception _e) { System.out.println("Error Reading DOwnload Item: " + _e.getMessage()); }
        return buffer;
    }



    public void clean() {

        path = "";
        length = 0;
        readPos = 0;
        blockSize = 0;
    }

    public boolean isAvailable() { return path.equals(""); }
}   