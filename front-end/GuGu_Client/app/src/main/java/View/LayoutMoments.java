package View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu_client.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterMomentItem;
import Server.ServerManager;
import Util.MomentMsg;

public class LayoutMoments extends Fragment {

    private View rootView;
    private List<MomentMsg> momentMsgList;
    private RecyclerView momentRecyclerView;
    private AdapterMomentItem adapterMomentItem;
    private TextView tvNewMoment;
    private Button btnSend;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_moments, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {
        momentRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_moments);
        tvNewMoment = (TextView) rootView.findViewById(R.id.tv_moment_new);
        btnSend = (Button) rootView.findViewById(R.id.btn_moment_send);

        momentMsgList = new ArrayList<>();
        momentMsgList.clear();
        loadData();

        adapterMomentItem = new AdapterMomentItem(getContext(), momentMsgList);
        momentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        momentRecyclerView.setAdapter(adapterMomentItem);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MomentMsg momentMsg = new MomentMsg();
                momentMsg.setUsername(ServerManager.getServerManager().getUserId());
                momentMsg.setIconID(ServerManager.getServerManager().getIconID());
                momentMsg.setMoment(tvNewMoment.getText().toString());
                momentMsg.setGood(R.drawable.ungood);
                tvNewMoment.setText("");
                momentMsgList.add(momentMsg);
                MomentMsg.momentMsgList.add(momentMsg);
            }
        });
    }

    private void loadData() {
        for (MomentMsg momentMsg : MomentMsg.momentMsgList) {
            momentMsgList.add(momentMsg);
        }
    }
}

