package com.example.shushi.testfirebase;

import com.google.firebase.database.Exclude;

/**
 * Created by Shushi on 3/30/2017.
 */
public class MessageChat {
    public void setText(String text) {
        this.text = text;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getText() {
        return text;
    }

    public String getTime() {
        return time;
    }



    private String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public MessageChat(String text, String time) {
        this.text = text;
        this.time = time;
    }

    public MessageChat() {
    }

    public String text;
    public String time;
}
