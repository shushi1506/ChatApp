package com.example.shushi.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.example.shushi.testfirebase.R;
import com.squareup.picasso.Picasso;

public class ViewImageActivity extends AppCompatActivity {

    private ImageView img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        //region code init toobar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        img=(ImageView)findViewById(R.id.imgPhotoViewImage) ;
        Intent t=getIntent();
        String path=t.getStringExtra("path");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Picasso.with(getApplicationContext()).load(path).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(img);

        //endregion
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
