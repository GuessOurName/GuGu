package View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu_client.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterUserItem;
import Server.ParaseData;
import Server.ServerManager;
import Util.UserItemMsg;

public class LayoutContacts extends Fragment {

    private View rootView;
    private Context context;

    //Group群组列表
    private List<UserItemMsg> groupMsgList;
    //Contacts联系人列表
    private List<UserItemMsg> contactMsgList;
    //Group群组缩放按钮
    private PicAndTextBtn patbBarGroup;
    //Conctacts联系人缩放按钮
    private PicAndTextBtn patbBarContact;
    //Group群组列表Recycler视图
    private RecyclerView rvGroup;
    //Contacts联系人列表Recycler视图
    private RecyclerView rvContact;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_contacts, container, false);
        this.context = inflater.getContext();
        initGroupViews();
        initContactViews();
        return rootView;
    }


    //初始化群组View
    private void initGroupViews() {

        patbBarGroup = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar_groups);
        rvGroup = (RecyclerView) rootView.findViewById(R.id.rv_list_groups);
        groupMsgList = new ArrayList<>();

        //请求加载群组信息
        loadGroups();

        //使用AdapterUserItem适配器将信息显示
        AdapterUserItem adapterGroup = new AdapterUserItem(context, groupMsgList);
        rvGroup.setLayoutManager(new LinearLayoutManager(context));
        rvGroup.setAdapter(adapterGroup);

        //Group群组缩放按钮监听设置
        patbBarGroup.setOnClickListener(new PicAndTextBtn.picAndTextBtnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvGroup.getVisibility() == View.VISIBLE) {
                    rvGroup.setVisibility(View.GONE);
                    patbBarGroup.setImageView(R.drawable.shink);
                } else {
                    rvGroup.setVisibility(View.VISIBLE);
                    patbBarGroup.setImageView(R.drawable.rise);
                }
            }
        });
    }

    //使用ServerManager向服务端请求当前用户群组信息存入groupMsgList
    private void loadGroups() {

        ServerManager serverManager = ServerManager.getServerManager();
        String username = serverManager.getUsername();
        List<String> groStr = ParaseData.getGroupList(context, username);
        for (String string : groStr) {
            UserItemMsg msg = new UserItemMsg();
            msg.setIconID(5);
            msg.setState("1");
            msg.setUsername(string);
            groupMsgList.add(msg);
        }
    }

    //初始化联系人View
    private void initContactViews() {
        patbBarContact = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar__contacts);
        rvContact = (RecyclerView) rootView.findViewById(R.id.rv_list_contacts);
        contactMsgList = new ArrayList<>();

        //请求加载群组信息
        loadFriends();

        //使用AdapterUserItem适配器将信息显示
        AdapterUserItem adapterContact = new AdapterUserItem(context, contactMsgList);
        rvContact.setLayoutManager(new LinearLayoutManager(context));
        rvContact.setAdapter(adapterContact);

        //Contacts联系人缩放按钮监听设置
        patbBarContact.setOnClickListener(new PicAndTextBtn.picAndTextBtnClickListener() {
            @Override
            public void onClick(View view) {
                if (rvContact.getVisibility() == View.VISIBLE) {
                    rvContact.setVisibility(View.GONE);
                    patbBarContact.setImageView(R.drawable.shink);
                } else {
                    rvContact.setVisibility(View.VISIBLE);
                    patbBarContact.setImageView(R.drawable.rise);
                }
            }
        });
    }

    //使用ServerManager向服务端请求当前用户联系人信息存入contactMsgList
    private void loadFriends() {
        ServerManager serverManager = ServerManager.getServerManager();
        String userName = serverManager.getUsername();
        List<String> friStr = ParaseData.getFriendList(context, userName);

        for (String string : friStr) {
            UserItemMsg msg = new UserItemMsg();
            msg.setUsername(string);
            String[] str = ParaseData.getFriendProfile(context, string);
            int i = Integer.parseInt(str[0]);
            msg.setIconID(Integer.parseInt(str[0]));
            msg.setSign(str[1]);
            contactMsgList.add(msg);
        }
    }
}
