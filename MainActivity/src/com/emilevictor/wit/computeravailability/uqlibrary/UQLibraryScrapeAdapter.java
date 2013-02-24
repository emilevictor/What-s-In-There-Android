package com.emilevictor.wit.computeravailability.uqlibrary;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.emilevictor.wit.R;

public class UQLibraryScrapeAdapter extends ArrayAdapter<Library>{

    Context context; 
    int layoutResourceId;    
    Library data[] = null;
    
    public UQLibraryScrapeAdapter(Context context, int layoutResourceId, Library[] data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LibraryHolder holder = null;
        
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            
            holder = new LibraryHolder();
            holder.name = (TextView)row.findViewById(R.id.UQLibraryName);
            holder.openToday = (TextView)row.findViewById(R.id.UQLibraryOpenToday);
            holder.UQLibraryAvailabilityBar = (ProgressBar)row.findViewById(R.id.UQlibraryAvailabilityBar);
            holder.availabilityNumbers = (TextView)row.findViewById(R.id.UQLibraryAvailabilityNumbers);

            
            
            row.setTag(holder);
        }
        else
        {
            holder = (LibraryHolder)row.getTag();
        }
        
        holder.name.setText("");
        holder.openToday.setText("");
        holder.UQLibraryAvailabilityBar.setVisibility(View.VISIBLE);
        holder.availabilityNumbers.setVisibility(View.VISIBLE);
        holder.availabilityNumbers.setText("");
        
        Library lib = data[position];
        
        holder.name.setText(lib.getName());
        if (lib.getOpenToday())
        {
        	holder.openToday.setText(lib.getOpenTimesToday());
        	holder.openToday.setBackgroundColor(0xFF01860a);
        } else {
        	holder.openToday.setText("CLOSED TODAY");
        	holder.openToday.setBackgroundColor(0xFFc80000);
        }
        
        //Get the ratio of computers available.
        if (lib.getNumberComputersAvailable() != null && lib.getNumberComputersTotal() != null && lib.getOpenToday())
        {
            holder.UQLibraryAvailabilityBar.setProgress((int)(((double)lib.getNumberComputersAvailable()/(double)lib.getNumberComputersTotal())*100));
            holder.availabilityNumbers.setText(lib.getNumberComputersAvailable().toString() +
            		"/" + lib.getNumberComputersTotal().toString());
        } else if (lib.getNumberComputersTotal() != null && lib.getOpenToday())
        {
        	holder.availabilityNumbers.setText(lib.getNumberComputersTotal().toString() + " computers");
        } else {
        	holder.UQLibraryAvailabilityBar.setVisibility(View.INVISIBLE);
        	holder.availabilityNumbers.setVisibility(View.INVISIBLE);
        }

        
        
        return row;
    }
    
    static class LibraryHolder
    {
    	TextView name;
    	TextView openToday;
    	TextView openTimesToday;
    	ProgressBar UQLibraryAvailabilityBar;
    	TextView availabilityNumbers;
    	
    }
}