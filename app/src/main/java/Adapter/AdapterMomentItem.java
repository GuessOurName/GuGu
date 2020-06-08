package Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu.R;

import java.util.List;

import Util.MsgMoment;

public class AdapterMomentItem extends RecyclerView.Adapter<AdapterMomentItem.BaseViewHolder>{

    private Context context;
    private List<MsgMoment> MsgMomentList;

    public AdapterMomentItem(Context context, List<MsgMoment> MsgMomentList) {
        this.context = context;
        this.MsgMomentList = MsgMomentList;
    }


    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_moment, parent, false));
    }

    //设置MomentItem控件属性
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.imageView.setImageResource(MsgMomentList.get(position).getIconID());
        holder.username.setText(MsgMomentList.get(position).getUsername());
        holder.moment.setText(MsgMomentList.get(position).getMoment());
        holder.good.setImageResource(MsgMomentList.get(position).getGood());
    }

    @Override
    public int getItemCount() {
        return (MsgMomentList == null ? 0 : MsgMomentList.size());
    }

    //继承ViewHolder内部类
    //设置UserItem各控件，可设置监听
    class BaseViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;
        private TextView username;
        private TextView moment;
        private ImageView good;
        BaseViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_moment_content_avatar);
            username = (TextView) itemView.findViewById(R.id.tv_moment_content_username);
            moment = (TextView) itemView.findViewById(R.id.tv_moment_content);
            good = (ImageView) itemView.findViewById(R.id.iv_good);
        }
    }
}
