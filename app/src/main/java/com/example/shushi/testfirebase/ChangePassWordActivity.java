package com.example.shushi.testfirebase;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ChangePassWordActivity extends AppCompatActivity {
    private EditText pass,confirmpass;
    private Button btn_update_pass,btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_word);
        pass=(EditText) findViewById(R.id.password_reset);
        confirmpass=(EditText) findViewById(R.id.confirm_password_reset);
        btn_update_pass=(Button)findViewById(R.id.btn_update_password);
        btn_back=(Button)findViewById(R.id.btn_back);


        btn_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String passd=pass.getText().toString();
                String confrim_pas=confirmpass.getText().toString();
                if(TextUtils.isEmpty(passd)){
                    Toast.makeText(getApplicationContext(),"Nhập mật khẩu mới",Toast.LENGTH_LONG).show();
                    return;
                }
                if(TextUtils.isEmpty(confrim_pas)){
                    Toast.makeText(getApplicationContext(),"Nhập lại mật khẩu mới",Toast.LENGTH_LONG).show();
                    confirmpass.requestFocus();
                    return;
                }
                if(passd.equals(confrim_pas)){

                }
                else {

                }
            }
        });
    }
}
