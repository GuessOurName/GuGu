package View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterUserItem;
import Util.MsgUserItem;

public class LayoutContacts extends Fragment {

    private View rootView;
    private Context context;
    private List<MsgUserItem> groupMsgList;
    private List<MsgUserItem> contactMsgList;
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
        for (int i = 1; i < 4; i++) {
            MsgUserItem MsgUserItem = new MsgUserItem();
            MsgUserItem.setIconID(R.drawable.avastertony);
            MsgUserItem.setUsername("Group " + i);
            MsgUserItem.setSign("onLine : " + i + "/10");
            groupMsgList.add(MsgUserItem);
        }

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

    private void initContactViews() {
        patbBarContact = (PicAndTextBtn) rootView.findViewById(R.id.patb_bar_contacts);
        rvContact = (RecyclerView) rootView.findViewById(R.id.rv_list_contacts);

        contactMsgList = new ArrayList<>();
        for (int i = 1; i < 8; i++) {
            MsgUserItem MsgUserItem = new MsgUserItem();
            MsgUserItem.setIconID(R.drawable.avasterdr);
            MsgUserItem.setUsername("Friend " + i);
            MsgUserItem.setSign("You know who I am !");
            contactMsgList.add(MsgUserItem);
        }

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
}
