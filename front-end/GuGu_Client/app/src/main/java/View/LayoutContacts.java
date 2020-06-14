package View;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu_client.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterUserItem;
import Server.ParaseData;
import Server.ServerManager;
import Util.UserItemMsg;

public class LayoutContacts extends Fragment {

    private View rootView;
    private Context context;

    //Contacts联系人列表
    private List<UserItemMsg> userItemMsgList;

    private List<UserItemMsg> groupItemMsgList;
    private List<UserItemMsg> contactItemMsgList;

    //Group群组缩放按钮
    private PicAndTextBtn patbBarGroup;
    //Conctacts联系人缩放按钮
    private PicAndTextBtn patbBarContact;
    //Group群组列表Recycler视图
    private RecyclerView rvGroup;
    //Contacts联系人列表Recycler视图
    private RecyclerView rvContact;
    private Gson gson = new Gson();
    private final static String TAG = "LayoutContacts";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_contacts, container, false);
        this.context = inflater.getContext();
        loadUserMsgList();
        initGroupViews();
        initContactViews();
        return rootView;
    }


    public void loadUserMsgList() {
        ServerManager serverManager = ServerManager.getServerManager();
        String userId = serverManager.getUserId();
        serverManager.sendMessage("", "GETUSERITEM");
        String msg = serverManager.getMessage();
//        Log.d(TAG, msg);
        System.out.println(msg);
        userItemMsgList = gson.fromJson(msg, new TypeToken<List<UserItemMsg>>() {
        }.getType());

    }

    //初始化群组View
    private void initGroupViews() {
        patbBarGroup = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar_groups);
        rvGroup = (RecyclerView) rootView.findViewById(R.id.rv_list_groups);
        groupItemMsgList = new ArrayList<>();
        for (UserItemMsg tmp : userItemMsgList) {
            if (tmp.getItemType() == 1) {
                groupItemMsgList.add(tmp);
            }
        }

        //使用AdapterUserItem适配器将信息显示
        AdapterUserItem adapterGroup = new AdapterUserItem(context, groupItemMsgList);
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

    //初始化联系人View
    private void initContactViews() {
        patbBarContact = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar__contacts);
        rvContact = (RecyclerView) rootView.findViewById(R.id.rv_list_contacts);
        contactItemMsgList = new ArrayList<>();
        for (UserItemMsg tmp : userItemMsgList) {
            if (tmp.getItemType() == 2) {
                contactItemMsgList.add(tmp);
            }
        }
        //使用AdapterUserItem适配器将信息显示
        AdapterUserItem adapterContact = new AdapterUserItem(context, contactItemMsgList);
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
}
