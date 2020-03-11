package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wehome.controller.DeviceArrayAdapter;
import com.example.wehome.model.Device;
import com.example.wehome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;


public class Dashboard extends AppCompatActivity {
    User current_user;
    ToggleButton temp_toggle;
    ListView lv;

    ArrayList<Device> devices = new ArrayList<>();
    ArrayList<String> devices_id = new ArrayList<>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    ArrayList<DatabaseReference> devices_node_path = new ArrayList<>();

    DatabaseReference users_node = myRef.child("users");
    DatabaseReference devices_node = myRef.child("devices");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        current_user = (User) getIntent().getSerializableExtra("current_user");

        TextView user_fullname = (TextView) findViewById(R.id.user_fullname);

        lv = (ListView) findViewById(R.id.list_dynamic_item);


        try {
            user_fullname.setText(current_user.getFullname());

        } catch (Exception e) {
            e.getLocalizedMessage();
        }
        getDevices();

    }

    int counter = 0;
    public void getDevices() {
        final Query query = users_node.orderByChild("username").equalTo(current_user.getUsername());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot node : dataSnapshot.getChildren()) {
                        DataSnapshot device = node.child("devices");
                        for (DataSnapshot node2 : device.getChildren()) {
                            devices_id.add(node2.getValue().toString());
                            devices_node_path.add(devices_node.child(node2.getValue().toString()));
                        }
                    }

                    if (!devices_node_path.isEmpty()) {

                        for (final DatabaseReference a : devices_node_path) {
                            a.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Device temp = dataSnapshot.getValue(Device.class);
                                    devices.add(temp);
                                    if(counter == devices_id.size()-1)
                                    {
                                        setupList();
                                    }
                                    else
                                    {
                                        counter++;
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                                public void setupList() {

                                    if (devices.size() > 0) {
                                        Log.d("data", String.valueOf(devices.size()));
                                        DeviceArrayAdapter dal = new DeviceArrayAdapter(Dashboard.this, R.layout.comp_view, devices);
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
