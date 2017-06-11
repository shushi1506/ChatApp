package com.example.shushi.models;



import java.io.Serializable;
import java.util.ArrayList;
import com.example.shushi.models.MessageChat;
/**
 * Created by Shushi on 4/24/2017.
 */

public class FriendModel implements Serializable {
    private  String urlPhoto;
    private  String displayNameFriend;
    private  String lastMessage;
    private  String keyFriend;
    private  String keySave;
    private  long timeConnect;
    private ArrayList<MessageChat> listMessage;

    public FriendModel() {
    }

    public ArrayList<MessageChat> getListMessage() {
        return listMessage;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public void setListMessage(ArrayList<MessageChat> listMessage) {
        this.listMessage = listMessage;
    }

    public void setDisplayNameFriend(String displayNameFriend) {
        this.displayNameFriend = displayNameFriend;
    }

    public void setTimeConnect(long timeConnect) {
        this.timeConnect = timeConnect;
    }

    public String getKeySave() {
        return keySave;
    }

    public void setKeySave(String keySave) {
        this.keySave = keySave;
    }

    public long getTimeConnect() {
        return timeConnect;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }



    public String getUrlPhoto() {
        return urlPhoto;
    }

    public String getDisplayNameFriend() {
        return displayNameFriend;
    }

    public String getLastMessage() {
        return lastMessage;
    }


    public String getKeyFriend() {
        return keyFriend;
    }

    public void setKeyFriend(String key) {
        this.keyFriend = key;
    }
}
