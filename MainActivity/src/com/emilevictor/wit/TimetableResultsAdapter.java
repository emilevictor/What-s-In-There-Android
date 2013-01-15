package com.emilevictor.wit;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TimetableResultsAdapter extends ArrayAdapter<Class>{

    Context context; 
    int layoutResourceId;    
    Class data[] = null;
    
    public TimetableResultsAdapter(Context context, int layoutResourceId, Class[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TimetableResultHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new TimetableResultHolder();
            holder.classType = (TextView)row.findViewById(R.id.classType);
            holder.courseCode = (TextView)row.findViewById(R.id.courseCode);
            holder.timeOfDay = (TextView)row.findViewById(R.id.timeOfDay);
            
            row.setTag(holder);
        }
        else
        {
            holder = (TimetableResultHolder)row.getTag();
        }
        
        Class cls = data[position];
        holder.classType.setText(cls.classType);
        holder.courseCode.setText(cls.courseCode);
        holder.timeOfDay.setText(cls.startTime);
        
        return row;
    }
    
    static class TimetableResultHolder
    {
        TextView classType;
        TextView courseCode;
        TextView timeOfDay;
    }
}