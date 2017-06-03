package card.blink.com.httpdemo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.LoginFilter;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SimpleAdapter;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.gson.Gson;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import card.blink.com.httpdemo.view.PullToRefreshListView;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @InjectView(R.id.btn)
    Button btn;

    @InjectView(R.id.btn2)
    Button btn2;

    @InjectView(R.id.btn3)
    Button btn3;

    @InjectView(R.id.btn4)
    Button btn4;

    @InjectView(R.id.btn5)
    Button btn5;

    @InjectView(R.id.btn6)
    Button btn6;

    @InjectView(R.id.btn7)
    Button btn7;

    @InjectView(R.id.lv)
    PullToRefreshListView lv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[] {"item1","item2","item3"});
        lv.setAdapter(adapter);
        lv.setonRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.v(TAG, "下拉刷新,请求刷新");
                //lv.onRefreshComplete();
            }

        });

    }

    @OnClick(R.id.btn7)
    public void uploadFile() {
        Log.i(TAG, "uploadFile");
        // 封装file文件
        File file = new File(Environment.getExternalStorageDirectory().toString(), "test.xls");
        String fileAbsolutePath = file.getAbsolutePath();
        Log.i(TAG, "fileAbsolutePath==" + fileAbsolutePath);
        long length = file.length();
        Log.i(TAG, "length==" + length);

        new Thread() {
            @Override
            public void run() {
                postUploadRequest();
            }
        }.start();

    }

    public static byte[] longToBytes(long l) {
        byte[] result = new byte[8];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    public static long bytesToLong(byte[] b) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (b[i] & 0xFF);
        }
        return result;
    }

    /**
     * 以post方式上传数据
     */
    private void postUploadRequest() {
        // 将数据封装到content
        byte[] content = getDataFromLocalFile();

        Log.i(TAG, "btn");
        OkHttpClient client = new OkHttpClient();

        // 生成body
        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create(mediaType, content);

        Request request = new Request.Builder()
                .url("http://192.168.16.1:8080/cgi-bin/upload_files.cgi")
                .post(body)
                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .addHeader("authorization", "Basic YWRtaW46YWRtaW4=")
                .addHeader("Content-Length", "100632")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.i(TAG, "response==" + response.toString());
            ResponseBody responseBody = response.body();
            Log.i(TAG, "responseBody==" + responseBody.toString());
            String string = responseBody.string();
            Log.i(TAG, "responseBody.string()==" + string);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 存放的路径 /mount1/mytest.xls
     * <p/>
     * 如何存放的路径大于256要怎么处理?
     *
     * @return
     */
    private byte[] getDataFromLocalFile() {
        File file = new File(Environment.getExternalStorageDirectory().toString(), "test.xls");
        long length = file.length();
        Log.i(TAG, "length==" + length);

        // empty
        byte[] postmsg = new byte[280];

        String path = "/mount1/mytest.xls";
        byte[] pathBytes = path.getBytes();
        // 填充路径
        for (int i = 0; i < pathBytes.length; i++) {
            postmsg[i] = pathBytes[i];
        }
        Log.i(TAG, "postmsg===" + Arrays.toString(postmsg));

        long block = 1;
        long blocksize = 1;
        long filesize = length;

//        byte[] bk = longToBytes(block);
//        Log.i(TAG,"bk==" + Arrays.toString(bk));
//        for (int i = 0; i < bk.length; i++) {
//            postmsg[256 + i] = bk[i];
//        }
        postmsg[256 + 0] = 1;
        Log.i(TAG, "postmsg===" + Arrays.toString(postmsg));

//        byte[] bksize = longToBytes(blocksize);
//        Log.i(TAG,"bksize==" + Arrays.toString(bksize));
//        for (int i = 0; i < bksize.length; i++) {
//            postmsg[256 + 8 + i] = bksize[i];
//        }
        postmsg[256 + 8 + 0] = 1;
        Log.i(TAG, "postmsg===" + Arrays.toString(postmsg));

//        byte[] flsize = longToBytes(filesize);
//        Log.i(TAG,"flsize==" + Arrays.toString(flsize));
//        for (int i = 0; i < flsize.length; i++) {
//            postmsg[256 + 8 + 8 + i] = flsize[i];
//        }
        postmsg[256 + 8 + 8 + 0] = 1;
        postmsg[256 + 8 + 8 + 1] = -120;
        postmsg[256 + 8 + 8 + 2] = 0;

        Log.i(TAG, "postmsg===" + Arrays.toString(postmsg));

        //byte[] fSize = new byte[(int) length];
        // 从本地文件读取数据取放到数组中
        byte[] fSize = readFile(file);
        int size = postmsg.length + fSize.length;
        Log.i(TAG, "size==" + size + "---postmg.length==" + postmsg.length + "---fSize.length===" + fSize.length);
        byte[] content = new byte[size];
        for (int i = 0; i < content.length; i++) {
            content[i] = i < postmsg.length ? postmsg[i] : fSize[i - postmsg.length];
        }
        Log.i(TAG, "content===" + Arrays.toString(content));

        return content;
    }

    private byte[] readFile(File file) {

        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            byte[] data = new byte[(int) file.length()];
            int read = raf.read(data);
            Log.i(TAG, "read==" + read);
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


    @OnClick(R.id.btn6)
    public void playOnline() {
        Log.i(TAG, "btn6");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        Uri data = Uri.parse("http://192.168.16.1:8080/media/mount1/Aegean_Sea.mp4");
        intent.setDataAndType(data, "video/mp4");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.btn5)
    public void btn5Dl() {
        Log.i(TAG, "btn5");
        String url = "http://192.168.16.1:8080/media/mount1/jd.rar";
        OkHttpUtils//
                .get()//
                .url(url)//
                .build()//
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "jd.jar")//
                {

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e(TAG, "onError");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.i(TAG, "response===" + response.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Log.d(TAG, "inProgress===" + progress * 100);
                    }
                });

    }

    @OnClick(R.id.btn4)
    public void btn4GetRequest() {
        Log.i(TAG, "btn4");
        new Thread() {
            @Override
            public void run() {
                postRequest();
            }
        }.start();
    }

    @OnClick(R.id.btn3)
    public void btnDl() {
        Log.i(TAG, "btnDl");
        new Thread() {
            @Override
            public void run() {
                getRequest();
            }
        }.start();


    }

    // http://192.168.16.1:8080/media/mount1/jd.rar
    private void getRequest() {
//        Log.i(TAG, "getRequest");
//        OkHttpClient client = new OkHttpClient();
//        Request request = new Request.Builder()
//                .url("http://192.168.16.1:8080/media/mount1/jd.rar")
//                .build();
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Log.i(TAG, "onFailure");
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Log.i(TAG, "onResponse");
//                if (!response.isSuccessful()) {
//                    throw new IOException("Failed to download file: " + response);
//                }
//
//                String path = Environment.getExternalStorageDirectory().toString() + "/jd.rar";
//                Log.i(TAG, path);
//                FileOutputStream fos = new FileOutputStream(path);
//                fos.write(response.body().bytes());
//                fos.close();
//            }
//        });


    }

    @OnClick(R.id.btn2)
    public void btnTC() {
        android.app.ProgressDialog progressDialog = new ProgressDialog(this, "test");
        progressDialog.show();
    }

    @OnClick(R.id.btn)
    public void btnPostRequest() {
        new Thread() {
            @Override
            public void run() {
                postRequest();
            }
        }.start();

    }

    // "{\"type\":\"getpath\",\"path\":\"\"}"
    private void postRequest() {
        Log.i(TAG, "btn");
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"type\":\"getpath\",\"path\":\"/\"}");
        Request request = new Request.Builder()
                .url("http://192.168.16.1:8080/cgi-bin/dir_list.cgi")
                .post(body)
                .addHeader("authorization", "Basic YWRtaW46YWRtaW4=")
                .addHeader("content-type", "application/json")
                .addHeader("cache-control", "no-cache")
                .build();

        try {
            Response response = client.newCall(request).execute();
            Log.i(TAG, "response==" + response.toString());
            ResponseBody responseBody = response.body();
            Log.i(TAG, "responseBody==" + responseBody.toString());
            String string = responseBody.string();
            Log.i(TAG, "responseBody.string()==" + string);
            // 获取到Json数据的内容
            String content = string;

            Gson gson = new Gson();
            Data data = gson.fromJson(content, Data.class);
            Log.d(TAG, data.toString());
            int result = data.result;
            Log.i(TAG, result + "");


        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
