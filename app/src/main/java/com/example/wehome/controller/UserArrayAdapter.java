package com.example.wehome.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.wehome.R;
import com.mohammedalaa.seekbar.RangeSeekBarView;
import com.zcw.togglebutton.ToggleButton;

import java.util.ArrayList;
import java.util.HashMap;

public class UserArrayAdapter extends BaseExpandableListAdapter {

    private Context ctx;
    private HashMap<String, ArrayList<String>> childItem;
    private ArrayList<String> headerItem;
    private int hl;
    private int cl;
    private ViewHolder holder;

    public UserArrayAdapter(Context ctx,int header_layout,int child_layout,ArrayList<String> headerItem,HashMap<String, ArrayList<String>> childItem) {
        this.ctx = ctx;
        this.headerItem = headerItem;
        this.childItem = childItem;
        this.hl = header_layout;
        this.cl = child_layout;
    }

    @Override
    public int getGroupCount() {
        return this.headerItem.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childItem.get(headerItem.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerItem.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childItem.get(headerItem.get(groupPosition)).get(childPosition);
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);
        final View result;
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(hl,null);
            holder = new ViewHolder();
            holder.user_name = (TextView)convertView.findViewById(R.id.user_fullname);

            holder.delete_btn = (Button)convertView.findViewById(R.id.delete_btn);

            result = convertView;
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)convertView.getTag();
            result = convertView;
        }


        holder.user_name.setText(headerItem.get(groupPosition));
        holder.delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("user","sni oi jadi ");
            }
        });
        return result;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String title = (String) this.getChild(groupPosition,childPosition);


        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) this.ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(cl,null);

        }


        TextView header = (TextView)convertView.findViewById(R.id.id_input);


        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    static class ViewHolder {
        TextView user_name;
        Button delete_btn;

    }
}
