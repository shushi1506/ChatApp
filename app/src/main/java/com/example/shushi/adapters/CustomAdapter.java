package com.example.shushi.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Handler;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.shushi.models.MessageChat;

import com.example.shushi.testfirebase.ChangePassWordActivity;
import com.example.shushi.testfirebase.R;
import com.example.shushi.ui.ViewImageActivity;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.shushi.supports.SupportString.millisecondsToString;

/**
 * Created by Shushi on 3/30/2017.
 */

public class CustomAdapter extends BaseAdapter {
    ArrayList<MessageChat> messageChats;
    LayoutInflater layoutInflater;
    String urlImgUser, urlImgFriend;
    MediaPlayer mPlayer;
    Handler mHandler;


    public CustomAdapter(Context context, ArrayList<MessageChat> listData, String urlImgUser, String urlImgFriend) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.messageChats = listData;
        this.urlImgUser = urlImgUser;
        this.urlImgFriend = urlImgFriend;
        mPlayer=new MediaPlayer();
        mHandler=new Handler();
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
        final ViewHolder holder;
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

        boolean myMsg = item.isMe();//Just a dummy check to simulate whether it me or other sender
        setAlignment(holder, myMsg);
        DateFormat format = new SimpleDateFormat("MMddyyHHmmss");
        Date date = null;
        DateFormat df = new SimpleDateFormat("EEE HH:mm:ss");
        long epoch = Long.parseLong(item.getTimeMessage());
        String date1 = new java.text.SimpleDateFormat("MM/dd/yyyy HH:mm:ss").format(new java.util.Date(epoch));
        try {
            date = format.parse(item.getTimeMessage());

        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.txtMessage.setText(item.getTextMessage());
        holder.txtInfo.setText(date1);
        if (item.getUrlPhoto() != null) {
            holder.lnAudio.setVisibility(View.GONE);
            holder.mPath=item.getUrlPhoto();
            if (item.isAudio()) {
                holder.imgPhoto.setVisibility(View.GONE);
                if (isInternetAvailable()) {
                    try {
                        holder.lnAudio.setVisibility(View.VISIBLE);

                    } catch (Exception ex) {
                    }
                } else holder.lnAudio.setVisibility(View.GONE);
            } else if(item.isDoc())
            {
                holder.imgPhoto.setImageResource(0);
                holder.txtMessage.setText(Html.fromHtml(item.getUrlPhoto()));
                holder.txtMessage.setTextColor(R.color.colorAccent);
                holder.txtMessage.setMovementMethod(LinkMovementMethod.getInstance());

            }
            else
            {
                holder.imgPhoto.setVisibility(View.VISIBLE);
                Picasso.with(layoutInflater.getContext()).load(item.getUrlPhoto()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.imgPhoto);
            }
        } else {
            holder.lnAudio.setVisibility(View.GONE);
            holder.imgPhoto.setImageResource(0);
        }

        return convertView;
    }

