package card.blink.com.fileexplore.gson;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/6/1.
 */
public class FileListData {

    public String type;

    public String path;

    public Disk[] disk;

    public Folder[] folder;

    public Document[] document;


    public int result;

    public class Disk {
        public String name;
        public long total;
        public long free;

        @Override
        public String toString() {
            return "Disk{" +
                    "name='" + name + '\'' +
                    ", total=" + total +
                    ", free=" + free +
                    '}';
        }
    }

    public class Folder {
        public String name;
        public long time;

        @Override
        public String toString() {
            return "Folder{" +
                    "name='" + name + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

    public class Document {
        public String name;
        public String size;
        public long time;

        @Override
        public String toString() {
            return "Document{" +
                    "name='" + name + '\'' +
                    ", size='" + size + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "FileListData{" +
                "type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", disk=" + Arrays.toString(disk) +
                ", folder=" + Arrays.toString(folder) +
                ", document=" + Arrays.toString(document) +
                ", result=" + result +
                '}';
    }
}
