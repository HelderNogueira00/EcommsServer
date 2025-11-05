
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;

public class DownloadsManager {

    private boolean isLoaded;
    private final int maxSize;
    private HashMap<Integer, DownloadItem> mItems;
    private HashMap<Integer, NewItem> mNewItems;

    private static DownloadsManager INSTANCE = null;

    public DownloadsManager(int maxSize) {

        INSTANCE = this;
        this.maxSize = maxSize;
    }

    public void load() {

        mNewItems = new HashMap<>();
        if(!isLoaded) {

            mItems = new HashMap<>();
            for(int n = 0; n < maxSize; n++)
                mItems.put(n, new DownloadItem(n));

            isLoaded = true;
        }
    }

    public DownloadItem createItem(String _path) {

        for(int n = 0; n < maxSize; n++) {

            System.out.println("Creating Item: " + _path);
            if(mItems.get(n).isAvailable()) {

                System.out.println("Creating Item: " + _path);
                mItems.get(n).assign(_path);
                System.out.println("Creating Item: " + _path);
                return mItems.get(n);
            }
        }

        return null;
    }

    public void addItem(int _itemID, int _length, String _path) {

        NewItem item = new NewItem(_itemID, _length, _path);
        mNewItems.put(-_itemID, item);
    }

    public DownloadItem getItem(int id) {

        return mItems.get(id);
    }

    public NewItem getNewItem(int _id) {

        return mNewItems.get(_id);
    }

    public void removeItem(int id) {

        mItems.get(id).clean();
    }

    public class NewItem {

        public int id;
        private long readPos;
        private long length;
        private String path;

        private File f;
        private FileOutputStream fos;

        public NewItem(int _id, int _length, String _path) {

            try {

                this.id = _id;
                this.path = _path;
                this.length = _length;

                this.f = new File(_path);
                this.fos = new FileOutputStream(f);
            }
            catch(Exception _e) { System.out.println(); }
        }       

        public void writeBlock(byte[] _buffer) {

            try {
                fos.write(_buffer);
                readPos += _buffer.length;
            }
            catch(Exception _e) { _e.printStackTrace(); }
        }
    }

    public static DownloadsManager getInstance() { return INSTANCE; }
}