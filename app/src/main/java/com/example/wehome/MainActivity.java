package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;

import model.User;

public class MainActivity extends AppCompatActivity {

    Button login_btn;
    EditText username,password;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users_node = myRef.child("users");
    ArrayList <User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        login_btn = (Button) findViewById(R.id.btn_login);
        username = (EditText) findViewById(R.id.username_input);
        password = (EditText) findViewById(R.id.password_input);



        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"click n try",Toast.LENGTH_SHORT).show();

                checkLoginUser("lala","ahahha");


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        users_node.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkLoginUser(String username,String password){
        Query query = users_node.orderByChild("username").equalTo("shidan95");
        query.addListenerForSingleValueEvent(result);
    }


    ValueEventListener result = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists())
            {
                users.clear();
                Toast.makeText(getApplicationContext(),"ada",Toast.LENGTH_SHORT).show();
                for(DataSnapshot node : dataSnapshot.getChildren())
                {
                    User user = node.getValue(User.class);
                    users.add(user);
                    Toast.makeText(getApplicationContext(),user.getUsername(),Toast.LENGTH_SHORT).show();
                }

                Toast.makeText(getApplicationContext(),((users.get(0).getPassword().equals("test12312348567/8"))?"match":"xmatch"),Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getApplicationContext(),"xde",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
}
