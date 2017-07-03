package com.example.shushi.controller;

import android.view.DragEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Shushi on 6/14/2017.
 */

public class MyDragListener implements View.OnDragListener{
    @Override
    public boolean onDrag(View v, DragEvent event) {
        switch(event.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Do nothing
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED :
                break;
            case DragEvent.ACTION_DRAG_LOCATION  :
                break;
            case DragEvent.ACTION_DRAG_ENDED   :
                // Do nothing
                break;
            case DragEvent.ACTION_DROP:
                ImageView view=(ImageView)event.getLocalState();

                break;
            default: break;
        }
        return true;
    }
}
