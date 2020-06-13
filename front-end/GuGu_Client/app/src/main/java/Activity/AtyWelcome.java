package Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu_client.R;

public class AtyWelcome extends AppCompatActivity {

    private static final int DELAY = 2000;
    private static final int GO_GUIDE = 0;
    private static final int GO_HOME = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_welcome);
        initLoad();
    }

    private void initLoad() {
        handler.sendEmptyMessageDelayed(GO_HOME, DELAY);
    }

    private void goHome() {
        Intent intent = new Intent(this, AtyLoginOrRegister.class);
        startActivity(intent);
        finish();
    }


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            goHome();
        }
    };
}
