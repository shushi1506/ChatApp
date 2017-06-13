package com.example.shushi.ui;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

/**
 * Created by Shushi on 6/12/2017.
 */

public class ActionItem {
    private Drawable icon;
    private String title;
    private int actionId = -1;
    private boolean sticky;

    public ActionItem(int actionId, String title, Drawable icon) {

        this.title = title;
        this.icon = icon;
        this.actionId = actionId;
    }

    public ActionItem() {

        this(-1, null, null);
    }

    public ActionItem(int actionId, String title) {

        this(actionId, title, null);
    }

    public ActionItem(Drawable icon) {

        this(-1, null, icon);
    }

    public ActionItem(int actionId, Drawable icon) {

        this(actionId, null, icon);
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public Drawable getIcon() {
        return this.icon;
    }

    public void setActionId(int actionId) {

        this.actionId = actionId;
    }

    public int getActionId() {

        return actionId;
    }

    public void setSticky(boolean sticky) {

        this.sticky = sticky;
    }

    public boolean isSticky() {

        return sticky;
    }
}
