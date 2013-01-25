package com.emilevictor.wit.computeravailability;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Class;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ComputerAvailabilityOverviewAdapter extends ArrayAdapter<Room>{

    Context context; 
    int layoutResourceId;    
    Room data[] = null;
    
    public ComputerAvailabilityOverviewAdapter(Context context, int layoutResourceId, Room[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        RoomHolder holder = null;
        
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new RoomHolder();
            holder.room = (TextView)row.findViewById(R.id.roomName);
            holder.currentAvailable = (TextView)row.findViewById(R.id.currentAvailableComputers);
            holder.totalAvailable = (TextView)row.findViewById(R.id.totalAvailableComputers);
            
            row.setTag(holder);
        }
        else
        {
            holder = (RoomHolder)row.getTag();
        }
        
        Room rm = data[position];
        holder.room.setText(rm.roomNumber);
        holder.currentAvailable.setText(String.valueOf(rm.currentAvailable)+ " available");
        holder.totalAvailable.setText("out of " + String.valueOf(rm.totalAvailable));
        
        //Calculate the ratio
        double ratio = (double)rm.currentAvailable/(double)rm.totalAvailable;
        
        if (ratio < 0.25d)
        {
        	holder.currentAvailable.setBackgroundColor(0xFFc80000);
        } else if (ratio >= 0.25d && ratio < 0.5d)
        {
        	holder.currentAvailable.setBackgroundColor(0xFFffc000);
        } else if (ratio >= 0.5d && ratio < 0.75d)
        {
        	holder.currentAvailable.setBackgroundColor(0xFF00a2ff);
        } else if (ratio >= 0.75d)
        {
        	holder.currentAvailable.setBackgroundColor(0xFF01860a);
        }
        
        return row;
    }
    
    static class RoomHolder
    {
        TextView room;
        TextView totalAvailable;
        TextView currentAvailable;
    }
}