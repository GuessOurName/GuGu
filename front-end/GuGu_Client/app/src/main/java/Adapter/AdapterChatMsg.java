package Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.gugu_client.R;

import java.util.List;

import Util.ChatMsg;
import Util.ImageManager;

public class AdapterChatMsg extends ArrayAdapter<ChatMsg> {

    private LayoutInflater inflater;
    private List<ChatMsg> chatMsgs;
    public AdapterChatMsg(@NonNull Context context, @LayoutRes int resource, List<ChatMsg> chatMsgs) {
        super(context, resource);
        this.inflater = LayoutInflater.from(context);
        this.chatMsgs = chatMsgs;
    }

    @Override
    public int getCount() {
        return chatMsgs.size();
    }

    @Nullable
    @Override
    public ChatMsg getItem(int position) {
        return chatMsgs.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ChatMsg msg = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (!msg.isMyInfo()) {
            view = inflater.inflate(R.layout.chat_other, parent, false);
        } else {
            view = inflater.inflate(R.layout.chat_me, parent, false);
        }
        viewHolder = new ViewHolder();
        viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
        viewHolder.username = (TextView) view.findViewById(R.id.username);
        viewHolder.content = (TextView) view.findViewById(R.id.content);
        view.setTag(viewHolder);
        viewHolder.icon.setImageResource(ImageManager.imagesAvatar[chatMsgs.get(position).getIconID()]);
        viewHolder.username.setText(chatMsgs.get(position).getUsername());
        viewHolder.content.setText(chatMsgs.get(position).getContent());
        return view;

    }

    private class ViewHolder {
        ImageView icon;
        TextView username;
        TextView content;
    }
}
