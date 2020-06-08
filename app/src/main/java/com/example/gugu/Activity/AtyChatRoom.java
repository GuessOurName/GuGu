package com.example.gugu.Activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterMsgChat;
import Util.MsgChat;
import View.TitleBar;

public class AtyChatRoom extends AppCompatActivity {

    private TitleBar titleBar;
    private ListView listView;
    private EditText myMsg;
    private Button btnSend;
    private List<MsgChat> MsgChatList;
    private AdapterMsgChat adapterMsgChatList;
    private String chatObj;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.aty_chat_room);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        initViews();
    }

    private void initViews() {

        titleBar = (TitleBar) findViewById(R.id.tb_chat_room);
        listView = (ListView) findViewById(R.id.lv_chat_room);
        myMsg = (EditText) findViewById(R.id.myMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        MsgChatList = new ArrayList<>();

        chatObj = getIntent().getStringExtra("username");
        titleBar.setTitleText(chatObj);

        adapterMsgChatList = new AdapterMsgChat(AtyChatRoom.this, R.layout.char_other, MsgChatList);

        listView.setAdapter(adapterMsgChatList);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = myMsg.getText().toString();
                if (!content.isEmpty()) {
                    MsgChat msg = new MsgChat();
                    msg.setContent(content);
                    msg.setUsername("hello");
                    msg.setIconID(R.drawable.avasterwe);
                    msg.setMyInfo(true);
                    msg.setChatObj(chatObj);
                    MsgChatList.add(msg);
                    myMsg.setText("");
                }
            }
        });

        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }
            @Override
            public void rightButtonClick() { }
        });
    }
}
