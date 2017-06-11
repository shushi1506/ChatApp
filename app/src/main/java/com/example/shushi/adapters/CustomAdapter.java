package com.example.shushi.adapters;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.shushi.models.MessageChat;
import com.example.shushi.testfirebase.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Shushi on 3/30/2017.
 */

public class CustomAdapter extends BaseAdapter {
    ArrayList<MessageChat> messageChats;
    LayoutInflater layoutInflater;
    public CustomAdapter(Context context, ArrayList<MessageChat> listData) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageChats = listData;
    }
    @Override
    public int getCount() {
        return messageChats.size();
    }

    @Override
    public Object getItem(int position) {
        return messageChats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        // Lấy ra đối tượng cần hiển thị ở vị trí thứ position
        MessageChat item = messageChats.get(position);
        // Khai báo các component
        TextView txtName, txtPhone;
        // Khởi tạo view.
        if (convertView == null) {
           convertView = layoutInflater.inflate(R.layout.list_item_chat_message, parent, false);
            holder = createViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        txtName = (TextView) convertView.findViewById(R.id._textviewmess);
//        txtPhone = (TextView) convertView.findViewById(R.id._textviewstamp);
//        // Set dữ liệu vào item của list view
//        txtName.setText(item.getTextMessage());
//        txtPhone.setText(item.getTimeMessage());

        boolean myMsg = item.isMe() ;//Just a dummy check to simulate whether it me or other sender
        setAlignment(holder, myMsg);
        DateFormat format = new SimpleDateFormat("MMddyyHHmmss");
        Date date=null;
        DateFormat df = new SimpleDateFormat("EEE HH:mm:ss");
        try {
             date = format.parse(item.getTimeMessage());



        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtMessage.setText(item.getTextMessage());
        holder.txtInfo.setText(df.format(date));
        return convertView;
    }
    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtInfo.setLayoutParams(layoutParams);
        } else {
            holder.contentWithBG.setBackgroundResource(R.drawable.out_message_bg);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.content.getLayoutParams();
            lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 0);
            lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            holder.content.setLayoutParams(lp);
            layoutParams = (LinearLayout.LayoutParams) holder.txtMessage.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtMessage.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtInfo.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtInfo.setLayoutParams(layoutParams);
        }
    }

    private ViewHolder createViewHolder(View v) {
        ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        return holder;
    }


    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo;
        public LinearLayout content;
        public LinearLayout contentWithBG;
    }
}
