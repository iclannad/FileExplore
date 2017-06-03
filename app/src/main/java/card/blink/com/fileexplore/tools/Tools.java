package card.blink.com.fileexplore.tools;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import card.blink.com.fileexplore.adapter.FileListAdapter;
import card.blink.com.fileexplore.adapter.Protocol;

/**
 * Created by Administrator on 2017/5/27.
 */
public class Tools {

    public static ArrayList<String> sortFileList(String parent) {
        File file = new File(parent + "/");
        String[] files = file.list();
        if (files == null)
            return null;
        if (files.length == 0)
            return null;
        else {
            List<String> dir_list = new ArrayList<String>();
            List<String> file_list = new ArrayList<String>();
            for (int i = 0; i < files.length; i++) {
                File tmp = new File(parent + "/" + files[i]);
                if (tmp.isDirectory())
                    dir_list.add(tmp.getPath());
                else
                    file_list.add(tmp.getPath());
            }
            Collections.sort(dir_list);
            Collections.sort(file_list);
            for (String string : file_list) {
                dir_list.add(string);
            }
            return (ArrayList<String>) dir_list;
        }
    }

    /**
     * 获取文件的路径
     *
     * @param path
     * @param positionFile
     * @return
     */
    public static final String GetFilePath(String path, File positionFile) {
        if (path.equals(".")) {
            Log.d("run", "yes");
            return Environment.getExternalStorageDirectory().getPath();
        } else if (path.equals("..")) {
            return positionFile.getParent();
        } else {
            return path;
        }
    }

    /**
     * 获取文件夹下的子文件
     *
     * @param f
     * @param list
     * @return
     */
    public static final ArrayList<FileListAdapter.Pair<String, Integer>> GetFilechild(File f, ArrayList<FileListAdapter.Pair<String, Integer>> list) {
        ArrayList<String> nlist = new ArrayList<>();
        nlist = sortFileList(f.getPath());
        list.clear();
        if (nlist == null) {
            nlist = new ArrayList<>();
            return list;
        }
        for (int i = 0; i < nlist.size(); i++) {
            FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
            pair.setA(nlist.get(i));
            File fA = new File(nlist.get(i));
            if (fA.isDirectory()) {
                pair.setB(Protocol.DIR);
            } else {
                pair.setB(Protocol.FL);
            }
            list.add(pair);
        }
        return list;
    }

    /**
     * long to byte[]
     *
     * @param l
     * @return
     */
    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    /**
     * byte[] to long
     *
     * @param b
     * @return
     */
    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }


    /**
     * 去除数组前面的0，如 00002000，可转化为2000，返回数组的长度为发生变化
     *
     * @param array
     * @return
     */
    public static byte[] resortBytes(byte[] array) {
        int length = array.length;
        int index = 0;
        for (int i = 0; i < length; i++) {
            if (array[i] != 0) {
                break;
            }
            index++;
        }
        byte[] content = new byte[(length - index)];

        for (int i = index; i < array.length; i++) {
            content[i - index] = array[i];

        }

        return content;
    }

    /**
     * 将文件的数据读取到字节数组中，每次最多读取5M的大小
     *
     * @param file
     * @param index
     * @param total
     * @return
     */
    public static byte[] readFileToBytes(File file, int index, long total) {
        final int SIZE = 5 * 1024 * 1024;
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek((index - 1) * SIZE);
            byte[] data;
            if (index == total) {
                int size = (int) (file.length() % SIZE);
                data = new byte[size];
            } else {
                data = new byte[SIZE];

            }

            int read = raf.read(data);
            raf.close();

            return data;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            return null;
        } catch (IOException e) {
            //e.printStackTrace();
            return null;
        }
    }

}
