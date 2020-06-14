package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu_client.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.ServerManager;
import Util.LoginMsg;

public class AtyLoginOrRegister extends AppCompatActivity implements View.OnClickListener {

    private TabHost tabHost;

    private Button btnLogin;
    private EditText etLoginUsername;
    private EditText etLoginPassword;
    private Button btnRegister;
    private EditText etRegisterUsername;
    private EditText etRegisterPassword;
    private EditText etInsurePassword;

    private Gson gson = new Gson();
    private ServerManager serverManager = ServerManager.getServerManager();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_login_or_register);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        initViews();
    }

    //layout布局初始化
    private void initViews() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etLoginUsername = (EditText) findViewById(R.id.et_login_username);
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        btnRegister = (Button) findViewById(R.id.btn_register);
        etRegisterUsername = (EditText) findViewById(R.id.et_register_username);
        etRegisterPassword = (EditText) findViewById(R.id.et_register_password);
        etInsurePassword = (EditText) findViewById(R.id.et_insure_password);

        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Login").setIndicator("Login").setContent(R.id.layout_login));
        tabHost.addTab(tabHost.newTabSpec("Register").setIndicator("Register").setContent(R.id.layout_register));
        for (int i = 0; i < 2; i++) {
            TextView tv = ((TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title));
            tv.setAllCaps(false);
            tv.setTextSize(16);
        }
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        serverManager.start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                String username = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                if (login(username, password)) {
                    serverManager.setUsername(username);
                    Intent intent = new Intent(this, AtyMain.class);
                    startActivity(intent);
                    finish();
                } else {
                    etLoginUsername.setText("");
                    etLoginPassword.setText("");
                }
                break;
            }
            case R.id.btn_register: {
                Intent intent = new Intent(this, AtyMain.class);
                startActivity(intent);
                finish();
                break;
            }
            default:
                break;
        }
    }


    private boolean login(String username, String password) {
        //发送数据是否符合格式
        if (username == null || password == null || username.length() > 10 || password.length() > 20) {
            return false;
        }

        //创建登录消息
        LoginMsg loginMsg=new LoginMsg(username,password);

        //序列化为json字符串
        String msg=gson.toJson(loginMsg);

        //通过severMessage发生消息
        serverManager.sendMessage(this, msg,"LOGIN");

        //从serverMessage获取返回消息
        String ack = serverManager.getMessage();

        // 处理返回消息
        if (ack == null) {
            return false;
        }
        serverManager.setMessage(null);
        return ack.equals("SUCCESS");
    }
}
