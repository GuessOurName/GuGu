package View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu.R;

import java.util.ArrayList;
import java.util.List;

import Adapter.AdapterMomentItem;
import Util.MsgMoment;

public class LayoutMoments extends Fragment {
    private View rootView;
    private List<MsgMoment> MsgMomentList;
    private RecyclerView momentRecyclerView;
    private AdapterMomentItem adapterMomentItem;
    private TextView tvNewMoment;
    private Button btnSend;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.layout_moments, container, false);
        initViews();
        return rootView;
    }

    private void initViews() {
        momentRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_list_moments);
        tvNewMoment = (TextView) rootView.findViewById(R.id.tv_moment_new);
        btnSend = (Button) rootView.findViewById(R.id.btn_moment_send);

        MsgMomentList = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            MsgMoment MsgMoment = new MsgMoment();
            MsgMoment.setUsername("Stark " + i);
            MsgMoment.setIconID(R.drawable.avasterwe);
            MsgMoment.setMoment("moments,moments,moments,moments,moments,moments" + i);
            MsgMoment.setGood((i % 3) == 1 ? R.drawable.good : R.drawable.ungood);
            MsgMomentList.add(MsgMoment);
        }

        adapterMomentItem = new AdapterMomentItem(getContext(), MsgMomentList);
        momentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        momentRecyclerView.setAdapter(adapterMomentItem);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MsgMoment MsgMoment = new MsgMoment();
                MsgMoment.setUsername("Stark ");
                MsgMoment.setIconID(R.drawable.avasterwe);
                MsgMoment.setMoment(tvNewMoment.getText().toString());
                MsgMoment.setGood(R.drawable.ungood);
                MsgMomentList.add(MsgMoment);
            }
        });
    }
}
