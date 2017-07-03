package com.example.shushi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shushi.models.MessageChat;
import com.example.shushi.supports.SupportString;
import com.example.shushi.testfirebase.LoginActivity;
import com.example.shushi.testfirebase.PlaceCallActivity;
import com.example.shushi.testfirebase.R;
import com.example.shushi.testfirebase.SinchService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import static com.example.shushi.supports.SupportString.CreatePassword;
import static com.example.shushi.supports.SupportString.EncodeString;
import static com.example.shushi.supports.SupportString.addSuffixeString;

/**
 * Created by Shushi on 4/23/2017.
 */

public class ThreeFragment extends Fragment  {
    private FirebaseAuth auth;
    private ImageView imgPhoto;
    private EditText mDisPlayName;
    private TextView mphoneNumber;
    private Button btnupdate;
    private static final int GALARY_INTENT = 1;
    private static Uri uriphoto;
    private DatabaseReference mRoot;
    private StorageReference mStorage;
    private ProgressDialog mprogressDialog;
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
        auth=FirebaseAuth.getInstance();
        mRoot= FirebaseDatabase.getInstance().getReference();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mStorage= FirebaseStorage.getInstance().getReference();
        imgPhoto=(ImageView)view.findViewById(R.id.imgPhotoProfile);
        mDisPlayName=(EditText)view.findViewById(R.id.displayName_Profile_Model);
        mphoneNumber=(TextView)view.findViewById(R.id.phoneNumber_editText_Profile_model);
        btnupdate=(Button)view.findViewById(R.id.btnUpdateProfile) ;
        mprogressDialog=new ProgressDialog(view.getContext());
        Picasso.with(view.getContext()).load(user.getPhotoUrl()).placeholder(R.drawable.giphy).error(R.drawable.common_google_signin_btn_icon_dark).into(imgPhoto);
        mphoneNumber.setText(SupportString.subSuffixeString(user.getEmail()));
        mDisPlayName.setText(auth.getCurrentUser().getDisplayName());

        imgPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(Intent.ACTION_PICK);
                in.setType("image/*");
                in.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(in, GALARY_INTENT);
            }
        });
        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mprogressDialog.setMessage("Loading...");
                mprogressDialog.show();
                if(uriphoto==null){
                    String ds;
                    if(mDisPlayName.getText().toString()!=null){
                        ds=mDisPlayName.getText().toString();
                    }else ds=auth.getCurrentUser().getDisplayName();
                    mRoot.child("Users").child(SupportString.EncodeString(auth.getCurrentUser().getEmail())).child("displayName").setValue(ds);
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(ds )
                            .build();
                    auth.getCurrentUser().updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mprogressDialog.dismiss();
                                        Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
                else {
                    String child;
                    if (uriphoto.getLastPathSegment() == null) {
                        child = CreatePassword(12);
                    } else {
                        child = CreatePassword(12) + uriphoto.getLastPathSegment();
                    }
                    final StorageReference storageChile = mStorage.child("UserPhotos").child(child);
                    storageChile.putFile(uriphoto).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                        }
                    }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Handle successful uploads on complete

                            Uri u = taskSnapshot.getDownloadUrl();
                            mRoot.child("Users").child(SupportString.EncodeString(auth.getCurrentUser().getEmail())).child("urlPhoto").setValue(u.toString());
                            String ds;
                            if(mDisPlayName.getText().toString()!=null){
                                ds=mDisPlayName.getText().toString();
                            }else ds=auth.getCurrentUser().getDisplayName();
                            mRoot.child("Users").child(SupportString.EncodeString(auth.getCurrentUser().getEmail())).child("displayName").setValue(ds);
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(ds )
                                    .setPhotoUri(u)
                                    .build();
                            auth.getCurrentUser().updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                mprogressDialog.dismiss();
Toast.makeText(getContext(),"Success",Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                        }
                    });
                }


            }
        });
        return  view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALARY_INTENT && data != null) {
            uriphoto = data.getData();
            imgPhoto.setImageURI(uriphoto);
        }
        }
}
