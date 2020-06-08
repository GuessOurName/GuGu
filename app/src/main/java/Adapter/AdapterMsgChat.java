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

import com.example.gugu.R;

import java.util.List;

import Util.MsgChat;

public class AdapterMsgChat extends ArrayAdapter<MsgChat> {

    private LayoutInflater inflater;
    private List<MsgChat> msgChats;
    public AdapterMsgChat(@NonNull Context context, @LayoutRes int resource, List<MsgChat> MsgChats) {
        super(context, resource);
        this.inflater = LayoutInflater.from(context);
        this.msgChats = MsgChats;
    }

    @Override
    public int getCount() {
        return msgChats.size();
    }

    @Nullable
    @Override
    public MsgChat getItem(int position) {
        return msgChats.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        MsgChat msg = getItem(position);
        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            assert msg != null;
            if (msg.isMyInfo()) {
                view = inflater.inflate(R.layout.char_me, parent, false);
            } else {
                view = inflater.inflate(R.layout.char_other, parent, false);
            }
            viewHolder = new ViewHolder();
            viewHolder.icon = (ImageView) view.findViewById(R.id.icon);
            viewHolder.username = (TextView) view.findViewById(R.id.username);
            viewHolder.content = (TextView) view.findViewById(R.id.content);

            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.icon.setImageResource(msgChats.get(position).getIconID());
        viewHolder.username.setText(msgChats.get(position).getUsername());
        viewHolder.content.setText(msgChats.get(position).getContent());
        return view;
    }

    private class ViewHolder {
        ImageView icon;
        TextView username;
        TextView content;
    }
}
