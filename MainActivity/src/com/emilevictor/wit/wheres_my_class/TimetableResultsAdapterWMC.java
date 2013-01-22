package com.emilevictor.wit.wheres_my_class;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Class;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TimetableResultsAdapterWMC extends ArrayAdapter<Class>{

    Context context; 
    int layoutResourceId;    
    Class data[] = null;
    
    public TimetableResultsAdapterWMC(Context context, int layoutResourceId, Class[] data) {
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
            holder.classType = (TextView)row.findViewById(R.id.classTypeWMC);
            holder.timeOfDay = (TextView)row.findViewById(R.id.timeOfDayWMC);
            holder.locationText = (TextView)row.findViewById(R.id.locationTextWMC);

            
            
            row.setTag(holder);
        }
        else
        {
            holder = (TimetableResultHolder)row.getTag();
        }
        
        Class cls = data[position];
        holder.classType.setText(cls.classType);
        holder.timeOfDay.setText(cls.startTime);
        holder.locationText.setText("Room " + cls.room + ", " + cls.buildingName);
        
        
        if (cls.classType.equals("F"))
        {
        	holder.classType.setBackgroundColor(0xFF009900);
        }
        
        return row;
    }
    
    static class TimetableResultHolder
    {
        TextView classType;
        TextView timeOfDay;
        TextView locationText;
    }
}