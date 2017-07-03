package com.example.shushi.ui;

import android.content.ClipData;
import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.shushi.testfirebase.R;

public class DrogAndDragActivity extends AppCompatActivity {
    ImageView im2;
    private boolean isSpeakButtonLongPressed = false;
    private boolean isDrop = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drog_and_drag);
        ImageView im=(ImageView)findViewById(R.id.ndf);
         im2=(ImageView)findViewById(R.id.dty);
        im.setOnTouchListener(new MyTouchListener());
        im2.setOnDragListener(new MyDragListener());
    }

    public class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Toast.makeText(getApplicationContext(),"drop start",Toast.LENGTH_LONG).show();
                    // Do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Toast.makeText(getApplicationContext(),"drop enter",Toast.LENGTH_LONG).show();
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Toast.makeText(getApplicationContext(),"drop exit",Toast.LENGTH_LONG).show();
                    break;
                case DragEvent.ACTION_DRAG_LOCATION:

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    // Do nothing
                    if(isDrop==true){
                        Toast.makeText(getApplicationContext(),"Da drop k gui file",Toast.LENGTH_LONG).show();
                        isDrop=false;
                    }
                    else {
                        im2.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),"gui file",Toast.LENGTH_LONG).show();
                    }
                    isDrop=true;
                    break;
                case DragEvent.ACTION_DROP:
                    final View view = (View) event.getLocalState();
                    v.setVisibility(View.GONE);
                    isDrop=true;

                    break;
                default:
                    break;
            }
            return true;
        }
    }
    private final class MyLongClickListener implements View.OnLongClickListener {
        @Override
        public boolean onLongClick(View v) {

            isSpeakButtonLongPressed=true;
            return  true;
        }
    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    if(isSpeakButtonLongPressed){
                    im2.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(),"file",Toast.LENGTH_LONG).show();
                    ClipData data = ClipData.newPlainText("", "");
                    View vi=findViewById(R.id.dty);
                    View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(
                            vi);
                    view.startDrag(data, shadowBuilder, view, 0);
                        isSpeakButtonLongPressed=false;
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Giữ để ghi âm",Toast.LENGTH_LONG).show();

                    }
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    isSpeakButtonLongPressed=false;
                }

            return false;

        }
    }



}
