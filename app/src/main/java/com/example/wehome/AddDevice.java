package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.wehome.controller.IconSpinnerAdapter;
import com.example.wehome.model.Icon;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddDevice extends AppCompatActivity {
    Spinner spinner;

    ArrayList<String> icon_name = new ArrayList<String>();
    ArrayList<String> icon_path = new ArrayList<String>();

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users_node = myRef.child("users");
    DatabaseReference images_node = myRef.child("image");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);

        spinner = (Spinner) findViewById(R.id.icon_spinner);
        setupSpinner();
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
