package com.example.wehome.controller;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
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
        TextView toggle_label;
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
        holder.device_icon.setBackgroundResource((cur_device.getIcon()!="")?R.drawable.icon_temp:R.drawable.icon_light);


        setupSeekbar(holder.range_seekbar,cur_device);
        setupToggle(holder.toggle,holder.toggle_label,cur_device);

        return convertView;

    }

    public void setupSeekbar(RangeSeekBarView rsb,final Device dev)
    {
        holder.range_seekbar.setAnimated(true, 3000L);
        holder.range_seekbar.setMaxValue(dev.getMax());
        holder.range_seekbar.setMinValue(dev.getMin());
        holder.range_seekbar.setValue(dev.getValue());
        rsb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d("data",dev.getId());
                myRef.child(dev.getId()).child("value").setValue(progress/(100/dev.getMax()));

                ((RangeSeekBarView) seekBar).setValue(progress/(100/dev.getMax()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void setupToggle(ToggleButton tb,final TextView tl,final Device dev)
    {
        if ((dev.getOn() == 1)) {
            tb.setToggleOn();
            tl.setText(" ON");
            tl.setGravity(Gravity.START);
        } else {
            tb.setToggleOff();
            tl.setText("OFF ");
            tl.setGravity(Gravity.END);
        }

        tb.setOnToggleChanged(new ToggleButton.OnToggleChanged() {
            @Override
            public void onToggle(boolean on) {
                myRef.child(dev.getId()).child("on").setValue((on)?1:0);
                tl.setText((on)?" ON":"OFF ");
                tl.setGravity((on)?Gravity.START:Gravity.END);
            }
        });
    }
}
