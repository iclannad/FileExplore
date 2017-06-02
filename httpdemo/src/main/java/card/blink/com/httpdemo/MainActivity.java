package card.blink.com.httpdemo;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Button;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn6)
    public void playOnline() {
        Log.i(TAG,"btn6");
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
        Uri data = Uri.parse("http://192.168.16.1:8080/media/mount1/Aegean_Sea.mp4");
        intent.setDataAndType(data,"video/mp4");
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
                        Log.e(TAG,"onError");
                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.i(TAG,"response===" + response.getAbsolutePath());
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        super.inProgress(progress, total, id);
                        Log.d(TAG,"inProgress==="+ progress * 100);
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
