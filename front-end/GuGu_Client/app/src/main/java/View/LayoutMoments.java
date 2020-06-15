package View;

import android.net.sip.SipErrorCode;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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
    private Gson gson = new Gson();

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


        ServerManager serverManager = ServerManager.getServerManager();
        String msg = "GETMOMENTMSG";
        serverManager.sendMessage(msg);
        try {
            Thread.sleep(1000);
        }catch (Exception e){
            e.printStackTrace();
        }
        String ack = serverManager.getMessage();
        List<MomentMsg> momentMsgs = gson.fromJson(ack, new TypeToken<List<MomentMsg>>() {
        }.getType());

        if(momentMsgs != null) {
            momentMsgList = momentMsgs;
        }


        loadData();

        adapterMomentItem = new AdapterMomentItem(getContext(), momentMsgList);
        momentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        momentRecyclerView.setAdapter(adapterMomentItem);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                MomentMsg momentMsg = new MomentMsg();
                momentMsg.setUserId(ServerManager.getServerManager().getUserId());
                momentMsg.setIconID(ServerManager.getServerManager().getIconID());
                momentMsg.setMoment(tvNewMoment.getText().toString());
                momentMsg.setGood(R.id.tv_good_number);
                tvNewMoment.setText("");
                System.out.println(momentMsgList);
                momentMsgList.add(momentMsg);
                MomentMsg.momentMsgList.add(momentMsg);
                String msg = gson.toJson(momentMsg);
                ServerManager serverManager = ServerManager.getServerManager();
                serverManager.sendMessage(msg, "ADDMOMENTMSG");
            }
        });
    }

    private void loadData() {
        for (MomentMsg momentMsg : MomentMsg.momentMsgList) {
            momentMsgList.add(momentMsg);
        }
    }
}

