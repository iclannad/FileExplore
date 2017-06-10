package card.blink.com.filetransportdemo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import butterknife.ButterKnife;
import butterknife.OnClick;
import card.blink.com.filetransportdemo.R;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

//    @Bind(R.id.btn_left)
//    Button btnLeft;
//    @Bind(R.id.btn_right)
//    Button btnRight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.btn_left)
    public void btnLeft() {
        Log.v(TAG, "btnLeft");
        startActivity(new Intent(this, DownloadActivity.class));

    }


    @OnClick(R.id.btn_right)
    public void btnRigth() {
        Log.v(TAG, "btnRight");
        startActivity(new Intent(this, UploadActivity.class));
    }
}
