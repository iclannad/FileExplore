package card.blink.com.fileexplore.tools;

import android.os.Environment;
import android.util.Log;

import java.io.File;
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
}
