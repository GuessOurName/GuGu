package Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu_client.R;

import Server.ServerManager;
import View.TitleBar;
import View.LayoutContacts;

public class AtyAdd extends AppCompatActivity {

    private TitleBar titleBar;
    private EditText etSearch;
    private Button butFriend;
    private Button butGroup;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_add);
        initView();
    }

    private void initView(){
        titleBar=findViewById(R.id.tb_add);
        etSearch=findViewById(R.id.et_add);
        butFriend=findViewById(R.id.btn_search_friend);
        butGroup=findViewById(R.id.btn_search_group);

        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }

            @Override
            public void rightButtonClick() {
            }
        });

        butFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=etSearch.getText().toString();
                if(msg.equals("")||msg==null){
                    Toast.makeText(AtyAdd.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ServerManager.getServerManager().sendMessage(msg,"ADDFRIEND");
                if(ServerManager.getServerManager().getMessage().equals("1")){
                    Intent intent=new Intent();
                    intent.putExtra("STATE","1");
                    setResult(1,intent);
                    finish();
                }
                else{
                    Intent intent=new Intent();
                    intent.putExtra("STATE","0");
                    setResult(1,intent);
                    finish();
                }
            }
        });

        butGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg=etSearch.getText().toString();
                if(msg.equals("")||msg==null){
                    Toast.makeText(AtyAdd.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                ServerManager.getServerManager().sendMessage(msg,"ADDGROUP");
                if(ServerManager.getServerManager().getMessage().equals("1")){
                    Intent intent=new Intent();
                    intent.putExtra("STATE","1");
                    setResult(1,intent);
                    finish();
                }
                else{
                    Intent intent=new Intent();
                    intent.putExtra("STATE","0");
                    setResult(1,intent);
                    finish();
                }

            }
        });
    }
}
