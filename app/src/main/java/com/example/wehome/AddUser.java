package com.example.wehome;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wehome.model.User;

public class AddUser extends AppCompatActivity {

    User current_user;
    String type_trans;
    TextView title;
    Button submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        current_user = (User)getIntent().getSerializableExtra("current_user");
        type_trans = getIntent().getStringExtra("function");
        title = (TextView) findViewById(R.id.title_label);
        submit_btn = (Button) findViewById(R.id.submit_btn);

        if(type_trans.equals("add"))
        {
            title.setText("Add User");
            submit_btn.setText("SUBMIT");
        }
        else if(type_trans.equals("update"))
        {
            title.setText("Update User");
            submit_btn.setText("UPDATE");
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_name = ((Button)v).getText().toString();

                if(btn_name.equals("SUBMIT"))
                {
                    Toast.makeText(AddUser.this,"submit new user",Toast.LENGTH_SHORT).show();
                }
                else if(btn_name.equals("UPDATE"))
                {
                    Toast.makeText(AddUser.this,"update user",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
