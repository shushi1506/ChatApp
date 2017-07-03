package com.example.shushi.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

/**
 * Created by Shushi on 3/30/2017.
 */
public class MessageChat implements Parcelable {


    public MessageChat() {
    }

    private String textMessage;
    private String timeMessage;
    private boolean isMe;
    private String urlPhoto;
    private String keyMessage;
    private boolean isAudio;
    private boolean isDoc;


    protected MessageChat(Parcel in) {
        textMessage = in.readString();
        timeMessage = in.readString();
        isMe = in.readByte() != 0;
        urlPhoto = in.readString();
        keyMessage = in.readString();
        isAudio = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(textMessage);
        dest.writeString(timeMessage);
        dest.writeByte((byte) (isMe ? 1 : 0));
        dest.writeString(urlPhoto);
        dest.writeString(keyMessage);
        dest.writeByte((byte) (isAudio ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MessageChat> CREATOR = new Creator<MessageChat>() {
        @Override
        public MessageChat createFromParcel(Parcel in) {
            return new MessageChat(in);
        }

        @Override
        public MessageChat[] newArray(int size) {
            return new MessageChat[size];
        }
    };

    public boolean isDoc() {
        return isDoc;
    }

    public void setDoc(boolean doc) {
        isDoc = doc;
    }

    public String getKey() {
        return keyMessage;
    }

    public void setKey(String key) {
        this.keyMessage = key;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public void setTimeMessage(String timeMessage) {
        this.timeMessage = timeMessage;
    }

    public void setMe(boolean me) {
        isMe = me;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public String getTimeMessage() {
        return timeMessage;
    }

    public boolean isMe() {
        return isMe;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public boolean isAudio() {
        return isAudio;
    }

    public void setAudio(boolean audio) {
        isAudio = audio;
    }
}
