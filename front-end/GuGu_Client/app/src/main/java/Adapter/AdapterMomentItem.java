package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu_client.R;

import java.util.List;

import Server.ServerManager;
import Util.ImageManager;
import Util.MomentMsg;

public class AdapterMomentItem extends RecyclerView.Adapter<AdapterMomentItem.BaseViewHolder> {

    private Context context;
    private List<MomentMsg> momentMsgList;

    public AdapterMomentItem(Context context, List<MomentMsg> momentMsgList) {
        this.context = context;
        this.momentMsgList = momentMsgList;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_moment, parent, false));
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.imageView.setImageResource(ImageManager.imagesAvatar[momentMsgList.get(position).getIconID()]);
        holder.username.setText(momentMsgList.get(position).getUserId());


        holder.moment.setText(momentMsgList.get(position).getMoment());
//        holder.good.setImageResource(momentMsgList.get(position).getGood());
        holder.good.setImageResource(R.drawable.ungood);
        holder.good_num.setText(String.valueOf(momentMsgList.get(position).getGoodNum()));
    }

    @Override
    public int getItemCount() {
        return (momentMsgList == null ? 0 : momentMsgList.size());
    }

    class BaseViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView username;
        private TextView moment;
        private ImageView good;
        private boolean isgood = false;
        private TextView good_num;

        BaseViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_moment_content_avatar);
            username = (TextView) itemView.findViewById(R.id.tv_moment_content_username);
            moment = (TextView) itemView.findViewById(R.id.tv_moment_content);
            good = (ImageView) itemView.findViewById(R.id.iv_good);
            good_num = (TextView) itemView.findViewById(R.id.tv_good_number);

            good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    good.setImageResource(isgood ? R.drawable.ungood : R.drawable.good);
                    for (MomentMsg momentMsg : MomentMsg.momentMsgList) {
                        if (momentMsg.getMoment().equals(moment.getText().toString())) {
                            int goods = Integer.parseInt(good_num.getText().toString());
                            goods = isgood ? goods - 1 : goods + 1;
                            momentMsg.setGood(goods);
                            String str_goods = String.valueOf(goods);
                            good_num.setText(str_goods);

                        }
                    }
                    isgood = !isgood;
                }
            });
        }
    }
}
