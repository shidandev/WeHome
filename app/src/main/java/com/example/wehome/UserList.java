package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
                                    Log.d("user", dataSnapshot.toString());
                                    if (dataSnapshot.exists()) {

                                        User temp = dataSnapshot.getValue(User.class);
                                        Toast.makeText(UserList.this, temp.getFullname(), Toast.LENGTH_SHORT).show();
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

                                        HashMap<String, ArrayList<String>> root = new HashMap<>();
                                        ArrayList<String> child1 = new ArrayList<>();

                                        child1.add("test1");
                                        child1.add("test2");
                                        child1.add("test3");
                                        child1.add("test4");

                                        root.put("root1", child1);
                                        root.put("root2",child1);
                                        ArrayList<String> header_list = new ArrayList<>();
                                        header_list.add("root1");
                                        header_list.add("root2");
//                                        Log.d("data", String.valueOf(devices.size()));
                                        UserArrayAdapter dal = new UserArrayAdapter(UserList.this, R.layout.user_header_list, R.layout.user_child_list, header_list, root);

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

}
