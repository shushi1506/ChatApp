package com.example.shushi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.shushi.models.FriendModel;
import com.example.shushi.testfirebase.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;



/**
 * Created by Shushi on 4/24/2017.
 */

public class CustomFriendAdapter extends BaseAdapter {
    ArrayList<FriendModel> friendList;
    LayoutInflater layoutInflater;

    public CustomFriendAdapter(Context context, ArrayList<FriendModel> listData) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.friendList = listData;
    }

    public ArrayList<FriendModel> getFriendList() {
        return friendList;
    }

    @Override
    public int getCount() {
        return friendList.size();
    }

    @Override
    public Object getItem(int position) {
        return friendList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Lấy ra đối tượng cần hiển thị ở vị trí thứ position
        FriendModel item = friendList.get(position);
        // Khai báo các component
        TextView txtName, txtLastMess;
        ImageView imageview;
        // Khởi tạo view.
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.modelfriend, parent, false);
        }
        txtName = (TextView) convertView.findViewById(R.id.txtdisplay_model_friend);
//        txtLastMess = (TextView) convertView.findViewById(R.id.txtlast_mess_model_friend);
        imageview=(ImageView)convertView.findViewById(R.id.imageview_model_friend);
        // Set dữ liệu vào item của list view
        txtName.setText(item.getDisplayNameFriend());
//        txtLastMess.setText(item.getLastMessage());

        try {
            Picasso.with(layoutInflater.getContext()).load(item.getUrlPhoto()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(imageview);
        }catch(Exception ex){imageview.setImageResource(R.drawable.giphy);}
        return convertView;
    }
}
