package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wehome.controller.IconSpinnerAdapter;
import com.example.wehome.model.Icon;
import com.example.wehome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDevice extends AppCompatActivity {
    Spinner spinner;
    User current_user;
    ArrayList<String> icon_name = new ArrayList<String>();
    ArrayList<String> icon_path = new ArrayList<String>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users_node = myRef.child("users");
    DatabaseReference devices_node = myRef.child("devices");
    DatabaseReference images_node = myRef.child("image");
    DatabaseReference cur_user_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        current_user = (User) getIntent().getSerializableExtra("current_user");

        cur_user_path = users_node.child(current_user.getId());
        spinner = (Spinner) findViewById(R.id.icon_spinner);
        setupSpinner();
    }

    public void add_device(View v){
//        Toast.makeText(AddDevice.this,spinner.getSelectedItem().toString(),Toast.LENGTH_SHORT).show();
        String name = ((TextView)findViewById(R.id.name_input)).getText().toString().trim();
        String max = ((TextView)findViewById(R.id.max_input)).getText().toString().trim();
        String min = ((TextView)findViewById(R.id.min_input)).getText().toString().trim();
        String icon_path = spinner.getSelectedItem().toString().trim();

        if(!name.isEmpty() && !max.isEmpty() && !min.isEmpty() && !icon_path.isEmpty()){
            String id = devices_node.push().getKey();
            devices_node.child(id).child("broken_status").setValue(false);
            devices_node.child(id).child("icon").setValue(icon_path);
            devices_node.child(id).child("id").setValue(id);
            devices_node.child(id).child("max").setValue(Integer.valueOf(max));
            devices_node.child(id).child("min").setValue(Integer.valueOf(min));
            devices_node.child(id).child("name").setValue(name);
            devices_node.child(id).child("on").setValue(0);
            devices_node.child(id).child("value").setValue(0);
            cur_user_path.child("devices").push().setValue(id, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete( DatabaseError databaseError,  DatabaseReference databaseReference) {
                    if(databaseError != null)
                    {
                        Toast.makeText(AddDevice.this,databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(AddDevice.this,Dashboard.class);
                        intent.putExtra("current_user",current_user);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
        else
        {
            if(name.isEmpty())
            {
                Toast.makeText(AddDevice.this,"Device name is empty",Toast.LENGTH_SHORT).show();
            }
            if(max.isEmpty())
            {
                Toast.makeText(AddDevice.this,"Device maximum value is empty",Toast.LENGTH_SHORT).show();
            }
            if(min.isEmpty())
            {
                Toast.makeText(AddDevice.this,"Device minimum value is empty",Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void setupSpinner() {
        Query query = images_node.orderByChild("name");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot a : dataSnapshot.getChildren()) {
                        Icon temp = a.getValue(Icon.class);
                        icon_name.add(temp.getName());
                        icon_path.add(temp.getPath());

                    }
                }
                if (icon_name.size() > 0) {
                    IconSpinnerAdapter iconSpinnerAdapter  = new IconSpinnerAdapter(AddDevice.this,icon_name,icon_path);
                    spinner.setAdapter(iconSpinnerAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
