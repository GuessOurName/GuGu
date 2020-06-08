package View;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import Adapter.AdapterUserItem;
import Util.MsgUserItem;

import com.example.gugu.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LayoutChats extends Fragment {

    private View rootView;
    private RecyclerView recyclerView;
    private List<MsgUserItem> MsgUserItemList = new ArrayList<>();
    private Context context;
    private AdapterUserItem adapterUserItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_chats, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {

        context = getContext();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.chatsRecycleView);


        //滑动监听，实现UserItem中右滑删除，上下移动
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(MsgUserItemList, i, i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(MsgUserItemList, i, i - 1);
                    }
                }
                adapterUserItem.notifyItemMoved(fromPosition, toPosition);
                return true;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                MsgUserItemList.remove(position);
                adapterUserItem.notifyItemRemoved(position);
            }
        };

        //数据加载
        loadData();
        adapterUserItem = new AdapterUserItem(context, MsgUserItemList);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapterUserItem);
    }

    private void loadData() {
        for (int i = 0; i < 12; i++) {
            MsgUserItem MsgUserItem = new MsgUserItem();
            MsgUserItem.setIconID(R.drawable.avastertony);
            MsgUserItem.setUsername("Tony Stark");
            MsgUserItem.setSign("You know who I am !");
            MsgUserItemList.add(MsgUserItem);
        }
    }
}