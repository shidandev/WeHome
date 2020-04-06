package com.example.wehome.controller;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.wehome.Dashboard;
import com.example.wehome.MainActivity;
import com.example.wehome.R;
import com.example.wehome.model.Device;
import com.example.wehome.model.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mohammedalaa.seekbar.RangeSeekBarView;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.Queue;

public class DeviceArrayAdapter extends ArrayAdapter {
    private ViewHolder holder;
    private int mResource;
    private Context mContext;
    private ArrayList<Device> devices;
    private int lastPosition = -1;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("devices");
    DatabaseReference root = FirebaseDatabase.getInstance().getReference();
    DatabaseReference cur_user;
    User current_user;

    static class ViewHolder {
        TextView device_name;
        ImageView device_icon;
        RangeSeekBarView range_seekbar;
        ToggleButton toggle;
        TextView toggle_label;
        TextView input_id;
    }

    public DeviceArrayAdapter(Context context, int resource, ArrayList<Device> objects, User user) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        devices = objects;
        cur_user = FirebaseDatabase.getInstance().getReference().child("users").child(user.getId());
        current_user = user;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View result;
        final Device cur_device = devices.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new DeviceArrayAdapter.ViewHolder();
            holder.device_name = convertView.findViewById(R.id.device_name_input);
            holder.device_icon = convertView.findViewById(R.id.device_icon);
            holder.toggle = convertView.findViewById(R.id.toggle);
            holder.range_seekbar = convertView.findViewById(R.id.range_seekbar);
            holder.toggle_label = convertView.findViewById(R.id.toggle_label);
            holder.input_id = convertView.findViewById(R.id.input_id);

            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (DeviceArrayAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.scroll_down_anim : R.anim.scroll_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

        holder.device_name.setText(cur_device.getName());
        holder.device_icon.setImageResource((cur_device.getIcon() != "") ? R.drawable.icon_temp : R.drawable.icon_light);
        holder.device_icon.setImageResource(mContext.getResources().getIdentifier(cur_device.getIcon(), "drawable", mContext.getPackageName()));
        Log.d("d", cur_device.getIcon());
        holder.input_id.setText(cur_device.getId());
        setupSeekbar(holder.range_seekbar, cur_device);
        setupToggle(holder.toggle, holder.toggle_label, cur_device);
        try {
            if (current_user.getType().equals("admin")) {
                ((LinearLayout) result).setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        try {
                            new AlertDialog.Builder(mContext)
                                    .setTitle("Remove Device")
                                    .setMessage("Are you sure to remove this device?")
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            myRef.child(cur_device.getId()).removeValue();
                                            cur_user.child("devices").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    if (dataSnapshot.exists()) {
                                                        for (DataSnapshot e : dataSnapshot.getChildren()) {
                                                            String temp = e.getValue(String.class);
//                                                    Toast.makeText(mContext, cur_device.getId(), Toast.LENGTH_SHORT).show();
//                                                    Toast.makeText(mContext, e.getKey(), Toast.LENGTH_SHORT).show();

                                                            if (temp.equals(cur_device.getId())) {
                                                                cur_user.child("devices").child(e.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void aVoid) {
                                                                        Intent intent = new Intent(mContext, Dashboard.class);
                                                                        intent.putExtra("current_user", current_user);
                                                                        mContext.startActivity(intent);
                                                                        ((Activity) mContext).finish();
                                                                    }
                                                                });

                                                                Log.d("test",e.toString());

                                                                root.child("users").child(current_user.getId()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        if(dataSnapshot.exists())
                                                                        {
                                                                            for(final DataSnapshot a : dataSnapshot.getChildren())
                                                                            {
                                                                                root.child("users").child(a.getValue().toString()).child("devices").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                                    @Override
                                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                                        if(dataSnapshot.exists())
                                                                                        {
                                                                                            for(DataSnapshot b:dataSnapshot.getChildren())
                                                                                            {
                                                                                                String temp_val = b.getValue().toString();
                                                                                                if(cur_device.getId().equals(temp_val))
                                                                                                {
                                                                                                    root.child("users").child(a.getValue().toString()).child("devices").child(b.getKey()).removeValue();
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
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                                                    }
                                                                });

                                                            }

                                                        }
                                                    } else {
                                                        Toast.makeText(mContext, "xde data", Toast.LENGTH_SHORT).show();
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                                }
                                            });

                                        }
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();

                        } catch (Exception e) {
                        }
                        return false;
                    }
                });
            }
        }catch (Exception e){}

        return convertView;

    }

    public void setupSeekbar(RangeSeekBarView rsb, final Device dev) {
        try {

            rsb.setAnimated(true, 3000L);
            rsb.setMaxValue(((dev.getMax() == 0) ? 1 : dev.getMax()));
            rsb.setMinValue(dev.getMin());
            rsb.setValue(dev.getValue());

            rsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    Log.d("data", dev.getId());
                    if(dev.getMax() == 1)
                    {
                        ((RangeSeekBarView) seekBar).setValue(progress / 4);
                    }
                    else
                    {
                        ((RangeSeekBarView) seekBar).setValue(progress / (100 / ((dev.getMax() == 0) ? 1 : dev.getMax())));

                    }
                    myRef.child(dev.getId()).child("value").setValue(progress / (100 / ((dev.getMax() == 0) ? 1 : dev.getMax())));

                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

//                Toast.makeText(mContext,String.valueOf(dev.getMax()),Toast.LENGTH_SHORT).show();
//                holder.range_seekbar.setVisibility(View.INVISIBLE);

        } catch (Exception e) {
//            holder.range_seekbar.setVisibility(View.INVISIBLE);
        }
    }

    public void setupToggle(ToggleButton tb, final TextView tl, final Device dev) {
        if ((dev.getOn() == 1)) {
            tb.setToggleOn();
            tl.setText(" ON");
            tl.setGravity(Gravity.START);
            tl.setTextColor(Color.WHITE);
        } else {
            tb.setToggleOff();
            tl.setText("OFF ");
            tl.setGravity(Gravity.END);
            tl.setTextColor(Color.DKGRAY);
        }

        tb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                myRef.child(dev.getId()).child("on").setValue((on) ? 1 : 0);
                tl.setText((on) ? " ON" : "OFF ");
                tl.setGravity((on) ? Gravity.START : Gravity.END);
                tl.setTextColor((on) ?Color.WHITE:Color.DKGRAY);
            }
        });
    }


}
