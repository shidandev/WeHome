package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.wehome.model.Device;
import com.example.wehome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    Button login_btn;
    EditText username,password;
    boolean islogin = false;


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

                if(username.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0)
                {
                    checkLoginUser(username.getText().toString().trim(), password.getText().toString().trim());

                }
                else
                {
                    if(username.getText().toString().trim().length() <= 0)
                    {
                        Toast.makeText(getApplicationContext(),"Please insert username",Toast.LENGTH_SHORT).show();
                    }
                    if(password.getText().toString().trim().length() <= 0)
                    {
                        Toast.makeText(getApplicationContext(),"Please insert password",Toast.LENGTH_SHORT).show();
                    }
                }




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

    private boolean checkLoginUser(String username, final String password){


        Query query = users_node.orderByChild("username").equalTo(username);
        query.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    users.clear();
                    Log.d("user",dataSnapshot.toString());
                    for(DataSnapshot node : dataSnapshot.getChildren())
                    {
                        User user = new User();
                        try {
                            user = node.getValue(User.class);
                        }catch(Exception e){
                            user.setFullname(node.child("fullname").getValue().toString());
                            user.setUsername(node.child("username").getValue().toString());
                            user.setPassword(node.child("password").getValue().toString());
                            user.setId(node.child("id").getValue().toString());

                            HashMap<String,String> devices = new HashMap<>();
                            for(DataSnapshot dev:(node.child("devices")).getChildren()){
                                Log.d("try",dev.toString());
                                devices.put(dev.getKey(),dev.getValue().toString());
                            }
                            user.setDevices(devices);
//                            for(DataSnapshot d:((DataSnapshot)node.child("devices").getValue()).getChildren())
//                            {
//                                //Device temp = new Device();
//                                devices.put(d.getKey(),d.getValue().toString());
//                            }
//                            user.setDevices(devices);

//                            HashMap<String,String> general_devices = new HashMap<>();
//                            for(DataSnapshot d:((DataSnapshot)node.child("general_devices").getValue()).getChildren())
//                            {
//                                //Device temp = new Device();
//                                general_devices.put(d.getKey(),d.getValue().toString());
//                            }




                        }
                        users.add(user);

                    }

                    Log.d("test",users.get(0).getPassword());
                    if(users.get(0).getPassword().equals(password))
                    {
                        islogin = true;
                        Intent dashboard = new Intent(getApplicationContext(),Dashboard.class).putExtra("current_user",users.get(0));

                        startActivity(dashboard);

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });

        return islogin;
    }



}
