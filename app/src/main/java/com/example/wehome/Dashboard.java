package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mohammedalaa.seekbar.RangeSeekBarView;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import model.Device;
import model.User;

public class Dashboard extends AppCompatActivity {
    User current_user;
    ToggleButton temp_toggle;

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

        current_user = (User)getIntent().getSerializableExtra("current_user");

        TextView user_fullname = (TextView)findViewById(R.id.user_fullname);


        try {
            user_fullname.setText(current_user.getFullname());

        }
        catch(Exception e){
            e.getLocalizedMessage();
        }
        getDevices();


        temp_toggle = (ToggleButton)findViewById(R.id.toggle);

        temp_toggle.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                Toast.makeText(getApplicationContext(),(on)?"On":"Off",Toast.LENGTH_SHORT).show();
            }
        });

        temp(getApplicationContext());

        RangeSeekBarView rsb = (RangeSeekBarView)  findViewById(R.id.range_seekbar);
        rsb.setMaxValue(20);
    }

    public void temp(Context a)
    {


    }
    public void getDevices()
    {
        Query query = users_node.orderByChild("username").equalTo(current_user.getUsername());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot node: dataSnapshot.getChildren()){
                        DataSnapshot device = node.child("devices");
                        for(DataSnapshot node2:device.getChildren())
                        {
                            devices_id.add(node2.getValue().toString());
                            devices_node_path.add(devices_node.child(node2.getValue().toString()));
                        }
                    }

                    if(!devices_node_path.isEmpty())
                    {
                        for(DatabaseReference a : devices_node_path)
                        {
                            a.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Device temp = dataSnapshot.getValue(Device.class);
                                    devices.add(temp);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            for(Device x : devices)
                            {
                                Log.d("test",x.getName());
                            }

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
