package com.example.shushi.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.shushi.testfirebase.LoginActivity;
import com.example.shushi.testfirebase.PlaceCallActivity;
import com.example.shushi.testfirebase.R;
import com.example.shushi.testfirebase.SinchService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

/**
 * Created by Shushi on 4/23/2017.
 */

public class ThreeFragment extends Fragment  {
    private FirebaseAuth auth;
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       final View view= inflater.inflate(R.layout.fragment_three, container, false);
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


//        ImageView image=(ImageView)view.findViewById(R.id.imageView_Profile_model);
//
//
//        try {
//            Picasso.with(inflater.getContext()).load(user.getPhotoUrl()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(image);
//        }catch(Exception ex){image.setImageResource(R.drawable.giphy);}
//        Button btn=(Button)view.findViewById(R.id.btnCallPlace_fragment);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), PlaceCallActivity.class));
//            }
//        });
//        Button btnlogou=(Button)view.findViewById(R.id.btnLogutAuth);
//        btnlogou.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                auth = FirebaseAuth.getInstance();
//                auth.signOut();
//
//                startActivity(new Intent(getContext(), LoginActivity.class));
//            }
//        });

        return  view;
    }


}
