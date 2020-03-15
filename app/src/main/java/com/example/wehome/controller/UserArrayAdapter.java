package com.example.wehome.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.wehome.Dashboard;
import com.example.wehome.MainActivity;
import com.example.wehome.R;
import com.example.wehome.UserList;
import com.example.wehome.model.Device;
import com.example.wehome.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mohammedalaa.seekbar.RangeSeekBarView;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class UserArrayAdapter extends BaseExpandableListAdapter {

    int iniator = 0; //as counter
    //
    private Context ctx;
    private HashMap<String, ArrayList<Device>> childItem;
    private ArrayList<User> users;
    private int hl;
    private int cl;
    private ViewHolder holder;
    private User current_user;

    DatabaseReference myRoot = FirebaseDatabase.getInstance().getReference();
    DatabaseReference user_root = myRoot.child("users");

    public UserArrayAdapter(Context ctx, int header_layout, int child_layout, ArrayList<User> users, HashMap<String, ArrayList<Device>> childItem, User current_user) {
        this.ctx = ctx;
        this.users = users;
        this.childItem = childItem;
        this.hl = header_layout;
        this.cl = child_layout;
        this.current_user = current_user;
    }

    @Override
    public int getGroupCount() {
        return this.users.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childItem.get(users.get(groupPosition).getFullname()).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return users.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItem.get(users.get(groupPosition).getFullname()).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        User title = (User) this.getGroup(groupPosition);
        final View result;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(hl, null);
            holder = new ViewHolder();
            holder.user_name = (TextView) convertView.findViewById(R.id.user_fullname);
            holder.delete_btn = (Button) convertView.findViewById(R.id.delete_btn);
            holder.add_btn = (Button) convertView.findViewById(R.id.add_device_btn);

            result = convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        holder.user_name.setText(users.get(groupPosition).getFullname());
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("user", "sni oi jadi ");
            }
        });

        setupAddDevice(holder.add_btn, groupPosition);
        return result;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Device title = (Device) this.getChild(groupPosition, childPosition);


        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(cl, null);

        }

        ImageView comp_icon = (ImageView) convertView.findViewById(R.id.comp_icon);
        TextView id_input = (TextView) convertView.findViewById(R.id.id_input);
        TextView name_input = (TextView) convertView.findViewById(R.id.name_input);
        Button delete_btn = (Button) convertView.findViewById(R.id.delete_btn);

        comp_icon.setImageResource(ctx.getResources().getIdentifier(((Device) getChild(groupPosition, childPosition)).getIcon(), "drawable", ctx.getPackageName()));
        id_input.setText(((Device) getChild(groupPosition, childPosition)).getId());
        name_input.setText(((Device) getChild(groupPosition, childPosition)).getName());
        setupDeleteBtn(delete_btn, groupPosition, childPosition);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class ViewHolder {
        TextView user_name;
        Button delete_btn;
        Button add_btn;

    }

    public void setupAddDevice(Button addBtn, final int gPos) {
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setTitle("Choose device assign to user " + ((User) getGroup(gPos)).getFullname());

                final ArrayList<String> dev_list_id = new ArrayList<>();
                final ArrayList<String> dev_list_name = new ArrayList<>();
                final ArrayList<Boolean> dev_list_select = new ArrayList<>();


                final int counter = current_user.getDevices().size();
                for (String list_dev : current_user.getDevices().keySet()) {

//                    Log.d("user",current_user.getDevices().get(list_dev).toString());
                    dev_list_id.add(current_user.getDevices().get(list_dev).toString());
                    dev_list_select.add(false);

                    myRoot.child("devices").child(current_user.getDevices().get(list_dev).toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()) {
                                dev_list_name.add(dataSnapshot.child("name").getValue().toString());

                                if (iniator == counter - 1) {
                                    setupModal();
                                    iniator = 0;
                                } else {
                                    iniator++;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }

                        public void setupModal() {
//                            Log.d("user",String.valueOf(dev_list_name.size()));

                            final String[] dev_list_id2 = dev_list_id.toArray(new String[0]);
                            String[] dev_list_name2 = dev_list_name.toArray(new String[0]);
                            final boolean[] dev_list_select2 = new boolean[dev_list_select.size()];
                            for (int i = 0; i < dev_list_select.size(); i++) {
                                dev_list_select2[i] = dev_list_select.get(i);
                            }

                            builder.setMultiChoiceItems(dev_list_name2, dev_list_select2, new DialogInterface.OnMultiChoiceClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                    dev_list_select2[which] = isChecked;
                                }
                            });


                            builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {


                                    myRoot.child("users").child(((User) getGroup(gPos)).getId()).child("devices").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (int i = 0; i < dev_list_select2.length; i++) {
                                                if(dev_list_select2[i])
                                                {
                                                    boolean duplicate = false;

                                                    for(DataSnapshot node:dataSnapshot.getChildren())
                                                    {
//                                                        Log.d("try dari fb",node.getValue().toString());
//                                                        Log.d("try dari app",dev_list_id.get(i));
                                                        if((node.getValue().toString()).equals(dev_list_id.get(i)))
                                                        {
//                                                            Log.d("try","match ->"+ dev_list_id.get(i));
                                                            duplicate = true;
                                                            break;
                                                        }
                                                        else
                                                        {

//                                                            Log.d("try","xmatch -> "+dev_list_id.get(i));
                                                            duplicate = false;
                                                        }

                                                    }
                                                    if(!duplicate)
                                                    {
                                                        myRoot.child("users").child(((User) getGroup(gPos)).getId()).child("devices").push().setValue(dev_list_id.get(i));
                                                        Intent intent = new Intent(ctx,UserList.class);
                                                        intent.putExtra("current_user",current_user);
                                                        ctx.startActivity(intent);
                                                        ((Activity)ctx).finish();
                                                    }

                                                }
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                                }
                            });
                            builder.setNegativeButton("Cancel", null);

                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }


            }
        });
    }

    public void setupDeleteBtn(Button deleteBtn, final int gPos, final int cPos) {
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ctx)
                        .setTitle("")
                        .setMessage("Are you sure to remove these device from user " + users.get(gPos).getFullname() + "?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                DatabaseReference te = user_root.child(users.get(gPos).getId()).child("devices").child(((Device) getChild(gPos, cPos)).getId());

                                te.removeValue();

                                Intent intent = new Intent(ctx, UserList.class);
                                intent.putExtra("current_user", current_user);
                                ctx.startActivity(intent);
                                ((Activity) ctx).finish();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }
}
