package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.wehome.controller.DeviceArrayAdapter;
import com.example.wehome.controller.UserArrayAdapter;
import com.example.wehome.model.Device;
import com.example.wehome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class UserList extends AppCompatActivity {
    User current_user;
    int counter = 0;
    ExpandableListView lv;
    Button add_user_btn;

    ArrayList<User> users = new ArrayList<>();

    ArrayList<String> users_id = new ArrayList<>();

    ArrayList<DatabaseReference> users_node_path = new ArrayList<>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users_node = myRef.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        current_user = (User) getIntent().getSerializableExtra("current_user");
        lv = (ExpandableListView) findViewById(R.id.lv_user_list);
        add_user_btn = (Button) findViewById(R.id.add_user);
        add_user_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserList.this,AddUser.class);
                intent.putExtra("current_user",current_user);
                intent.putExtra("function","add");
                startActivity(intent);
            }
        });
        setup_user_list();
    }

    private void setup_user_list() {
        final Query query = users_node.orderByChild("username").equalTo(current_user.getUsername());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot root : dataSnapshot.getChildren()) {

                        DataSnapshot users = root.child("users");
                        for (DataSnapshot node2 : users.getChildren()) {

                            users_id.add(node2.getValue().toString());
                            users_node_path.add(users_node.child(node2.getValue().toString()));

                        }

                    }
                    if (!users_node_path.isEmpty()) {
                        for (final DatabaseReference a : users_node_path) {
                            a.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if (dataSnapshot.exists()) {
//                                        Log.d("user",dataSnapshot.toString());
                                        User temp = new User();
                                        temp.setFullname(dataSnapshot.child("fullname").getValue().toString());
                                        temp.setId(dataSnapshot.child("id").getValue().toString());
                                        temp.setUsername(dataSnapshot.child("username").getValue().toString());
                                        temp.setPassword(dataSnapshot.child("password").getValue().toString());

                                        HashMap<String, String> dev_map = new HashMap<>();
                                        for (DataSnapshot dev : dataSnapshot.child("devices").getChildren()) {
                                            dev_map.put(dev.getKey(), dev.getValue().toString());
                                        }
                                        temp.setDevices(dev_map);
//                                        Toast.makeText(UserList.this, temp.getFullname(), Toast.LENGTH_SHORT).show();
                                        users.add(temp);


                                        if (counter == users_node_path.size() - 1) {
                                            setupList();
                                        } else {
                                            counter++;
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }

                                public void setupList() {

                                    if (users.size() > 0) {


                                        HashMap<String, ArrayList<Device>> root = new HashMap<>();
                                        ArrayList<String> header_list = new ArrayList<>();

                                        DatabaseReference device_root = myRef.child("devices");
                                        for (int i = 0; i < users.size(); i++) {
                                            header_list.add(users.get(i).getFullname());
                                            final ArrayList<Device> child1 = new ArrayList<>();

                                            for (final String a : users.get(i).getDevices().keySet()) {
                                                final Device temp = new Device();
                                                 device_root.child(users.get(i).getDevices().get(a)).addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {


                                                            temp.setName(dataSnapshot.child("name").getValue().toString());
                                                            temp.setIcon(dataSnapshot.child("icon").getValue().toString());
                                                            temp.setId(a);
                                                            child1.add(temp);


                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                    }
                                                });

                                            }

                                            root.put(users.get(i).getFullname(), child1);
                                        }

                                        UserArrayAdapter dal = new UserArrayAdapter(UserList.this, R.layout.user_header_list, R.layout.user_child_list, users, root,current_user);

                                        lv.setAdapter(dal);

                                    } else {


                                    }
                                }
                            });
                        }


                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        //Execute your code here
        Intent dashboard = new Intent(getApplicationContext(),Dashboard.class).putExtra("current_user",current_user);

        startActivity(dashboard);
        finish();

    }
}
