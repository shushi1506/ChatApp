package com.example.shushi.models;

/**
 * Created by Shushi on 6/9/2017.
 */

public class ProfileInviteModel extends ProfileModel {
    private boolean isFriend;
    private boolean isInvited;
    private boolean isBeInvited;

    public ProfileInviteModel() {
    }

    public boolean isFriend() {
        return isFriend;
    }

    public boolean isInvited() {
        return isInvited;
    }

    public void setFriend(boolean friend) {
        isFriend = friend;
    }

    public boolean isBeInvited() {
        return isBeInvited;
    }

    public void setBeInvited(boolean beInvited) {
        isBeInvited = beInvited;
    }

    public void setInvited(boolean invited) {
        isInvited = invited;
    }
}
