package card.blink.com.fileexplore.activity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.administrator.ui_sdk.MyBaseActivity.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import card.blink.com.fileexplore.R;
import card.blink.com.fileexplore.adapter.FileListAdapter;
import card.blink.com.fileexplore.adapter.Protocol;
import card.blink.com.fileexplore.tools.Tools;

/**
 * Created by Administrator on 2017/6/3.
 */
public class ChooseUploadFileActivity extends BaseActivity implements AdapterView.OnItemClickListener {

    @InjectView(R.id.lv)
    ListView lv;

    private static final String TAG = ChooseUploadFileActivity.class.getSimpleName();
    private List<FileListAdapter.Pair<String, Integer>> list;
    private FileListAdapter fileListAdapter;
    private File positionFile = null;


    /**
     * Start()
     */
    @Override
    public void init() {
        View view = LayoutInflater.from(context).inflate(R.layout.choose_upload_files, null);
        setContent(view);

        // 注解框架
        ButterKnife.inject(this, view);

        setTitle("选择文件");
        setLeftTitle("上一级");
        setRightTitleVisiable(false);
        setLeftTitleColor(R.color.White);
        setRightTitleColor(R.color.White);
        setTopTitleColor(R.color.White);
        setTopColor(R.color.MainColorBlue);

        list = new ArrayList<>();
        fileListAdapter = new FileListAdapter(this, list);
        lv.setAdapter(fileListAdapter);
        lv.setOnItemClickListener(this);

        AllFile();
    }


    /**
     * ListView 点击
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        onclickfile(position);
    }

    /**
     * 左边的点击事件重写
     */
    @Override
    public void setLeftCLick() {
        Log.v(TAG, "左边的点击事件");
        if (positionFile == null) {
            return;
        }
        String currentPath = positionFile.getAbsolutePath();
        if (currentPath.equals("/storage/emulated/0")) {
            Log.i(TAG, "已经到达了根目录");
            finish();

            return;
        }

        int lastIndexOf = currentPath.lastIndexOf("/");
        String previousPath = currentPath.substring(0, lastIndexOf);
        Log.i(TAG, "substring===" + previousPath);
        File f = new File(previousPath);
        positionFile = f;
        onclickfiledir(positionFile);
    }

    /**
     * 获取手机全部文件和文件夹
     */
    private void AllFile() {
        list.clear();
        ArrayList<String> nlist = new ArrayList<>();
        nlist = new ArrayList<>();
        File sdcard = Environment.getExternalStorageDirectory();
        positionFile = sdcard;
        nlist = Tools.sortFileList(sdcard.getPath());
        for (int i = 0; i < nlist.size(); i++) {
            FileListAdapter.Pair<String, Integer> pair = new FileListAdapter.Pair<>();
            pair.setA(nlist.get(i).toString());
            File f = new File(nlist.get(i).toString());
            if (f.isDirectory()) {
                pair.setB(Protocol.DIR);
            } else {
                pair.setB(Protocol.FL);
            }
            list.add(pair);
        }
        // 更新界面
        if (fileListAdapter != null) {
            fileListAdapter.setList(list);
        }

    }

    /**
     * 点击文件夹的操作
     *
     * @param f
     */
    private void onclickfiledir(File f) {
        positionFile = f;
        list = Tools.GetFilechild(f,
                (ArrayList<FileListAdapter.Pair<String, Integer>>) list);
        if (list == null) {
            list = new ArrayList<>();
        }
        fileListAdapter.setList(list);

    }

    /**
     * 点击文件的操作
     *
     * @param position
     */
    private void onclickfile(int position) {
        Log.i(TAG, positionFile.getAbsolutePath());
        String filepath = Tools.GetFilePath(list.get(position).getA(), positionFile);
        File f = new File(filepath);
        if (f.isDirectory()) {
            Log.v(TAG, "点击的是文件夹");
            Log.i(TAG, f.getAbsolutePath());
            onclickfiledir(f);
        } else {
            Log.v(TAG, "点击的是文件,在此处应该获取文件的全路径");
            String filePath = f.getAbsolutePath();
            Log.v(TAG, "filePath==" + filePath);
            Intent intent = new Intent();
            intent.putExtra("path", filePath);
            setResult(1, intent);
            finish();
        }
    }

}
