package card.blink.com.httpdemo;

import android.app.Activity;
import android.nfc.Tag;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.gc.materialdesign.widgets.ProgressDialog;
import com.google.gson.Gson;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import card.blink.com.httpdemo.view.MyProgressDIalog;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @InjectView(R.id.btn)
    Button btn;

    @InjectView(R.id.btn2)
    Button btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
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
