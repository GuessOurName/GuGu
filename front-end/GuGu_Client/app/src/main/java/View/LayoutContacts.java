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
    private List<UserItemMsg> groupMsgList;
    private List<UserItemMsg> contactMsgList;
    private PicAndTextBtn patbBarGroup;
    private PicAndTextBtn patbBarContact;
    private RecyclerView rvGroup;
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

    private void initGroupViews() {
        patbBarGroup = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar_groups);
        rvGroup = (RecyclerView) rootView.findViewById(R.id.rv_list_groups);

        groupMsgList = new ArrayList<>();

        loadGroups();

        AdapterUserItem adapterGroup = new AdapterUserItem(context, groupMsgList);
        rvGroup.setLayoutManager(new LinearLayoutManager(context));
        rvGroup.setAdapter(adapterGroup);

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

    private void initContactViews() {
        patbBarContact = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar__contacts);
        rvContact = (RecyclerView) rootView.findViewById(R.id.rv_list_contacts);

        contactMsgList = new ArrayList<>();

        loadFriends();

        AdapterUserItem adapterContact = new AdapterUserItem(context, contactMsgList);
        rvContact.setLayoutManager(new LinearLayoutManager(context));
        rvContact.setAdapter(adapterContact);

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
