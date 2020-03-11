package com.example.wehome.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wehome.R;
import com.example.wehome.model.Device;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mohammedalaa.seekbar.RangeSeekBarView;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;

public class DeviceArrayAdapter extends ArrayAdapter {
    private ViewHolder holder;
    private int mResource;
    private Context mContext;
    private ArrayList<Device> devices;
    private int lastPosition = -1;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("devices");

    static class ViewHolder {
        TextView device_name;
        ImageView device_icon;
        RangeSeekBarView range_seekbar;
        ToggleButton toggle;
    }

    public DeviceArrayAdapter(Context context, int resource, ArrayList<Device> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
        devices = objects;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View result;
        Device plant_temp = devices.get(position);
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
            holder = new DeviceArrayAdapter.ViewHolder();
            holder.device_name = convertView.findViewById(R.id.device_name_input);
            holder.device_icon = convertView.findViewById(R.id.device_icon);
            holder.toggle = convertView.findViewById(R.id.toggle);
            holder.range_seekbar = convertView.findViewById(R.id.range_seekbar);
            result = convertView;
            convertView.setTag(holder);

        } else {
            holder = (DeviceArrayAdapter.ViewHolder) convertView.getTag();
            result = convertView;
        }
        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.scroll_down_anim : R.anim.scroll_up_anim);
        result.startAnimation(animation);
        lastPosition = position;

//        holder.plant_id.setText(String.valueOf(plant_temp.getId()));
//        holder.plant_name.setText(plant_temp.getName());

        holder.range_seekbar.setAnimated(true, 3000L);
        holder.range_seekbar.setMaxValue(5);
        holder.range_seekbar.setMinValue(0);
        holder.range_seekbar.setValue(0);


        holder.range_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("data",devices.get(position).getId());
                myRef.child(devices.get(position).getId()).child("value").setValue(progress/(100/5));

                ((RangeSeekBarView) seekBar).setValue(progress/(100/5));
//                holder.range_seekbar.setValue(progress/(100/5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        return convertView;

    }
}
