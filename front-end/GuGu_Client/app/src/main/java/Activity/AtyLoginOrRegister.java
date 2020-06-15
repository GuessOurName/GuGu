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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu_client.R;
import com.google.gson.Gson;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Server.ServerManager;
import Util.LoginMsg;
import Util.RegisterMsg;

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
//                serverManager.sendMessage("","GETGROUPLIST");
                String userId = etLoginUsername.getText().toString();
                String password = etLoginPassword.getText().toString();
                if (login(userId, password)) {
                    serverManager.setUserId(userId);
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
                String username = etRegisterUsername.getText().toString();
                String password1 = etRegisterPassword.getText().toString();
                String password2 = etInsurePassword.getText().toString();

                register(username, password1, password2);
                System.out.println(username);

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
        serverManager.sendMessage(msg,"LOGIN");

        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }

        //从serverMessage获取返回消息
        String ack = serverManager.getMessage();

        // 处理返回消息
        if (ack == null) {
            Toast.makeText(AtyLoginOrRegister.this, "登录失败，请检查账号或密码", Toast.LENGTH_SHORT).show();
            return false;
        }
        serverManager.setMessage(null);
        return ack.equals("SUCCESS");
    }

    private boolean register(String username, String password1, String password2) {
        if(username.length() == 0) {
            Toast.makeText(AtyLoginOrRegister.this, "用户名为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!password1.equals(password2)) {
            Toast.makeText(AtyLoginOrRegister.this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            return false;
        } else if(password1.length() < 6) {
            Toast.makeText(AtyLoginOrRegister.this, "密码长度太短", Toast.LENGTH_SHORT).show();
            return false;
        }

        RegisterMsg registerMsg = new RegisterMsg(username,password1);
        String msg = gson.toJson(registerMsg);
        serverManager.sendMessage(msg,"REGISTER");
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        String ack = serverManager.getMessage();
        if (ack == null) {
            Toast.makeText(AtyLoginOrRegister.this, "注册失败", Toast.LENGTH_SHORT).show();
            return false;
        }
        serverManager.setMessage(null);
        AlertDialog alertDialog1 = new AlertDialog.Builder(this)
                .setTitle("注册成功")//标题
                .setMessage("你的登录账号是"+ack)//内容
                .setIcon(R.mipmap.ic_launcher)//图标
                .create();
        alertDialog1.show();
        return true;
    }
}
