package com.example.wehome;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wehome.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddUser extends AppCompatActivity {

    User current_user;
    String type_trans;
    TextView title;
    Button submit_btn;
    EditText name_input, username_input, password_input;
    User edit_user;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference users_node = myRef.child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);
        type_trans = getIntent().getStringExtra("function");
        current_user = (User) getIntent().getSerializableExtra("current_user");
        title = (TextView) findViewById(R.id.title_label);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        name_input = (EditText) findViewById(R.id.name_input);
        username_input = (EditText) findViewById(R.id.username_input);
        password_input = (EditText) findViewById(R.id.password_input);

        if (type_trans.equals("add")) {

            title.setText("Add User");
            submit_btn.setText("SUBMIT");
        } else if (type_trans.equals("update")) {
            edit_user = (User)getIntent().getSerializableExtra("edit_user_id");
            name_input.setText(edit_user.getFullname());
            username_input.setText(edit_user.getUsername());
            password_input.setText(edit_user.getPassword());
            title.setText("Update User");
            submit_btn.setText("UPDATE");
        }

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String btn_name = ((Button) v).getText().toString();

                if (btn_name.equals("SUBMIT")) {
                    String name = name_input.getText().toString().trim();
                    String username = username_input.getText().toString().trim();
                    String password = password_input.getText().toString().trim();

                    if (!name.isEmpty() && !username.isEmpty() && !password.isEmpty()) {
                        final String cur_id = users_node.push().getKey();

                        //set user under which admin user
                        users_node.child(current_user.getId()).child("users").push().setValue(cur_id);

                        //get admin general devices
                        users_node.child(current_user.getId()).child("general_devices").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                Log.d("user",dataSnapshot.getValue());
                                users_node.child(cur_id).child("general_devices").setValue(dataSnapshot.getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        users_node.child(cur_id).child("id").setValue(cur_id);
                        users_node.child(cur_id).child("fullname").setValue(name);
                        users_node.child(cur_id).child("username").setValue(username);
                        users_node.child(cur_id).child("password").setValue(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddUser.this, UserList.class);
                                intent.putExtra("current_user", current_user);
                                startActivity(intent);
                                finish();
                            }
                        });

                    } else {
                        if (name.isEmpty()) {
                            Toast.makeText(AddUser.this, "Name Empty", Toast.LENGTH_SHORT).show();
                        }
                        if (username.isEmpty()) {
                            Toast.makeText(AddUser.this, "Username Empty", Toast.LENGTH_SHORT).show();
                        }
                        if (password.isEmpty()) {
                            Toast.makeText(AddUser.this, "Password Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (btn_name.equals("UPDATE")) {
                    Toast.makeText(AddUser.this, "update user", Toast.LENGTH_SHORT).show();
                    String name = name_input.getText().toString().trim();
                    String username = username_input.getText().toString().trim();
                    String password = password_input.getText().toString().trim();
                    if (!name.isEmpty() && !username.isEmpty() && !password.isEmpty()) {

                        users_node.child(edit_user.getId()).child("fullname").setValue(name);
                        users_node.child(edit_user.getId()).child("username").setValue(username);
                        users_node.child(edit_user.getId()).child("password").setValue(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Intent intent = new Intent(AddUser.this, UserList.class);
                                intent.putExtra("current_user", current_user);
                                startActivity(intent);
                                finish();
                            }
                        });
                    } else {
                        if (name.isEmpty()) {
                            Toast.makeText(AddUser.this, "Name Empty", Toast.LENGTH_SHORT).show();
                        }
                        if (username.isEmpty()) {
                            Toast.makeText(AddUser.this, "Username Empty", Toast.LENGTH_SHORT).show();
                        }
                        if (password.isEmpty()) {
                            Toast.makeText(AddUser.this, "Password Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });

    }
}
