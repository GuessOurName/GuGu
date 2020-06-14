package Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.gugu_client.R;

import java.util.List;

import Activity.AtyChatRoom;
import Util.ImageManager;
import Util.UserItemMsg;
import View.LayoutChats;

public class AdapterUserItem extends RecyclerView.Adapter<AdapterUserItem.BaseViewHolder> {

    private Context context;
    private List<UserItemMsg> userItemMsgList;

    public AdapterUserItem(Context context, List<UserItemMsg> userItemMsgList) {
        this.context = context;
        this.userItemMsgList = userItemMsgList;
    }

    //设置layout样式
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new BaseViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user, parent, false));
    }

    //设置当前item项数据为传入userItemMsgList中的数据
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {

        if(userItemMsgList.get(position).getItemType()==1){
            holder.ivAvatar.setImageResource(ImageManager.imagesAvatar[0]);
            holder.tvUsername.setText("GroupId : "+userItemMsgList.get(position).getGroupName());
            holder.tvSign.setText("CreaterId : "+userItemMsgList.get(position).getCreaterId());
            holder.setUserItemMsg(userItemMsgList.get(position));
        }
        else if(userItemMsgList.get(position).getItemType()==2){
            holder.ivAvatar.setImageResource(ImageManager.imagesAvatar[1]);
            holder.tvUsername.setText("GroupId : "+userItemMsgList.get(position).getUserName());
            holder.tvSign.setText("CreaterId : "+userItemMsgList.get(position).getSign());
            holder.setUserItemMsg(userItemMsgList.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return (userItemMsgList == null ? 0 : userItemMsgList.size());
    }


    //内部类BaseViewHolder与layout控件对应，并设置监听事件
    class BaseViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivAvatar;
        private TextView tvUsername;
        private TextView tvSign;
        private UserItemMsg userItemMsg;

        BaseViewHolder(View itemView) {
            super(itemView);
            ivAvatar = (ImageView) itemView.findViewById(R.id.iv_item_avatar);
            tvUsername = (TextView) itemView.findViewById(R.id.tv_item_username);
            tvSign = (TextView) itemView.findViewById(R.id.tv_item_sign);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //监听事件创建聊天室，传入对应聊天用户信息
                    Intent intent = new Intent(context, AtyChatRoom.class);
                    intent.putExtra("Type",userItemMsg.getItemType() );
                    if(userItemMsg.getItemType()==1){
                        intent.putExtra("Id",userItemMsg.getGroupId());
                    }
                    else if(userItemMsg.getItemType()==2) {
                        intent.putExtra("Id",userItemMsg.getUserId());
                    }
                    context.startActivity(intent);

                    //将建立聊天室的聊天加入Chat碎片中
                    for (UserItemMsg item : LayoutChats.userItemMsgList) {
                        if(userItemMsg.getItemType()==1){
                            if (item.getUserId().equals(userItemMsg.getGroupId())) {
                                return;
                            }
                        }
                        else if(userItemMsg.getItemType()==2){
                            if(item.getGroupId().equals(userItemMsg.getUserId())){
                                return;
                            }
                        }
                    }
                    LayoutChats.userItemMsgList.add(userItemMsg);
                    UserItemMsg.userItemMsgList.add(userItemMsg);
                }
            });
        }

        public void setUserItemMsg(UserItemMsg tmp){
            this.userItemMsg=tmp;
        }
    }
}
