package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.wehome.controller.DeviceArrayAdapter;
import com.example.wehome.model.Device;
import com.example.wehome.model.GeneralDevice;
import com.example.wehome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.zcw.togglebutton.ToggleButton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;


public class Dashboard extends AppCompatActivity {
    User current_user;
    ToggleButton temp_toggle;
    ListView lv;
    TextView temperature_reading;
    TextView humidity_reading;
    Button logout_btn,user_list_btn,add_device_btn;
    int counter = 0;

    ArrayList<Device> devices = new ArrayList<>();

    ArrayList<String> devices_id = new ArrayList<>();
    ArrayList<String> general_devices_id = new ArrayList<>();
    ArrayList<DatabaseReference> devices_node_path = new ArrayList<>();
    ArrayList<DatabaseReference> general_devices_node_path = new ArrayList<>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users_node = myRef.child("users");
    DatabaseReference devices_node = myRef.child("devices");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        current_user = (User) getIntent().getSerializableExtra("current_user");

        TextView user_fullname = (TextView) findViewById(R.id.user_fullname);
        temperature_reading = (TextView) findViewById(R.id.temperature_reading);
        humidity_reading = (TextView) findViewById(R.id.humidity_reading);
        lv = (ListView) findViewById(R.id.list_dynamic_item);
        logout_btn = (Button) findViewById(R.id.logout_btn);

        user_list_btn = (Button)findViewById(R.id.user_list_btn);
        add_device_btn = (Button)findViewById(R.id.add_device_btn);

        if(current_user.getType()!=null)
        {
            if(current_user.getType().equals("admin"))
            {
                user_list_btn.setVisibility(View.VISIBLE);
                add_device_btn.setVisibility(View.VISIBLE);
            }
            else
            {
                user_list_btn.setVisibility(View.GONE);
                add_device_btn.setVisibility(View.GONE);
            }
        }
        else
        {
            user_list_btn.setVisibility(View.GONE);
            add_device_btn.setVisibility(View.GONE);
        }


        try {

            user_fullname.setText(current_user.getFullname());



        } catch (Exception e) {
            Log.d("try",e.getLocalizedMessage());
        }
        getDevices();

    }
    public void user_list_page(View v){
        Intent intent = new Intent(Dashboard.this,UserList.class);
        intent.putExtra("current_user",current_user);
        startActivity(intent);
        finish();
    }
    public void logout(View v) {
        try {
            new AlertDialog.Builder(this)
                    .setTitle("")
                    .setMessage("Are you sure to Log out?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            Intent intent = new Intent(Dashboard.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();

        } catch (Exception e) {
        }
    }

    public void add_device_page(View v) {
        try {
            Intent intent = new Intent(Dashboard.this,AddDevice.class);
            intent.putExtra("current_user",current_user);
            startActivity(intent);

        } catch (Exception e) {
            Log.d("d",e.getLocalizedMessage());
        }
    }

    public void getDevices() {
        final Query query = users_node.orderByChild("username").equalTo(current_user.getUsername());

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot node : dataSnapshot.getChildren()) {
                        DataSnapshot device = node.child("devices");
                        DataSnapshot general_device = node.child("general_devices");
                        for (DataSnapshot node2 : device.getChildren()) {
                            devices_id.add(node2.getValue().toString());
                            devices_node_path.add(devices_node.child(node2.getValue().toString()));
                        }
                        for (DataSnapshot node3 : general_device.getChildren()) {
                            general_devices_id.add(node3.getValue().toString());
                            general_devices_node_path.add(devices_node.child(node3.getValue().toString()));
                        }
                    }

                    if (!general_devices_node_path.isEmpty()) {
                        for (final DatabaseReference a : general_devices_node_path) {

                            a.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        GeneralDevice generalDevice = dataSnapshot.getValue(GeneralDevice.class);
//                                        private DecimalFormat df2 = new DecimalFormat("0.00");
                                        Log.d("d", generalDevice.toString());
                                        if (generalDevice.getName().equals("temperature")) {
                                            temperature_reading.setText(String.format("%.2f", generalDevice.getValue()));
                                        }
                                        if (generalDevice.getName().equals("humidity")) {
                                            humidity_reading.setText(String.format("%.2f", generalDevice.getValue()));
                                        }
                                    } catch (Exception e) {
                                        Log.d("d", e.getLocalizedMessage());
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    if (!devices_node_path.isEmpty()) {

                        for (final DatabaseReference a : devices_node_path) {
                            a.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    try {
                                        Device temp = dataSnapshot.getValue(Device.class);
                                        if(temp.getName() != null)
                                        {
                                            devices.add(temp);
                                        }
                                        if (counter == devices_id.size() - 1) {
//                                            Toast.makeText(Dashboard.this, "here", Toast.LENGTH_SHORT).show();
                                            setupList();
                                        } else {
                                            counter++;
                                        }
                                    } catch (Exception e) {
                                        Log.d("d", "uina");
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }


                                public void setupList() {

                                    if (devices.size() > 0) {



//                                        Log.d("data", String.valueOf(devices.size()));
                                        DeviceArrayAdapter dal = new DeviceArrayAdapter(Dashboard.this, R.layout.comp_view, devices,current_user);
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
