package Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gugu_client.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Adapter.AdapterChatMsg;
import Server.ServerManager;
import Util.ChatMsg;
import View.TitleBar;

public class AtyChatRoom extends AppCompatActivity {

    private TitleBar titleBar;
    private ListView listView;
    private EditText myMsg;
    private Button btnSend;
    public static List<ChatMsg> chatMsgList = new ArrayList<>();
    public static AdapterChatMsg adapterChatMsgList;
    private String chatObj;
    private String group;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_chat_room);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//        AndroidBug5497Workaround.assistActivity(this);
        initViews();
    }

    private void initViews() {

        titleBar = (TitleBar) findViewById(R.id.tb_chat_room);
        listView = (ListView) findViewById(R.id.lv_chat_room);
        myMsg = (EditText) findViewById(R.id.myMsg);
        btnSend = (Button) findViewById(R.id.btnSend);
        chatObj = getIntent().getStringExtra("Id");
        group = getIntent().getStringExtra("Type");
        titleBar.setTitleText(chatObj);
//        group = ParaseData.getAllGroupList(this).contains(chatObj) ? "0" : "1";
        chatMsgList.clear();
        loadChatMsg();
        adapterChatMsgList = new AdapterChatMsg(AtyChatRoom.this, R.layout.chat_other, chatMsgList);
        listView.setAdapter(adapterChatMsgList);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = myMsg.getText().toString();
                if (!content.isEmpty()) {
                    ChatMsg msg = new ChatMsg();
                    msg.setContent(content);
                    msg.setUsername(ServerManager.getServerManager().getUserId());
                    msg.setIconID(ServerManager.getServerManager().getIconID());
                    // 标记消息归属
                    msg.setMyInfo(true);
                    msg.setChatObj(chatObj);
                    if(group.equals("1")){
                        msg.setIsGroup(1);
                    }else {
                        msg.setIsGroup(0);
                    }
                    if (sendToChatObj(msg)) {
                        ChatMsg.chatMsgList.add(msg);
                        chatMsgList.add(msg);
                        myMsg.setText("");
                    } else {
                        Toast.makeText(AtyChatRoom.this, "send failed", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        titleBar.setTitleBarClickListetner(new TitleBar.titleBarClickListener() {
            @Override
            public void leftButtonClick() {
                finish();
            }

            @Override
            public void rightButtonClick() {
            }
        });
    }

    @SuppressLint("HandlerLeak")
    public static Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                adapterChatMsgList.notifyDataSetChanged();
            }
        }
    };

    private boolean sendToChatObj(ChatMsg chatMsg) {
        ServerManager serverManager = ServerManager.getServerManager();
        String msg = gson.toJson(chatMsg);
        System.out.println(msg);
        serverManager.sendMessage(msg, "CHATMSG");
        adapterChatMsgList.notifyDataSetChanged();
//        try {
//            Thread.sleep(500);
//        }catch (Exception e){
//            e.printStackTrace();
//        }
        String ack = serverManager.getMessage();
        if (ack == null) {
            return false;
        }
//        AtyChatRoom.chatMsgList.add(chatMsg);
        return true;
    }


    private void loadChatMsg() {
//        if(ChatMsg.chatMsgList.isEmpty()){
//            return;
//        }
        if (group.equals("1")) {
            for (ChatMsg chatMsg : ChatMsg.chatMsgList) {
                if (chatMsg.getIsGroup()==1) {
                    chatMsgList.add(chatMsg);
                }
            }
        } else {
            for (ChatMsg chatMsg : ChatMsg.chatMsgList) {
                if (chatMsg.getChatObj().equals(chatObj) && chatMsg.getIsGroup()==0) {
                    chatMsgList.add(chatMsg);
                }
            }
        }
    }
}
