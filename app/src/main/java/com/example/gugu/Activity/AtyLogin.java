package com.example.gugu.Activity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu.R;

public class AtyLogin extends AppCompatActivity implements View.OnClickListener{

    private TabHost tabHost;
    private Button btnLogin;
    private EditText etUsernameLogin;
    private EditText etPasswordLogin;
    private Button btnRegister;
    private EditText etUsernameRegister;
    private EditText etPasswordRegister;
    private EditText etPasswordInsure;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_login);
        initViews();
    }
    private void initViews() {
        tabHost = (TabHost) findViewById(R.id.tabHost);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etUsernameLogin = (EditText) findViewById(R.id.et_username_login);
        etPasswordLogin = (EditText) findViewById(R.id.et_password_login);
        btnRegister = (Button) findViewById(R.id.btn_register);
        etUsernameRegister = (EditText) findViewById(R.id.et_username_register);
        etPasswordRegister = (EditText) findViewById(R.id.et_password_register);
        etPasswordInsure = (EditText) findViewById(R.id.et_password_insure);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("Login").setIndicator("Login").setContent(R.id.layout_login));
        tabHost.addTab(tabHost.newTabSpec("Register").setIndicator("Register").setContent(R.id.layout_register));
        btnLogin.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login: {
                Intent intent = new Intent(this, AtyMain.class);
                startActivity(intent);
                finish();
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
}
