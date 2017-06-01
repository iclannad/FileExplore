package card.blink.com.httpdemo;

import java.util.Arrays;

/**
 * Created by Administrator on 2017/6/1.
 */
public class Data {

    public String type;

    public String path;

    public Folder[] folder;

    public Document[] document;


    public int result;

    class Folder {
        public String name;
        public int time;

        @Override
        public String toString() {
            return "Folder{" +
                    "name='" + name + '\'' +
                    ", time=" + time +
                    '}';
        }
    }

    class Document {
        public String name;
        public String size;
        public int time;

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
        return "Data{" +
                "type='" + type + '\'' +
                ", path='" + path + '\'' +
                ", folder=" + Arrays.toString(folder) +
                ", document=" + Arrays.toString(document) +
                ", result=" + result +
                '}';
    }
}
