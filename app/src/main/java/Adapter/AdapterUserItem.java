package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu.R;

import java.util.List;

import Util.MsgUserItem;

public class AdapterUserItem extends RecyclerView.Adapter<AdapterUserItem.BaseViewHolder> {

    private Context context;
    private List<MsgUserItem> userItemMsgList;

    //构造函数
    public AdapterUserItem(Context context, List<MsgUserItem> userItemMsgList) {
        this.context = context;
        this.userItemMsgList = userItemMsgList;
    }

    //ViewHolder创建重载
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    //设置控件属性
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.ivAvatar.setImageResource(userItemMsgList.get(position).getIconID());
        holder.tvUsername.setText(userItemMsgList.get(position).getUsername());
        holder.tvSign.setText(userItemMsgList.get(position).getSign());
    }

    //返回Item数量
    @Override
    public int getItemCount() {
        return (userItemMsgList == null ? 0 : userItemMsgList.size());
    }

    //继承ViewHolder内部类
    //设置UserItem各控件，并设置监听
    class BaseViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivAvatar;
        private TextView tvUsername;
        private TextView tvSign;

        BaseViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_item_avatar);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_item_username);
            tvSign = (TextView) itemView.findViewById(R.id.tv_item_sign);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, tvUsername.getText().toString(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
