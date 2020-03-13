package com.example.wehome.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wehome.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class IconSpinnerAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> name;
    ArrayList<String> path;
    LayoutInflater inflter;

    public IconSpinnerAdapter(Context applicationContext, ArrayList<String> name, ArrayList<String> path) {
        this.context = applicationContext;
        this.name = name;
        this.path = path;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return name.size();
    }

    @Override
    public Object getItem(int i) {
        return path.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.spinner_icon, null);
        ImageView icon = (ImageView) view.findViewById(R.id.image_icon);
        TextView names = (TextView) view.findViewById(R.id.name_icon);
        icon.setImageResource(context.getResources().getIdentifier("drawable/"+path.get(i),null,context.getPackageName()));
        names.setText(name.get(i));
        return view;
    }
}