    private void setAlignment(ViewHolder holder, boolean isMe) {
        if (isMe) {
            holder.contentWithBG.setBackgroundResource(R.drawable.in_message_bg);
            Picasso.with(layoutInflater.getContext()).load(urlImgUser).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.imgUser);
            holder.imgUser.setVisibility(View.VISIBLE);
            holder.imgFriend.setVisibility(View.GONE);
            LinearLayout.LayoutParams layoutParams;
            //region new
            layoutParams = (LinearLayout.LayoutParams) holder.contentMain.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgUser.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.imgUser.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgFriend.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.imgFriend.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgPhoto.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.imgPhoto.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.lnAudio.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.lnAudio.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgPlayPauseAudio.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.imgPlayPauseAudio.setLayoutParams(layoutParams);

//            layoutParams = (LinearLayout.LayoutParams) holder.seekBarAudio.getLayoutParams();
//            layoutParams.gravity = Gravity.RIGHT;
//            holder.seekBarAudio.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtCurrentPosion.getLayoutParams();
            layoutParams.gravity = Gravity.RIGHT;
            holder.txtCurrentPosion.setLayoutParams(layoutParams);
            //endregion
            layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
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
            Picasso.with(layoutInflater.getContext()).load(urlImgFriend).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(holder.imgFriend);
            holder.imgUser.setVisibility(View.GONE);
            holder.imgFriend.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams;
            //region new
            layoutParams = (LinearLayout.LayoutParams) holder.contentMain.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.contentWithBG.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgFriend.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.imgFriend.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgUser.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.imgUser.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgPhoto.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.imgPhoto.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.lnAudio.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.lnAudio.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.imgPlayPauseAudio.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.imgPlayPauseAudio.setLayoutParams(layoutParams);

            layoutParams = (LinearLayout.LayoutParams) holder.txtCurrentPosion.getLayoutParams();
            layoutParams.gravity = Gravity.LEFT;
            holder.txtCurrentPosion.setLayoutParams(layoutParams);
            //endregion
            layoutParams = (LinearLayout.LayoutParams) holder.contentWithBG.getLayoutParams();
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
        final ViewHolder holder = new ViewHolder();
        holder.txtMessage = (TextView) v.findViewById(R.id.txtMessage);
        holder.content = (LinearLayout) v.findViewById(R.id.content);
        holder.contentWithBG = (LinearLayout) v.findViewById(R.id.contentWithBackground);
        holder.contentMain = (LinearLayout) v.findViewById(R.id.contentMain);
        holder.lnAudio = (LinearLayout) v.findViewById(R.id.lnPlayAudio);
        holder.txtInfo = (TextView) v.findViewById(R.id.txtInfo);
        holder.txtCurrentPosion = (TextView) v.findViewById(R.id.currentPosionAudio);
        holder.imgPhoto = (ImageView) v.findViewById(R.id.imageViewPhoto);
        holder.imgUser = (ImageView) v.findViewById(R.id.imageUser);
        holder.imgFriend = (ImageView) v.findViewById(R.id.imageUserFriend);
        holder.imgPlayPauseAudio = (ImageView) v.findViewById(R.id.btnPlayPauseAudio);
        holder.imgPauseAudio = (ImageView) v.findViewById(R.id.btnPauseAudio);
        holder.mView=v;
        holder.imgPlayPauseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mPlayer.reset();
                    mHandler.removeCallbacksAndMessages(null);
                    mPlayer.setDataSource(holder.mPath.toString());
                    mPlayer.prepareAsync();
                    mPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            count_timer(holder.txtCurrentPosion);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        holder.imgPauseAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();

            }
        });
        holder.imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.imgPhoto.getResources()!=null){
                    Context va=v.getContext();
                    Intent tr=new Intent(va, ViewImageActivity.class);
                    tr.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    tr.putExtra("path",holder.mPath);
                    va.startActivity(tr);
                }
            }
        });

        return holder;
    }

    private void count_timer(final TextView tv){
        Runnable r=new Runnable() {
            @Override
            public void run() {
                tv.setText(millisecondsToString( mPlayer.getDuration()-mPlayer.getCurrentPosition()));
                count_timer(tv);
            }
        };
        mHandler.postDelayed(r,1000);
        if(mPlayer.getCurrentPosition()==mPlayer.getDuration()){
            tv.setText(millisecondsToString(mPlayer.getDuration()));
            mHandler.removeCallbacks(r);

        }
    }

    private static class ViewHolder {
        public TextView txtMessage;
        public TextView txtInfo, txtCurrentPosion;
        public LinearLayout content;
        public LinearLayout contentWithBG;
        public LinearLayout contentMain, lnAudio;
        public ImageView imgPhoto, imgFriend, imgUser, imgPlayPauseAudio, imgPauseAudio;
        public String mPath;
        public  View mView;
    }

    public boolean isInternetAvailable() {
        try {
            InetAddress ipAddr = InetAddress.getByName("www.google.com"); //You can replace it with your name
            return !ipAddr.equals("");
        } catch (Exception e) {
            return false;
        }
    }
}
