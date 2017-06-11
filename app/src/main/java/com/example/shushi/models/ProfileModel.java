package com.example.shushi.models;

import com.google.firebase.database.Exclude;

import java.security.Timestamp;
import java.util.Date;
import java.util.Map;

/**
 * Created by Shushi on 4/24/2017.
 */

public class ProfileModel {


    private String displayName;
    private String emailReset;
    private String urlPhoto;
    private Boolean isConnect;
//    private Map<String,String> lastConnect;


    public ProfileModel() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getEmailReset() {
        return emailReset;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setEmailReset(String emailReset) {
        this.emailReset = emailReset;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public Boolean getConnect() {
        return isConnect;
    }

    public void setConnect(Boolean connect) {
        isConnect = connect;
    }

//    public Map<String, String> getLastConnect() {
//        return lastConnect;
//    }
//
//    public void setLastConnect(Map<String,String> lastConnect) {
//        this.lastConnect = lastConnect;
//    }
}
