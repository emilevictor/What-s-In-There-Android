package com.emilevictor.wit.uqrota;

import com.emilevictor.wit.R;
import com.emilevictor.wit.helpers.Class;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class UQRotaTimetableAdapter extends ArrayAdapter<RotaClass>{

    Context context; 
    int layoutResourceId;    
    RotaClass data[] = null;
    
    public UQRotaTimetableAdapter(Context context, int layoutResourceId, RotaClass[] data) {
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
            holder.buildingName = (TextView) row.findViewById(R.id.buildingNameUQROTA);
            holder.room = (TextView) row.findViewById(R.id.roomUQROTA);
            holder.startTime = (TextView) row.findViewById(R.id.startTimeUQROTA);
            holder.finishTime = (TextView) row.findViewById(R.id.finishTimeUQROTA);
            holder.courseNameAndType = (TextView) row.findViewById(R.id.courseNameAndTypeUQROTA);
            holder.day = (TextView) row.findViewById(R.id.dayUQROTA);
            
            row.setTag(holder);
        }
        else
        {
            holder = (RoomHolder)row.getTag();
        }
        
        RotaClass rm = data[position];
        
        holder.buildingName.setText(rm.getBuilding());
        holder.courseNameAndType.setText(rm.getCourseNameAndType());
        holder.room.setText(rm.getRoom());
        
        holder.startTime.setText(calculateTimeString(rm.getStartTime()));
        holder.finishTime.setText(calculateTimeString(rm.getFinishTime()));
        holder.day.setText(rm.getDay());
       
        return row;
    }

    /**
     * This function will take the Rota formatted minutes past midnight and
     * turn it into a human readable time string in 12-hour time.
     * @param timeInMinutesPastMidnight
     * @return
     */
	private String calculateTimeString(Long timeInMinutesPastMidnight) {
		//Calculate the time from minutes past midnight.
        String AMorPM = "AM";
        int hours = (int)Math.round(Math.floor(((double)timeInMinutesPastMidnight / 60)));
        int minutes = (int)((((double)timeInMinutesPastMidnight / 60)-hours)*60);
        
        if (hours == 12)
        {
        	AMorPM = "PM";
        } else if (hours > 12) {
        	hours -= 12;
        	AMorPM = "PM";
        }
        
        if (minutes == 0)
        {
        	return String.valueOf(hours) + ":" +
					"00" + " " + AMorPM;
        } else {
        	return String.valueOf(hours) + ":" +
					String.valueOf(minutes) + " " + AMorPM;
        }
        
	}
    
    static class RoomHolder
    {
        TextView room;
        TextView buildingName;
        TextView startTime; 
        TextView finishTime; 
        TextView courseNameAndType; 
        TextView day;
    }
}